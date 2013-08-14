package org.jhaws.common.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

/**
 * this class can find read UTF-8, UTF-16BI, UTF-16LI and pure binary files and will put them in a byte array, it will strip the <i>Byte Order
 * Mark</i> if necessary.<br>
 * <br>
 * UTF-32BI and UTF-32LI throws {@link java.io.UnsupportedEncodingException}<br>
 * <br>
 * <b><u>Byte Order Mark (BOM)</u></b><br>
 * <br>
 * 00 00 FE FF -> UTF-32, big-endian<br>
 * FF FE 00 00 -> UTF-32, little-endian <br>
 * FE FF -> UTF-16, big-endian <br>
 * FF FE -> UTF-16, little-endian <br>
 * EF BB BF -> UTF-8<br>
 * <br>
 * <b><u>encoding</u></b><br>
 * <br>
 * US-ASCII -> Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the Unicode character set<br>
 * ISO-8859-1 -> ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1<br>
 * UTF-8 -> Eight-bit UCS Transformation Format<br>
 * UTF-16BE -> Sixteen-bit UCS Transformation Format, big-endian byte-order<br>
 * UTF-16LE -> Sixteen-bit UCS Transformation Format, little-endian byte-order<br>
 * UTF-16 -> Sixteen-bit UCS Transformation Format, byte-order identified by an optional byte-order mark<br>
 * 
 * @author Jurgen
 * @version 1.0.0 - 24 February 2005
 * 
 * @see <a href="http://mindprod.com/jgloss/encoding.html">here</a>
 */
public class DecodingReader {
    /**
     * will read the first bytes of a file and returns the {@link EncodingInfo}
     * 
     * @param file : File : input file
     * 
     * @return : EncodingInfo : contains encoding name, name and <i>BOM</i> string
     */
    public static EncodingInfo findEncoding(final File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            String BOM = ""; //$NON-NLS-1$
            int length = 0;

            while ((fis.available() > 0) && (length < 4)) {
                BOM += (char) fis.read();
                length++;
            }

            fis.close();

            return EncodingInfo.getEncodingInfo(BOM);
        } catch (final IOException ex) {
            System.err.println(ex + ": " + ex.getMessage()); //$NON-NLS-1$

            return null;
        }
    }

    /**
     * reads file (decoded) if necessary (and possible) to a byte array (pure for binary files or unknown decoding, usable for constructing a String
     * if known encoding)
     * 
     * @param file : File : input file
     * 
     * @return : byte[] : byte array from string (pure if binary, usable for constructing a String if other)
     */
    public static byte[] readDecoded(final File file) {
        try {
            EncodingInfo encodingInfo = DecodingReader.findEncoding(file);
            String encoding = encodingInfo.getEncoding();
            int bomLength = encodingInfo.getBOM().length();

            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream is = new BufferedInputStream(fis);
            InputStreamReader isr = new InputStreamReader(is, encoding);
            ByteBuffer bb = ByteBuffer.allocate(fis.available());

            int byt = isr.read();
            int read = 0;

            while (byt != -1) {
                bb.put((byte) byt);
                read++;
                byt = isr.read();
            }

            fis.close();

            byte[] bbuffer = new byte[read - bomLength];
            byte[] bbtobytes = bb.array();
            int startPos = 0;

            if (bomLength > 0) {
                startPos = bomLength - 1;
            }

            for (int i = 0; i < (read - bomLength); i++) {
                bbuffer[i] = bbtobytes[i + startPos];
            }

            return bbuffer;
        } catch (final Exception ex) {
            ex.printStackTrace();

            return new byte[0];
        }
    }
}
