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
  private static List<Pattern> DOY_FORMATS = Arrays.asList(
      Pattern.compile("(-)?[0-9]{4}(Z?)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96))-366(Z?)"),
      Pattern.compile("(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-5]))))(Z?)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-366(Z?)"));

  /**
   * Valid Day of Year datetime formats.
   * 
   */
  private static List<Pattern> DATE_TIME_DOY_FORMATS = Arrays.asList(
      Pattern.compile("(-)?[0-9]{4}(Z?)"),
      Pattern.compile("(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-5]))))(Z?)"),
      Pattern.compile("(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-5]))))(T)(([0-1][0-9])|(2[0-3]))(Z?)"),
      Pattern.compile("(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-5]))))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z?)"),
      Pattern.compile("(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-5]))))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z?)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96))-366(Z?)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96))-366(T)(([0-1][0-9])|(2[0-3]))(Z?)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96))-366(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z?)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96))-366(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z?)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-366(Z?)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-366(T)(([0-1][0-9])|(2[0-3]))(Z?)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-366(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z?)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-366(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z?)"),
      Pattern.compile("(1981|1982|1983|1985|1993|1994|1997|2015)-181T23:59:60(\\.[0-9]+)?(Z?)"),
      Pattern.compile("(1972|1992|2012)-182T23:59:60(\\.[0-9]+)?(Z?)"),
      Pattern.compile("(1971|1973|1974|1975|1977|1978|1979|1987|1989|1990|1995|1998|2005)-365T23:59:60(\\.[0-9]+)?(Z?)"),
      Pattern.compile("(1972|1976|2008|2016)-366T23:59:60(\\.[0-9]+)?(Z?)"));

  /**
   * Valid datetime UTC formats.
   * 
   */
  private static List<Pattern> DATE_TIME_DOY_UTC_FORMATS = Arrays.asList(
      Pattern.compile("(-)?[0-9]{4}(Z)"),
      Pattern.compile("(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-5]))))(Z)"),
      Pattern.compile("(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-5]))))(T)(([0-1][0-9])|(2[0-3]))(Z)"),
      Pattern.compile("(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-5]))))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z)"),
      Pattern.compile("(-)?[0-9]{4}-((00[1-9])|(0[1-9][0-9])|([1-2][0-9][0-9])|(3(([0-5][0-9])|(6[0-5]))))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96))-366(Z)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96))-366(T)(([0-1][0-9])|(2[0-3]))(Z)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96))-366(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96))-366(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-366(Z)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-366(T)(([0-1][0-9])|(2[0-3]))(Z)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-366(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-366(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z)"),
      Pattern.compile("(1981|1982|1983|1985|1993|1994|1997|2015)-181T23:59:60(\\.[0-9]+)?(Z)"),
      Pattern.compile("(1972|1992|2012)-182T23:59:60(\\.[0-9]+)?(Z)"),
      Pattern.compile("(1971|1973|1974|1975|1977|1978|1979|1987|1989|1990|1995|1998|2005)-365T23:59:60(\\.[0-9]+)?(Z)"),
      Pattern.compile("(1972|1976|2008|2016)-366T23:59:60(\\.[0-9]+)?(Z)"));

  /**
   * Valid datetime year month day formats.
   * 
   */
  private static List<Pattern> DATE_TIME_YMD_FORMATS = Arrays.asList(
      Pattern.compile("(-)?[0-9]{4}(Z?)"),
      Pattern.compile("(-)?[0-9]{4}-((0[1-9])|(1[0-2]))(Z?)"),
      Pattern.compile("(-)?[0-9]{4}-(02-((0[1-9])|(1[0-9])|(2[0-8])))(Z?)"),
      Pattern.compile("(-)?[0-9]{4}-((04|06|09|11)-((0[1-9])|([1-2][0-9])|30))(Z?)"),
      Pattern.compile("(-)?[0-9]{4}-((01|03|05|07|08|10|12)-((0[1-9])|([1-2][0-9])|(30|31)))(Z?)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)-02-29)(Z?)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-02-29(Z?)"),
      Pattern.compile("(-)?[0-9]{4}-(02-((0[1-9])|(1[0-9])|(2[0-8])))(T)(([0-1][0-9])|(2[0-3]))(Z?)"),
      Pattern.compile("(-)?[0-9]{4}-((04|06|09|11)-((0[1-9])|([1-2][0-9])|30))(T)(([0-1][0-9])|(2[0-3]))(Z?)"),
      Pattern.compile("(-)?[0-9]{4}-((01|03|05|07|08|10|12)-((0[1-9])|([1-2][0-9])|(30|31)))(T)(([0-1][0-9])|(2[0-3]))(Z?)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)-02-29)(T)(([0-1][0-9])|(2[0-3]))(Z?)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-02-29(T)(([0-1][0-9])|(2[0-3]))(Z?)"),
      Pattern.compile("(-)?[0-9]{4}-(02-((0[1-9])|(1[0-9])|(2[0-8])))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z?)"),
      Pattern.compile("(-)?[0-9]{4}-((04|06|09|11)-((0[1-9])|([1-2][0-9])|30))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z?)"),
      Pattern.compile("(-)?[0-9]{4}-((01|03|05|07|08|10|12)-((0[1-9])|([1-2][0-9])|(30|31)))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z?)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)-02-29)(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z?)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-02-29(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z?)"),
      Pattern.compile("(-)?[0-9]{4}-(02-((0[1-9])|(1[0-9])|(2[0-8])))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z?)"),
      Pattern.compile("(-)?[0-9]{4}-((04|06|09|11)-((0[1-9])|([1-2][0-9])|30))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z?)"),
      Pattern.compile("(-)?[0-9]{4}-((01|03|05|07|08|10|12)-((0[1-9])|([1-2][0-9])|(30|31)))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z?)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)-02-29)(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z?)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-02-29(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z?)"),
      Pattern.compile("((1971|1972|1973|1974|1975|1976|1977|1978|1979|1987|1989|1990|1995|1998|2005|2008|2016)-12-31T23:59:60(\\.[0-9]+)?(Z?)|(1972|1981|1982|1983|1985|1992|1993|1994|1997|2012|2015)-06-30T23:59:60(\\.[0-9]+)?(Z?))"));

  /**
   * Valid datetime year month day UTC formats.
   * 
   */
  private static List<Pattern> DATE_TIME_YMD_UTC_FORMATS = Arrays.asList(
      Pattern.compile("(-)?[0-9]{4}(Z)"),
      Pattern.compile("(-)?[0-9]{4}-((0[1-9])|(1[0-2]))(Z)"),
      Pattern.compile("(-)?[0-9]{4}-(02-((0[1-9])|(1[0-9])|(2[0-8])))(Z)"),
      Pattern.compile("(-)?[0-9]{4}-((04|06|09|11)-((0[1-9])|([1-2][0-9])|30))(Z)"),
      Pattern.compile("(-)?[0-9]{4}-((01|03|05|07|08|10|12)-((0[1-9])|([1-2][0-9])|(30|31)))(Z)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)-02-29)(Z)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-02-29(Z)"),
      Pattern.compile("(-)?[0-9]{4}-(02-((0[1-9])|(1[0-9])|(2[0-8])))(T)(([0-1][0-9])|(2[0-3]))(Z)"),
      Pattern.compile("(-)?[0-9]{4}-((04|06|09|11)-((0[1-9])|([1-2][0-9])|30))(T)(([0-1][0-9])|(2[0-3]))(Z)"),
      Pattern.compile("(-)?[0-9]{4}-((01|03|05|07|08|10|12)-((0[1-9])|([1-2][0-9])|(30|31)))(T)(([0-1][0-9])|(2[0-3]))(Z)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)-02-29)(T)(([0-1][0-9])|(2[0-3]))(Z)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-02-29(T)(([0-1][0-9])|(2[0-3]))(Z)"),
      Pattern.compile("(-)?[0-9]{4}-(02-((0[1-9])|(1[0-9])|(2[0-8])))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z)"),
      Pattern.compile("(-)?[0-9]{4}-((04|06|09|11)-((0[1-9])|([1-2][0-9])|30))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z)"),
      Pattern.compile("(-)?[0-9]{4}-((01|03|05|07|08|10|12)-((0[1-9])|([1-2][0-9])|(30|31)))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)-02-29)(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-02-29(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z)"),
      Pattern.compile("(-)?[0-9]{4}-(02-((0[1-9])|(1[0-9])|(2[0-8])))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z)"),
      Pattern.compile("(-)?[0-9]{4}-((04|06|09|11)-((0[1-9])|([1-2][0-9])|30))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z)"),
      Pattern.compile("(-)?[0-9]{4}-((01|03|05|07|08|10|12)-((0[1-9])|([1-2][0-9])|(30|31)))(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)-02-29)(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-02-29(T)(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z)"),
      Pattern.compile("((1971|1972|1973|1974|1975|1976|1977|1978|1979|1987|1989|1990|1995|1998|2005|2008|2016)-12-31T23:59:60(\\.[0-9]+)?(Z)|(1972|1981|1982|1983|1985|1992|1993|1994|1997|2012|2015)-06-30T23:59:60(\\.[0-9]+)?(Z))"));

  /**
   * Valid year month day formats.
   * 
   */
  private static List<Pattern> DATE_YMD_FORMATS = Arrays.asList(
      Pattern.compile("(-)?[0-9]{4}(Z?)"),
      Pattern.compile("(-)?[0-9]{4}-((0[1-9])|(1[0-2]))(Z?)"),
      Pattern.compile("((-)?[0-9]{4}-(02-((0[1-9])|(1[0-9])|(2[0-8])))|(-)?[0-9]{4}-((04|06|09|11)-((0[1-9])|([1-2][0-9])|30))|(-)?[0-9]{4}-((01|03|05|07|08|10|12)-((0[1-9])|([1-2][0-9])|(30|31))))(Z?)"),
      Pattern.compile("(-)?([0-9]{2}(04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)-02-29)(Z?)"),
      Pattern.compile("(-)?(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)00-02-29(Z?)"));

  private static List<Pattern> TIME_FORMATS = Arrays.asList(
      Pattern.compile("(([0-1][0-9])|(2[0-3]))(Z?)"),
      Pattern.compile("(([0-1][0-9])|(2[0-3])):[0-5][0-9](Z?)"),
      Pattern.compile("(([0-1][0-9])|(2[0-3])):[0-5][0-9]:([0-5][0-9])(\\.([0-9]+))?(Z?)"),
      Pattern.compile("(23):[0-5][0-9]:(([0-5][0-9])|60)((\\.[0-9]+))?(Z?)"));

  /**
   * Mapping of field datetime types to its list of valid datetime formats.
   * 
   */
  public static final HashMap<String, List<Pattern>> DATE_TIME_FORMATS = new HashMap<>();
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
    for (Pattern format : DATE_TIME_FORMATS.get(type.getXMLType())) {
      if (format.matcher (value.trim()).matches()) {
        success = true;
        break;
      }
    }
    return success;
  }
}
