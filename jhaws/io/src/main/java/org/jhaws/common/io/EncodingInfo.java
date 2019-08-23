package org.jhaws.common.io;

import java.util.List;
import java.util.Vector;

/**
 * holds encoding info
 *
 * @author Jurgen
 * @version 1.0.0 - 24 February 2005
 */
public class EncodingInfo {
    /** contains list of EncodingInfo objects */
    private static List<EncodingInfo> encodingList = new Vector<>();

    static {
        EncodingInfo.encodingList.add(new EncodingInfo(null, "UTF-32, big-endian", new String(new byte[] { 0, 0, -2, -1 }))); // not //$NON-NLS-1$
                                                                                                                              // supported by java,
                                                                                                                              // will throw
                                                                                                                              // UnsupportedEncodingException
        EncodingInfo.encodingList.add(new EncodingInfo(null, "UTF-32, little-endian", new String(new byte[] { 0, 0, -1, -2 }))); // not //$NON-NLS-1$
                                                                                                                                 // supported by java,
                                                                                                                                 // will throw
                                                                                                                                 // UnsupportedEncodingException
        EncodingInfo.encodingList.add(new EncodingInfo("UTF-16BE", "UTF-16, big-endian", new String(new byte[] { -2, -1 }))); //$NON-NLS-1$ //$NON-NLS-2$
        EncodingInfo.encodingList.add(new EncodingInfo("UTF-16LE", "UTF-16, little-endian", new String(new byte[] { -1, -2 }))); //$NON-NLS-1$ //$NON-NLS-2$
        EncodingInfo.encodingList.add(new EncodingInfo("UTF-8", "UTF-8", new String(new byte[] { -17, -69, -65 }))); //$NON-NLS-1$ //$NON-NLS-2$

        // encodingList.add(new EncodingInfo("Shift_JIS", "Shift JIS, Japanese", HexString.hexToString("81"))); // 81-9F and E0-EF
        EncodingInfo.encodingList.add(new EncodingInfo("US-ASCII", "unknown (binary)", "")); // must be //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                                                                             // added as last
    }

    /**
     * gets encoding info on first bytes read from a file
     *
     * @param bomBuffer : byte[] : first bytes read from file
     *
     * @return : EncodingInfo : appropriate encoding info
     */
    public static EncodingInfo getEncodingInfo(final byte[] bomBuffer) {
        return EncodingInfo.getEncodingInfo(new String(bomBuffer));
    }

    /**
     * gets encoding info on first bytes read from a file
     *
     * @param bomBuffer : char[] : first bytes read from file as a char array
     *
     * @return : EncodingInfo : appropriate encoding info
     */
    public static EncodingInfo getEncodingInfo(final char[] bomBuffer) {
        return EncodingInfo.getEncodingInfo(new String(bomBuffer));
    }

    /**
     * gets encoding info on first bytes read from a file
     *
     * @param bom : String : first bytes read from file as a string
     *
     * @return : EncodingInfo : appropriate encoding info
     */
    public static EncodingInfo getEncodingInfo(final String bom) {
        EncodingInfo[] infos = EncodingInfo.getEncodingInfos();

        for (EncodingInfo info : infos) {
            if (bom.startsWith(info.getBOM())) {
                return info;
            }
        }

        return new EncodingInfo();
    }

    /**
     * returns a list (array) of all EncodingInfo objects
     *
     * @return : EncodingInfo[] : complete list
     */
    public static EncodingInfo[] getEncodingInfos() {
        Object[] oos = EncodingInfo.encodingList.toArray();
        EncodingInfo[] encodingInfos = new EncodingInfo[oos.length];

        for (int i = 0; i < oos.length; i++) {
            encodingInfos[i] = (EncodingInfo) oos[i];
        }

        return encodingInfos;
    }

    /** <i>BOM</i> string */
    private String BOM = ""; //$NON-NLS-1$

    /** encoding name */
    private String encoding = ""; //$NON-NLS-1$

    /** name */
    private String name = ""; //$NON-NLS-1$

    /**
     * Creates a new EncodingInfo object.
     */
    private EncodingInfo() {
        super();
    }

    /**
     * Creates a new EncodingInfo object.; internal use
     *
     * @param encoding : String : encoding name
     * @param name : String : name
     * @param BOM : String : <i>BOM</i> string
     */
    private EncodingInfo(final String encoding, final String name, final String BOM) {
        this.encoding = encoding;
        this.name = name;
        this.BOM = BOM;
    }

    /**
     * gets <i>BOM</i> string
     *
     * @return : String : <i>BOM</i> string
     */
    public String getBOM() {
        return this.BOM;
    }

    /**
     * gets encoding name
     *
     * @return : String : encoding name
     */
    public String getEncoding() {
        return this.encoding;
    }

    /**
     * gets name
     *
     * @return : String : name
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.encoding + ": " + this.name + " [" + this.BOM + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
