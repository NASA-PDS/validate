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
// $Id: BundleValidator.java -1M 2010-11-01 17:33:35Z (local) $
package gov.nasa.pds.validate;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelException;
import gov.nasa.pds.validate.inventory.reader.InventoryEntry;
import gov.nasa.pds.validate.inventory.reader.InventoryReaderException;
import gov.nasa.pds.validate.inventory.reader.InventoryXMLReader;
import gov.nasa.pds.validate.report.Report;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sf.saxon.trans.XPathException;

import org.xml.sax.SAXParseException;

/**
 * A class that validates a PDS Bundle file.
 *
 * @author mcayanan
 *
 */
public class BundleValidator extends Validator {

  /**
   * Constructor.
   *
   * @param modelVersion The model version to use for validation.
   * @param report A Report to capture the results of the validation.
   */
  public BundleValidator(String modelVersion, Report report) {
        super(modelVersion, report);
    }

  /**
   * Validates a PDS bundle file and all files that it references inside
   * the file.
   *
   * @param bundle A PDS Bundle file.
   * @throws InventoryReaderException
   */
  public void validate(File bundle) throws InventoryReaderException {
    FileValidator fValidator = new FileValidator(modelVersion, report);
    if (schema != null) {
      fValidator.setSchema(schema);
    }
    InventoryXMLReader reader = null;
    try {
      fValidator.validate(bundle);
      reader = new InventoryXMLReader(bundle);
    } catch (InventoryReaderException ie) {
      throw ie;
    }  catch (Exception e) {
      LabelException le = null;
      if (e instanceof SAXParseException) {
        SAXParseException se = (SAXParseException) e;
        le = new LabelException(ExceptionType.FATAL, se.getMessage(),
            bundle.toString(), bundle.toString(), se.getLineNumber(),
            se.getColumnNumber());
      } else {
        le = new LabelException(ExceptionType.FATAL,
            e.getMessage(), bundle.toString(), bundle.toString(),
            null, null);
      }
      report.record(bundle.toURI(), le);
      return;
    }
    CollectionValidator cValidator = new CollectionValidator(modelVersion,
        report);
    if (schema != null) {
      cValidator.setSchema(schema);
    }
    List<LabelException> problems = new ArrayList<LabelException>();
    for (InventoryEntry entry = new InventoryEntry(); entry != null;) {
      try {
        if (!entry.isEmpty()) {
          try {
            cValidator.validate(entry.getFile());
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
                spe.getMessage(), bundle.toString(),
                bundle.toString(), spe.getLineNumber(), null));
          } else {
            problems.add(new LabelException(ExceptionType.FATAL,
                e.getMessage(), bundle.toString(),
                bundle.toString(), null, null));
          }
        } else {
          problems.add(new LabelException(ExceptionType.FATAL,
              e.getMessage(), bundle.toString(),
              bundle.toString(), null, null));
        }
      }
    }
    if (!problems.isEmpty()) {
      report.record(bundle.toURI(), problems);
    }
  }
}
