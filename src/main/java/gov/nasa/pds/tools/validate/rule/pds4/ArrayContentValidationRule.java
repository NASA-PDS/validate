// Copyright 2006-2018, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology Transfer
// at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id$
package gov.nasa.pds.tools.validate.rule.pds4;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gov.nasa.arc.pds.xml.generated.Array;
import gov.nasa.arc.pds.xml.generated.File;
import gov.nasa.arc.pds.xml.generated.FileArea;
import gov.nasa.arc.pds.xml.generated.FileAreaBrowse;
import gov.nasa.arc.pds.xml.generated.FileAreaObservational;
import gov.nasa.arc.pds.xml.generated.Product;
import gov.nasa.arc.pds.xml.generated.ProductBrowse;
import gov.nasa.arc.pds.xml.generated.ProductObservational;
import gov.nasa.pds.label.object.ArrayObject;
import gov.nasa.pds.objectAccess.ObjectAccess;
import gov.nasa.pds.objectAccess.ObjectProvider;
import gov.nasa.pds.objectAccess.ParseException;
import gov.nasa.pds.objectAccess.DataType.NumericDataType;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.SourceLocation;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.XPaths;
import gov.nasa.pds.tools.validate.content.array.ArrayContentProblem;
import gov.nasa.pds.tools.validate.content.array.ArrayContentValidator;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.ValidationTest;

public class ArrayContentValidationRule extends AbstractValidationRule {
  /** Used in evaluating xpath expressions. */
  private XPathFactory xPathFactory;
  
  public ArrayContentValidationRule() {
    xPathFactory = new net.sf.saxon.xpath.XPathFactoryImpl();
  }

  private static Object lock = new Object();

  @Override
  public boolean isApplicable(String location) {
	if (!getContext().getCheckData()) {
      return false;
    }
    boolean isApplicable = false;
    synchronized (lock) {
      // The rule is applicable if a label has been parsed and tables exist in the label.
      if (getContext().containsKey(PDS4Context.LABEL_DOCUMENT)) {
      Document label = getContext().getContextValue(PDS4Context.LABEL_DOCUMENT,
              Document.class);

      DOMSource source = new DOMSource(label);
        try {
          NodeList arrays = (NodeList) xPathFactory.newXPath().evaluate(
                  XPaths.ARRAYS, source, XPathConstants.NODESET);
          if (arrays.getLength() > 0) {
            isApplicable = true;
          }
        } catch (XPathExpressionException e) {
          //Ignore
        }

      }
    }
    return isApplicable;
  }

  @ValidationTest
  public void validateArray() throws MalformedURLException,
  URISyntaxException {
    URL target = getTarget();
    String targetFileName = target.toString().substring(target.toString().lastIndexOf("/") + 1);
    long t0 = System.currentTimeMillis();
    ObjectProvider objectAccess = null;
    objectAccess = new ObjectAccess(target);
    List<FileArea> arrayFileAreas = new ArrayList<FileArea>();
    try {
      arrayFileAreas = getArrayFileAreas(objectAccess);
    } catch (ParseException e) {
      //Ignore. Shouldn't happen
    }
    NodeList fileAreaNodes = null;

    XPath xpath = xPathFactory.newXPath();
    try {
      synchronized (lock) {
        fileAreaNodes = (NodeList) xpath.evaluate(
          XPaths.ARRAY_FILE_AREAS,
          new DOMSource(getContext().getContextValue(PDS4Context.LABEL_DOCUMENT, Document.class)),
          XPathConstants.NODESET);
      }
    } catch (XPathExpressionException e) {
      addXPathException(null, XPaths.ARRAY_FILE_AREAS, e.getMessage());
    }

    Map<String, Integer> numArrays = scanArrays(arrayFileAreas, objectAccess);
    Map<URL, Integer> arrayIndexes = new LinkedHashMap<URL, Integer>();
    int fileAreaObserveIndex = 0;
    for (FileArea fileArea : arrayFileAreas) {
      int arrayIndex = -1;
      String fileName = getDataFile(fileArea);

      if (fileName == null) {
        getListener().addProblem(
          new ValidationProblem(
            new ProblemDefinition(ExceptionType.FATAL,
              ProblemType.INVALID_LABEL, "Missing File Area."), target));
        continue;
      }

      URL dataFile = new URL(Utility.getParent(target), fileName);
      if (arrayIndexes.containsKey(dataFile)) {
        arrayIndex = arrayIndexes.get(dataFile).intValue() + 1;
        arrayIndexes.put(dataFile, arrayIndex);
      } else {
        arrayIndex = 1;
        arrayIndexes.put(dataFile, 1);
      }
      List<Array> arrayObjects = objectAccess.getArrays(fileArea);
      for (Array array : arrayObjects) {
        try {
          try {
            // Check if the data type is something we support
            String dataType = "";
            try {
              dataType = array.getElementArray().getDataType();
              Enum.valueOf(NumericDataType.class, dataType);
            } catch (IllegalArgumentException a) {
              throw new IllegalArgumentException(dataType
                      + " is not supported at this time.");
            }
            //The size of the array object is equal to the element size
            // times the product of the sizes of all axes
            // (i.e. total size =
            // number of lines * number of samples
            //               * number of bands * element size)
            ArrayObject ao = null;
            try {
              ao = new ArrayObject(
                      Utility.getParent(target),
                      getFileObject(fileArea),
                      array,
                      array.getOffset().getValue().longValueExact());

              //Array elements have values which conform to their data type

              //Verify that the elements match the object statistics defined
              //within their associated label, if they exist
              if (!array.getAxisArraies().isEmpty()) {
                ArrayContentValidator validator = new ArrayContentValidator(
                        getListener(), target, dataFile, arrayIndex);
                validator.setSpotCheckData(getContext().getSpotCheckData());
                validator.validate(array, ao);
              } else {
                addArrayProblem(ExceptionType.FATAL,
                        ProblemType.INVALID_LABEL,
                        "Missing Axis_Array area.",
                        dataFile,
                        arrayIndex);
              }
            } finally {
              if (ao != null) {
                ao.closeChannel();
              }
            }
          } catch (IllegalArgumentException ae) {
            addArrayProblem(ExceptionType.FATAL,
                    ProblemType.ARRAY_DATA_FILE_READ_ERROR,
                    "Error while reading array: " + ae.getMessage(),
                    dataFile,
                    arrayIndex);
          } catch (UnsupportedOperationException ue) {
            addArrayProblem(ExceptionType.WARNING,
                    ProblemType.ARRAY_INTERNAL_WARNING,
                    ue.getMessage(),
                    dataFile,
                    arrayIndex);
          } catch (OutOfMemoryError me) {
            addArrayProblem(ExceptionType.FATAL,
                    ProblemType.OUT_OF_MEMORY,
                    "Out of memory error occurred while processing array. "
                            + "Please adjust the JVM Heap "
                            + "Space settings and try again.", dataFile,
                    arrayIndex);
          }
        } catch (IOException io) {
          addArrayProblem(ExceptionType.FATAL,
                  ProblemType.ARRAY_DATA_FILE_READ_ERROR,
                  io.getMessage(),
                  dataFile,
                  arrayIndex);
        }
        arrayIndex++;
      }
      fileAreaObserveIndex++;
    }

    if (isDebugLogLevel()) {
      System.out.println("\nDEBUG  [" + ProblemType.TIMING_METRICS.getKey() + "]  " + System.currentTimeMillis() + " :: " + targetFileName + " :: validateArray() in " + (System.currentTimeMillis() - t0) + " ms\n");
    }
  }
  
  private void addArrayProblem(ExceptionType exceptionType, 
      ProblemType problemType, String message, URL dataFile, int array) {
    addArrayProblem(exceptionType, problemType, message, dataFile, array, 
        null);
  }
  
  private void addArrayProblem(ExceptionType exceptionType, 
      ProblemType problemType, String message,
      URL dataFile, int array, int[] location) {
        getListener().addProblem(
        new ArrayContentProblem(exceptionType,
            problemType,
            message, 
            dataFile, 
            getTarget(),
            array,
            location));
  }
  
  private Map<String, Integer> scanArrays(List<FileArea> fileAreas, 
      ObjectProvider objectAccess) {
    Map<String, Integer> results = new LinkedHashMap<String, Integer>();
    for (FileArea fileArea : fileAreas) {
      String dataFile = getDataFile(fileArea);
      List<Array> arrays = objectAccess.getArrays(fileArea);
      Integer numArrays = results.get(dataFile);
      if (numArrays == null) {
        results.put(dataFile, new Integer(arrays.size()));
      } else {
        numArrays = results.get(dataFile);
        numArrays = new Integer(numArrays.intValue() + arrays.size());
        results.put(dataFile, numArrays);
      }
    }
    return results;
  }
  
  /**
   * Gets the data file within the given FileArea.
   * 
   * @param fileArea The FileArea.
   * 
   * @return The data file name found within the given FileArea.
   */
  private String getDataFile(FileArea fileArea) {
    String result = "";
    try {
        if (fileArea instanceof FileAreaObservational) {
            result = ((FileAreaObservational) fileArea).getFile().getFileName();
        } else if (fileArea instanceof FileAreaBrowse) {
            result = ((FileAreaBrowse) fileArea).getFile().getFileName();
        }
    } catch (NullPointerException e) {
        // Null pointer means the file area is missing
        return null;
    }
    return result;
  }
  
  private File getFileObject(FileArea fileArea) {
    File file = null;
    if (fileArea instanceof FileAreaObservational) {
      file = ((FileAreaObservational) fileArea).getFile();
    } else if (fileArea instanceof FileAreaBrowse) {
      file = ((FileAreaBrowse) fileArea).getFile();
    }
    return file;
  }
  
  private List<FileArea> getArrayFileAreas(ObjectProvider objectAccess)
      throws ParseException {
    List<FileArea> results = new ArrayList<FileArea>();
    Product product = objectAccess.getProduct(getTarget(), Product.class);
    if (product instanceof ProductObservational) {
      results.addAll(((ProductObservational) product)
          .getFileAreaObservationals());
    } else if (product instanceof ProductBrowse) {
      results.addAll(((ProductBrowse) product).getFileAreaBrowses());
    }
    return results;
  }
  /**
   * Adds an XPath exception to the ProblemListener.
   * 
   * @param node The node associated with the XPath error.
   * @param xpath The XPath that caused the error.
   * @param message The reason why the XPath caused the error.
   */
  private void addXPathException(Node node, String xpath, String message) {
    int lineNumber = -1;
    if (node != null) {
      SourceLocation nodeLocation = 
        (SourceLocation) node.getUserData(
            SourceLocation.class.getName());
      lineNumber = nodeLocation.getLineNumber();
    }
    getListener().addProblem(
        new ArrayContentProblem(ExceptionType.FATAL,
            ProblemType.ARRAY_INTERNAL_ERROR,
            "Error evaluating XPath expression '" + xpath + "': " + message,
            getTarget(),
            getTarget(),
            lineNumber,
            null));
  }
}
