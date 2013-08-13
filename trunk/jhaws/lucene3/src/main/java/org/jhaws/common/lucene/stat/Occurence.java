package org.jhaws.common.lucene.stat;

import java.io.Serializable;

/**
 * na
 * 
 * @author Jurgen
 * 
 * @since 3.0.0
 */
public class Occurence implements Serializable, Comparable<Occurence> {
    /** serialVersionUID */
    private static final long serialVersionUID = -3138320287147881332L;

    /** field */
    public final int id;

    /** field */
    public final int occurences;

    /**
     * Creates a new Occurence object.
     * 
     * @param id na
     * @param occurences na
     */
    protected Occurence(final int id, final int occurences) {
        this.id = id;
        this.occurences = occurences;
    }

    /**
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Occurence o) {
        return o.occurences - this.occurences;
    }

    /**
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Occurence)) {
            return false;
        }

        Occurence other = (Occurence) obj;

        return this.id == other.id;
    }

    /**
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.id;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.id + ":" + this.occurences; //$NON-NLS-1$
    }
}
