package gov.nasa.pds.tools.validate.content;

import java.math.BigDecimal;
import java.math.BigInteger;

public class SpecialConstantBitPatternTransforms {
  public static BigInteger asBigInt (String value, int radix) {
    value = value.strip();
    if (value.startsWith ("0x") || value.startsWith ("0X") || value.startsWith ("16#")) {
      radix = 16;
      value = value.substring(value.startsWith ("16#") ? 3 : 2);
    }
    if (value.startsWith ("0o") || value.startsWith ("0O") || value.startsWith ("8#")) {
      radix = 8;
      value = value.substring(2);
    }
    if (value.startsWith ("0b") || value.startsWith ("0B") || value.startsWith ("2#")) {
      radix = 2;
      value = value.substring(2);
    }
    if (value.endsWith("#")) {
      value = value.substring(0, value.length()-1);
    }
    return new BigInteger(value, radix);
  }
  public static BigDecimal asBigDecimal (String value, int radix) {
    if (value.startsWith ("0x") || value.startsWith ("0X") || value.startsWith ("16#") ||
        value.startsWith ("0o") || value.startsWith ("0O") || value.startsWith ("8#") ||
        value.startsWith ("0b") || value.startsWith ("0B") || value.startsWith ("2#") || 
        radix != 10) {
      BigInteger bitPattern = asBigInt(value, radix);
      if (bitPattern.bitCount() > 32) {
        return BigDecimal.valueOf (Double.longBitsToDouble(bitPattern.longValue()));
      } else if (radix == 10) {
        return BigDecimal.valueOf (Float.intBitsToFloat(bitPattern.intValue()));
      } else {
        return BigDecimal.valueOf(bitPattern.longValue());
      }
    }
    else {
      return new BigDecimal (value);
    }
  }
}
