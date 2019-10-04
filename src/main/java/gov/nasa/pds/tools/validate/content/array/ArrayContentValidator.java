// Copyright 2006-2018, by the California Institute of Technology.
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
package gov.nasa.pds.tools.validate.content.array;

import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import gov.nasa.arc.pds.xml.generated.Array;
import gov.nasa.arc.pds.xml.generated.ElementArray;
import gov.nasa.arc.pds.xml.generated.ObjectStatistics;
import gov.nasa.arc.pds.xml.generated.SpecialConstants;
import gov.nasa.pds.label.object.ArrayObject;
import gov.nasa.pds.objectAccess.DataType.NumericDataType;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.ProblemType;
import org.apache.commons.lang3.Range;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Arrays;

/**
 * Class that performs content validation on Array objects.
 * 
 * @author mcayanan
 *
 */
public class ArrayContentValidator {
  
  /** Container to capture messages. */
  private ProblemListener listener;
  
  /** The label associated with the Array being validated. */
  private URL label;
  
  /** The data file containing the array content. */
  private URL dataFile;
  
  /** The index of the array. */
  private int arrayIndex;
  
  private int spotCheckData;

  private static final Range SignedByte_RANGE = Range.between(Byte.MIN_VALUE, Byte.MAX_VALUE);
  private static final Range UnsignedByte_RANGE = Range.between(0, 255);
  private static final Range UnsignedLSB2_RANGE = Range.between(0, 65535);
  private static final Range SignedLSB2_RANGE = Range.between(Short.MIN_VALUE, Short.MAX_VALUE);
  private static final Range UnsignedMSB2_RANGE = Range.between(0, 65535);
  private static final Range SignedMSB2_RANGE = Range.between(Short.MIN_VALUE, Short.MAX_VALUE);
  private static final Range UnsignedLSB4_RANGE = Range.between(UnsignedInteger.ZERO, UnsignedInteger.MAX_VALUE);
  private static final Range SignedLSB4_RANGE = Range.between(Integer.MIN_VALUE, Integer.MAX_VALUE);
  private static final Range UnsignedMSB4_RANGE = Range.between(UnsignedInteger.ZERO, UnsignedInteger.MAX_VALUE);
  private static final Range SignedMSB4_RANGE = Range.between(Integer.MIN_VALUE, Integer.MAX_VALUE);
  private static final Range UnsignedLSB8_RANGE = Range.between(UnsignedLong.ZERO, UnsignedLong.MAX_VALUE);
  private static final Range SignedLSB8_RANGE = Range.between(Long.MIN_VALUE, Long.MAX_VALUE);
  private static final Range UnsignedMSB8_RANGE = Range.between(UnsignedLong.ZERO,UnsignedLong.MAX_VALUE);
  private static final Range SignedMSB8_RANGE = Range.between(Long.MIN_VALUE, Long.MAX_VALUE);
  private static final Range IEEE754LSBSingle_RANGE = Range.between(-Float.MAX_VALUE, Float.MAX_VALUE);
  private static final Range IEEE754MSBSingle_RANGE = Range.between(-Float.MAX_VALUE, Float.MAX_VALUE);
  private static final Range IEEE754LSBDouble_RANGE = Range.between(-Double.MAX_VALUE, Double.MAX_VALUE);
  private static final Range IEEE754MSBDouble_RANGE = Range.between(-Double.MAX_VALUE, Double.MAX_VALUE);

  private static int PROGRESS_COUNTER = 0;

  /**
   * Constructor.
   * 
   * @param listener to capture messages.
   * @param label the label file.
   * @param dataFile the data file.
   * @param arrayIndex the index of the array.
   */
  public ArrayContentValidator(ProblemListener listener, URL label, 
      URL dataFile, int arrayIndex) {
    this.listener = listener;
    this.label = label;
    this.dataFile = dataFile;
    this.arrayIndex = arrayIndex;
  }
  
  /**
   * Validates the given array.
   * 
   * @param array Object representation of the array as described in the label.
   * @param arrayObject Object representation of the array.
   */
  public void validate(Array array, ArrayObject arrayObject) {
    int[] dimensions = new int[array.getAxisArraies().size()];
    for (int i = 0; i < dimensions.length; i++) {
      dimensions[i] = array.getAxisArraies().get(i).getElements().intValueExact();
    }
    try {
      process(array, arrayObject, dimensions, new int[dimensions.length], 0,
          dimensions.length - 1);

    } catch (IOException io) {
      listener.addProblem(new ArrayContentProblem(
          new ProblemDefinition(
              ExceptionType.FATAL,
              ProblemType.ARRAY_DATA_FILE_READ_ERROR,
              "Error occurred while reading data file: " + io.getMessage()),
          dataFile,
          label,
          arrayIndex,
          null));
    } catch (Exception e) {
      listener.addProblem(new ArrayContentProblem(
          new ProblemDefinition(
              ExceptionType.FATAL,
              ProblemType.ARRAY_DATA_FILE_READ_ERROR,
              "Error occurred while reading data file: " + e.getMessage()),
          dataFile,
          label,
          arrayIndex,
          null));
    } finally {
      arrayObject.closeChannel();
    }
  }
  
  private void process(Array array, ArrayObject arrayObject, int[] dimensions, 
      int[] position, int depth, int maxDepth) throws IOException {
    // Print something to indicate the program is still executing since content
    // validation can take some time
    if (PROGRESS_COUNTER++ == Integer.MAX_VALUE) { PROGRESS_COUNTER = 0; }
    else if (PROGRESS_COUNTER % 1000 == 0) { System.out.print("."); }

    for (int i = 0; i < dimensions[depth];) {
      if( depth < maxDepth ) { // max depth not reached, do another recursion
        position[depth] = i;
        process(array, arrayObject, dimensions, position, depth + 1, maxDepth);
        i++;
      } else {
        position[depth] = i;
        int[] position_1based = new int[position.length];
        for (int j = 0; j < position.length; j++) {
          position_1based[j] = position[j] + 1;
        }
        ArrayLocation location = new ArrayLocation(label, dataFile, 
            arrayIndex, position_1based);
        validatePosition(array, arrayObject, location, position);
        if (spotCheckData != -1) {
          i = i + spotCheckData;
        } else {
          i++;
        }
      }
    }
  }
  
  private void validatePosition(Array array, ArrayObject arrayObject, 
      ArrayLocation location, int[] position) throws IOException {
    NumericDataType dataType = Enum.valueOf(NumericDataType.class, 
        array.getElementArray().getDataType());
    Number value = null;
    Range rangeChecker = null;
    try {
      switch (dataType) {
      case SignedByte:
        value = (byte) arrayObject.getInt(position);
        rangeChecker = SignedByte_RANGE;
        break;
      case UnsignedByte:
        value = arrayObject.getInt(position);
        rangeChecker = UnsignedByte_RANGE;
        break;
      case UnsignedLSB2:
        value = arrayObject.getInt(position);
        rangeChecker = UnsignedLSB2_RANGE;
        break;
      case SignedLSB2:
        value = (short) arrayObject.getInt(position);
        rangeChecker = SignedLSB2_RANGE;
        break;
      case UnsignedMSB2:
        value = arrayObject.getInt(position);
        rangeChecker = UnsignedMSB2_RANGE;
        break;              
      case SignedMSB2:
        value = (short) arrayObject.getInt(position);
        rangeChecker = SignedMSB2_RANGE;
        break;
      case UnsignedLSB4:
        value = UnsignedInteger.valueOf(arrayObject.getLong(position));
        rangeChecker = UnsignedLSB4_RANGE;
        break;
      case SignedLSB4:
        value = arrayObject.getInt(position);
        rangeChecker = SignedLSB4_RANGE;
        break;
      case UnsignedMSB4:
        value = UnsignedInteger.valueOf(arrayObject.getLong(position));
        rangeChecker = UnsignedMSB4_RANGE;
        break;
      case SignedMSB4:
        value = arrayObject.getInt(position);
        rangeChecker = SignedMSB4_RANGE;
        break;
      case UnsignedLSB8:
        value = UnsignedLong.valueOf(Long.toUnsignedString(arrayObject.getLong(position)));
        rangeChecker = UnsignedLSB8_RANGE;
        break;
      case SignedLSB8:
        value = arrayObject.getLong(position);
        rangeChecker = SignedLSB8_RANGE;
        break;
      case UnsignedMSB8:
        value = UnsignedLong.valueOf(Long.toUnsignedString(arrayObject.getLong(position)));
        rangeChecker = UnsignedMSB8_RANGE;
        break;        
      case SignedMSB8:
        value = arrayObject.getLong(position);
        rangeChecker = SignedMSB8_RANGE;
        break;
      case IEEE754LSBSingle:
        value = (float) arrayObject.getDouble(position);
        rangeChecker = IEEE754LSBSingle_RANGE;
        break;
      case IEEE754MSBSingle:
        value = (float) arrayObject.getDouble(position);
        rangeChecker = IEEE754MSBSingle_RANGE;
        break;
      case IEEE754LSBDouble:
        value = arrayObject.getDouble(position);
        rangeChecker = IEEE754LSBDouble_RANGE;
        break;
      case IEEE754MSBDouble:
        value = arrayObject.getDouble(position);
        rangeChecker = IEEE754MSBDouble_RANGE;
        break;
      }
    } catch (Exception ee) {
      String loc = Arrays.toString(location.getLocation());
      if (location.getLocation().length > 1) {
        loc = loc.replaceAll("\\[", "\\(");
        loc = loc.replaceAll("\\]", "\\)");
      } else {
        loc = loc.replaceAll("\\[", "");
        loc = loc.replaceAll("\\]", "");
      }
      throw new IOException("Error occurred while trying to "
          + "read data at location " + loc + ": " + ee.getMessage());
    }
    boolean isSpecialConstant = false;
    if (array.getSpecialConstants() != null) {
      isSpecialConstant = isSpecialConstant(value, array.getSpecialConstants());
    }
    if (!isSpecialConstant) {
      if (!rangeChecker.contains(value)) {
          addArrayProblem(ExceptionType.ERROR,
              ProblemType.ARRAY_VALUE_OUT_OF_DATA_TYPE_RANGE,
              "Value is not within the valid range of the data type '"
                  + dataType.name() + "': " + value.toString(),
            location
             );
      }
      if (array.getObjectStatistics() != null) {
        // At this point, it seems like it only makes sense
        // to check that the values are within the min/max values
        checkObjectStats(value, array.getElementArray(),
            array.getObjectStatistics(), location);
      }
    } else {
      addArrayProblem(ExceptionType.INFO,
          ProblemType.ARRAY_VALUE_IS_SPECIAL_CONSTANT,
          "Value is a special constant defined in the label: "
              + value.toString(),
          location
      );              
    }
  }
  
  /**
   * Checks if the given value is a Special Constant defined in the label.
   * 
   * @param value The value to check.
   * @param constants An object representation of the Special_Constants area
   * in a label.
   * 
   * @return true if the given value is a Special Constant.
   */
  private boolean isSpecialConstant(Number value, SpecialConstants constants) {
    if (constants.getErrorConstant() != null) {
      if (value.toString().equals(constants.getErrorConstant())) {
        return true;
      }
    } 
    if (constants.getInvalidConstant() != null) { 
      if (value.toString().equals(constants.getInvalidConstant())) {
        return true;
      }
    }
    if (constants.getMissingConstant() != null) {
      if (value.toString().equals(constants.getMissingConstant())) {
        return true;
      }
    }
    if (constants.getHighInstrumentSaturation() != null) {
      if (value.toString().equals(constants.getHighInstrumentSaturation())) {
        return true;
      }
    }
    if (constants.getHighRepresentationSaturation() != null) {
      if (value.toString().equals(constants.getHighRepresentationSaturation())) {
        return true;
      }
    }
    if (constants.getLowInstrumentSaturation() != null) {
      if (value.toString().equals(constants.getLowInstrumentSaturation())) {
        return true;
      }
    }
    if (constants.getLowRepresentationSaturation() != null) {
      if (value.toString().equals(constants.getLowRepresentationSaturation())) {
        return true;
      }
    }
    if (constants.getNotApplicableConstant() != null) {
      if (value.toString().equals(constants.getNotApplicableConstant())) {
        return true;
      }
    }
    if (constants.getSaturatedConstant() != null) {
      if (value.toString().equals(constants.getSaturatedConstant())) {
        return true;
      }
    }
    if (constants.getUnknownConstant() != null) {
      if (value.toString().equals(constants.getUnknownConstant())) {
        return true;
      }
    }
    if (constants.getValidMaximum() != null) {
      if (value.toString().equals(constants.getValidMaximum())) {
        return true;
      }
    }
    if (constants.getValidMinimum() != null) {
      if (value.toString().equals(constants.getValidMinimum())) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Checks the given number against the object statistics characteristics
   * as defined in the product label.
   * 
   * @param value The element value.
   * @param elementArray The Element Array.
   * @param objectStats The Object Statistics.
   * @param location The location of the given element value.
   */
  private void checkObjectStats(Number value, ElementArray elementArray, 
      ObjectStatistics objectStats, ArrayLocation location) {
    if (objectStats.getMinimum() != null) {
      if (value.doubleValue() < objectStats.getMinimum()) {
        addArrayProblem(ExceptionType.ERROR,
            ProblemType.ARRAY_VALUE_OUT_OF_MIN_MAX_RANGE,
            "Value is less than the minimum value in the label (min="
            + objectStats.getMinimum().toString()
            + ", got=" + value.toString() + ").", location);
      }
    }
    if (objectStats.getMaximum() != null) {
      if (value.doubleValue() > objectStats.getMaximum()) {
        addArrayProblem(ExceptionType.ERROR, 
            ProblemType.ARRAY_VALUE_OUT_OF_MIN_MAX_RANGE,
            "Value is greater than the maximum value in the label (max="
            + objectStats.getMaximum().toString()
            + ", got=" + value.toString() + ").", location);        
      }
    }
    double scalingFactor = 1.0;
    double valueOffset = 0.0;
    boolean checkScaledValue = false;
    if (elementArray.getScalingFactor() != null) {
      scalingFactor = elementArray.getScalingFactor();
      checkScaledValue = true;
    }
    if (elementArray.getValueOffset() != null) {
      valueOffset = elementArray.getValueOffset();
      checkScaledValue = true;
    }
    if (checkScaledValue) {
      double scaledValue = (value.doubleValue() * scalingFactor) + valueOffset;
      if (objectStats.getMinimumScaledValue() != null) {
        if (compare(scaledValue, objectStats.getMinimumScaledValue()) == -1) {
          addArrayProblem(ExceptionType.ERROR,
              ProblemType.ARRAY_VALUE_OUT_OF_SCALED_MIN_MAX_RANGE,
              "Scaled value is less than the scaled minimum value in the "
              + "label (min=" + objectStats.getMinimumScaledValue().toString()
              + ", got=" + value.toString() + ").", location);          
        }
      }
      if (objectStats.getMaximumScaledValue() != null) {
        if (compare(scaledValue, objectStats.getMaximumScaledValue()) == 1) {
          addArrayProblem(ExceptionType.ERROR,
              ProblemType.ARRAY_VALUE_OUT_OF_SCALED_MIN_MAX_RANGE,
              "Scaled value is greater than the scaled maximum value in the "
              + "label (max=" + objectStats.getMaximumScaledValue().toString()
              + ", got=" + value.toString() + ").", location);             
        }
      }
    }
  }
  
  /**
   * Compares 2 double values. If the values have different
   * precisions, this method will set the precisions to the 
   * same scale before doing a comparison.
   * 
   * @param value The element value.
   * @param minMax The min or max value to compare against.
   * 
   * @return -1 if value is less than minMax, 0 if they are equal
   *  and 1 if value is greater than minMax.
   */
  private int compare(Double value, Double minMax) {
    BigDecimal bdValue = new BigDecimal(value.toString());
    BigDecimal bdMinMax = new BigDecimal(minMax.toString());
    if (bdValue.precision() == bdMinMax.precision()) {
      return bdValue.compareTo(bdMinMax);
    } else if (bdValue.precision() > bdMinMax.precision()) {
      BigDecimal scaledValue = bdValue.setScale(bdMinMax.precision(), 
          RoundingMode.HALF_UP);
      return scaledValue.compareTo(bdMinMax);
    } else {
      BigDecimal scaledMinMax = bdMinMax.setScale(bdValue.precision(), 
          RoundingMode.HALF_UP);
      return bdValue.compareTo(scaledMinMax);
    }
  }
  
  /**
   * Records an Array Content related message to the listener.
   * 
   * @param exceptionType exception type.
   * @param message The message to record.
   * @param location The array location associated with the message.
   */
  private void addArrayProblem(ExceptionType exceptionType, 
      ProblemType problemType, String message, ArrayLocation location) {
    listener.addProblem(
        new ArrayContentProblem(exceptionType,
            problemType,
            message,
            location.getDataFile(),
            location.getLabel(),
            location.getArray(),
            location.getLocation()));
  }
  
  public void setSpotCheckData(int value) {
    this.spotCheckData = value;
  }
}
