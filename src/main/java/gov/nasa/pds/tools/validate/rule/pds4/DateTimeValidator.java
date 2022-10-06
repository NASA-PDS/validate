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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import gov.nasa.pds.label.object.FieldType;

/**
 * Class to validate datetime values.
 *
 * @author mcayanan
 *
 */
public class DateTimeValidator {

  /**
   * Valid Day Of Year formats.
   * 
   */
  private static List<String> DOY_FORMATS = Arrays.asList("(-)?[0-9]{4}(Z?)",
      "(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-6]))))(Z?)");

  /**
   * Valid Day of Year datetime formats.
   * 
   */
  private static List<String> DATE_TIME_DOY_FORMATS = Arrays.asList("(-)?[0-9]{4}(Z?)",
      "(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-6]))))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z?)",
      "(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-6]))))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:(([0-5][0-9])|60)(\\.([0-9]{1,6}))?(Z?)",
      "(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-6]))))(T)(([0-1][0-9])|(2[0-4]))(Z?)",
      "(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-6]))))(T)24((:00)|(:00:00))?(Z?)",
      "(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-6]))))(T)24:00:00(\\.([0]{1,6}))(Z?)",
      "(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-6]))))(Z?)");

  /**
   * Valid datetime UTC formats.
   * 
   */
  private static List<String> DATE_TIME_DOY_UTC_FORMATS = Arrays.asList("(-)?[0-9]{4}(Z)",
      "(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-6]))))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z)",
      "(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-6]))))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:(([0-5][0-9])|60)(\\.([0-9]{1,6}))?(Z)",
      "(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-6]))))(T)(([0-1][0-9])|(2[0-4]))(Z)",
      "(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-6]))))(T)24((:00)|(:00:00))?(Z)",
      "(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-6]))))(T)24:00:00(\\.([0]{1,6}))(Z)",
      "(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-6]))))(Z)");

  /**
   * Valid datetime year month day formats.
   * 
   */
  private static List<String> DATE_TIME_YMD_FORMATS = Arrays.asList("(-)?[0-9]{4}(Z?)",
      "(-)?[0-9]{4}-((0[1-9])|(1[0-2]))(Z?)",
      "(-)?[0-9]{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2][0-9])|(3[0-1]))(T)(([0-1][0-9])|(2[0-3]))(Z?)",
      "(-)?[0-9]{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2][0-9])|(3[0-1]))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z?)",
      "(-)?[0-9]{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2][0-9])|(3[0-1]))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:(([0-5][0-9])|60)(\\.([0-9]{1,6}))?(Z?)",
      "(-)?[0-9]{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2][0-9])|(3[0-1]))(T)24((:00)|(:00:00))?(Z?)",
      "(-)?[0-9]{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2][0-9])|(3[0-1]))(T)24:00:00(\\.([0]{1,6}))(Z?)",
      "(-)?[0-9]{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2][0-9])|(3[0-1]))(Z?)");

  /**
   * Valid datetime year month day UTC formats.
   * 
   */
  private static List<String> DATE_TIME_YMD_UTC_FORMATS = Arrays.asList("(-)?[0-9]{4}(Z)",
      "(-)?[0-9]{4}-((0[1-9])|(1[0-2]))(Z)",
      "(-)?[0-9]{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2][0-9])|(3[0-1]))(T)(([0-1][0-9])|(2[0-3]))(Z)",
      "(-)?[0-9]{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2][0-9])|(3[0-1]))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z)",
      "(-)?[0-9]{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2][0-9])|(3[0-1]))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:(([0-5][0-9])|60)(\\.([0-9]{1,6}))?(Z)",
      "(-)?[0-9]{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2][0-9])|(3[0-1]))(T)24((:00)|(:00:00))?(Z)",
      "(-)?[0-9]{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2][0-9])|(3[0-1]))(T)24:00:00(\\.([0]{1,6}))(Z)",
      "(-)?[0-9]{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2][0-9])|(3[0-1]))(Z)");

  /**
   * Valid year month day formats.
   * 
   */
  private static List<String> DATE_YMD_FORMATS =
      Arrays.asList("(-)?[0-9]{4}(Z?)", "(-)?[0-9]{4}-((0[1-9])|(1[0-2]))(Z?)",
          "(-)?[0-9]{4}-((0[1-9])|(1[0-2]))-((0[1-9])|([1-2][0-9])|(3[0-1]))(Z?)");

  private static List<String> TIME_FORMATS = Arrays.asList("(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z?)",
      "(([0-1][0-9])|(2[0-3])):[0-5][0-9]:(([0-5][0-9])|60)((\\.[0-9]+)|)(Z?)",
      "(([0-1][0-9])|(2[0-4]))(Z?)", "24:00((:00((\\.0+)|))|)(Z?)");

  /**
   * Mapping of field datetime types to its list of valid datetime formats.
   * 
   */
  public static final HashMap<String, List<String>> DATE_TIME_FORMATS = new HashMap<>();
  static {
    DATE_TIME_FORMATS.put(FieldType.ASCII_DATE_DOY.getXMLType(), DOY_FORMATS);
    DATE_TIME_FORMATS.put(FieldType.ASCII_DATE_TIME_DOY.getXMLType(), DATE_TIME_DOY_FORMATS);
    DATE_TIME_FORMATS.put(FieldType.ASCII_DATE_TIME_DOY_UTC.getXMLType(),
        DATE_TIME_DOY_UTC_FORMATS);
    DATE_TIME_FORMATS.put(FieldType.ASCII_DATE_TIME_YMD.getXMLType(), DATE_TIME_YMD_FORMATS);
    DATE_TIME_FORMATS.put(FieldType.ASCII_DATE_TIME_YMD_UTC.getXMLType(),
        DATE_TIME_YMD_UTC_FORMATS);
    DATE_TIME_FORMATS.put(FieldType.ASCII_DATE_YMD.getXMLType(), DATE_YMD_FORMATS);
    DATE_TIME_FORMATS.put(FieldType.ASCII_TIME.getXMLType(), TIME_FORMATS);
  }

  /**
   * Checks to see if the given datetime value matches its defined data type.
   * 
   * @param type The datetime type.
   * @param value The value to check against.
   * 
   * @return 'true' if the value matches its data type. 'false' otherwise.
   * 
   * @throws Exception
   */
  public static boolean isValid(FieldType type, String value) throws Exception {
    boolean success = false;
    if (!DATE_TIME_FORMATS.containsKey(type.getXMLType())) {
      throw new Exception("'" + type.getXMLType() + "' is not one of the valid datetime formats: "
          + DATE_TIME_FORMATS.toString());
    }
    for (String format : DATE_TIME_FORMATS.get(type.getXMLType())) {
      if (Pattern.matches(format, value.trim())) {
        success = true;
        break;
      }
    }
    return success;
  }
}
