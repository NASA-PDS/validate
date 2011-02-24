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
// $Id: CollectionValidator.java -1M 2010-11-01 17:33:35Z (local) $
package gov.nasa.pds.validate;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelException;
import gov.nasa.pds.validate.inventory.reader.InventoryEntry;
import gov.nasa.pds.validate.inventory.reader.InventoryReaderException;
import gov.nasa.pds.validate.inventory.reader.InventoryTableReader;
import gov.nasa.pds.validate.report.Report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import net.sf.saxon.trans.XPathException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Class that validates a PDS Collection file.
 *
 * @author mcayanan
 *
 */
public class CollectionValidator extends Validator {

  /**
   * Constructor.
   *
   * @param report A Report to capture the results of the validation.
   */
  public CollectionValidator(Report report) {
    super(report);
  }

  /**
   * Validates a PDS Collection file and all files that it references to
   * inside the file.
   *
   * @param collection A PDS Collection file.
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   * @throws XPathExpressionException
   *
   */
  public void validate(File collection) throws InventoryReaderException,
  XPathExpressionException, SAXException, IOException,
  ParserConfigurationException {
    FileValidator fValidator = new FileValidator(report);
    if (schema != null) {
      fValidator.setSchema(schema);
    }
    fValidator.validate(collection);
    InventoryTableReader reader = new InventoryTableReader(collection);
    List<LabelException> problems = new ArrayList<LabelException>();
    for (InventoryEntry entry = new InventoryEntry(); entry != null;) {
      try {
        if (!entry.isEmpty()) {
          try {
            fValidator.validate(entry.getFile());
          }  catch (Exception e) {
            LabelException le = null;
            if (e instanceof SAXParseException) {
              SAXParseException se = (SAXParseException) e;
              le = new LabelException(ExceptionType.FATAL, se.getMessage(),
                  entry.getFile().toString(), entry.getFile().toString(),
                  se.getLineNumber(), se.getColumnNumber());
            } else {
              le = new LabelException(ExceptionType.FATAL,
                  e.getMessage(), entry.getFile().toString(),
                  entry.getFile().toString(), null, null);
            }
            report.record(entry.getFile().toURI(), le);
          }
        }
        entry = reader.getNext();
      } catch (InventoryReaderException e) {
        if (e.getException() instanceof XPathException) {
          XPathException xe = (XPathException) e.getException();
          if (xe.getException() instanceof SAXParseException) {
            SAXParseException spe = (SAXParseException) xe.getException();
            problems.add(new LabelException(ExceptionType.FATAL,
                spe.getMessage(), collection.toString(),
                collection.toString(), spe.getLineNumber(), null));
          } else {
            problems.add(new LabelException(ExceptionType.FATAL,
                e.getMessage(), collection.toString(),
                collection.toString(), null, null));
          }
        } else {
            problems.add(new LabelException(ExceptionType.FATAL,
                e.getMessage(), collection.toString(),
                collection.toString(), e.getLineNumber(), null));
        }
      }
    }
    // This makes sure to log any problems in the collection data file.
    if (!problems.isEmpty()) {
      report.record(reader.getDataFile().toURI(), problems);
    }
  }
}
