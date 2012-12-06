package org.jhaws.common.lucene.stat;

import java.io.Serializable;

/**
 * na
 * 
 * @author Jurgen De Landsheer
 * 
 * @since 3.0.0
 */
public class DocHighScoreTerms implements Serializable, Comparable<DocHighScoreTerms> {
    /** serialVersionUID */
    private static final long serialVersionUID = -4614853338399545628L;

    /** field */
    public final String fieldName;

    /** field */
    public final String termText;

    /** field */
    public final int docFrequency;

    /**
     * Creates a new DocHighScoreTerms object.
     * 
     * @param docFrequency na
     * @param fieldName na
     * @param termText na
     */
    public DocHighScoreTerms(int frequency, String fieldName, String termText) {
        this.docFrequency = frequency;
        this.fieldName = fieldName;
        this.termText = termText;
    }

    /**
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(DocHighScoreTerms o) {
        return o.docFrequency - this.docFrequency;
    }

    /**
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DocHighScoreTerms)) {
            return false;
        }

        DocHighScoreTerms other = (DocHighScoreTerms) obj;

        return this.fieldName.equals(other.fieldName);
    }

    /**
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.fieldName.hashCode();
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.docFrequency + ":" + this.fieldName + ":" + this.termText; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
