package gov.nasa.pds.web.ui.utils;

import java.io.UnsupportedEncodingException;

public class BinaryConversionUtils {

	/**
	 * 
	 * Converts an array of bytes into an int. The array of bytes may be of
	 * length 1, 2, 3, or 4, in big-endian order. That is, b[0] holds the most
	 * significant bits, b[n-1] the least. The bytes are assumed to be signed.
	 * 
	 * @param b
	 *            the array of bytes, in big-endian order
	 * @param n
	 *            the number of bytes to convert in the array, from 1 to the
	 *            size of an int
	 * @return the signed int value represented by the bytes
	 */

	public static int convertMSBSigned(byte[] b, int n) {
		// Integer.size is The number of bits used to represent an <tt>int</tt>
		// value in two's complement binary form.
		// static final int SIZE = 32;
		// so assert n(number of bytes in array) is between 1 and 8*n is less
		// than 32)
		assert (1 <= n && 8 * n <= Integer.SIZE);

		int value = 0;
		// From least- to most-significant byte, put each byte
		// into the accumulating value, in the most significant
		// byte position, shifting the prior value 8 bits to
		// the right. That way the most significant byte will
		// be the last byte we put in, setting the sign bit.
		for (int i = n - 1; i >= 0; --i) {
			value = (value >>> 8) | (b[i] << (Integer.SIZE - 8));
		}
		// Pad with the sign bit on the left, if we used fewer bytes
		// than the size of an int.
		if (n < 4) {
			value = value >> (Integer.SIZE - 8 * n);
		}
		return value;
	}

	/**
	 * 
	 * Converts an array of bytes into an int. The array of bytes may be of
	 * length 1, 2, 3, or 4, in little-endian order. That is, b[n-1] holds the
	 * most significant bits, b[n-1] the least. The bytes are assumed to be
	 * signed.
	 * 
	 * @param b
	 *            the array of bytes, in little-endian order
	 * @param n
	 *            the number of bytes to convert in the array, from 1 to the
	 *            size of an int
	 * @return the signed int value represented by the bytes
	 */

	public static int convertLSBSigned(byte[] b, int n) {
		assert (1 <= n && 8 * n <= Integer.SIZE);
		int value = 0;
		// From least- to most-significant byte, put each byte
		// into the accumulating value, in the most significant
		// byte position, shifting the prior value 8 bits to
		// the right. That way the most significant byte will
		// be the last byte we put in, setting the sign bit.

		for (int i = 0; i < n; i++) {
			value = (value >>> 8) | (b[i] << (Integer.SIZE - 8));

		}
		// TODO should this pad on the right?
		// Pad with the sign bit on the left, if we used fewer bytes
		// than the size of an int.
		if (n < 4) {
			value = value >> (Integer.SIZE - 8 * n);
		}
		return value;
	}

	/**
	 * 
	 * Converts an array of bytes into a long, permitting unsigned bytes. The
	 * array of bytes may be of length 1, 2, 3, or 4, in big-endian order. That
	 * is, b[0] holds the most significant bits, b[n-1] the least.
	 * 
	 * @param b
	 *            the array of bytes, in big-endian order
	 * @param n
	 *            the number of bytes to convert in the array, from 1 to the
	 *            size of an int
	 * @return the signed int value represented by the bytes
	 */

	@SuppressWarnings("cast")
	public static long convertMSBUnsigned(int[] b, int n) {

		// MSB_UNSIGNED_INTEGER 1-, 2-, and 4-byte unsigned integers

		long anUnsignedInt = 0;

		if (n == 1) {
			anUnsignedInt = (long) (0xFF & ((int) b[0]));
		}

		if (n == 2) {
			anUnsignedInt = (long) ((0xFF & ((int) b[0])) << 8 | (0xFF & ((int) b[1])));

		}
		if (n == 3) {
			anUnsignedInt = ((long) ((0xFF & ((int) b[0])) << 16
					| (0xFF & ((int) b[1])) << 8 | (0xFF & ((int) b[2])))) & 0xFFFFFFFFL;
		}
		if (n == 4) {
			anUnsignedInt = ((long) ((0xFF & ((int) b[0])) << 24
					| (0xFF & ((int) b[1])) << 16 | (0xFF & ((int) b[2])) << 8 | (0xFF & ((int) b[3])))) & 0xFFFFFFFFL;
		}

		return anUnsignedInt;
	}

	/**
	 * 
	 * Converts an array of bytes into a long, permitting unsigned bytes. The
	 * array of bytes may be of length 1, 2, 3, or 4, in big-endian order. That
	 * is, b[0] holds the most significant bits, b[n-1] the least.
	 * 
	 * @param b
	 *            the array of bytes, in big-endian order
	 * @param n
	 *            the number of bytes to convert in the array, from 1 to the
	 *            size of an int
	 * @return the signed int value represented by the bytes
	 */

	@SuppressWarnings("cast")
	public static long convertMSBUnsigned(byte[] b, int n) {
		// MSB_UNSIGNED_INTEGER 1-, 2-, and 4-byte unsigned integers
		long anUnsignedInt = 0;
		if (n == 1) {
			anUnsignedInt = (long) (0xFF & ((int) b[0]));
		}
		if (n == 2) {
			anUnsignedInt = (long) ((0xFF & ((int) b[0])) << 8 | (0xFF & ((int) b[1])));
		}
		if (n == 3) {
			anUnsignedInt = ((long) ((0xFF & ((int) b[0])) << 16
					| (0xFF & ((int) b[1])) << 8 | (0xFF & ((int) b[2])))) & 0xFFFFFFFFL;
		}
		if (n == 4) {
			anUnsignedInt = ((long) ((0xFF & ((int) b[0])) << 24
					| (0xFF & ((int) b[1])) << 16 | (0xFF & ((int) b[2])) << 8 | (0xFF & ((int) b[3])))) & 0xFFFFFFFFL;
		}
		return anUnsignedInt;
	}

	@SuppressWarnings("cast")
	public static long convertLSBUnsigned(int[] b, int n) {
		long anUnsignedInt = 0;
		if (n == 1) {
			anUnsignedInt = (long) (0xFF & ((int) b[n - 1]));
		}
		if (n == 2) {
			anUnsignedInt = (long) ((0xFF & ((int) b[1])) << 8 | (0xFF & ((int) b[0])));
		}
		if (n == 3) {
			anUnsignedInt = ((long) ((0xFF & ((int) b[2])) << 16
					| (0xFF & ((int) b[1])) << 8 | (0xFF & ((int) b[0])))) & 0xFFFFFFFFL;
		}
		if (n == 4) {
			anUnsignedInt = ((long) ((0xFF & ((int) b[3])) << 24
					| (0xFF & ((int) b[2])) << 16 | (0xFF & ((int) b[1])) << 8 | (0xFF & ((int) b[0])))) & 0xFFFFFFFFL;
		}
		return anUnsignedInt;
	}

	@SuppressWarnings("cast")
	public static long convertLSBUnsigned(byte[] b, int n) {
		long anUnsignedInt = 0;
		if (n == 1) {
			anUnsignedInt = (long) (0xFF & ((int) b[n - 1]));
		}
		if (n == 2) {
			anUnsignedInt = (long) ((0xFF & ((int) b[1])) << 8 | (0xFF & ((int) b[0])));
		}
		if (n == 3) {
			anUnsignedInt = ((long) ((0xFF & ((int) b[2])) << 16
					| (0xFF & ((int) b[1])) << 8 | (0xFF & ((int) b[0])))) & 0xFFFFFFFFL;
		}
		if (n == 4) {
			anUnsignedInt = ((long) ((0xFF & ((int) b[3])) << 24
					| (0xFF & ((int) b[2])) << 16 | (0xFF & ((int) b[1])) << 8 | (0xFF & ((int) b[0])))) & 0xFFFFFFFFL;
		}
		return anUnsignedInt;
	}

	/**
	 * convert MSB byte array (of size 4) to float
	 * 
	 * @param test
	 * @return
	 */
	public static float MSBByteArrayToFloat(byte bytes[]) {
		final int MASK = 0xff;
		int bits = 0;
		int i = 0;
		for (int shifter = 3; shifter >= 0; shifter--) {
			bits |= (bytes[i] & MASK) << (shifter * 8);
			i++;
		}

		return Float.intBitsToFloat(bits);
	}

	/**
	 * convert LSB byte array (of size 4) to float
	 * 
	 * @param test
	 * @return
	 */
	// LAB l5/30/10 not used yet
	public static float LSBByteArrayToFloat(byte bytes[]) {
		final int MASK = 0xff;
		int bits = 0;
		int i = 0;
		for (int shifter = 0; shifter < 3; shifter++) {
			bits |= (bytes[i] & MASK) << (shifter * 8);
			i++;
		}

		return Float.intBitsToFloat(bits);
	}

	/**
	 * Convert byte array to String. Use the correct encoding, ASCII (7-bit) or EBCDIC (8-bit)
	 * In the case it is not able to use the correct encoding, display the bytes in decimal format
	 * as follows:  [87,87,....]
	 * 
	 * @param byteArray
	 * @return
	 */
	@SuppressWarnings("nls")
	public static String byteArrayToString(byte[] byteArray, String encoding) {
		StringBuilder sb = new StringBuilder("");
		String text = null;
		
		if (byteArray == null) {
			throw new IllegalArgumentException("byteArray must not be null");
		}
		
		try {
			text = new String(byteArray, 0, byteArray.length, encoding);
			sb.append(text);
			
		} catch(UnsupportedEncodingException e) {
			
			int arrayLen = byteArray.length;
			sb.append("[");
			
			for (int i = 0; i < arrayLen; i++) {
				sb.append(byteArray[i]);
				if (i == arrayLen - 1) {
					sb.append("]");
				} else {
					sb.append(", ");
				}
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Appends a logical 1 or 0 to the existing string based on the value of the bit
	 * Used to convert a byte array to string. 
	 */
	@SuppressWarnings("nls")
	public static void bitToString(StringBuilder byteValue, boolean bit) {
		
		if(bit)
			byteValue.append(1);
		else
			byteValue.append(0);
	}
	
    /**
     * Convert a byte array of size 8 bytes to type long
     * @param b: byte array of size 8 bytes
     * @return long representation of the byte array
     * 
     * Note: Need to type cast to type long since cannot shift pass 31 bits
     */
    public static long convertByteArrayToLong(byte[] b) {
        
        assert (b.length == 8);
        
        long aSignedLong = 0;
        
        aSignedLong =   ((long)(0xFF & b[0]) << 56) |
                        ((long)(0xFF & b[1]) << 48) |
                        ((long)(0xFF & b[2]) << 40) |
                        ((long)(0xFF & b[3]) << 32) |
                        ((long)(0xFF & b[4]) << 24) |
                        ((long)(0xFF & b[5]) << 16) |
                        ((long)(0xFF & b[6]) << 8)  |
                        ((long)(0xFF & b[7]));
        
        return aSignedLong;
    }
    
    /**
     * Convert a byte array of size 4 bytes to type int
     * @param b: byte array if size 4 bytes
     * @return int representation of the byte array
     */
    public static int convertByteArrayToInt(byte[] b) {
        
        assert (b.length == 4);
    
        int aSignedInt = 0;
        
        aSignedInt =    ((int)(0xFF & b[0]) << 24) |
                        ((int)(0xFF & b[1]) << 16) |
                        ((int)(0xFF & b[2]) << 8)  |
                        ((int)(0xFF & b[3]));
        
        return aSignedInt;
    }
    
    /**
     * Convert from VAX F-type 4-byte to IEEE single precision
     * 
     * @param b - byte array of size 4 bytes
     * @return Byte array in integer format with all the bits
     *         ordered in IEEE-754 4 byte format
     */
    public static int vaxFTypeToIEEESingle(byte[] b) {
        
        assert (b.length == 4);
        
        //Extract the arrangement of bytes in VAX format and arrange
        //them to IEEE format
        int iEEESingleFormat = 0;
        
        int sign = ((int)b[1] & 0x00000080) << 24;
        int e1 = ((int)b[1] & 0x0000007F) << 1;
        int e0 = ((int)b[0] & 0x00000080) >>> 7;
        
        //Subtract 2 due to VAX bias being 129 instead of 127 for IEEE
        //Then shift to right position in IEEE format
        int e = ( (e1 | e0) - 0x00000002) << 23;
        
        //Mantissa extraction
        int m0 = ((int)b[0] & 0x0000007F) << 16;
        int m1 = ((int)b[3] & 0x000000FF) << 8;
        int m2 = ((int)b[2] & 0x000000FF);
        
        //Piece together the different IEEE Float format components
        iEEESingleFormat = sign | e | m0 | m1 | m2;
            
        return iEEESingleFormat;
    }

    /**
     * Convert from VAX D-type to IEEE double precision. For compatibility,
     * the mantissa is truncated from 55 bits to 52 bits
     *
     * @param b - byte array of size 8 bytes
     * @return Byte array in long format with all the bits
     *         ordered in IEEE-754 8 byte format
     */
    public static long vaxDTypeToIEEEDouble(byte[] b) {
        
        assert (b.length == 8);
        
        //Extract the arrangement of bytes in VAX format and arrange
        //them to IEEE format
        long iEEEDoubleFormat = 0;
                                        
        long sign = (long)b[1] & (0x0000000000000080L) << 56;
        
        long e1 = ((long)b[1] & 0x000000000000007FL) << 1;
        long e0 = ((long)b[0] & 0x0000000000000080L) >>> 7;
        
        //VAX D-Type has a bias of 129. However, IEEE Double has a bias
        //of 1023. Therefore adding 894 due to VAX bias being 1025 instead
        //of 129 and -2 due to IEEE bias being 1023 = 0x37E
        //Shifting left to conform to IEEE format
        long e = ( (e1 | e0) +  0x000000000000037EL) << 52;
        
        //Mantissa extraction and shifting to correct location
        //in IEEE mantissa format
        long m0 = ((long)b[0] & 0x000000000000007FL) << 48;
        long m1 = ((long)b[3] & 0x00000000000000FFL) << 40;
        long m2 = ((long)b[2] & 0x00000000000000FFL) << 32;
        long m3 = ((long)b[5] & 0x00000000000000FFL) << 24;
        long m4 = ((long)b[4] & 0x00000000000000FFL) << 16;
        long m5 = ((long)b[7] & 0x00000000000000FFL) << 8;
        long m6 = ((long)b[6] & 0x00000000000000FFL);
        
        //VAX 8-byte D-type has a longer mantissa (55bits) than (52bits) as in
        //IEEE 8-byte (Double Precision). Therefore, the mantissa is truncated
        //from 55 bits to 52 bits in order to do the conversion
        long m = (m0 | m1 | m2 | m3 | m4 | m5 | m6) >>> 3;
        
        //Piece together the different IEEE Float format components
        iEEEDoubleFormat = sign | e | m;
            
        return iEEEDoubleFormat;
    }

    
    
    /**
     * Convert from VAX G-type to IEEE 8-byte double precision
     *
     * @param b - byte array of size 8 bytes
     * @return Byte array in long format with all the bits
     *         ordered in IEEE-754 8 byte format
     */
    public static long vaxGTypeToIEEEDouble(byte[] b) {
        
        assert (b.length == 8);
        
        //Extract the arrangement of bytes in VAX format and arrange
        //them to IEEE format
        long iEEEDoubleFormat = 0;
                                        
        long sign = (long)b[1] & (0x0000000000000080L) << 56;
        
        long e1 = ((long)b[1] & 0x000000000000007FL) << 4;
        long e0 = ((long)b[0] & 0x00000000000000F0L) >>> 4;
        
        //Subtract 2 due to VAX bias being 1025 instead of 1023 as for IEEE
        //Then shift to right position to conform to IEEE format
        long e = ( (e1 | e0) -  0x0000000000000002L) << 52;
        
        //Mantissa extraction
        long m0 = ((long)b[0] & 0x000000000000000FL) << 48;
        long m1 = ((long)b[3] & 0x00000000000000FFL) << 40;
        long m2 = ((long)b[2] & 0x00000000000000FFL) << 32;
        long m3 = ((long)b[5] & 0x00000000000000FFL) << 24;
        long m4 = ((long)b[4] & 0x00000000000000FFL) << 16;
        long m5 = ((long)b[7] & 0x00000000000000FFL) << 8;
        long m6 = ((long)b[6] & 0x00000000000000FFL);
        
        //Piece together the different IEEE Float format components
        iEEEDoubleFormat = sign | e | m0 | m1 | m2 | m3 | m4 | m5 | m6;
            
        return iEEEDoubleFormat;
    }

    
	/**
	* Convert from PC_REAL type to IEEE single precision
	* @param b - byte array of size 4 bytes
	* @return The byte array in integer format
	*/
	public static int pcReal4BTypeToIEEESingle(byte[] b) {
	
	assert (b.length == 4);
	
	//Extract the arrangement of bytes in PC_REAL format and arrange
	//them to IEEE format
	int iEEESingleFormat = 0;
	
	int sign = ((int)b[3] & 0x00000080) << 24;
	int e1 = ((int)b[3] & 0x0000007F) << 1;
	int e0 = ((int)b[2] & 0x00000080) >>> 7;
	
	//Add  2 due to PC_REAL bias being 129 instead of 127 for IEEE
	//Then shift to right position in IEEE format
	int e = ( (e1 | e0) - 0x00000002) << 23;
	
	//Mantissa extraction
	int m0 = ((int)b[2] & 0x0000007F) << 16;
	int m1 = ((int)b[1] & 0x000000FF) << 8;
	int m2 = ((int)b[0] & 0x000000FF);
	
	//Piece together the different IEEE Float format components
	iEEESingleFormat = sign | e | m0 | m1 | m2;
	
	return iEEESingleFormat;
	}
	
	/**
	* Convert from PC_REAL 8 Byte type to IEEE double precision
	*
	* @param b - byte array of size 8 bytes
	* @return Byte array in long type
	*/
	public static long pcReal8BTypeToIEEEDouble(byte[] b) {
	
	assert (b.length == 8);
	
	//Extract the arrangement of bytes in PC_REAL format and arrange
	//them to IEEE format
	long iEEEDoubleFormat = 0;
	        
	long sign = (long)b[7] & (0x0000000000000080L) << 56;
	
	long e1 = ((long)b[7] & 0x000000000000007FL) << 4;
	long e0 = ((long)b[6] & 0x00000000000000F0L) >>> 4;
	
	//Add  2 due to PC_REAL bias being 1025 instead of 1023 as for IEEE
	//Then shift to right position to conform to IEEE format
	long e = ( (e1 | e0) -  0x0000000000000002L) << 52;
	
	//Mantissa extraction
	long m0 = ((long)b[6] & 0x000000000000000FL) << 48;
	long m1 = ((long)b[5] & 0x00000000000000FFL) << 40;
	long m2 = ((long)b[4] & 0x00000000000000FFL) << 32;
	long m3 = ((long)b[3] & 0x00000000000000FFL) << 24;
	long m4 = ((long)b[2] & 0x00000000000000FFL) << 16;
	long m5 = ((long)b[1] & 0x00000000000000FFL) << 8;
	long m6 = ((long)b[0] & 0x00000000000000FFL);
	
	//Piece together the different IEEE Float format components
	iEEEDoubleFormat = sign | e | m0 | m1 | m2 | m3 | m4 | m5 | m6;
	
	return iEEEDoubleFormat;
	}
	


}
