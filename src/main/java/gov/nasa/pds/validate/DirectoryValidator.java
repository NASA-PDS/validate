// Copyright 2006-2014, by the California Institute of Technology.
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
package gov.nasa.pds.validate;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelException;
import gov.nasa.pds.tools.label.MissingLabelSchemaException;
import gov.nasa.pds.validate.crawler.Crawler;
import gov.nasa.pds.validate.crawler.CrawlerFactory;
import gov.nasa.pds.validate.report.Report;
import gov.nasa.pds.validate.target.Target;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXParseException;

/**
 * Class that validates a directory containing PDS products.
 *
 * @author mcayanan
 *
 */
public class DirectoryValidator extends FileValidator {
  /** Flag to enable/disable recursion. */
  private boolean recurse;

  /** Holds a list of wildcard patterns for the file filter. */
  private List<String> fileFilters;

  /**
   * Constructor.
   *
   * @param modelVersion The model version to use for validation.
   * @param report A Report object to output the results.
   * @throws ParserConfigurationException 
   *
   */
  public DirectoryValidator(String modelVersion, Report report)
      throws ParserConfigurationException {
    super(modelVersion, report);
    recurse = true;
    fileFilters = new ArrayList<String>();
  }

  /**
   * Sets the recursion flag. By default, it is set to 'true'.
   *
   * @param value A boolean value.
   */
  public void setRecurse(boolean value) {
    recurse = value;
  }

  /**
   * Sets the file filter.
   *
   * @param filters A list of file patterns to look for while
   * traversing a directory.
   */
  public void setFileFilters(List<String> filters) {
    fileFilters = filters;
  }

  /**
   * Perform validation on a directory.
   *
   * @param directory A directory path to start traversing.
   * @throws Exception
   *
   */
  public void validate(URL directory) throws Exception {
    Crawler crawler = CrawlerFactory.newInstance(directory, recurse,
        fileFilters);
    List<Target> targets = crawler.crawl(directory);
    for (Target target : targets) {
      if (target.isDir()) {
        validate(target.getUrl());
      } else {
        try {
          super.validate(target.getUrl());
        } catch (Exception e) {
          LabelException le = null;
          if (e instanceof MissingLabelSchemaException) {
            MissingLabelSchemaException mse = (MissingLabelSchemaException) e;
            le = new LabelException(ExceptionType.WARNING, mse.getMessage(), 
                target.toString(), target.toString(), null, null);
            try {
              report.recordSkip(target.getUrl().toURI(), le);
            } catch (URISyntaxException u) {
              le = new LabelException(ExceptionType.FATAL,
                  e.getMessage(), target.toString(), target.toString(),
                  null, null);
            }
          } else {
            if (e instanceof SAXParseException) {
              SAXParseException se = (SAXParseException) e;
              le = new LabelException(ExceptionType.FATAL,
                  se.getMessage(), target.getUrl().toString(),
                  target.toString(), se.getLineNumber(),
                  se.getColumnNumber());
            } else {
              le = new LabelException(ExceptionType.FATAL,
                  e.getMessage(), target.toString(),
                  target.toString(), null, null);
            }
            try {
              report.record(target.getUrl().toURI(), le);
            } catch(URISyntaxException ignore) {
              //Ignore for now
            }
          }
        }
      }
    }
  }
}
