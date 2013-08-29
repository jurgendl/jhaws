package org.jhaws.common.ldap.filters;

import java.util.ArrayList;
import java.util.List;

/**
 * filter1 AND filter2 AND filter3 ...
 * 
 * @author Jurgen
 */
public class And implements Filter {
    /** alle filters */
    private List<Filter> filters = new ArrayList<Filter>();

    /**
     * Creates a new And object.
     */
    public And() {
        super();
    }

    /**
     * Creates a new And object.
     * 
     * @param filters lijst van Filter (1 of meer)
     */
    public And(Filter... filters) {
        this();

        for (Filter f : filters) {
            this.filters.add(f);
        }
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
     * gets filters
     * 
     * @return Returns the filters.
     */
    public List<Filter> getFilters() {
        return this.filters;
    }

    /**
     * verwijderd een filter uit de lijst
     * 
     * @param filter Filter
     */
    public void removeFilter(Filter filter) {
        this.filters.remove(filter);
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
     * wordt gebruikt om filter op te bouwen
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(10);
        sb.append("(&"); //$NON-NLS-1$

        for (Filter f : this.filters) {
            sb.append(f.toString());
        }

        sb.append(")"); //$NON-NLS-1$

        return sb.toString();
    }
}
