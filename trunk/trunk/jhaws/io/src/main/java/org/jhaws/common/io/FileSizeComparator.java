package org.jhaws.common.io;

import java.util.Comparator;

/**
 * file size comparator <br>
 * 
 * @author Jurgen De Landsheer
 * @version 6 June 2006
 */
public class FileSizeComparator implements Comparator<IOFile> {
    /**
     * 
     * sorting
     * 
     * @author Jurgen De Landsheer
     */
    public enum Sort {
        /** LARGEST_FIRST */
        LARGEST_FIRST,
        /** SMALLEST_FIRST */
        SMALLEST_FIRST;
        /**
         * returns 1 or -1
         * 
         * @return 1 or -1
         */
        protected int direction() {
            switch (this) {
                case SMALLEST_FIRST:
                    return 1;

                case LARGEST_FIRST:
                    return -1;

                default:
                    return 0;
            }
        }
    }

    /** default: is from small to large */
    private Sort type = Sort.SMALLEST_FIRST;

    /**
     * Creates a new FileDateComparator object.
     */
    public FileSizeComparator() {
        super();
    }

    /**
     * Creates a new FileSizeComparator object.
     * 
     * @param type : type, SMALLEST_FIRST or LARGEST_FIRST
     */
    public FileSizeComparator(final Sort type) {
        this.type = type;
    }

    /**
     * 
     * @see java.util.Comparator#compare(java.lang.Object,java.lang.Object)
     */
    @Override
    public int compare(final IOFile f1, final IOFile f2) {
        long s1 = 0;
        long s2 = 0;

        if (!f1.isDirectory()) {
            s1 = f1.length();
        }

        if (!f2.isDirectory()) {
            s2 = f2.length();
        }

        if (s1 > s2) {
            return 1 * this.type.direction();
        }
        return -1 * this.type.direction();
    }

    /**
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        return false;
    }

    /**
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
