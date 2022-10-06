package gov.nasa.pds.tools.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*--->
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.util.FileSizesUtil;
import gov.nasa.pds.tools.util.LabelParser;
import gov.nasa.pds.tools.util.MD5Checksum;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.ValidationTarget;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.ValidationTest;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.tree.tiny.TinyNodeImpl;
<---*/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileReferencedMapList {

  private static final Logger LOG = LoggerFactory.getLogger(FileReferencedMapList.class);

  // Class to hold a map of every labels that referenced a physical file.
  // If a file is referenced by more than one label, an error should be thrown.
  class FileReferencedMap {
    private URL url = null;
    private List<String> labelNames = new ArrayList<>();

    public FileReferencedMap(URL url) {
      this.url = url;
    }

    public void setLabels(String labelName) {
      this.labelNames.add(labelName);
    }

    public List<String> getLabels() {
      return (this.labelNames);
    }

    @Override
    public String toString() {
      return (Arrays.toString(labelNames.toArray()));
    }

    public int getNumLabelsReferencedFile() {
      return (this.labelNames.size());
    }
  }

  private Map<URL, FileReferencedMap> fileReferencedMaps = new HashMap<>();

  public FileReferencedMap setLabels(URL url, String labelName) {
    FileReferencedMap fileReferencedMap = fileReferencedMaps.get(url);
    if (fileReferencedMap != null) {
      // If fileReferencedMaps does contain an existing, url, add the labelName to the
      // existing list.
      fileReferencedMap.setLabels(labelName);
      fileReferencedMaps.put(url, fileReferencedMap);
    } else {
      // If fileReferencedMaps does not contain an existing, url, create a new
      // FileReferencedMap and add labelName to the new empty list.
      fileReferencedMap = new FileReferencedMap(url);
      fileReferencedMap.setLabels(labelName);
      fileReferencedMaps.put(url, fileReferencedMap);
    }
    LOG.debug("FileReferencedMapList:url,fileReferencedMap.toString() {},{}", url,
        fileReferencedMap.toString());
    LOG.debug("FileReferencedMapList:url,getNumLabelsReferencedFile() {},{}", url,
        fileReferencedMap.getNumLabelsReferencedFile());
    // if (fileReferencedMap.getNumLabelsReferencedFile() > 1) {
    // LOG.error("File " + url.toString() + " is referenced by more than one labels " +
    // fileReferencedMap.toString());
    // System.exit(1);
    // }
    return (fileReferencedMap);
  }

  public List<String> getLabels(URL url) {
    FileReferencedMap fileReferencedMap = fileReferencedMaps.get(url);
    if (fileReferencedMap != null) {
      return (fileReferencedMap.getLabels());
    }
    return (new ArrayList<>());
  }
}
