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

import java.util.HashMap;
import java.util.List;
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
