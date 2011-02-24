// Copyright 2006-2010, by the California Institute of Technology.
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
import gov.nasa.pds.validate.crawler.DirectoryCrawler;
import gov.nasa.pds.validate.report.Report;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXParseException;

/**
 * Class that validates a directory containing PDS products.
 *
 * @author mcayanan
 *
 */
public class DirectoryValidator extends Validator {
  /** Flag to enable/disable recursion. */
  private boolean recurse;

  /** Holds a list of wildcard patterns for the file filter. */
  private List<String> fileFilters;

  /**
   * Constructor.
   *
   * @param report A Report object to output the results.
   *
   */
  public DirectoryValidator(Report report) {
    super(report);
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
   *
   */
  public void validate(File directory) {
    DirectoryCrawler crawler = new DirectoryCrawler();
    List<File> targets = crawler.crawl(directory, recurse, fileFilters);
    for (File target : targets) {
      if (target.isDirectory()) {
        validate(target);
      } else {
        FileValidator fv = new FileValidator(report);
        if (schema != null) {
          fv.setSchema(schema);
        }
        try {
          fv.validate(target);
        } catch (Exception e) {
          LabelException le = null;
          if (e instanceof SAXParseException) {
            SAXParseException se = (SAXParseException) e;
            le = new LabelException(ExceptionType.FATAL,
                se.getMessage(), target.toString(),
                target.toString(), se.getLineNumber(),
                se.getColumnNumber());
          } else {
            le = new LabelException(ExceptionType.FATAL,
                e.getMessage(), target.toString(),
                target.toString(), null, null);
          }
          report.record(target.toURI(), le);
        }
      }
    }
  }
}
