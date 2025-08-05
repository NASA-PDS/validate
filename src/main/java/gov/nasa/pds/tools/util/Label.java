// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// * Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package gov.nasa.pds.tools.util;

import gov.nasa.arc.pds.xml.generated.*;
import gov.nasa.pds.label.LabelStandard;
import gov.nasa.pds.label.ProductType;
import gov.nasa.pds.label.object.*;
import gov.nasa.pds.objectAccess.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implements a mechanism for accessing labels and the objects they represent.
 *
 * <p>
 * For PDS4 labels, the user should load a label file and access the objects as follows:
 * </p>
 *
 * <pre>
 * Label label = Label.open(new File("labelfile.xml");
 *
 * &#x2F;&#x2F; Get the tables in the data file.
 * List&lt;TableObject&gt; tables = label.getObjects(TableObject.class);
 *
 * &#x2F;&#x2F; Read the table record-by-record.
 * TableObject table = tables.get(0);
 * TableRecord record = table.readNext();
 * while (record != null) {
 *     ... process this record ...
 * }
 *
 * label.close();
 * </pre>
 *
 * <p>
 * See the package <code>gov.nasa.pds.object</code> to see all the object types that can be
 * represented in a PDS label.
 * </p>
 */
public class Label {

  private ObjectAccess oa;
  private URL parentDir;
  private Product genericProduct;
  private LabelStandard standard;

  private Label(File labelFile) throws ParseException, MalformedURLException {
    this(labelFile.toURI().toURL());
  }

  private Label(URL label) throws ParseException {
    // Need to get the absolute version of the label file, because
    // relative paths in the current directory will give a null parent
    // file.
    try {
      URI labelUri = label.toURI().normalize();
      parentDir = labelUri.getPath().endsWith("/") ? labelUri.resolve("..").toURL()
          : labelUri.resolve(".").toURL();
      oa = new ObjectAccess(parentDir);
      genericProduct = oa.getProduct(labelUri.toURL(), Product.class);
      standard = LabelStandard.PDS4;
    } catch (Exception e) {
      throw new ParseException(e.getMessage(), e);
    }
  }

  /**
   * Closes the label. All further calls on the label may generate errors.
   */
  public void close() {
    oa = null;
    parentDir = null;
    genericProduct = null;
  }

  /**
   * Opens a label from a file.
   *
   * @param labelFile the label file
   * @return the label
   * @throws ParseException if there is an error reading the label from the file
   */
  public static Label open(File labelFile) throws ParseException {
    try {
      return new Label(labelFile.toURI().toURL());
    } catch (MalformedURLException e) {
      throw new ParseException(e.getMessage(), e);
    }
  }

  /**
   * Opens a label from a url.
   *
   * @param label the label url
   * @return the label
   * @throws ParseException if there is an error reading the label from the url
   */
  public static Label open(URL label) throws ParseException {
    return new Label(label);
  }

  /**
   * Gets the label standard that the label conforms to.
   *
   * @return the label standard
   */
  public LabelStandard getLabelStandard() {
    return standard;
  }

  /**
   * Returns the version number of the label standard used by the product.
   *
   * @return the label standard version number, as a string
   */
  public String getStandardVersion() {
    return genericProduct.getIdentificationArea().getInformationModelVersion();
  }

  /**
   * Gets the object class of the product.
   *
   * @return the product object class
   */
  public Class<? extends Product> getProductClass() {
    return genericProduct.getClass();
  }

  /**
   * Gets the type of the product, as an enumerated type.
   *
   * @return the product type
   */
  public ProductType getProductType() {
    return ProductType.typeForClass(getProductClass());
  }

  /**
   * Gets the data objects represented by the label.
   *
   * @return a list of data objects
   * @throws Exception if there is an error accessing the objects in the product
   */
  public List<DataObject> getObjects() throws Exception {
    return getObjects(DataObject.class);
  }

  /**
   * Gets the data objects that are of a given class.
   *
   * @param <T> the class of the objects desired
   * @param clazz the class object of the object class
   * @return a list of data objects
   * @throws Exception if there is an error accessing the objects in the product
   */
  @SuppressWarnings("unchecked")
  public <T extends DataObject> List<T> getObjects(Class<T> clazz) throws Exception {
    // Find the subset of all objects that matches given class.
    List<DataObject> subset = new ArrayList<>();

    for (DataObject object : getDataObjects(genericProduct)) {
      if (clazz.isAssignableFrom(object.getClass())) {
        subset.add(object);
      }
    }

    return (List<T>) subset;
  }


  private List<DataObject> getDataObjects(Product product) throws Exception {
    if (product instanceof ProductAIP) {
      return getDataObjects((ProductAIP) product);
    } else if (product instanceof ProductAncillary) {
      return getDataObjects((ProductAncillary) product);
    } else if (product instanceof ProductBrowse) {
      return getDataObjects((ProductBrowse) product);
      // } else if (product instanceof ProductBundle) {
      // return getDataObjects((ProductBundle) product);
    } else if (product instanceof ProductCollection) {
      return getDataObjects((ProductCollection) product);
    } else if (product instanceof ProductFileRepository) {
      return getDataObjects((ProductFileRepository) product);
    } else if (product instanceof ProductFileText) {
      return getDataObjects((ProductFileText) product);
    } else if (product instanceof ProductMetadataSupplemental) {
      return getDataObjects((ProductMetadataSupplemental) product);
    } else if (product instanceof ProductNative) {
      return getDataObjects((ProductNative) product);
    } else if (product instanceof ProductObservational) {
      return getDataObjects((ProductObservational) product);
    } else if (product instanceof ProductSIP) {
      return getDataObjects((ProductSIP) product);
    } else if (product instanceof ProductSIPDeepArchive) {
      return getDataObjects((ProductSIPDeepArchive) product);
    } else if (product instanceof ProductSPICEKernel) {
      return getDataObjects((ProductSPICEKernel) product);
    } else if (product instanceof ProductService) {
      return getDataObjects((ProductService) product);
    } else if (product instanceof ProductThumbnail) {
      return getDataObjects((ProductThumbnail) product);
    } else if (product instanceof ProductXMLSchema) {
      return getDataObjects((ProductXMLSchema) product);
    } else {
      throw new ClassCastException("Unsupported product type.");
    }
  }

  /**
   * Extract data objects from Product_AIP label
   *
   * @param product Product_AIP label
   * @return a list of data objects
   * @throws Exception an exception
   */
  private List<DataObject> getDataObjects(ProductAIP product) throws Exception {
    List<DataObject> objects = new ArrayList<>();

    for (InformationPackageComponent comp : product.getInformationPackageComponents()) {
      addObject(objects, comp.getFileAreaChecksumManifest().getFile(),
          comp.getFileAreaChecksumManifest().getChecksumManifest(), new DataObjectLocation(1, 1));
      addObject(objects, comp.getFileAreaTransferManifest().getFile(),
          comp.getFileAreaTransferManifest().getTransferManifest(), new DataObjectLocation(2, 1));
    }

    return objects;
  }

  /**
   * Extract data objects from Product_Ancillary label
   *
   * @param product Product_Ancillary label
   * @return a list of data objects
   * @throws Exception an exception
   */
  private List<DataObject> getDataObjects(ProductAncillary product) throws Exception {
    List<DataObject> objects = new ArrayList<>();

    int fileAreaIndex = 0;
    int dataObjectIndex = 0;
    for (FileAreaAncillary fileArea : product.getFileAreaAncillaries()) {
      fileAreaIndex++;
      for (ByteStream bs : fileArea.getArraiesAndArray1DsAndArray2Ds()) {
        addObject(objects, fileArea.getFile(), bs,
            new DataObjectLocation(fileAreaIndex, ++dataObjectIndex));
      }
      dataObjectIndex = 0;
    }

    return objects;
  }

  /**
   * Extract data objects from Product_Browse label
   *
   * @param product Product_Browse label
   * @return a list of data objects
   * @throws Exception an exception
   */
  private List<DataObject> getDataObjects(ProductBrowse product) throws Exception {
    List<DataObject> objects = new ArrayList<>();

    int fileAreaIndex = 0;
    int dataObjectIndex = 0;
    for (FileAreaBrowse fileArea : product.getFileAreaBrowses()) {
      fileAreaIndex++;
      for (ByteStream bs : fileArea.getDataObjects()) {
        addObject(objects, fileArea.getFile(), bs,
            new DataObjectLocation(fileAreaIndex, ++dataObjectIndex));
      }
      dataObjectIndex = 0;
    }

    return objects;
  }

  /**
   * Extract data objects from Product_Collection label
   *
   * @param product Product_Collection label
   * @return a list of data objects
   * @throws Exception an exception
   */
  private List<DataObject> getDataObjects(ProductCollection product) throws Exception {
    List<DataObject> objects = new ArrayList<>();

    FileAreaInventory fileArea = product.getFileAreaInventory();
    addObject(objects, fileArea.getFile(), fileArea.getInventory(), new DataObjectLocation(1, 1));

    return objects;
  }

  /**
   * Extract data objects from Product_File_Repository label
   *
   * @param product Product_File_Repository label
   * @return a list of data objects
   * @throws Exception an exception
   */
  private List<DataObject> getDataObjects(ProductFileRepository product) throws Exception {
    List<DataObject> objects = new ArrayList<>();

    FileAreaBinary fileArea = product.getFileAreaBinary();

    int dataObjectIndex = 0;
    for (EncodedBinary eb : fileArea.getEncodedBinaries()) {
      addObject(objects, fileArea.getFile(), eb, new DataObjectLocation(1, ++dataObjectIndex));
    }

    return objects;
  }

  /**
   * Extract data objects from Product_File_Text label
   *
   * @param product Product_File_Text label
   * @return a list of data objects
   * @throws Exception an exception
   */
  private List<DataObject> getDataObjects(ProductFileText product) throws Exception {
    List<DataObject> objects = new ArrayList<>();

    FileAreaText fileArea = product.getFileAreaText();

    addObject(objects, fileArea.getFile(), fileArea.getStreamText(), new DataObjectLocation(1, 1));

    return objects;
  }

  /**
   * Extract data objects from Product_Metadata_Supplemental label
   *
   * @param product Product_Metadata_Supplemental label
   * @return a list of data objects
   * @throws Exception an exception
   */
  private List<DataObject> getDataObjects(ProductMetadataSupplemental product) throws Exception {
    List<DataObject> objects = new ArrayList<>();

    FileAreaMetadata fileArea = product.getFileAreaMetadata();

    int fileAreaIndex = 0;
    int dataObjectIndex = 0;
    if (fileArea.getTableCharacter() != null) {
      addObject(objects, fileArea.getFile(), fileArea.getTableCharacter(),
          new DataObjectLocation(++fileAreaIndex, ++dataObjectIndex));
    }

    if (fileArea.getTableDelimited() != null) {
      addObject(objects, fileArea.getFile(), fileArea.getTableDelimited(),
          new DataObjectLocation(++fileAreaIndex, ++dataObjectIndex));
    }

    return objects;
  }

  /**
   * Extract data objects from Product_Native label
   *
   * @param product Product_Native label
   * @return a list of data objects
   * @throws Exception an exception
   */
  private List<DataObject> getDataObjects(ProductNative product) throws Exception {
    List<DataObject> objects = new ArrayList<>();

    int fileAreaIndex = 0;
    int dataObjectIndex = 0;
    for (FileAreaNative fileArea : product.getFileAreaNatives()) {
      fileAreaIndex++;
      for (EncodedNative en : fileArea.getEncodedNatives()) {
        addObject(objects, fileArea.getFile(), en,
            new DataObjectLocation(fileAreaIndex, ++dataObjectIndex));
      }
      dataObjectIndex = 0;
    }

    return objects;
  }

  /**
   * Extract data objects from Product_Observational label
   *
   * @param product Product_Observational label
   * @return a list of data objects
   * @throws Exception an exception
   */
  private List<DataObject> getDataObjects(ProductObservational product) throws Exception {
    List<DataObject> objects = new ArrayList<>();

    int fileAreaIndex = 0;
    int dataObjectIndex = 0;
    for (FileAreaObservational fileArea : product.getFileAreaObservationals()) {
      fileAreaIndex++;
      for (ByteStream stream : fileArea.getDataObjects()) {
        addObject(objects, fileArea.getFile(), stream,
            new DataObjectLocation(fileAreaIndex, ++dataObjectIndex));
      }
      dataObjectIndex = 0;
    }
    for (FileAreaObservationalSupplemental supplementalArea : product
        .getFileAreaObservationalSupplementals()) {
      fileAreaIndex++;
      for (ByteStream stream : supplementalArea.getDataObjects()) {
        addObject(objects, supplementalArea.getFile(), stream,
            new DataObjectLocation(fileAreaIndex, dataObjectIndex));
      }
      dataObjectIndex = 0;
    }

    return objects;
  }

  /**
   * Extract data objects from Product_Service label
   *
   * @param product Product_Service label
   * @return a list of data objects
   * @throws Exception an exception
   */
  private List<DataObject> getDataObjects(ProductService product) throws Exception {
    List<DataObject> objects = new ArrayList<>();

    int fileAreaIndex = 0;
    int dataObjectIndex = 0;
    for (FileAreaServiceDescription fileArea : product.getFileAreaServiceDescriptions()) {
      fileAreaIndex++;
      for (ServiceDescription sd : fileArea.getServiceDescriptions()) {
        addObject(objects, fileArea.getFile(), sd,
            new DataObjectLocation(fileAreaIndex, ++dataObjectIndex));
      }
      dataObjectIndex = 0;
    }

    return objects;
  }

  /**
   * Extract data objects from Product_SIP label
   *
   * @param product Product_SIP label
   * @return a list of data objects
   * @throws Exception an exception
   */
  private List<DataObject> getDataObjects(ProductSIP product) throws Exception {
    List<DataObject> objects = new ArrayList<>();

    for (InformationPackageComponent comp : product.getInformationPackageComponents()) {
      addObject(objects, comp.getFileAreaChecksumManifest().getFile(),
          comp.getFileAreaChecksumManifest().getChecksumManifest(), new DataObjectLocation(1, 1));
      addObject(objects, comp.getFileAreaTransferManifest().getFile(),
          comp.getFileAreaTransferManifest().getTransferManifest(), new DataObjectLocation(2, 1));
    }

    return objects;
  }

  /**
   * Extract data objects from Product_SIP_Deep_Archive label
   *
   * @param product Product_SIP_Deep_Archive label
   * @return a list of data objects
   * @throws Exception an exception
   */
  private List<DataObject> getDataObjects(ProductSIPDeepArchive product) throws Exception {
    List<DataObject> objects = new ArrayList<>();

    FileAreaSIPDeepArchive fileArea =
        product.getInformationPackageComponentDeepArchive().getFileAreaSIPDeepArchive();

    addObject(objects, fileArea.getFile(), fileArea.getManifestSIPDeepArchive(),
        new DataObjectLocation(1, 1));

    return objects;
  }

  /**
   * Extract data objects from Product_SPICE_Kernel label
   *
   * @param product Product_SPICE_Kernel label
   * @return a list of data objects
   * @throws Exception an exception
   */
  private List<DataObject> getDataObjects(ProductSPICEKernel product) throws Exception {
    List<DataObject> objects = new ArrayList<>();

    FileAreaSPICEKernel fileArea = product.getFileAreaSPICEKernel();

    addObject(objects, fileArea.getFile(), fileArea.getSPICEKernel(), new DataObjectLocation(1, 1));

    return objects;
  }

  /**
   * Extract data objects from Product_Thumbnail label
   *
   * @param product Product_Thumbnail label
   * @return a list of data objects
   * @throws Exception an exception
   */
  private List<DataObject> getDataObjects(ProductThumbnail product) throws Exception {
    List<DataObject> objects = new ArrayList<>();

    FileAreaEncodedImage fileArea = product.getFileAreaEncodedImage();
    addObject(objects, fileArea.getFile(), fileArea.getEncodedImage(),
        new DataObjectLocation(1, 1));

    return objects;
  }

  /**
   * Extract data objects from Product_XML_Schema label
   *
   * @param product Product_XML_Schema label
   * @return a list of data objects
   * @throws Exception an exception
   */
  private List<DataObject> getDataObjects(ProductXMLSchema product) throws Exception {
    List<DataObject> objects = new ArrayList<>();

    int dataObjectIndex = 0;
    for (FileAreaXMLSchema fileArea : product.getFileAreaXMLSchemas()) {
      addObject(objects, fileArea.getFile(), fileArea.getXMLSchema(),
          new DataObjectLocation(1, ++dataObjectIndex));
    }

    return objects;
  }

  private void addObject(Collection<DataObject> objects, gov.nasa.arc.pds.xml.generated.File file,
      ByteStream stream, DataObjectLocation location) throws Exception {
    if (stream instanceof TableBinary) {
      objects.add(makeTable(file, (TableBinary) stream, location));
    } else if (stream instanceof TableCharacter) {
      objects.add(makeTable(file, (TableCharacter) stream, location));
    } else if (stream instanceof TableDelimited) {
      objects.add(makeTable(file, (TableDelimited) stream, location));
    } else if (stream instanceof Array) {
      objects.add(makeArray(file, (Array) stream, location));
    } else {
      objects.add(makeGenericObject(file, stream, location));
    }
  }

  private DataObject makeTable(gov.nasa.arc.pds.xml.generated.File file, TableBinary table,
      DataObjectLocation location) throws Exception {
    BigInteger size =
        table.getRecords().multiply(table.getRecordBinary().getRecordLength().getValue());
    return new TableObject(parentDir, file, table, table.getOffset().getValue().longValueExact(),
        size.longValueExact(), location);
  }

  private DataObject makeTable(gov.nasa.arc.pds.xml.generated.File file, TableCharacter table,
      DataObjectLocation location) throws Exception {
    BigInteger size =
        table.getRecords().multiply(table.getRecordCharacter().getRecordLength().getValue());
    return new TableObject(parentDir, file, table, table.getOffset().getValue().longValueExact(),
        size.longValueExact(), location);
  }

  private DataObject makeTable(gov.nasa.arc.pds.xml.generated.File file, TableDelimited table,
      DataObjectLocation location) throws Exception {
    // The range for a delimited table must be the rest of the file past the offset position.
    long offset = 0;
    if (table.getOffset() != null) {
      offset = table.getOffset().getValue().longValueExact();
    }
    long size = -1;
    if (table.getRecordDelimited() != null) {
      RecordDelimited definition = table.getRecordDelimited();
      if (definition.getMaximumRecordLength() != null) {
        size = definition.getMaximumRecordLength().getValue().longValue() * table.getRecords().longValue();
      } else if (table.getObjectLength() != null) {
        size = table.getObjectLength().getValue().longValueExact();
      } else if (file.getFileSize() != null) {
        size = 0;
      }
    } else if (table.getObjectLength() != null) {
      size = table.getObjectLength().getValue().longValueExact();
    } else if (file.getFileSize() != null) {
      size = file.getFileSize().getValue().longValue() - offset;
    }
    return new TableObject(parentDir, file, table, offset, size, location);
  }

  private DataObject makeArray(gov.nasa.arc.pds.xml.generated.File file, Array array,
      DataObjectLocation location) throws FileNotFoundException, IOException, URISyntaxException {
    return new ArrayObject(parentDir, file, array, array.getOffset().getValue().longValueExact(),
        location);
  }

  private DataObject makeGenericObject(gov.nasa.arc.pds.xml.generated.File file, ByteStream stream,
      DataObjectLocation location) throws IOException, URISyntaxException {
    long size = -1;
    long offset = -1;
    if (stream instanceof EncodedByteStream) {
      EncodedByteStream ebs = (EncodedByteStream) stream;
      if (ebs.getObjectLength() != null) {
        size = ebs.getObjectLength().getValue().longValueExact();
      }
      offset = ebs.getOffset().getValue().longValueExact();
    } else if (stream instanceof ParsableByteStream) {
      ParsableByteStream pbs = (ParsableByteStream) stream;
      if (pbs.getObjectLength() != null) {
        size = pbs.getObjectLength().getValue().longValueExact();
      }
      offset = pbs.getOffset().getValue().longValueExact();
    }
    return new GenericObject(parentDir, file, offset, size, location);
  }
}
