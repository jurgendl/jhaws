package org.jhaws.common.io.filter;

import java.io.File;

/**
 * creates operator filters (filter linking)
 * 
 * @author Jurgen De Landsheer
 * @version 2.0.0 - 27 June 2006
 * 
 * @see org.jhaws.common.io.filter.AbstractFileFilter
 */
public abstract class Operator {
    /**
     * filter1 AND filter2 AND filter3 AND ...
     * 
     * @param description description
     * 
     * @return AbstractFileFilter
     */
    public static final AbstractFileFilter and(String description, AbstractFileFilter... filters) {
        return new AndFileFilter(description, filters);
    }

    /**
     * filter1 OR filter2 OR filter3 OR ...
     * 
     * @param description description
     * 
     * @return AbstractFileFilter
     */
    public static final AbstractFileFilter or(String description, AbstractFileFilter... filters) {
        return new OrFileFilter(description, filters);
    }

    /**
     * filter1 AND filter2
     * 
     * @param description description
     * @param filter1 AbstractFileFilter
     * @param filter2 AbstractFileFilter
     * 
     * @return AbstractFileFilter
     */
    public static final AbstractFileFilter and(String description, AbstractFileFilter filter1, AbstractFileFilter filter2) {
        return and(description, new AbstractFileFilter[] { filter1, filter2 });
    }

    /**
     * filter1 OR filter2
     * 
     * @param description description
     * @param filter1 AbstractFileFilter
     * @param filter2 AbstractFileFilter
     * 
     * @return AbstractFileFilter
     */
    public static final AbstractFileFilter or(String description, AbstractFileFilter filter1, AbstractFileFilter filter2) {
        return or(description, new AbstractFileFilter[] { filter1, filter2 });
    }

    /**
     * filter1 AND filter2 AND filter3 AND ...
     * 
     * @return AbstractFileFilter
     */
    public static final AbstractFileFilter and(AbstractFileFilter... filters) {
        return and(getDescription("and", filters), filters); //$NON-NLS-1$
    }

    /**
     * filter1 OR filter2 OR filter3 OR ...
     * 
     * @return AbstractFileFilter
     */
    public static final AbstractFileFilter or(AbstractFileFilter... filters) {
        return or(getDescription("or", filters), filters); //$NON-NLS-1$
    }

    /**
     * filter1 AND filter2
     * 
     * @param filter1 AbstractFileFilter
     * @param filter2 AbstractFileFilter
     * 
     * @return AbstractFileFilter
     */
    public static final AbstractFileFilter and(AbstractFileFilter filter1, AbstractFileFilter filter2) {
        return and(new AbstractFileFilter[] { filter1, filter2 });
    }

    /**
     * filter1 OR filter2
     * 
     * @param filter1 AbstractFileFilter
     * @param filter2 AbstractFileFilter
     * 
     * @return AbstractFileFilter
     */
    public static final AbstractFileFilter or(AbstractFileFilter filter1, AbstractFileFilter filter2) {
        return or(new AbstractFileFilter[] { filter1, filter2 });
    }

    /**
     * returns description of multiple filters with given operator
     * 
     * @param op operator as string
     * 
     * @return description string
     */
    private static String getDescription(String op, AbstractFileFilter... filters) {
        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < filters.length; i++) {
            sb.append(filters[i].getDescription());

            if (i < (filters.length - 1)) {
                sb.append(" "); //$NON-NLS-1$
                sb.append(op);
                sb.append(" "); //$NON-NLS-1$
            }
        }

        return sb.toString();
    }

    /**
     * AND operation file filter
     * 
     * @author Jurgen De Landsheer
     */
    public static class AndFileFilter extends AbstractFileFilter {
        /** filters */
        private AbstractFileFilter[] filters;

        /**
         * Creates a new AndFileFilter object.
         * 
         * @param description description
         * @param filters
         */
        protected AndFileFilter(String description, AbstractFileFilter[] filters) {
            super(description);
            this.filters = filters;
        }

        /**
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString() {
            StringBuilder sb = new StringBuilder(10);

            sb.append("( "); //$NON-NLS-1$

            for (int i = 0; i < filters.length; i++) {
                sb.append(filters[i].toString());

                if (i < (filters.length - 1)) {
                    sb.append(" AND "); //$NON-NLS-1$
                }
            }

            sb.append(" )"); //$NON-NLS-1$

            return sb.toString();
        }

        /**
         * 
         * @see org.jhaws.common.io.filter.AbstractFileFilter#acceptFile(java.io.File)
         */
        @Override
        public final boolean acceptFile(File f) {
            for (AbstractFileFilter filter : filters) {
                if (!filter.accept(f)) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * OR operation file filter
     * 
     * @author Jurgen De Landsheer
     */
    public static class OrFileFilter extends AbstractFileFilter {
        /** filters */
        private AbstractFileFilter[] filters;

        /**
         * Creates a new OrFileFilter object.
         * 
         * @param description description
         * @param filters
         */
        protected OrFileFilter(String description, AbstractFileFilter[] filters) {
            super(description);
            this.filters = filters;
        }

        /**
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString() {
            StringBuilder sb = new StringBuilder(10);

            sb.append("( "); //$NON-NLS-1$

            for (int i = 0; i < filters.length; i++) {
                sb.append(filters[i].toString());

                if (i < (filters.length - 1)) {
                    sb.append(" OR "); //$NON-NLS-1$
                }
            }

            sb.append(" )"); //$NON-NLS-1$

            return sb.toString();
        }

        /**
         * 
         * @see org.jhaws.common.io.filter.AbstractFileFilter#acceptFile(java.io.File)
         */
        @Override
        public final boolean acceptFile(File f) {
            for (AbstractFileFilter filter : filters) {
                if (filter.accept(f)) {
                    return true;
                }
            }

            return false;
        }
    }
}
