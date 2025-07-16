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
import gov.nasa.pds.label.jaxb.PDSXMLEventReader;
import gov.nasa.pds.label.jaxb.XMLLabelContext;
import gov.nasa.pds.objectAccess.ObjectProvider;
import gov.nasa.pds.objectAccess.ParseException;
import gov.nasa.pds.objectAccess.utility.Utility;
import jakarta.xml.bind.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The <code>ObjectAccess</code> class is a point of entry into parsed PDS (including PDS
 * 4/XML-schema-labeled) objects.
 *
 * @author dcberrio
 */
public class ObjectAccess implements ObjectProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(ObjectAccess.class);
  private static final JAXBContext context = getJAXBContext("gov.nasa.arc.pds.xml.generated");
  private String archiveRoot;
  private URL root;
  private final XMLInputFactory xif = XMLInputFactory.newInstance();
  private XMLLabelContext labelContext;

  /**
   * Creates a new instance with the current local directory as the archive root path.
   *
   * @throws URISyntaxException
   * @throws MalformedURLException
   */

  public ObjectAccess() throws MalformedURLException, URISyntaxException {
    this(new File("."));
  }


  /**
   * Constructs an <code>ObjectAccess</code> object and sets the archive root path.
   *
   * @param archiveRoot the archive root path
   * @throws URISyntaxException
   * @throws MalformedURLException
   */
  public ObjectAccess(String archiveRoot) throws MalformedURLException, URISyntaxException {
    URL url = null;
    try {
      url = new URL(archiveRoot);
    } catch (MalformedURLException mu) {
      url = new File(archiveRoot).toURI().toURL();
    }
    this.root = url.toURI().normalize().toURL();
    this.archiveRoot = this.root.toString();
    this.labelContext = new XMLLabelContext();
  }

  /**
   * Constructs an <code>ObjectAccess</code> object and sets the archive root path.
   *
   * @param archiveRoot the archive root path
   * @throws URISyntaxException
   * @throws MalformedURLException
   */
  public ObjectAccess(File archiveRoot) throws MalformedURLException, URISyntaxException {
    this(archiveRoot.toURI().toURL());
  }

  /**
   * Constructs an <code>ObjectAccess</code> object and sets the archive root path.
   *
   * @param archiveRoot the archive root path
   * @throws URISyntaxException
   * @throws MalformedURLException
   */
  public ObjectAccess(URL archiveRoot) throws URISyntaxException, MalformedURLException {
    this.root = archiveRoot.toURI().normalize().toURL();
    this.archiveRoot = this.root.toString();
    this.labelContext = new XMLLabelContext();
  }

  private static JAXBContext getJAXBContext(String pkgName)  {
    ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
    if (!(currentLoader instanceof WorkaroundClassLoader)) {
      ClassLoader loader = new WorkaroundClassLoader(
          currentLoader != null ? currentLoader : ObjectAccess.class.getClassLoader());
      Thread.currentThread().setContextClassLoader(loader);
    }
    try {
      return JAXBContext.newInstance(pkgName);
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T> T getProduct(File labelFile, Class<T> productClass) throws ParseException {
    try {
      return getProduct(labelFile.toURI().toURL(), productClass);
    } catch (MalformedURLException e) {
      LOGGER.error("Failed to load the product from the label.", e);
      throw new ParseException("Unable to parse the product label", e);
    }
  }

  @Override
  public <T> T getProduct(URL label, Class<T> productClass) throws ParseException {
    try {

      Unmarshaller u = context.createUnmarshaller();
      u.setEventHandler(new LenientEventHandler());
      return productClass.cast(u.unmarshal(Utility.openConnection(label)));
    } catch (JAXBException je) {
      LOGGER.error("Failed to load the product from the label.", je);
      throw new ParseException("Unable to parse the product label", je);
    } catch (IOException io) {
      LOGGER.error("Failed to load the product from the label.", io);
      throw new ParseException("Unable to parse the product label", io);
    } catch (Exception e) {
      throw new ParseException("Error while parsing the product label", e);
    }
  }

  @Override
  public ProductObservational getObservationalProduct(String relativeXmlFilePath) {
    InputStream in = null;
    try {
      JAXBContext context = getJAXBContext("gov.nasa.arc.pds.xml.generated");
      Unmarshaller u = context.createUnmarshaller();
      u.setEventHandler(new LenientEventHandler());
      URL url = new URL(getRoot(), relativeXmlFilePath);
      in = url.openStream();
      XmlRootElement a = ProductObservational.class.getAnnotation(XmlRootElement.class);
      String root = a.name();
      PDSXMLEventReader xsr = new PDSXMLEventReader(xif.createXMLEventReader(in), root);
      ProductObservational po = (ProductObservational) u.unmarshal(xsr);
      labelContext = xsr.getLabelContext();
      return po;
    } catch (JAXBException e) {
      LOGGER.error("Failed to get the product observational.", e);
      e.printStackTrace();
      return null;
    } catch (MalformedURLException e) {
      LOGGER.error("Failed to get the product observational.", e);
      e.printStackTrace();
      return null;
    } catch (XMLStreamException e) {
      LOGGER.error("Failed to get the product observational: ", e);
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      LOGGER.error("Failed to get the product observational.", e);
      e.printStackTrace();
      return null;
    } finally {
      IOUtils.closeQuietly(in);
    }
  }

  @Override
  public void setObservationalProduct(String relativeXmlFilePath, ProductObservational product)
      throws Exception {
    setObservationalProduct(relativeXmlFilePath, product, null);
  }

  /**
   * Writes a label given the product XML file. This method assumes that the label will be written
   * to the local file system. Therefore, the protocol of the ObjectAccess archive root must be a
   * 'file'.
   *
   * @param relativeXmlFilePath the XML file path and name of the product to set, relative to the
   *        ObjectAccess archive root
   * 
   * @param product The Product_Observational object to serialize into an XML file.
   *
   * @param labelContext A context to use when creating the XML file. Can be set to null.
   *
   * @throws Exception If there was an error creating the XML file.
   */
  @Override
  public void setObservationalProduct(String relativeXmlFilePath, ProductObservational product,
      XMLLabelContext labelContext) throws Exception {
    try {
      JAXBContext context = getJAXBContext("gov.nasa.arc.pds.xml.generated");
      Marshaller m = context.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      if (labelContext != null) {
        m.setProperty("com.sun.xml.bind.namespacePrefixMapper", labelContext.getNamespaces());
        m.setProperty("com.sun.xml.bind.xmlHeaders", labelContext.getXmlModelPIs());
        m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, labelContext.getSchemaLocation());
      }
      if ("file".equalsIgnoreCase(getRoot().getProtocol())) {
        File parent = FileUtils.toFile(getRoot());
        File f = new File(parent, relativeXmlFilePath);
        m.marshal(product, f);
      } else {
        OutputStream os = null;
        try {
          URL u = new URL(getRoot(), relativeXmlFilePath);
          URLConnection conn = u.openConnection();
          conn.setDoOutput(true);
          os = conn.getOutputStream();
          m.marshal(product, os);
        } catch (Exception e) {
          LOGGER.error("Failed to set the product observational.", e);
          e.printStackTrace();
          throw e;
        } finally {
          IOUtils.closeQuietly(os);
        }
      }
    } catch (JAXBException e) {
      LOGGER.error("Failed to set the product observational.", e);
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public List<Object> getDataObjects(Product product) throws ParseException {
    List<Object> list = new ArrayList<Object>();

    if (product instanceof ProductAIP) {
      for (InformationPackageComponent comp : ((ProductAIP) product)
          .getInformationPackageComponents()) {
        list.add(comp.getFileAreaChecksumManifest().getChecksumManifest());
        list.add(comp.getFileAreaTransferManifest().getTransferManifest());
      }
    } else if (product instanceof ProductAncillary) {
      for (FileAreaAncillary fileArea : ((ProductAncillary) product).getFileAreaAncillaries()) {
        list.addAll(fileArea.getArraiesAndArray1DsAndArray2Ds());
      }
    } else if (product instanceof ProductBrowse) {
      for (FileAreaBrowse fileArea : ((ProductBrowse) product).getFileAreaBrowses()) {
        list.addAll(fileArea.getDataObjects());
      }
    } else if (product instanceof ProductBundle) {
      list.add(((ProductBundle) product).getFileAreaText().getStreamText());
    } else if (product instanceof ProductCollection) {
      list.add(((ProductCollection) product).getFileAreaInventory().getInventory());
    } else if (product instanceof ProductFileRepository) {
      list.addAll(((ProductFileRepository) product).getFileAreaBinary().getEncodedBinaries());
    } else if (product instanceof ProductFileText) {
      list.add(((ProductFileText) product).getFileAreaText().getStreamText());
    } else if (product instanceof ProductMetadataSupplemental) {
      list.add(((ProductMetadataSupplemental) product).getFileAreaMetadata().getTableCharacter());
      list.add(((ProductMetadataSupplemental) product).getFileAreaMetadata().getTableDelimited());
    } else if (product instanceof ProductNative) {
      for (FileAreaNative fileArea : ((ProductNative) product).getFileAreaNatives()) {
        list.addAll(fileArea.getEncodedNatives());
      }
    } else if (product instanceof ProductObservational) {
      for (FileAreaObservational fileArea : ((ProductObservational) product)
          .getFileAreaObservationals()) {
        list.addAll(fileArea.getDataObjects());
      }
      for (FileAreaObservationalSupplemental fileArea : ((ProductObservational) product)
          .getFileAreaObservationalSupplementals()) {
        list.addAll(fileArea.getDataObjects());
      }
    } else if (product instanceof ProductSIP) {
      for (InformationPackageComponent comp : ((ProductSIP) product)
          .getInformationPackageComponents()) {
        list.add(comp.getFileAreaChecksumManifest().getChecksumManifest());
        list.add(comp.getFileAreaTransferManifest().getTransferManifest());
      }
    } else if (product instanceof ProductSIPDeepArchive) {
      list.add(((ProductSIPDeepArchive) product).getInformationPackageComponentDeepArchive()
          .getFileAreaSIPDeepArchive().getManifestSIPDeepArchive());
    } else if (product instanceof ProductSPICEKernel) {
      list.add(((ProductSPICEKernel) product).getFileAreaSPICEKernel().getSPICEKernel());
    } else if (product instanceof ProductService) {
      for (FileAreaServiceDescription fileArea : ((ProductService) product)
          .getFileAreaServiceDescriptions()) {
        list.addAll(fileArea.getServiceDescriptions());
      }
    } else if (product instanceof ProductThumbnail) {
      list.add(((ProductThumbnail) product).getFileAreaEncodedImage().getEncodedImage());
    } else if (product instanceof ProductXMLSchema) {
      for (FileAreaXMLSchema fileArea : ((ProductXMLSchema) product).getFileAreaXMLSchemas()) {
        list.add(fileArea.getXMLSchema());
      }
    }
    return list;
  }

  @Override
  public List<Array> getArrays(FileArea fileArea) {
    List<Array> list = new ArrayList<Array>();

    if (fileArea instanceof FileAreaObservational) {
      list.addAll(getArrays((FileAreaObservational) fileArea));
    } else if (fileArea instanceof FileAreaBrowse) {
      list.addAll(getArrays((FileAreaBrowse) fileArea));
    } else if (fileArea instanceof FileAreaAncillary) {
      list.addAll(getArrays((FileAreaAncillary) fileArea));
    } else if (fileArea instanceof FileAreaObservationalSupplemental) {
      list.addAll(getArrays((FileAreaObservationalSupplemental) fileArea));
    }

    return list;
  }

  @Override
  public List<Array> getArrays(FileAreaObservational fileArea) {
    List<Array> list = new ArrayList<Array>();
    for (Object obj : fileArea.getDataObjects()) {
      if (obj instanceof Array) {
        list.add(Array.class.cast(obj));
      }
    }
    return list;
  }

  @Override
  public List<Array> getArrays(FileAreaBrowse fileArea) {
    List<Array> list = new ArrayList<Array>();
    for (Object obj : fileArea.getDataObjects()) {
      if (obj instanceof Array) {
        list.add(Array.class.cast(obj));
      }
    }
    return list;
  }

  @Override
  public List<Array2DImage> getArray2DImages(FileAreaObservational observationalFileArea) {
    ArrayList<Array2DImage> list = new ArrayList<Array2DImage>();
    for (Object obj : observationalFileArea.getDataObjects()) {
      if (obj.getClass().equals(Array2DImage.class)) {
        list.add(Array2DImage.class.cast(obj));
      }
    }
    return list;
  }

  public List<Array3DImage> getArray3DImages(FileAreaObservational observationalFileArea) {
    ArrayList<Array3DImage> list = new ArrayList<Array3DImage>();
    for (Object obj : observationalFileArea.getDataObjects()) {
      if (obj.getClass().equals(Array3DImage.class)) {
        list.add(Array3DImage.class.cast(obj));
      }
    }
    return list;
  }

  @Override
  public List<Array3DSpectrum> getArray3DSpectrums(FileAreaObservational observationalFileArea) {
    ArrayList<Array3DSpectrum> list = new ArrayList<>();
    for (Object obj : observationalFileArea.getDataObjects()) {
      if (obj.getClass().equals(Array3DSpectrum.class)) {
        list.add(Array3DSpectrum.class.cast(obj));
      }
    }
    return list;
  }

  public List<Object> getHeaderObjects(FileArea fileArea) {
    List<Object> list = new ArrayList<Object>();
    if (fileArea instanceof FileAreaObservational) {
      list.addAll(getHeaderObjects((FileAreaObservational) fileArea));
    } else if (fileArea instanceof FileAreaBrowse) {
      list.addAll(getHeaderObjects((FileAreaBrowse) fileArea));
    } else if (fileArea instanceof FileAreaAncillary) {
      list.addAll(getHeaderObjects((FileAreaAncillary) fileArea));
    } else if (fileArea instanceof FileAreaObservationalSupplemental) {
      list.addAll(getHeaderObjects((FileAreaObservationalSupplemental) fileArea));
    } else if (fileArea instanceof FileAreaMetadata) {
      list.addAll(getHeaderObjects((FileAreaMetadata) fileArea));
    } else if (fileArea instanceof FileAreaUpdate) {
      list.addAll(getHeaderObjects((FileAreaUpdate) fileArea));
    }
    return list;
  }

  @Override
  public List<Object> getHeaderObjects(FileAreaAncillary anciilaryFileArea) {
    Class<?> clazz;
    ArrayList<Object> list = new ArrayList<>();
    for (Object obj : anciilaryFileArea.getArraiesAndArray1DsAndArray2Ds()) {
      clazz = obj.getClass();
      if (clazz.equals(Header.class)) {
        list.add(obj);
      }
    }
    return list;
  }

  @Override
  public List<Object> getHeaderObjects(FileAreaObservational observationalFileArea) {
    Class<?> clazz;
    ArrayList<Object> list = new ArrayList<>();
    for (Object obj : observationalFileArea.getDataObjects()) {
      clazz = obj.getClass();
      if (clazz.equals(Header.class)) {
        list.add(obj);
      }
    }
    return list;
  }

  @Override
  public List<Object> getHeaderObjects(FileAreaBrowse browseFileArea) {
    Class<?> clazz;
    ArrayList<Object> list = new ArrayList<>();
    for (Object obj : browseFileArea.getDataObjects()) {
      clazz = obj.getClass();
      if (clazz.equals(Header.class)) {
        list.add(obj);
      }
    }
    return list;
  }

  public List<Object> getHeaderObjects(FileAreaObservationalSupplemental fileArea) {
    Class<?> clazz;
    ArrayList<Object> list = new ArrayList<Object>();
    for (Object obj : fileArea.getDataObjects()) {
      clazz = obj.getClass();
      if (clazz.equals(Header.class)) {
        list.add(obj);
      }
    }
    return list;
  }

  public List<Object> getHeaderObjects(FileAreaMetadata fileArea) {
    ArrayList<Object> list = new ArrayList<Object>();
    Header header = fileArea.getHeader();
    if (header != null)
      list.add(fileArea.getHeader());

    return list;
  }

  public List<Object> getHeaderObjects(FileAreaUpdate fileArea) {
    ArrayList<Object> list = new ArrayList<Object>();
    Header header = fileArea.getHeader();
    if (header != null)
      list.add(fileArea.getHeader());

    return list;
  }

  @Override
  public List<Object> getTableObjects(FileArea fileArea) {
    List<Object> list = new ArrayList<Object>();
    if (fileArea instanceof FileAreaObservational) {
      list.addAll(getTableObjects((FileAreaObservational) fileArea));
    } else if (fileArea instanceof FileAreaInventory) {
      list.add(((FileAreaInventory) fileArea).getInventory());
    } else if (fileArea instanceof FileAreaSIPDeepArchive) {
      list.add(((FileAreaSIPDeepArchive) fileArea).getManifestSIPDeepArchive());
    } else if (fileArea instanceof FileAreaTransferManifest) {
      list.add(((FileAreaTransferManifest) fileArea).getTransferManifest());
    } else if (fileArea instanceof FileAreaBrowse) {
      list.addAll(getTableObjects((FileAreaBrowse) fileArea));
    } else if (fileArea instanceof FileAreaAncillary) {
      list.addAll(getTableObjects((FileAreaAncillary) fileArea));
    } else if (fileArea instanceof FileAreaObservationalSupplemental) {
      list.add(getTableObjects((FileAreaObservationalSupplemental) fileArea));
    }
    return list;
  }

  @Override
  public List<Object> getTableObjects(FileAreaObservational observationalFileArea) {
    Class<?> clazz;
    ArrayList<Object> list = new ArrayList<Object>();
    for (Object obj : observationalFileArea.getDataObjects()) {
      clazz = obj.getClass();
      if (clazz.equals(TableCharacter.class) || clazz.equals(TableBinary.class)
          || clazz.equals(TableDelimited.class)) {
        list.add(obj);
      }
    }
    return list;
  }

  @Override
  public List<Object> getTableObjects(FileAreaBrowse browseFileArea) {
    Class<?> clazz;
    ArrayList<Object> list = new ArrayList<>();
    for (Object obj : browseFileArea.getDataObjects()) {
      clazz = obj.getClass();
      if (clazz.equals(TableCharacter.class) || clazz.equals(TableBinary.class)
          || clazz.equals(TableDelimited.class)) {
        list.add(obj);
      }
    }
    return list;
  }

  @Override
  public List<Object> getTableObjects(FileAreaAncillary anciilaryFileArea) {
    Class<?> clazz;
    ArrayList<Object> list = new ArrayList<>();
    for (Object obj : anciilaryFileArea.getArraiesAndArray1DsAndArray2Ds()) {
      clazz = obj.getClass();
      if (clazz.equals(TableCharacter.class) || clazz.equals(TableBinary.class)
          || clazz.equals(TableDelimited.class)) {
        list.add(obj);
      }
    }
    return list;
  }

  @Override
  public List<Object> getTableObjects(
      FileAreaObservationalSupplemental observationalFileAreaSupplemental) {
    Class<?> clazz;
    ArrayList<Object> list = new ArrayList<>();
    for (Object obj : observationalFileAreaSupplemental.getDataObjects()) {
      clazz = obj.getClass();
      if (clazz.equals(TableCharacter.class) || clazz.equals(TableBinary.class)
          || clazz.equals(TableDelimited.class)) {
        list.add(obj);
      }
    }
    return list;
  }

  @Override
  public List<Object> getTablesAndImages(FileArea fileArea) {
    List<Object> list = new ArrayList<>();
    if (fileArea instanceof FileAreaObservational) {
      list.addAll(getTablesAndImages((FileAreaObservational) fileArea));
    } else if (fileArea instanceof FileAreaInventory) {
      list.add(((FileAreaInventory) fileArea).getInventory());
    } else if (fileArea instanceof FileAreaSIPDeepArchive) {
      list.add(((FileAreaSIPDeepArchive) fileArea).getManifestSIPDeepArchive());
    } else if (fileArea instanceof FileAreaTransferManifest) {
      list.add(((FileAreaTransferManifest) fileArea).getTransferManifest());
    } else if (fileArea instanceof FileAreaBrowse) {
      list.addAll(getTablesAndImages((FileAreaBrowse) fileArea));
    } else if (fileArea instanceof FileAreaAncillary) {
      list.addAll(getTableObjects((FileAreaAncillary) fileArea));
    }
    return list;
  }

  public List<Object> getTablesAndImages(FileAreaObservational observationalFileArea) {
    List<Object> list = new ArrayList<Object>();
    Class<?> clazz;
    for (Object obj : observationalFileArea.getDataObjects()) {
      clazz = obj.getClass();
      if (clazz.equals(Array3DSpectrum.class) || clazz.equals(Array2DImage.class)
          || clazz.equals(Array3DImage.class) || clazz.equals(TableCharacter.class)
          || clazz.equals(TableBinary.class) || clazz.equals(TableDelimited.class)) {
        list.add(obj);
      }
    }
    return list;
  }

  @Override
  public List<Object> getTablesAndImages(FileAreaBrowse browseFileArea) {
    List<Object> list = new ArrayList<>();
    Class<?> clazz;
    for (Object obj : browseFileArea.getDataObjects()) {
      clazz = obj.getClass();
      if (clazz.equals(Array3DSpectrum.class) || clazz.equals(Array2DImage.class)
          || clazz.equals(Array3DImage.class) || clazz.equals(TableCharacter.class)
          || clazz.equals(TableBinary.class) || clazz.equals(TableDelimited.class)) {
        list.add(obj);
      }
    }
    return list;
  }

  @Override
  public List<TableCharacter> getTableCharacters(FileAreaObservational observationalFileArea) {
    ArrayList<TableCharacter> list = new ArrayList<TableCharacter>();
    for (Object obj : observationalFileArea.getDataObjects()) {
      if (obj.getClass().equals(TableCharacter.class)) {
        list.add(TableCharacter.class.cast(obj));
      }
    }
    return list;
  }

  @Override
  public List<TableBinary> getTableBinaries(FileAreaObservational observationalFileArea) {
    ArrayList<TableBinary> list = new ArrayList<TableBinary>();
    for (Object obj : observationalFileArea.getDataObjects()) {
      if (obj.getClass().equals(TableBinary.class)) {
        list.add(TableBinary.class.cast(obj));
      }
    }
    return list;
  }

  @Override
  public List<TableDelimited> getTableDelimiteds(FileAreaObservational observationalFileArea) {
    ArrayList<TableDelimited> list = new ArrayList<TableDelimited>();
    for (Object obj : observationalFileArea.getDataObjects()) {
      if (obj.getClass().equals(TableDelimited.class)) {
        list.add(TableDelimited.class.cast(obj));
      }
    }
    return list;
  }

  @Override
  public List<FieldCharacter> getFieldCharacters(TableCharacter table) {
    ArrayList<FieldCharacter> list = new ArrayList<FieldCharacter>();
    for (Object obj : table.getRecordCharacter().getFieldCharactersAndGroupFieldCharacters()) {
      if (obj.getClass().equals(FieldCharacter.class)) {
        list.add(FieldCharacter.class.cast(obj));
      }
    }
    return list;
  }

  @Override
  public List<FieldDelimited> getFieldDelimiteds(TableDelimited table) {
    ArrayList<FieldDelimited> list = new ArrayList<FieldDelimited>();
    for (Object obj : table.getRecordDelimited().getFieldDelimitedsAndGroupFieldDelimiteds()) {
      if (obj.getClass().equals(FieldDelimited.class)) {
        list.add(FieldDelimited.class.cast(obj));
      }
    }
    return list;
  }

  @Override
  public List<GroupFieldDelimited> getGroupFieldDelimiteds(TableDelimited table) {
    ArrayList<GroupFieldDelimited> list = new ArrayList<GroupFieldDelimited>();
    for (Object obj : table.getRecordDelimited().getFieldDelimitedsAndGroupFieldDelimiteds()) {
      if (obj.getClass().equals(GroupFieldDelimited.class)) {
        list.add(GroupFieldDelimited.class.cast(obj));
      }
    }
    return list;
  }

  @Override
  public List<Object> getFieldDelimitedAndGroupFieldDelimiteds(TableDelimited table) {
    ArrayList<Object> list = new ArrayList<Object>();
    for (Object obj : table.getRecordDelimited().getFieldDelimitedsAndGroupFieldDelimiteds()) {
      list.add(obj);
    }
    return list;
  }

  @Override
  public List<Object> getFieldCharacterAndGroupFieldCharacters(TableCharacter table) {
    ArrayList<Object> list = new ArrayList<Object>();
    for (Object obj : table.getRecordCharacter().getFieldCharactersAndGroupFieldCharacters()) {
      list.add(obj);
    }
    return list;
  }

  @Override
  public List<Object> getFieldBinaryAndGroupFieldBinaries(TableBinary table) {
    ArrayList<Object> list = new ArrayList<Object>();
    for (Object obj : table.getRecordBinary().getFieldBinariesAndGroupFieldBinaries()) {
      list.add(obj);
    }
    return list;
  }

  @Override
  public List<FieldBinary> getFieldBinaries(TableBinary table) {
    ArrayList<FieldBinary> list = new ArrayList<FieldBinary>();
    for (Object obj : table.getRecordBinary().getFieldBinariesAndGroupFieldBinaries()) {
      if (obj.getClass().equals(FieldBinary.class)) {
        list.add(FieldBinary.class.cast(obj));
      }
    }
    return list;
  }

  /**
   * Determines if this is a PDS convertible image.
   * 
   * @param child
   * @return true if this image file can be converted, otherwise false.
   */
  static boolean isConvertibleImage(String child) {
    return child.endsWith("raw"); // PTOOL-51 Need Spec, try <Array_2D_Image
                                  // base_class="Array_Base">
  }

  @Override
  public String getArchiveRoot() {
    return this.archiveRoot;
  }

  @Override
  public URL getRoot() {
    return this.root;
  }

  /**
   * Implements a validation event handler that attempts to continue the unmarshalling even if
   * errors are present. Only fatal errors will halt the unmarshalling.
   */
  private static class LenientEventHandler implements ValidationEventHandler {

    @Override
    public boolean handleEvent(ValidationEvent event) {
      if (event.getSeverity() == ValidationEvent.FATAL_ERROR) {
        System.err
            .println("Fatal error: " + event.getLocator().toString() + ": " + event.getMessage());
        return false;
      }

      return true;
    }

  }

  private static class WorkaroundClassLoader extends ClassLoader {

    private static final String IGNORED_RESOURCE =
        "META-INF/services/javax.xml.parsers.DocumentBuilderFactory";

    /**
     * Create a new instance with the given parent classloader.
     *
     * @param parent the parent classloader
     */
    public WorkaroundClassLoader(ClassLoader parent) {
      super(parent);
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
      if (!name.equals(IGNORED_RESOURCE)) {
        return super.findResources(name);
      } else {
        return new EmptyEnumeration<URL>();
      }
    }

    @Override
    protected URL findResource(String name) {
      if (!name.equals(IGNORED_RESOURCE)) {
        return super.findResource(name);
      } else {
        return null;
      }
    }

    @Override
    public URL getResource(String name) {
      if (!name.equals(IGNORED_RESOURCE)) {
        return super.getResource(name);
      } else {
        return null;
      }
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
      if (!name.equals(IGNORED_RESOURCE)) {
        return super.getResources(name);
      } else {
        return new EmptyEnumeration<URL>();
      }
    }

  }

  private static class EmptyEnumeration<T> implements Enumeration<T> {

    @Override
    public boolean hasMoreElements() {
      return false;
    }

    @Override
    public T nextElement() throws NoSuchElementException {
      throw new NoSuchElementException();
    }

  }

  public XMLLabelContext getXMLLabelContext() {
    return this.labelContext;
  }

  public static boolean isTableObject(Object obj) {
    Class<?> clazz = obj.getClass();
    if (clazz.equals(TableCharacter.class) || clazz.equals(TableBinary.class)
        || clazz.equals(TableDelimited.class)) {
      return true;
    }
    return false;
  }

  public static boolean isHeaderObject(Object obj) {
    Class<?> clazz = obj.getClass();
    if (clazz.equals(Header.class) || clazz.equals(EncodedHeader.class)) {
      return true;
    }
    return false;
  }

  public boolean isTextObject(Object obj) {
    Class<?> clazz = obj.getClass();
    if (clazz.equals(StreamText.class)) {
      return true;
    }
    return false;
  }

  @Override
  public long getOffset(Object obj) {
    // TODO improve this functionality and move to where it belongs?
    Class<?> clazz = obj.getClass();

    if (clazz.equals(StreamText.class)) {
      return (StreamText.class.cast(clazz).getOffset().getValue().longValueExact());
    } else if (clazz.equals(Header.class)) {
      return (Header.class.cast(clazz).getOffset().getValue().longValueExact());
    } else if (clazz.equals(EncodedHeader.class)) {
      return (EncodedHeader.class.cast(clazz).getOffset().getValue().longValueExact());
    } else if (clazz.equals(TableCharacter.class)) {
      return (TableCharacter.class.cast(obj).getOffset().getValue().longValueExact());
    } else if (clazz.equals(TableBinary.class)) {
      return (TableBinary.class.cast(obj).getOffset().getValue().longValueExact());
    } else if (clazz.equals(TableDelimited.class)) {
      return (TableDelimited.class.cast(obj).getOffset().getValue().longValueExact());
    } else if (clazz.equals(Array.class)) {
      return ((Array.class.cast(clazz)).getOffset().getValue().longValueExact());
    }

    if (clazz.equals(ParsableByteStream.class)) {
      return (ParsableByteStream.class.cast(clazz).getOffset().getValue().longValueExact());
    }
    // TODO use more generic functionality
    // ArrayList<Object> classes =
    // new ArrayList<>(Arrays.asList(Header.class, EncodedHeader.class, TableCharacter.class,
    // TableBinary.class, TableDelimited.class, Array.class, StreamText.class));
    // for (Object c: classes) {
    // if (c.getClass().equals(currentClazz)) {
    // return ((c.getClass().cast(c)).getObjectLength().getValue().longValueExact())
    // }
    // }
    return -1;
  }

  @Override
  public long getObjectLength(Object obj) {
    Class<?> clazz = obj.getClass();
    if (clazz.equals(StreamText.class)) {
      return ((StreamText.class.cast(clazz)).getObjectLength().getValue().longValueExact());
    }
    return -1;
  }
}
