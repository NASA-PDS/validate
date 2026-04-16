// Copyright 2006-2017, by the California Institute of Technology.
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
package gov.nasa.pds.tools.validate.rule;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.TargetRegistrar;
import gov.nasa.pds.tools.validate.TargetType;
import gov.nasa.pds.tools.validate.crawler.Crawler;
import gov.nasa.pds.tools.validate.crawler.WildcardOSFilter;
import gov.nasa.pds.tools.validate.rule.pds4.PDS4Problems;

/**
 * Implements a rule that inserts this target into the target registry, if not already present, and
 * also adds all of its child targets.
 */
public class RegisterTargets extends AbstractValidationRule {

  private static final Logger LOG = LoggerFactory.getLogger(RegisterTargets.class);

  @Override
  public boolean isApplicable(String location) {
    return true;
  }

  @ValidationTest
  public void registerTargets() {
    TargetRegistrar registrar = getRegistrar();

    String targetLocation = getTarget().toString();
    String parentLocation = getParentTarget();
    TargetType type = Utility.getTargetType(getTarget());

    // Check for reserved filename pattern mismatches after determining product type
    checkReservedFilenameMismatch(getTarget(), type);

    if (registrar.getRoot() == null || !registrar.hasTarget(targetLocation)) {
      registrar.addTarget(parentLocation, type, targetLocation);
    }

    if (Utility.isDir(getTarget())) {
      try {
        Crawler crawler = getContext().getCrawler();
        WildcardOSFilter fileFilter = getContext().getFileFilters();
        if (!"PDS4 Directory".equalsIgnoreCase(getContext().getRule().getCaption())) {
          fileFilter = new WildcardOSFilter(Arrays.asList(new String[] {"*"}));
        }
        for (Target child : crawler.crawl(getTarget(), getContext().isRecursive(), fileFilter)) {
          try {
            String childLocation = child.getUrl().toURI().normalize().toString();
            TargetType childType = Utility.getTargetType(child.getUrl());

            // Check for reserved filename pattern mismatches for child targets
            checkReservedFilenameMismatch(child.getUrl(), childType);

            registrar.addTarget(targetLocation, childType, childLocation);
          } catch (URISyntaxException e) {
            reportError(GenericProblems.UNCAUGHT_EXCEPTION, getContext().getTarget(), -1, -1,
                e.getMessage());
          }
        }
      } catch (Exception e) {
        reportError(GenericProblems.UNCAUGHT_EXCEPTION, getContext().getTarget(), -1, -1,
            e.getMessage());
      }
    }
  }

  /**
   * Check if a file uses a reserved filename pattern (collection_*, bundle_*, etc.) but the product
   * type doesn't match the expected type. According to PDS4 Standards Reference 6C.1.3, certain file
   * names are reserved for specific purposes.
   *
   * @param target The URL of the target file
   * @param type The determined TargetType (already computed by Utility.getTargetType())
   */
  private void checkReservedFilenameMismatch(URL target, TargetType type) {
    // Only check label files, not directories
    if (type == TargetType.DIRECTORY) {
      return;
    }

    // Extract filename using URI/File for safer handling
    String filename;
    try {
      filename = new File(target.toURI()).getName();
    } catch (URISyntaxException e) {
      LOG.warn("Failed to extract filename from URL {}: {}", target, e.getMessage());
      return;
    }

    // PDS4 SR 6C.1.3 reserved name rules apply only to label files
    String extension = FilenameUtils.getExtension(filename).toLowerCase();
    if (!extension.equals("xml") && !extension.equals("lblx")) {
      return;
    }

    String basename = FilenameUtils.getBaseName(filename);
    LOG.debug("checkReservedFilenameMismatch:target,filename,basename,type {},{},{},{}", target,
        filename, basename, type);

    // Check if filename matches reserved patterns
    boolean isReservedCollectionPattern = basename.toLowerCase().startsWith("collection_");
    boolean isReservedBundlePattern = basename.toLowerCase().startsWith("bundle_");

    if (!isReservedCollectionPattern && !isReservedBundlePattern) {
      // Filename doesn't match any reserved patterns, no validation needed
      return;
    }

    // Check for mismatch between filename pattern and actual product type
    if (isReservedCollectionPattern && type != TargetType.COLLECTION) {
      String message = String.format(
          "File name '%s' uses the reserved 'collection_' pattern but the product type is not Product_Collection. "
              + "Reserved file names should only be used for their intended product types (PDS4 SR 6C.1.3).",
          filename);
      reportError(PDS4Problems.RESERVED_FILE_NAME_MISMATCH, target, -1, -1, message);
      LOG.warn("checkReservedFilenameMismatch: {}", message);
    } else if (isReservedBundlePattern && type != TargetType.BUNDLE) {
      String message = String.format(
          "File name '%s' uses the reserved 'bundle_' pattern but the product type is not Product_Bundle. "
              + "Reserved file names should only be used for their intended product types (PDS4 SR 6C.1.3).",
          filename);
      reportError(PDS4Problems.RESERVED_FILE_NAME_MISMATCH, target, -1, -1, message);
      LOG.warn("checkReservedFilenameMismatch: {}", message);
    }
  }
}
