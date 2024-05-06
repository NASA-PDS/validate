package gov.nasa.pds.tools.validate;

import java.math.BigDecimal;
import java.math.BigInteger;
import gov.nasa.arc.pds.xml.generated.SpecialConstants;
import gov.nasa.pds.tools.validate.content.ProblemReporter;
import gov.nasa.pds.tools.validate.content.SpecialConstantBitPatternTransforms;

public class SpecialConstantChecker {
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
      ProblemReporter reporter) {
    boolean matched = false;
    boolean matchEdge = true;
    boolean rangeChecked = false;
    matched |= SpecialConstantChecker.sameContent (value, constants.getErrorConstant());
    matched |= SpecialConstantChecker.sameContent (value, constants.getInvalidConstant());
    matched |= SpecialConstantChecker.sameContent (value, constants.getMissingConstant());
    matched |= SpecialConstantChecker.sameContent (value, constants.getHighInstrumentSaturation());
    matched |= SpecialConstantChecker.sameContent (value, constants.getHighRepresentationSaturation());
    matched |= SpecialConstantChecker.sameContent (value, constants.getLowInstrumentSaturation());
    matched |= SpecialConstantChecker.sameContent (value, constants.getLowRepresentationSaturation());
    matched |= SpecialConstantChecker.sameContent (value, constants.getNotApplicableConstant());
    matched |= SpecialConstantChecker.sameContent (value, constants.getSaturatedConstant());
    matched |= SpecialConstantChecker.sameContent (value, constants.getUnknownConstant());
    if (matched) return true;
    if (constants.getValidMaximum() != null) {
      int comparison = 0;
      rangeChecked = true;
      if (value instanceof BigDecimal) {
        comparison = ((BigDecimal) value)
            .compareTo(SpecialConstantBitPatternTransforms.asBigDecimal(constants.getValidMaximum()));
      } else if (value instanceof BigInteger) {
        comparison = ((BigInteger) value)
            .compareTo(SpecialConstantBitPatternTransforms.asBigInt(constants.getValidMaximum()));
      } else {
        if (constants.getValidMaximum().contains(".") || 
            ((constants.getValidMaximum().contains("e") || constants.getValidMaximum().contains("E")) && 
                !(constants.getValidMaximum().startsWith("0x") || constants.getValidMaximum().startsWith("0X") || constants.getValidMaximum().startsWith("16#")))) {
          comparison = Double.valueOf(value.doubleValue())
              .compareTo(SpecialConstantBitPatternTransforms.asBigDecimal(constants.getValidMaximum()).doubleValue());
        } else {
          Long con = SpecialConstantBitPatternTransforms.asBigInt(constants.getValidMaximum()).longValue();
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
            .compareTo(SpecialConstantBitPatternTransforms.asBigDecimal(constants.getValidMinimum()));
      } else if (value instanceof BigInteger) {
        comparison = ((BigInteger) value)
            .compareTo(SpecialConstantBitPatternTransforms.asBigInt(constants.getValidMinimum()));
      } else {
        if (constants.getValidMinimum().contains(".") || 
            ((constants.getValidMinimum().contains("e") || constants.getValidMinimum().contains("E")) && 
                !(constants.getValidMinimum().startsWith("0x") || constants.getValidMinimum().startsWith("0X") || constants.getValidMinimum().startsWith("16#")))) {
          comparison = Double.valueOf(value.doubleValue())
              .compareTo(SpecialConstantBitPatternTransforms.asBigDecimal(constants.getValidMinimum()).doubleValue());
        } else {
          Long con = SpecialConstantBitPatternTransforms.asBigInt(constants.getValidMinimum()).longValue();
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
  public static boolean sameContent (Number number, String constant_repr) {
    if (constant_repr == null) return false;
    if (number.toString().equals(constant_repr)) {
      return true;
    }
    if (number instanceof BigDecimal) number = ((BigDecimal)number).doubleValue();
    if (number instanceof Byte) number = BigInteger.valueOf(number.byteValue());
    if (number instanceof Double) number = BigInteger.valueOf(Double.doubleToRawLongBits((Double)number));
    if (number instanceof Float) number = BigInteger.valueOf(Float.floatToRawIntBits((Float)number) & 0xFFFFFFFFL);
    if (number instanceof Integer) number = BigInteger.valueOf(number.intValue());
    if (number instanceof Long) number = BigInteger.valueOf(number.longValue());
    if (number instanceof Short) number = BigInteger.valueOf(number.shortValue());
    boolean repr_decimal = constant_repr.contains (".") || constant_repr.contains("E") || constant_repr.contains("e");
    if (repr_decimal) {
      BigDecimal constant = SpecialConstantBitPatternTransforms.asBigDecimal(constant_repr);
      return constant.equals (number);
    } else {
      BigInteger constant = SpecialConstantBitPatternTransforms.asBigInt(constant_repr);
      BigInteger value = (BigInteger)number;
      return constant.xor (value).longValue() == 0L;
    }
  }

}
