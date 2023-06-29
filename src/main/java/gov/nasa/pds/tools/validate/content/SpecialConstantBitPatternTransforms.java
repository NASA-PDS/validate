package gov.nasa.pds.tools.validate.content;

import java.math.BigInteger;

public class SpecialConstantBitPatternTransforms {
  public static BigInteger asBigInt (String value) {
    int radix = 10;
    value = value.strip();
    if (value.startsWith ("0x") || value.startsWith ("16#")) {
      radix = 16;
      value = value.substring(value.startsWith ("0x") ? 2 : 3);
    }
    if (value.startsWith ("0o") || value.startsWith ("8#")) {
      radix = 8;
      value = value.substring(value.startsWith ("0o") ? 2 : 3);
    }
    if (value.startsWith ("0b") || value.startsWith ("2#")) {
      radix = 2;
      value = value.substring(value.startsWith ("0b") ? 2 : 3);
    }
    if (value.endsWith("#")) {
      value = value.substring(0, value.length()-1);
    }
    return new BigInteger(value, radix);
  }
}
