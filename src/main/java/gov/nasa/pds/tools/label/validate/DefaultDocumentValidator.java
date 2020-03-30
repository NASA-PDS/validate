//  Copyright 2009-2018, by the California Institute of Technology.
//  ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
//  Any commercial use must be negotiated with the Office of Technology
//  Transfer at the California Institute of Technology.
//
//  This software is subject to U. S. export control laws and regulations
//  (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
//  is subject to U.S. export control laws and regulations, the recipient has
//  the responsibility to obtain export licenses or other export authority as
//  may be required before exporting such information to foreign countries or
//  providing access to foreign nationals.
//
//  $Id$
//
package gov.nasa.pds.tools.label.validate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.util.VersionInfo;
import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemHandler;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.validate.constants.Constants;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.tree.tiny.TinyNodeImpl;

import javax.xml.transform.Source;

/**
 * The intent of this class is to perform some default semantic validation
 * on the parsed PDS4 label.
 *
 * @author mcayanan
 *
 */
public class DefaultDocumentValidator implements DocumentValidator {

  private final String XML_MODEL_XPATH = 
      "/processing-instruction('xml-model')";

  @Override
  public boolean validate(ProblemHandler handler, Source xml) {
    boolean passFlag = true;
    // Check the xml-model processing instructions specification
    List<TinyNodeImpl> xmlModels = new ArrayList<TinyNodeImpl>();
    try {
      XMLExtractor extractor = new XMLExtractor(xml);
      xmlModels = extractor.getNodesFromDoc(XML_MODEL_XPATH);
    } catch (Exception e) {
      //Ignore
    }
    URL systemIdUrl = null;
    try {
      systemIdUrl = new URL(xml.getSystemId());
    } catch (MalformedURLException mu) {
      //Ignore. Should not happen!!!
    }
    if (xmlModels.isEmpty()) {
      handler.addProblem(new ValidationProblem(
          new ProblemDefinition(ExceptionType.WARNING,
              ProblemType.MISSING_SCHEMATRON_SPEC,
              "No schematron specification found in the label."), 
          systemIdUrl));
    } else {
      Pattern pattern = Pattern.compile(Constants.SCHEMATRON_SCHEMATYPENS_PATTERN);
      for (TinyNodeImpl xmlModel : xmlModels) {
        String filteredValue = 
            xmlModel.getStringValue().replaceAll("\\s+", " ");
        filteredValue = filteredValue.trim();
        Matcher matcher = pattern.matcher(filteredValue);
        if (matcher.matches()) {
          if (matcher.group(3) != null) {
            if (!VersionInfo.getSchematronNamespace().equals(
                matcher.group(3).trim())) {
              handler.addProblem(
                  new ValidationProblem(
                      new ProblemDefinition(
                          ExceptionType.WARNING,
                          ProblemType.BAD_SCHEMATYPENS,
                          "Value of the 'schematypens' attribute, '"
                              + matcher.group(3).trim() + "', "
                              + "in the schematron specification is not "
                              + "equal to the recommended value of '"
                              + VersionInfo.getSchematronNamespace() + "'."),
                    systemIdUrl,
                    xmlModel.getLineNumber(),
                    -1));
              }
          } else {
            handler.addProblem(new ValidationProblem(
                new ProblemDefinition(
                    ExceptionType.WARNING,
                    ProblemType.MISSING_SCHEMATYPENS,
                    "Missing 'schematypens' attribute from the "
                    + "schematron specification."),
                systemIdUrl,
                xmlModel.getLineNumber(),
                -1));
          }
        }
      }
    }
    return passFlag;
  }
}
