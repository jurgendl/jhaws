package org.jhaws.common.io;

import java.util.Comparator;

/**
 * file (last modified) date comparator <br>
 * <br>
 * <b><u>version history</u></b><br>
 * 1.1.0:<br>
 * - sorting is now an enum<br>
 *
 * @author Jurgen
 * @version 1.1.0 - 6 June 2006
 */
@Deprecated
public class FileDateComparator implements Comparator<IOGeneralFile<?>> {
    /**
     *
     * sorting
     *
     * @author Jurgen
     */
    public enum Sort {
        /** NEWEST_FIRST */
        NEWEST_FIRST,
        /** OLDEST_FIRST */
        OLDEST_FIRST;
        /**
         * returns 1 or -1
         *
         * @return 1 or -1
         */
        protected int direction() {
            switch (this) {
                case OLDEST_FIRST:
                    return 1;

                case NEWEST_FIRST:
                    return -1;

                default:
                    return 0;
            }
        }
    }

    /** constant: from oldest to newest */
    public static final int TYPE_OLDEST_FIRST = 1;

    /** constant: from newest to oldest */
    public static final int TYPE_NEWEST_FIRST = -1;

    /** default: from oldest to newest */
    private Sort type = Sort.OLDEST_FIRST;

    /**
     * Creates a new FileDateComparator object.
     */
    public FileDateComparator() {
        super();
    }

    /**
     * Creates a new FileSizeComparator object.
     *
     * @param type
     */
    public FileDateComparator(final Sort type) {
        this.type = type;
    }

    /**
     *
     * @see java.util.Comparator#compare(java.lang.Object,java.lang.Object)
     */
    @Override
    public int compare(final IOGeneralFile<?> f1, final IOGeneralFile<?> f2) {
        long d1 = 0;
        long d2 = 0;

        if (!f1.isDirectory()) {
            d1 = f1.lastModified();
        }

        if (!f2.isDirectory()) {
            d2 = f2.lastModified();
        }

        if (d2 > d1) {
            return -1 * this.type.direction();
        }
        return +1 * this.type.direction();
    }
}
