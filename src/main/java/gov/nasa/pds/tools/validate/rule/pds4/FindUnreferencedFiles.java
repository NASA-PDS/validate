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
package gov.nasa.pds.tools.validate.rule.pds4;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.tools.validate.SkippedItems;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.GenericProblems;
import gov.nasa.pds.tools.validate.rule.ValidationTest;

/**
 * Implements a validation rule that checks that all files are referenced by some label.
 */
public class FindUnreferencedFiles extends AbstractValidationRule {
  private static final Logger LOG = LoggerFactory.getLogger(FindUnreferencedIdentifiers.class);

  private HashSet<String> buildReferencedFromSkipped() {
    HashSet<String> referencedBySkipped = new HashSet<String>();
    InputStream uis = null;
    final String closeTag = "</file_name>", openTag = "<file_name>";
    for (URL skipped : SkippedItems.getInstance().copy()) {
      try {
        int end = -1;
        uis = skipped.openStream();
        String content = new String(uis.readAllBytes(), Charset.defaultCharset());
        String path = this.extractPath(skipped);
        int begin = content.indexOf(openTag);

        while (-1 < begin) {
          end = content.indexOf(closeTag, begin);
          referencedBySkipped.add("file:"
              + Paths.get(path, content.substring(begin + openTag.length(), end)).toString());
          begin = content.indexOf(openTag, end + closeTag.length());
        }
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        if (uis != null) {
          try {
            uis.close();
          } catch (IOException e) {
          }
          uis = null;
        }
      }
    }
    return referencedBySkipped;
  }

  private String extractPath(URL url) {
    Path fullpath = FileUtils.toFile(url).toPath();
    return fullpath.getParent().toString();
  }

  @Override
  public boolean isApplicable(String location) {
    // This rule is applicable at the top level only.
    return getContext().isRootTarget();
  }

  /**
   * Iterate over unreferenced targets, reporting an error for each.
   */
  @ValidationTest
  public void findUnreferencedTargets() {
    LOG.debug("findUnreferencedTargets:getContext().getTarget(): {}", getContext().getTarget());
    LOG.debug(
        "findUnreferencedTargets:getContext().isRootTarget(),getContext().getAllowUnlabeledFiles() {},{}",
        getContext().isRootTarget(), getContext().getAllowUnlabeledFiles());
    LOG.debug("findUnreferencedTargets:getRegistrar().getUnreferencedTargets().size() {}",
        getRegistrar().getUnreferencedTargets().size());
    // Only run the test if we are the root target, to avoid duplicate errors.
    if (getContext().isRootTarget() && !getContext().getAllowUnlabeledFiles()) {
      HashSet<String> referencedBySkipped = this.buildReferencedFromSkipped();
      for (String location : getRegistrar().getUnreferencedTargets()) {
        LOG.debug("findUnreferencedTargets:location: {}", location);
        try {
          // issue42 - to skip Product level validation
          if (!getContext().getSkipProductValidation() && !referencedBySkipped.contains(location)) {
            reportError(PDS4Problems.UNLABELED_FILE, new URL(location), -1, -1);
          }
        } catch (MalformedURLException e) {
          reportError(GenericProblems.UNCAUGHT_EXCEPTION, getContext().getTarget(), -1, -1,
              e.getMessage());
        }
      }
    }
  }
}
