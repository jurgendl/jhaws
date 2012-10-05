package org.jhaws.common.ldap.filters;

import java.util.ArrayList;
import java.util.List;


/**
 * filter1 OR filter2 OR filter3 ...
 *
 * @author Jurgen De Landsheer
 */
public class Or implements Filter {
    /** alle filters */
    private List<Filter> filters = new ArrayList<Filter>();

/**
     * Creates a new Or object.
     */
    public Or() {
        super();
    }

/**
     * Creates a new Or object.
     *
     * @param filters lijst van filters (1 of meer)
     */
    public Or(Filter... filters) {
        this();

        for (Filter f : filters) {
            this.filters.add(f);
        }
    }

    /**
     * sets filters
     *
     * @param filters The filters to set.
     */
    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    /**
     * gets filters
     *
     * @return Returns the filters.
     */
    public List<Filter> getFilters() {
        return filters;
    }

    /**
     * voeg filters toe aan de lijst
     */
    public void addFilters(Filter... filtersToAdd) {
        for (Filter f : filtersToAdd) {
            this.filters.add(f);
        }
    }

    /**
     * verwijderd een filter uit de lijst
     *
     * @param filter Filter
     */
    public void removeFilter(Filter filter) {
        filters.remove(filter);
    }

    /**
     * wordt gebruikt om filter op te bouwen
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (filters.size() == 1) {
            return filters.iterator().next().toString();
        }

        StringBuilder sb = new StringBuilder(10);

        sb.append("(|"); //$NON-NLS-1$

        for (Filter f : filters) {
            sb.append(f.toString());
        }

        sb.append(")"); //$NON-NLS-1$

        return sb.toString();
    }
}
