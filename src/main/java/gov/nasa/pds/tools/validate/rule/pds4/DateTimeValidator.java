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

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import gov.nasa.pds.label.object.FieldType;
import gov.nasa.pds.registry.common.util.TimeFormatRegex;

/**
 * Class to validate datetime values.
 *
 * @author mcayanan
 *
 */
public class DateTimeValidator {
  private static final Map<String,String> Accepted_DateFormats = new TreeMap<String, String>();
  static {
    for (Pattern p : TimeFormatRegex.DATE_TIME_DOY_FORMATS) {
      Accepted_DateFormats.put(p.pattern(), "u[-D['T'H[:m[:s[.S]]]]]X");
    }
    for (Pattern p : TimeFormatRegex.DATE_TIME_YMD_FORMATS) {
      Accepted_DateFormats.put(p.pattern(), "u[-M[-d['T'H[:m[:s[.S]]]]]]X");
    }
    for (Pattern p : TimeFormatRegex.TIME_FORMATS) {
      Accepted_DateFormats.put(p.pattern(), "H[:m[:s[.S]]]X");
    }
  };

  /**
   * Mapping of field datetime types to its list of valid datetime formats.
   * 
   */
  public static final HashMap<String, List<Pattern>> DATE_TIME_FORMATS = new HashMap<>();
  static {
    DATE_TIME_FORMATS.put(FieldType.ASCII_DATE_DOY.getXMLType(), TimeFormatRegex.DOY_FORMATS);
    DATE_TIME_FORMATS.put(FieldType.ASCII_DATE_TIME_DOY.getXMLType(), TimeFormatRegex.DATE_TIME_DOY_FORMATS);
    DATE_TIME_FORMATS.put(FieldType.ASCII_DATE_TIME_DOY_UTC.getXMLType(),
        TimeFormatRegex.DATE_TIME_DOY_UTC_FORMATS);
    DATE_TIME_FORMATS.put(FieldType.ASCII_DATE_TIME_YMD.getXMLType(), TimeFormatRegex.DATE_TIME_YMD_FORMATS);
    DATE_TIME_FORMATS.put(FieldType.ASCII_DATE_TIME_YMD_UTC.getXMLType(),
        TimeFormatRegex.DATE_TIME_YMD_UTC_FORMATS);
    DATE_TIME_FORMATS.put(FieldType.ASCII_DATE_YMD.getXMLType(), TimeFormatRegex.DATE_YMD_FORMATS);
    DATE_TIME_FORMATS.put(FieldType.ASCII_TIME.getXMLType(), TimeFormatRegex.TIME_FORMATS);
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
    return locateRegEx(type.getXMLType(), value) != null;
  }
  
  private static Pattern locateRegEx (String type, String value) throws Exception {
    Pattern match = null; 
    if (!DATE_TIME_FORMATS.containsKey(type)) {
      throw new Exception("'" + type + "' is not one of the valid datetime formats: "
          + DATE_TIME_FORMATS.toString());
    }
    for (Pattern format : DATE_TIME_FORMATS.get(type)) {
      if (format.matcher (value.trim()).matches()) {
        match = format;
        break;
      }
    }
    return match;
  }
 
  private static Instant to (String value, String pattern) {
    // need to java datetime parse normalize value (super annoying)
    pattern = Accepted_DateFormats.get(pattern);
    value = value.trim(); // remove extra whitespace
    if (pattern.endsWith("X")) value = (value + "Z").replace("ZZ","Z"); // force timezone
    if (value.contains(".")) { // need enough S to do the trick
      int decidx = value.indexOf(".");
      int eol = value.length() - (value.endsWith("Z") ? 2:1);
      String enoughS = "";
      for (int i = 0 ; i < eol-decidx ; i++) enoughS += "S";
      pattern = pattern.replace("S", enoughS);
    }
    return ZonedDateTime.parse(value,DateTimeFormatter.ofPattern(pattern)).toInstant();
  }
  public static Instant toInstant (FieldType type, String value) throws Exception {
    Pattern regex = locateRegEx (type.getXMLType(), value);
    if (regex == null)
      throw new Exception("'" + value + "' cannot be parsed by the regular expressions defining '" + type.getXMLType() + "'");
    return to (value, regex.pattern());
  } 
  public static Instant anyToInstant (String value) throws Exception {
    Pattern regex = null;
    for (String ft : DATE_TIME_FORMATS.keySet()) {
      regex = locateRegEx (ft, value);
      if (regex != null) break;
    }
    if (regex == null)
      throw new Exception("'" + value + "' cannot be parsed by the regular expressions defining for any PDS4 support ASCII date time formats.");
    return to(value, regex.pattern());
  }
}
