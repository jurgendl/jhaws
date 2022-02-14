package org.jhaws.common.io;

public enum ByteTo {
    Byte, GigaByte, KiloByte, MegaByte, TerraByte;

    /** 1 */
    private static final long O = 1L;

    /** 1024 */
    private static final long KBV = 1024L;

    /** 1048576 */
    private static final long MBV = 1024L * 1024L;

    /** 1125899906842624 */
    private static final long GBV = 1024L * 1024L * 1024L;

    /** 1152921504606846976 */
    private static final long TBV = 1024L * 1024L * 1024L * 1024L;

    /** 0,0080645161290322580645161290322581 */
    private static final double IKBV = 1d / ByteTo.KBV;

    /** 7,8755040322580645161290322580645e-6 */
    private static final double IMBV = 1d / ByteTo.MBV;

    /** 7,6909219065020161290322580645161e-9 */
    private static final double IGBV = 1d / ByteTo.GBV;

    /** 7,510665924318375126008064516129e-12 */
    private static final double ITBV = 1d / ByteTo.TBV;

    /**
     * na
     *
     * @return
     */
    public String display() {
        switch (this) {
            case KiloByte:
                return "kB"; //$NON-NLS-1$

            case MegaByte:
                return "MB"; //$NON-NLS-1$

            case GigaByte:
                return "GB"; //$NON-NLS-1$

            case TerraByte:
                return "TB"; //$NON-NLS-1$

            default:
            case Byte:
                return "bytes"; //$NON-NLS-1$
        }
    }

    /**
     * na
     *
     * @return
     */
    public double doubleValue() {
        switch (this) {
            case KiloByte:
                return ByteTo.IKBV;

            case MegaByte:
                return ByteTo.IMBV;

            case GigaByte:
                return ByteTo.IGBV;

            case TerraByte:
                return ByteTo.ITBV;

            default:
            case Byte:
                return ByteTo.O;
        }
    }

    /**
     * na
     *
     * @return
     */
    public long longValue() {
        switch (this) {
            case KiloByte:
                return ByteTo.KBV;

            case MegaByte:
                return ByteTo.MBV;

            case GigaByte:
                return ByteTo.GBV;

            case TerraByte:
                return ByteTo.TBV;

            default:
            case Byte:
                return ByteTo.O;
        }
    }
}
