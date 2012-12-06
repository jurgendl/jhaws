package org.jhaws.common.lucene;

import java.io.FileFilter;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.lucene.search.Query;

/**
 * list of search results
 * 
 * @author Jurgen De Landsheer
 * @version 1.0.0 - 22 June 2006
 * 
 * @since 1.5
 */
public final class ResultList implements Iterable<SearchResult> {
    /** LuceneHelper */
    protected final LuceneInterface parent;

    /** {@link Query} */
    protected final Query q;

    /** sorted list of search results */
    protected final SortedSet<SearchResult> searchResults = new TreeSet<SearchResult>();

    /** query */
    protected final String query;

    /** word */
    protected final String word;

    /** total hits */
    protected final int totalHits;

    private transient String toString;

    /**
     * Creates a new ResultList object.
     * 
     * @param parent
     * @param word
     * @param query
     */
    protected ResultList(final LuceneInterface parent, final String word, final String query, final Query q, final int totalHits) {
        this.word = word;
        this.query = query;
        this.q = q;
        this.parent = parent;
        this.totalHits = totalHits;
    }

    /**
     * na
     * 
     * @param result na
     */
    protected void addSearchResult(final SearchResult result) {
        this.searchResults.add(result);
    }

    /**
     * 
     * @see #iterator()
     */
    public Iterator<SearchResult> getIterator() {
        return this.iterator();
    }

    /**
     * na
     * 
     * @param filter na
     * 
     * @return
     */
    public Iterator<SearchResult> getIterator(final FileFilter filter) {
        return this.getResults(filter).iterator();
    }

    /**
     * gets query
     * 
     * @return Returns the query.
     */
    public String getQueryText() {
        return this.query;
    }

    /**
     * na
     * 
     * @return
     */
    public SortedSet<SearchResult> getResults() {
        return Collections.unmodifiableSortedSet(this.searchResults);
    }

    /**
     * na
     * 
     * @param filter na
     * 
     * @return
     */
    public SortedSet<SearchResult> getResults(final FileFilter filter) {
        SortedSet<SearchResult> set = new TreeSet<SearchResult>();

        for (final SearchResult tmp : this.searchResults) {
            if (filter.accept(tmp.getFile())) {
                set.add(tmp);
            }
        }

        return Collections.unmodifiableSortedSet(set);
    }

    /**
     * gets searchResults
     * 
     * @return Returns the searchResults.
     */
    public SortedSet<SearchResult> getSearchResults() {
        return this.searchResults;
    }

    /**
     * na
     * 
     * @return
     */
    public int getSize() {
        return this.getResults().size();
    }

    /**
     * na
     * 
     * @param filter na
     * 
     * @return
     */
    public int getSize(final FileFilter filter) {
        return this.getResults(filter).size();
    }

    /**
     * @return the totalHits
     */
    public int getTotalHits() {
        return this.totalHits;
    }

    /**
     * gets word
     * 
     * @return Returns the word.
     */
    public String getWord() {
        return this.word;
    }

    /**
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<SearchResult> iterator() {
        return this.getResults().iterator();
    }

    /**
     * na
     */
    public void printInfo() {
        this.printInfo(System.out);
    }

    /**
     * na
     * 
     * @param filter na
     */
    public void printInfo(final FileFilter filter) {
        this.printInfo(System.out, filter);
    }

    /**
     * na
     * 
     * @param out na
     */
    public void printInfo(final PrintStream out) {
        out.println(this.getResults().size() + " results"); //$NON-NLS-1$

        for (final SearchResult result : this.getResults()) {
            out.println(result);
        }
    }

    /**
     * na
     * 
     * @param out na
     * @param filter na
     */
    public void printInfo(final PrintStream out, final FileFilter filter) {
        SortedSet<SearchResult> r = this.getResults(filter);
        out.println(r.size() + " results"); //$NON-NLS-1$

        int index = 1;

        for (final SearchResult result : r) {
            out.print(index + ": "); //$NON-NLS-1$
            out.println(result);
            index++;
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (this.toString == null) {
            this.toString = new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString()).append("query", this.query)
                    .append("totalHits", this.totalHits).toString();
        }
        return this.toString;
    }
}
