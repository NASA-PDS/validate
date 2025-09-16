package gov.nasa.pds.tools.validate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import gov.nasa.arc.pds.xml.generated.SpecialConstants;
import gov.nasa.pds.tools.validate.content.ProblemReporter;
import gov.nasa.pds.tools.validate.content.SpecialConstantBitPatternTransforms;
import gov.nasa.pds.tools.validate.rule.pds4.DateTimeValidator;

public class SpecialConstantChecker {
  final private static List<String> INF_NAN_VALUES = Arrays.asList("INF", "-INF", "+INF", "INFINITY",
      "-INFINITY", "+INFINITY", "NAN", "-NAN", "+NAN");
  public static boolean isInf (String value) {
    return INF_NAN_VALUES.subList(0, 6).contains(value.toUpperCase());
  }
  public static boolean isInfOrNan (String value) {
    return INF_NAN_VALUES.contains (value.toUpperCase());
  }
  /**
   * Use this when the special constant may not conform to the type constraints
   * meaning only string comparisons can be done.
   * @param value
   * @param constants
   * @return
   */
  public static boolean isNonConformantSpecialConstant(String value, SpecialConstants constants) {
    boolean matched = false;
    if (constants != null) {
      matched |= value.equals(constants.getErrorConstant());
      matched |= value.equals(constants.getInvalidConstant());
      matched |= value.equals(constants.getMissingConstant());
      //matched |= value.equals(constants.getHighInstrumentSaturation());
      //matched |= value.equals(constants.getHighRepresentationSaturation());
      //matched |= value.equals(constants.getLowInstrumentSaturation());
      //matched |= value.equals(constants.getLowRepresentationSaturation());
      matched |= value.equals(constants.getNotApplicableConstant());
      matched |= value.equals(constants.getSaturatedConstant());
      matched |= value.equals(constants.getUnknownConstant());
    }
    return matched;
  }
  /**
   * Checks if the given value is a Special Constant defined in the label.
   * Use this when check string values first then values and bit patterns
   * meaning the special constant must conform to the data type constraints.
   * 
   * @param value The value to check.
   * @param constants An object representation of the Special_Constants area in a label.
   * 
   * @return true if the given value is a Special Constant.
   */
  public static boolean isConformantSpecialConstant(Number value, SpecialConstants constants,
      ProblemReporter reporter, int radix) {
    boolean matched = false;
    boolean matchEdge = true;
    boolean rangeChecked = false;
    matched |= SpecialConstantChecker.sameContent (value, constants.getErrorConstant(), radix);
    matched |= SpecialConstantChecker.sameContent (value, constants.getInvalidConstant(), radix);
    matched |= SpecialConstantChecker.sameContent (value, constants.getMissingConstant(), radix);
    matched |= SpecialConstantChecker.sameContent (value, constants.getHighInstrumentSaturation(), radix);
    matched |= SpecialConstantChecker.sameContent (value, constants.getHighRepresentationSaturation(), radix);
    matched |= SpecialConstantChecker.sameContent (value, constants.getLowInstrumentSaturation(), radix);
    matched |= SpecialConstantChecker.sameContent (value, constants.getLowRepresentationSaturation(), radix);
    matched |= SpecialConstantChecker.sameContent (value, constants.getNotApplicableConstant(), radix);
    matched |= SpecialConstantChecker.sameContent (value, constants.getSaturatedConstant(), radix);
    matched |= SpecialConstantChecker.sameContent (value, constants.getUnknownConstant(), radix);
    if (matched) return true;
    if (constants.getValidMaximum() != null) {
      int comparison = 0;
      rangeChecked = true;
      if (value instanceof BigDecimal) {
        comparison = ((BigDecimal) value)
            .compareTo(SpecialConstantBitPatternTransforms.asBigDecimal(constants.getValidMaximum(), radix));
      } else if (value instanceof BigInteger) {
        comparison = ((BigInteger) value)
            .compareTo(SpecialConstantBitPatternTransforms.asBigInt(constants.getValidMaximum(), radix));
      } else {
        if (constants.getValidMaximum().contains(".") || 
            (value instanceof Float || value instanceof Double) ||
            ((constants.getValidMaximum().contains("e") || constants.getValidMaximum().contains("E")) && 
                !(constants.getValidMaximum().startsWith("0x") || constants.getValidMaximum().startsWith("0X") || constants.getValidMaximum().startsWith("16#")))) {
          comparison = Double.valueOf(value.doubleValue())
              .compareTo(SpecialConstantBitPatternTransforms.asBigDecimal(constants.getValidMaximum(), radix).doubleValue());
        } else {
          Long con = SpecialConstantBitPatternTransforms.asBigInt(constants.getValidMaximum(), radix).longValue();
          Long val = value.longValue();
          if (value instanceof Double) {
            val = Long.valueOf(Double.doubleToRawLongBits((Double)value));
            if (con.equals (val)) {
              matchEdge &= true;
            } else {
              comparison = ((Double)value).compareTo(Double.longBitsToDouble(con));
            }
          } else if (value instanceof Float) {
            val = Long.valueOf(Float.floatToRawIntBits((Float)value)) & 0xFFFFFFFFL;
            if (con.equals (val)) {
              matchEdge &= true;
            } else {
              comparison = ((Float)value).compareTo(Float.intBitsToFloat(con.intValue()));
            }
          } else {
            comparison = val.compareTo(con);
          }
        }
      }
      if (0 < comparison) {
        reporter.addProblem("Field has a value '" + value.toString()
            + "' that is greater than the defined maximum value '" + constants.getValidMaximum()
            + "'. ");
      } 
      matchEdge &= comparison == 0;
    }
    if (constants.getValidMinimum() != null) {
      int comparison = 0;
      rangeChecked = true;
      if (value instanceof BigDecimal) {
        comparison = ((BigDecimal) value)
            .compareTo(SpecialConstantBitPatternTransforms.asBigDecimal(constants.getValidMinimum(), radix));
      } else if (value instanceof BigInteger) {
        comparison = ((BigInteger) value)
            .compareTo(SpecialConstantBitPatternTransforms.asBigInt(constants.getValidMinimum(), radix));
      } else {
        if (constants.getValidMinimum().contains(".") || 
            (value instanceof Float || value instanceof Double) ||
            ((constants.getValidMinimum().contains("e") || constants.getValidMinimum().contains("E")) && 
                !(constants.getValidMinimum().startsWith("0x") || constants.getValidMinimum().startsWith("0X") || constants.getValidMinimum().startsWith("16#")))) {
          comparison = Double.valueOf(value.doubleValue())
              .compareTo(SpecialConstantBitPatternTransforms.asBigDecimal(constants.getValidMinimum(), radix).doubleValue());
        } else {
          Long con = SpecialConstantBitPatternTransforms.asBigInt(constants.getValidMinimum(), radix).longValue();
          Long val = value.longValue();
          if (value instanceof Double) {
            val = Long.valueOf(Double.doubleToRawLongBits((Double)value));
            if (con.equals (val)) {
              matchEdge &= true;
            } else {
              comparison = ((Double)value).compareTo(Double.longBitsToDouble(con));
            }
          } else if (value instanceof Float) {
            val = Long.valueOf(Float.floatToRawIntBits((Float)value)) & 0xFFFFFFFFL;
            if (con.equals (val)) {
              matchEdge &= true;
            } else {
              comparison = ((Float)value).compareTo(Float.intBitsToFloat(con.intValue()));
            }
          } else {
            comparison = val.compareTo(con);
          }
        }
      }
      if (comparison < 0) {
        reporter.addProblem("Field has a value '" + value.toString()
            + "' that is less than the defined minimum value '" + constants.getValidMinimum()
            + "'. ");
      }
      matchEdge = comparison == 0;
    }
    return rangeChecked ? matchEdge : false;
  }
  public static boolean sameContent (Number number, String constant_repr, int radix) {
    if (constant_repr == null) return false;
    if (number.toString().equals(constant_repr)) {
      return true;
    }
    if (isInfOrNan(constant_repr)) {
      if (number instanceof BigDecimal || number instanceof Double) {
        Double d = number instanceof Double ? (Double)number : ((BigDecimal)number).doubleValue();
        if (isInf(constant_repr)) {
          return d.isInfinite();
        } else {
          return d.isNaN();
        }
      }
      if (number instanceof Float) {
        if (isInf(constant_repr)) {
          return ((Float)number).isInfinite();
        } else {
          return ((Float)number).isNaN();
        }
      }
    }
    if (number instanceof BigDecimal) number = ((BigDecimal)number).doubleValue();
    if (number instanceof Byte) number = BigInteger.valueOf(number.byteValue());
    if (number instanceof Double) number = BigInteger.valueOf(Double.doubleToRawLongBits((Double)number));
    if (number instanceof Float) number = BigInteger.valueOf(Float.floatToRawIntBits((Float)number) & 0xFFFFFFFFL);
    if (number instanceof Integer || number instanceof UnsignedInteger) number = BigInteger.valueOf(number.intValue());
    if (number instanceof Long || number instanceof UnsignedLong) number = BigInteger.valueOf(number.longValue());
    if (number instanceof Short) number = BigInteger.valueOf(number.shortValue());
    boolean repr_decimal = constant_repr.contains (".") || 
        ((constant_repr.contains("E") || constant_repr.contains("e")) &&
            !(constant_repr.startsWith("0x") || constant_repr.startsWith("0X")));
    if (repr_decimal) {
      BigDecimal constant = SpecialConstantBitPatternTransforms.asBigDecimal(constant_repr, radix);
      return constant.equals (number);
    } else {
      BigInteger constant = SpecialConstantBitPatternTransforms.asBigInt(constant_repr, radix);
      BigInteger value = (BigInteger)number;
      return constant.xor (value).longValue() == 0L;
    }
  }
  public static boolean sameContent (Instant value, String constant_repr, ProblemReporter reporter) {
    if (constant_repr == null) return false;
    try {
      Instant spc = DateTimeValidator.anyToInstant(constant_repr);
      return spc == value;
    } catch (Exception e) {
      reporter.addProblem("Cannot cast special constant '" + value + "' to a java.time.Instant.");
    }
    return false;
  }
  public static boolean isTemporalSpecialConstant(Instant value, SpecialConstants constants, ProblemReporter reporter) {
    boolean matched = false;
    matched |= SpecialConstantChecker.sameContent (value, constants.getErrorConstant(), reporter);
    matched |= SpecialConstantChecker.sameContent (value, constants.getInvalidConstant(), reporter);
    matched |= SpecialConstantChecker.sameContent (value, constants.getMissingConstant(), reporter);
    matched |= SpecialConstantChecker.sameContent (value, constants.getHighInstrumentSaturation(), reporter);
    matched |= SpecialConstantChecker.sameContent (value, constants.getHighRepresentationSaturation(), reporter);
    matched |= SpecialConstantChecker.sameContent (value, constants.getLowInstrumentSaturation(), reporter);
    matched |= SpecialConstantChecker.sameContent (value, constants.getLowRepresentationSaturation(), reporter);
    matched |= SpecialConstantChecker.sameContent (value, constants.getNotApplicableConstant(), reporter);
    matched |= SpecialConstantChecker.sameContent (value, constants.getSaturatedConstant(), reporter);
    matched |= SpecialConstantChecker.sameContent (value, constants.getUnknownConstant(), reporter);
    if (matched) return true;
    if (constants.getValidMaximum() != null) {
      try {
        Instant maximum = DateTimeValidator.anyToInstant(constants.getValidMaximum());
        if (maximum.compareTo(value) < 0) {
          matched = true;
          reporter.addProblem("Field has a value '" + value.toString()
          + "' that is greater than the defined maximum value '" + constants.getValidMaximum()
          + "'. ");
        }
      } catch (Exception e) {
        reporter.addProblem("Cannot cast special constant maximum '" + constants.getValidMaximum() + "' to a java.time.Instant.");
      }
    }
    if (constants.getValidMinimum() != null) {
      try {
        Instant minimum = DateTimeValidator.anyToInstant(constants.getValidMinimum());
        if (value.compareTo(minimum) < 0) {
          matched = true;
          reporter.addProblem("Field has a value '" + value.toString()
          + "' that is less than the defined minimum value '" + constants.getValidMinimum()
          + "'. ");
        }
      } catch (Exception e) {
        reporter.addProblem("Cannot cast special constant minimum '" + constants.getValidMinimum() + "' to a java.time.Instant. " + e.getLocalizedMessage());
      }        
    }
    return matched;
  }
}
