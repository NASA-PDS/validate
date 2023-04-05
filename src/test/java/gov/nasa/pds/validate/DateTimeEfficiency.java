package gov.nasa.pds.validate;

import gov.nasa.pds.label.object.FieldType;
import gov.nasa.pds.tools.validate.rule.pds4.DateTimeValidator;

public class DateTimeEfficiency {

  public static void main(String[] args) {
    try {
      long t0 = System.currentTimeMillis();
      for (int i=0 ; i < 1000000 ; i++) {
        DateTimeValidator.isValid(FieldType.ASCII_DATE_TIME_DOY_UTC, "2023-120T10:23:55.123456Z");
      }
      System.out.println("Processing time: " + (System.currentTimeMillis() - t0)/1000. + " seconds");
    } catch (Exception e) {
      System.err.println("Complete and utter failure.");
    }
  } 
}
