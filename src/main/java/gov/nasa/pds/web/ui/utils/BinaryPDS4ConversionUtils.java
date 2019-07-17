package gov.nasa.pds.web.ui.utils;

/**
 * Helper class to convert between PDS4 data types
 * 
 * @author ghflore1
 *
 */
public class BinaryPDS4ConversionUtils {

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
	public static long convertSignedLSBIntegers(byte[] b, int n) {
		assert (1 <= n && 8 * n <= Long.SIZE);
		long value = 0;
		// From least- to most-significant byte, put each byte
		// into the accumulating value, in the most significant
		// byte position, shifting the prior value 8 bits to
		// the right. That way the most significant byte will
		// be the last byte we put in, setting the sign bit.

		for (int i = 0; i < n; i++) {
			value = (value >>> 8) | (b[i] << (Long.SIZE - 8));
		}
		// TODO should this pad on the right?
		// Pad with the sign bit on the left, if we used fewer bytes
		// than the size of an int.
		if (n < 8) {
			value = value >> (Long.SIZE - 8 * n);
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
	public static long convertUnsignedLSBIntegers(byte[] b, int n) {

		long anUnsignedInt = 0;
		
		if (n == 2) {
			anUnsignedInt = (long) ((0xFF & ((int) b[1])) << 8 | (0xFF & ((int) b[0])));
		}
		
		if (n == 4) {
			anUnsignedInt = ((long) ((0xFF & ((int) b[3])) << 24
					| (0xFF & ((int) b[2])) << 16 | (0xFF & ((int) b[1])) << 8 | (0xFF & ((int) b[0])))) & 0xFFFFFFFFL;
		}
		
		if (n == 8) {
			anUnsignedInt = ((long) ((0xFF & ((int) b[7])) << 56
					| (0xFF & ((int) b[6])) << 48 | (0xFF & ((int) b[5])) << 40 | (0xFF & ((int) b[4])) << 32
					| (0xFF & ((int) b[3])) << 24 | (0xFF & ((int) b[2])) << 16 | (0xFF & ((int) b[1])) << 8
					| (0xFF & ((int) b[0])))) & 0xFFFFFFFFFFFFFFFFL;
		}

		return anUnsignedInt;
	}
	
	
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
	public static long convertSignedMSBIntegers(byte[] b, int n) {
		// Integer.size is The number of bits used to represent an <tt>int</tt>
		// value in two's complement binary form.
		// static final int SIZE = 32;
		// so assert n(number of bytes in array) is between 1 and 8*n is less
		// than 32)
		assert (1 <= n && 8 * n <= Long.SIZE);

		long value = 0;
		// From least- to most-significant byte, put each byte
		// into the accumulating value, in the most significant
		// byte position, shifting the prior value 8 bits to
		// the right. That way the most significant byte will
		// be the last byte we put in, setting the sign bit.
		for (int i = n - 1; i >= 0; --i) {
			value = (value >>> 8) | (b[i] << (Long.SIZE - 8));
		}
		// Pad with the sign bit on the left, if we used fewer bytes
		// than the size of an int.
		if (n < 8) {
			value = value >> (Long.SIZE - 8 * n);
		}
		return value;
	}
	
	
	/**
	 * 
	 * Converts an array of bytes into a long, permitting unsigned bytes. The
	 * array of bytes may be of length 2, 4, or 8, in big-endian order. That
	 * is, b[0] holds the most significant bits, b[n-1] the least.
	 * 
	 * @param b
	 *            the array of bytes, in big-endian order
	 * @param n
	 *            the number of bytes to convert in the array, from 1 to the
	 *            size of an int
	 * @return the signed int value represented by the bytes
	 */
	public static long convertUnsignedMSBIntegers(byte[] b, int n) {
		// MSB_UNSIGNED_INTEGER 2-, 4-, and 8-byte unsigned integers
		long anUnsignedInt = 0;
		
		if (n == 2) {
			anUnsignedInt = (long) ((0xFF & ((int) b[0])) << 8 | (0xFF & ((int) b[1])));
		}
		if (n == 4) {
			anUnsignedInt = ((long) ((0xFF & ((int) b[0])) << 24
					| (0xFF & ((int) b[1])) << 16 | (0xFF & ((int) b[2])) << 8 | (0xFF & ((int) b[3])))) & 0xFFFFFFFFL;
		}
		if (n == 8) {
			anUnsignedInt = ((long) ((0xFF & ((int) b[0])) << 56
					| (0xFF & ((int) b[1])) << 48 | (0xFF & ((int) b[2])) << 40 | (0xFF & ((int) b[3])) << 32
					| (0xFF & ((int) b[4])) << 24 | (0xFF & ((int) b[5])) << 16 | (0xFF & ((int) b[6])) << 8
					| (0xFF & ((int) b[7])))) & 0xFFFFFFFFFFFFFFFFL;
		}
		
		return anUnsignedInt;
	}
	
	/**
	 * 
	 * Converts an array of bytes into a long, permitting unsigned bytes. The
	 * array of bytes is of size 1 byte
	 * 
	 * @param b
	 *            the array of bytes, in big-endian order
	 * @param n
	 *            the number of bytes to convert in the array, from 1 to the
	 *            size of an int
	 * @return the signed int value represented by the bytes
	 */
	public static long convertByte(byte[] b, int n) {
	
		long anUnsignedInt = 0;
		if (n == 1) {
			anUnsignedInt = (long) (0xFF & ((int) b[0]));
		}
		return anUnsignedInt;
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
     * Convert a byte array of size 2 bytes to type short
     * @param b: byte array if size 2 bytes
     * @return shot representation of the byte array
     */
    public static short convertByteArrayToShort(byte[] b) {
        
        assert (b.length == 2);
    
        short aSignedShort = 0;
        
        aSignedShort =    (short) (((short)(0xFF & b[0]) << 8) |
                          ((short)(0xFF & b[1])));
        
        return aSignedShort;
    }
    
    
    /**
     * Convert from IEEE754LSBSingle type to IEEE754MSBSingle precision
     * @param b - byte array of size 4 bytes
     * @return The byte array in integer format
     */
    public static int IEEE754LSBToIEEE754MSBSingle(byte[] b) {

		int msbSingleInt = 0;

		msbSingleInt = ((int) (0x000000FF & b[3]) << 24)
				| ((int) (0x000000FF & b[2]) << 16) | ((int) (0x000000FF & b[1]) << 8)
				| ((int) (0x000000FF & b[0]));

		return msbSingleInt;
	}

    /**
     * Convert from IEEE754LSBDouble type to IEEE754MSBDouble precision
     *
     * @param b - byte array of size 8 bytes
     * @return Byte array in long type
     */
	public static long IEEE754LSBToIEEE754MSBDouble(byte[] b) {

		long msbDoubleLong = 0;

		msbDoubleLong = ((long) (0x00000000000000FFL & b[7]) << 56)
				| ((long) (0x00000000000000FFL & b[6]) << 48) | ((long) (0x00000000000000FFL & b[5]) << 40)
				| ((long) (0x00000000000000FFL & b[4]) << 32) | ((long) (0x00000000000000FFL & b[3]) << 24)
				| ((long) (0x00000000000000FFL & b[2]) << 16) | ((long) (0x00000000000000FFL & b[1]) << 8)
				| ((long) (0x00000000000000FFL & b[0]));

		return msbDoubleLong;
    } 
}
