package org.swingeasy.imaging;

import java.math.BigInteger;

/**
 * contains utility functions for converting between hexadecimal strings and their equivalent byte buffers or String objects<br>
 *
 * @author original by Trygve Isaacson, adapted by Jurgen De Landsheer
 * @version ?? - 2001-2004 / 31 March 2005
 */
public class HexString {
    /**
     * appends a hexadecimal representation of a particular char value to a string buffer, two hexadecimal digits are appended to the string
     *
     * @param b : byte: a byte whose hex representation is to be obtained
     * @param hexString : String : the string to append the hex digits to
     */
    private static void appendHexPair(final byte b, final StringBuffer hexString) {
        char highNibble = HexString.kHexChars[(b & 0xF0) >> 4];
        char lowNibble = HexString.kHexChars[b & 0x0F];

        hexString.append(highNibble);
        hexString.append(lowNibble);

        if (HexString.spacesBetween) {
            hexString.append(HexString.separatorChar.toCharArray());
        }
    }

    /**
     * converts a BigInteger to a byte array
     *
     * @param bigInteger : BigInteger
     *
     * @return : bye[] : buffer
     */
    public static byte[] biToBuffer(final BigInteger bigInteger) {
        return bigInteger.toByteArray();
    }

    /**
     * Returns a string containing the hexadecimal representation of the input BigInteger. Each byte in the input array is converted to a two-digit
     * hexadecimal value. Thus the returned string is twice the length of the input byte array. The output hex characters are upper case.
     *
     * @param bigInteger : BigInteger
     *
     * @return : String : the hex string version of the input buffer
     */
    public static String biToHex(final BigInteger bigInteger) {
        return HexString.bufferToHex(bigInteger.toByteArray());
    }

    /**
     * Returns a string containing the hexadecimal representation of the input byte array. Each byte in the input array is converted to a two-digit
     * hexadecimal value. Thus the returned string is twice the length of the input byte array. The output hex characters are upper case.
     *
     * @param buffer : byte[] : a buffer to convert to hex
     *
     * @return : String : the hex string version of the input buffer
     */
    public static String bufferToHex(final byte[] buffer) {
        return HexString.bufferToHex(buffer, 0, buffer.length);
    }

    /**
     * Returns a string containing the hexadecimal representation of the input byte array. Each byte in the input array is converted to a two-digit
     * hexadecimal value. Thus the returned string is twice the length of the specified amount of the input byte array. The output hex characters are
     * upper case.
     *
     * @param buffer : byte[] : a buffer to convert to hex
     * @param startOffset : int : the offset of the first byte in the buffer to process
     * @param length : int : the number of bytes in the buffer to process
     *
     * @return : String : the hex string version of the input buffer
     */
    public static String bufferToHex(final byte[] buffer, final int startOffset, final int length) {
        StringBuffer hexString = new StringBuffer(2 * length);
        int endOffset = startOffset + length;

        for (int i = startOffset; i < endOffset; i++) {
            HexString.appendHexPair(buffer[i], hexString);
        }

        return hexString.toString();
    }

    /**
     * converts a single byte to a hexadecimal string
     *
     * @param b : byte : one byte
     *
     * @return : String : hexadecimal string
     */
    public static String byteToHex(final byte b) {
        int ib = b;

        if (ib < 0) {
            ib += 256;
        }

        int c1 = ib / 16;
        int c2 = ib - (16 * c1);

        return "" + HexString.kHexChars[c1] + HexString.kHexChars[c2];
    }

    /**
     * converts a hexadecimal representation of a BigInteger back to it's original form (BigInteger)
     *
     * @param hexString : String = hex form of BigInteger
     *
     * @return : BigInteger : original form
     */
    public static BigInteger hexToBI(final String hexString) {
        return new BigInteger(HexString.hexToBuffer(hexString));
    }

    /**
     * Returns a byte array built from the byte values represented by the input hexadecimal string. That is, each pair of hexadecimal characters in
     * the input string is decoded into the byte value that they represent, and that byte value is appended to a byte array which is ultimately
     * returned. function doesn't care whether the hexadecimal characters are upper or lower case, and it also can handle odd-length hex strings by
     * assuming a leading zero digit. If any character in the input string is not a valid hexadecimal digit, it throws a NumberFormatException, in
     * keeping with the behavior of Java functions like Integer.parseInt().
     *
     * @param hexString : String : a string of hexadecimal characters
     *
     * @return : byte[] : a byte array built from the bytes indicated by the input string
     *
     * @throws NumberFormatException : on errors
     */
    public static byte[] hexToBuffer(final String hexString) {
        int length = hexString.length();
        byte[] buffer = new byte[(length + 1) / 2];
        boolean evenByte = true;
        byte nextByte = 0;
        int bufferOffset = 0;

        // If given an odd-length input string, there is an implicit
        // leading '0' that is not being given to us in the string.
        // In that case, act as if we had processed a '0' first.
        // It's sufficient to set evenByte to false, and leave nextChar
        // as zero which is what it would be if we handled a '0'.
        if ((length % 2) == 1) {
            evenByte = false;
        }

        for (int i = 0; i < length; i++) {
            char c = hexString.charAt(i);
            int nibble; // A "nibble" is 4 bits: a decimal 0..15

            if ((c >= '0') && (c <= '9')) {
                nibble = c - '0';
            } else if ((c >= 'A') && (c <= 'F')) {
                nibble = (c - 'A') + 0x0A;
            } else if ((c >= 'a') && (c <= 'f')) {
                nibble = (c - 'a') + 0x0A;
            } else {
                throw new NumberFormatException("Invalid hex digit '" + c + "'."); //$NON-NLS-2$
            }

            if (evenByte) {
                nextByte = (byte) (nibble << 4);
            } else {
                nextByte += (byte) nibble;
                buffer[bufferOffset++] = nextByte;
            }

            evenByte = !evenByte;
        }

        return buffer;
    }

    /**
     * converts a hexadecimal string representing one byte (two hexadecimal characters) to that byte
     *
     * @param s : String : hexadecimal string (2 characters)
     *
     * @return : byte
     *
     * @throws RuntimeException NA
     */
    public static byte hexToByte(final String s) {
        if (s == null) {
            throw new RuntimeException("string must be exactly two decimal characters long");
        }

        char[] ca = s.toUpperCase().toCharArray();

        if (ca.length != 2) {
            throw new RuntimeException("string must be exactly two decimal characters long");
        }

        int total = 0;

        switch (ca[0]) {
            case '0':
                break;

            case '1':
                total += (1 * 16);

                break;

            case '2':
                total += (2 * 16);

                break;

            case '3':
                total += (3 * 16);

                break;

            case '4':
                total += (4 * 16);

                break;

            case '5':
                total += (5 * 16);

                break;

            case '6':
                total += (6 * 16);

                break;

            case '7':
                total += (7 * 16);

                break;

            case '8':
                total += (8 * 16);

                break;

            case '9':
                total += (9 * 16);

                break;

            case 'A':
                total += (10 * 16);

                break;

            case 'B':
                total += (11 * 16);

                break;

            case 'C':
                total += (12 * 16);

                break;

            case 'D':
                total += (13 * 16);

                break;

            case 'E':
                total += (14 * 16);

                break;

            case 'F':
                total += (15 * 16);

                break;

            default:
                throw new RuntimeException("string must be exactly two decimal characters long");
        }

        switch (ca[1]) {
            case '0':
                break;

            case '1':
                total += 1;

                break;

            case '2':
                total += 2;

                break;

            case '3':
                total += 3;

                break;

            case '4':
                total += 4;

                break;

            case '5':
                total += 5;

                break;

            case '6':
                total += 6;

                break;

            case '7':
                total += 7;

                break;

            case '8':
                total += 8;

                break;

            case '9':
                total += 9;

                break;

            case 'A':
                total += 10;

                break;

            case 'B':
                total += 11;

                break;

            case 'C':
                total += 12;

                break;

            case 'D':
                total += 13;

                break;

            case 'E':
                total += 14;

                break;

            case 'F':
                total += 15;

                break;

            default:
                throw new RuntimeException("string must be exactly two decimal characters long");
        }

        return (byte) total;
    }

    /**
     * Returns a string built from the byte values represented by the input hexadecimal string. That is, each pair of hexadecimal characters in the
     * input string is decoded into the byte value that they represent, and that byte value is appended to a string which is ultimately returned. This
     * function doesn't care whether the hexadecimal characters are upper or lower case, and it also can handle odd-length hex strings by assuming a
     * leading zero digit. If any character in the input string is not a valid hexadecimal digit, it throws a NumberFormatException, in keeping with
     * the behavior of Java functions like Integer.parseInt(). The conversion assumes the default charset; making the charset explicit could be
     * accomplished by just adding a parameter here and passing it through to the String constructor.
     *
     * @param hexString : String : a string of hexadecimal characters
     *
     * @return : String : a String built from the bytes indicated by the input string
     */
    public static String hexToString(final String hexString) {
        byte[] bytes = HexString.hexToBuffer(hexString);

        return new String(bytes);
    }

    /**
     * returns if spaces will be put between hex pairs
     *
     * @return : boolean
     */
    public static boolean isSpacesBetween() {
        return HexString.spacesBetween;
    }

    /**
     * prints the hex String to a easier to read text to the console with default 8 hex characters per block and 4 blocks on a line, default separator
     * is ' '
     *
     * @param hexedString : String : hex string
     *
     * @return : String : formatted hex string
     */
    public static String printHexString(final String hexedString) {
        return HexString.printHexString(hexedString, 8, 4, " ", ""); //$NON-NLS-2$
    }

    /**
     * prints the hex String to a easier to read text to the console
     *
     * @param hexedString : String : hex string
     * @param hexPairsOnBlock : int : number of characters pairs in a block without block separator ' ' (will always be even)
     * @param hexBlocksOnLine : int : number of blocks on a line without block separator '\n'
     * @param separator : String : separator string/character
     * @param suffix : String : suffix before each line
     *
     * @return : String : formatted hex string
     */
    public static String printHexString(final String hexedString, int hexPairsOnBlock, final int hexBlocksOnLine, final String separator,
            final String suffix) {
        if ((hexedString == null) || (hexedString.length() == 0)) {
            return "";
        }

        hexPairsOnBlock *= 2;

        StringBuffer buffer = new StringBuffer(10);
        buffer.append(suffix);

        StringBuffer source = new StringBuffer(hexedString);
        int sl = source.length();

        for (int i = 0; i < sl; i++) {
            buffer.append(source.charAt(i));

            boolean endBlock = false;

            if (((i + 1) % hexPairsOnBlock) == 0) {
                endBlock = true;
            }

            boolean endLine = false;

            if (((i + 1) % (hexBlocksOnLine * hexPairsOnBlock)) == 0) {
                endLine = true;
            }

            if (endBlock && !endLine && ((i + 1) != sl)) {
                buffer.append(separator);
            }

            if (endLine && ((i + 1) != sl)) {
                buffer.append("\n");
                buffer.append(suffix);
            }
        }

        // System.out.println(buffer);
        return buffer.toString();
    }

    /**
     * remove spaces and newline from a hex string
     *
     * @param source : String : source hex string with spaces
     *
     * @return : String : hex string without spaces
     */
    public static String removeSpaces(final String source) {
        return source.replaceAll(" ", "").replaceAll("\r", "").replaceAll("\n", ""); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
    }

    /**
     * <font color=red><b>DO NOT TOUCH THIS UNLESS YOU REALLY KNOW WHAT YOU ARE DOING</b></font>
     *
     * @param separatorChar2 : char : separator character (normally a space ' ')
     */
    public static void setSeparatorChar(final String separatorChar2) {
        HexString.separatorChar = separatorChar2;
    }

    /**
     * set if spaces will be put between the hex pairs
     *
     * @param spacesBetween : boolean
     */
    public static void setSpacesBetween(final boolean spacesBetween) {
        HexString.spacesBetween = spacesBetween;
    }

    /**
     * Returns a string containing the hexadecimal repr esentation of the input string. Each byte in the input string is converted to a two-digit
     * hexadecimal value. Thus the returned string is twice the length of the input string. The output hex characters are upper case. The conversion
     * assumes the default charset; making the charset explicit could be accomplished by just adding a parameter here and passing it through to
     * getBytes().
     *
     * @param string : String : a string to convert to hex
     *
     * @return : String : the hex version of the input string
     */
    public static String stringToHex(final String string) {
        byte[] stringBytes = string.getBytes();

        return HexString.bufferToHex(stringBytes);
    }

    /** hexadecimal character constants */
    private static final char[] kHexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /** don't touch this unless you really know what you are doing */
    private static String separatorChar = " ";

    /** put spaces between the hex pairs */
    private static boolean spacesBetween = false;
}
