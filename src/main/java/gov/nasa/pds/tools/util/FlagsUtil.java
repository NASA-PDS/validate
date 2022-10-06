// Copyright 2021, by the California Institute of Technology.
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

package gov.nasa.pds.tools.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.tools.label.ExceptionType;

/**
 * Class that keep track of different flags from command line to be made available to other
 * validator that cannot easily be passed to.
 *
 */
public class FlagsUtil {
  // This class is not meant to be instantiated since its goal is to keep track of
  // flags from command line.
  private static Logger LOG = LoggerFactory.getLogger(FlagsUtil.class);

  /** Flag to enable/disable data content validation. */
  private static boolean contentValidationFlag = true;

  /** Flag to enable/disable product level validation. */
  private static boolean skipProductValidation = false;

  private static ExceptionType severityType;

  /** Flag to enable/disable stack trace printing. */
  private static boolean stackPrintingFlag = false;

  /**
   * Initialize flags to their default states
   *
   * @param None
   * @return None
   */
  public static void initialize() {
    FlagsUtil.contentValidationFlag = true;
    FlagsUtil.skipProductValidation = false;
    FlagsUtil.stackPrintingFlag = false;
    LOG.debug("initialize:contentValidationFlag {}", FlagsUtil.contentValidationFlag);
    LOG.debug("initialize:skipProductValidation {}", FlagsUtil.skipProductValidation);
  }

  /**
   * Set the stackPrintingFlag value
   *
   * @param flag The boolean value to set stackPrintingFlag to
   * @return None
   */
  public static void setStackPrintingFlag(boolean flag) {
    FlagsUtil.stackPrintingFlag = flag;
    LOG.debug("setStackPrintingFlag: {}", FlagsUtil.stackPrintingFlag);
  }

  /**
   * Get the stackPrintingFlag value
   *
   * @param None
   * @return the stackPrintingFlag value
   */
  public static boolean getStackPrintingFlag() {
    return (FlagsUtil.stackPrintingFlag);
  }

  /**
   * Set the contentValidationFlagvalue
   *
   * @param flag The boolean value to set contentValidationFlag to
   * @return None
   */
  public static void setContentValidationFlag(boolean flag) {
    FlagsUtil.contentValidationFlag = flag;
    LOG.debug("setContentValidationFlag: {}", FlagsUtil.contentValidationFlag);
  }

  /**
   * Get the contentValidationFlag value
   *
   * @param None
   * @return the contentValidationFlag value
   */
  public static boolean getContentValidationFlag() {
    return (FlagsUtil.contentValidationFlag);
  }

  /**
   * Set the skipProductValidation value
   *
   * @param flag The boolean value to set skipProductValidation to
   * @return None
   */
  public static void setSkipProductValidation(boolean flag) {
    FlagsUtil.skipProductValidation = flag;
    LOG.debug("setSkipProductValidation: {}", FlagsUtil.skipProductValidation);
  }

  /**
   * Get the skipProductValidation value
   *
   * @param None
   * @return the skipProductValidation valueNone
   */
  public static boolean getSkipProductValidation() {
    return (FlagsUtil.skipProductValidation);
  }

  /**
   * Set the severity value
   *
   * @param value The boolean value to set skipProductValidation to
   * @return None
   */
  public static void setSeverity(int level) {
    if (level == 0) {
      FlagsUtil.severityType = ExceptionType.DEBUG;
    } else if (level == 1) {
      FlagsUtil.severityType = ExceptionType.INFO;
    } else if (level == 2) {
      FlagsUtil.severityType = ExceptionType.WARNING;
    } else if (level == 3) {
      FlagsUtil.severityType = ExceptionType.ERROR;
    }
  }

  /**
   * Get the severity value
   *
   * @param None
   * @return the severityType value
   */
  public static ExceptionType getSeverity() {
    return (FlagsUtil.severityType);
  }
}
