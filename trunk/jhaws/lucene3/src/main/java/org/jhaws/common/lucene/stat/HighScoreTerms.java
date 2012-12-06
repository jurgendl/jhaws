package org.jhaws.common.lucene.stat;

import java.io.Serializable;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * na
 * 
 * @author Jurgen De Landsheer
 * 
 * @since 3.0.0
 */
public class HighScoreTerms implements Serializable, Comparable<HighScoreTerms> {
    /** serialVersionUID */
    private static final long serialVersionUID = 2571639562647684786L;

    /** field */
    public final SortedSet<Occurence> occurences = new TreeSet<Occurence>();

    /** field */
    public final String termText;

    /** field */
    public int count = 0;

    /** field */
    public int total_occurences;

    /**
     * Creates a new HighScoreTerms object.
     * 
     * @param termText na
     */
    public HighScoreTerms(String termText) {
        this.termText = termText;
    }

    /**
     * na
     * 
     * @param id na
     * @param occurences na
     */
    public void add(final int id, @SuppressWarnings("hiding") final int occurences) {
        this.occurences.add(new Occurence(id, occurences));
        this.total_occurences += occurences;
        this.count++;
    }

    /**
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(HighScoreTerms o) {
        return o.total_occurences - this.total_occurences;
    }

    /**
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HighScoreTerms)) {
            return false;
        }

        HighScoreTerms other = (HighScoreTerms) obj;

        return this.termText.equals(other.termText);
    }

    /**
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.termText.hashCode();
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.termText + ":" + this.total_occurences + ":" + Arrays.toString(this.occurences.toArray()); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
