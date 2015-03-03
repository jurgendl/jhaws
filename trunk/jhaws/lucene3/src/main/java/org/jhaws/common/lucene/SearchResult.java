package org.jhaws.common.lucene;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.NullFragmenter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SpanGradientFormatter;
import org.apache.lucene.search.highlight.TokenGroup;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.jhaws.common.io.IODirectory;
import org.jhaws.common.io.IOFile;
import org.jhaws.common.lucene.exception.ConvertorNotSupportedException;
import org.jhaws.common.lucene.exception.NoSearchWordException;

/**
 * one single search result
 * 
 * @author Jurgen
 * @version 1.0.0 - 22 June 2006
 * 
 * @since 1.5
 */
public final class SearchResult extends LuceneDocument {
    /** Logger for this class */
    private static final Logger logger = Logger.getLogger(SearchResult.class);

    /** serialVersionUID */
    private static final long serialVersionUID = -1916317004890469432L;

    /** SpanGradientFormatter */
    private static final SpanGradientFormatter formatter = new SpanGradientFormatter(1.0f, "#000000", "#0000FF", "#FFFFFF", "#FFFF00"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    /** field */
    protected final ResultList parent;

    /** field */
    protected final int id;

    /** field */
    private final float score;

    /** field */
    private final int order;

    /**
     * Creates a new SearchResult object.
     * 
     * @param parent
     * @param doc
     * @param id
     * @param score
     */
    protected SearchResult(final int order, final ResultList parent, final Document doc, final int id, final float score, final LuceneInterface helper) {
        super(doc, helper);
        this.order = order;
        this.parent = parent;
        this.id = id;
        this.score = score;
    }

    /**
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final LuceneDocument other) {
        if (other instanceof SearchResult) {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            compareToBuilder.append(((SearchResult) other).score, this.score);
            compareToBuilder.append(this.getURL().toString(), other.getURL().toString());
            compareToBuilder.append(this.getLastModified().getTime(), other.getLastModified().getTime());
            return compareToBuilder.toComparison();
        }

        return super.compareTo(other);
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws CorruptIndexException na
     * @throws IOException na
     */
    public Explanation explain() throws CorruptIndexException, IOException {
        return this.parent.parent.explain(this.parent.q, this.getId());
    }

    /**
     * na
     * 
     * @param dir na
     * 
     * @return
     * 
     * @throws IOException IOException
     * @throws ConvertorNotSupportedException ConvertorNotSupportedException
     * @throws NoSearchWordException NoSearchWordException
     * @throws ParseException ParseException
     * @throws InvalidTokenOffsetsException
     */
    public IOFile exportToHtml(IODirectory dir) throws IOException, ConvertorNotSupportedException, NoSearchWordException, ParseException,
            InvalidTokenOffsetsException {
        return this.exportToHtml(new IOFile(dir, this.getFile().getName() + ".html")); //$NON-NLS-1$
    }

    /**
     * na
     * 
     * @param html na
     * 
     * @return
     * 
     * @throws IOException when something fails
     * @throws ConvertorNotSupportedException ConvertorNotSupportedException
     * @throws NoSearchWordException NoSearchWordException
     * @throws ParseException ParseException
     * @throws InvalidTokenOffsetsException
     */
    public IOFile exportToHtml(final IOFile html) throws IOException, ConvertorNotSupportedException, NoSearchWordException, ParseException,
            InvalidTokenOffsetsException {
        SearchResult.logger.info("exporting to html " + html.getAbsolutePath()); //$NON-NLS-1$

        final String text = this.getDocumentText();
        final String result = this.exportToHtmlText(text);

        html.getParentFile().mkdirs();

        FileWriter writer = new FileWriter(html);

        writer.write("<html><title>Search result for term '"); //$NON-NLS-1$
        writer.write(this.getSearchQuery());
        writer.write("' on document '"); //$NON-NLS-1$
        writer.write(this.getFile().getName());
        writer.write("' has a score of "); //$NON-NLS-1$
        writer.write(this.getScoreString());
        writer.write("</title><body>"); //$NON-NLS-1$

        if (result.equals("")) { //$NON-NLS-1$
            SearchResult.logger.warn("could not find search term in text for " + html.getAbsolutePath()); //$NON-NLS-1$
            writer.write(text);
        } else {
            writer.write(result);
        }

        writer.write("</body></html>"); //$NON-NLS-1$
        writer.close();

        return html;
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws IOException na
     * @throws ConvertorNotSupportedException na
     * @throws NoSearchWordException na
     * @throws ParseException na
     * @throws InvalidTokenOffsetsException
     */
    public String exportToHtmlText() throws IOException, ConvertorNotSupportedException, NoSearchWordException, ParseException,
            InvalidTokenOffsetsException {
        final String text = this.getDocumentText();
        final String result = this.exportToHtmlText(text);
        StringBuilder writer = new StringBuilder();

        writer.append("<html><title>Search result for term '"); //$NON-NLS-1$
        writer.append(this.getSearchQuery());
        writer.append("' on document '"); //$NON-NLS-1$
        writer.append(this.getFile().getName());
        writer.append("' has a score of "); //$NON-NLS-1$
        writer.append(this.getScoreString());
        writer.append("</title><body>"); //$NON-NLS-1$

        if (result.equals("")) { //$NON-NLS-1$
            SearchResult.logger.warn("could not find search term in text"); //$NON-NLS-1$
            writer.append(text);
        } else {
            writer.append(result);
        }

        writer.append("</body></html>"); //$NON-NLS-1$

        return writer.toString();
    }

    /**
     * highlight text
     * 
     * @param text na
     * 
     * @return
     * 
     * @throws IOException na
     * @throws ParseException na
     * @throws ConvertorNotSupportedException na
     * @throws InvalidTokenOffsetsException
     */
    private String exportToHtmlText(String text) throws IOException, ParseException, ConvertorNotSupportedException, InvalidTokenOffsetsException {
        org.apache.lucene.analysis.Analyzer analyzer = Analyzer.valueOf(this.getLuceneDocument().get(ID.language.f())).analyzer();
        QueryParser queryParser = new QueryParser(Version.LUCENE_36, ID.text.f(), analyzer);
        queryParser.setAllowLeadingWildcard(true);

        Query query = this.rewriteQuery(queryParser.parse(this.getSearchQuery()));
        QueryScorer scorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(SearchResult.formatter, scorer);
        Fragmenter fragmenter = new NullFragmenter();
        TokenStream tokenStream = Analyzer.valueOf(this.getLanguage()).analyzer().tokenStream(ID.text.f(), new StringReader(text));

        highlighter.setTextFragmenter(fragmenter);

        return highlighter.getBestFragments(tokenStream, text, Short.MAX_VALUE, ""); //$NON-NLS-1$
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws IllegalArgumentException na
     */
    @SuppressWarnings("deprecation")
    public IOFile getFile() {
        if (this.getURL().getProtocol().equals("file")) { //$NON-NLS-1$

            return new IOFile(URLDecoder.decode(this.getURL().getFile()));
        }

        throw new IllegalArgumentException("protocol is not file"); //$NON-NLS-1$
    }

    /**
     * Gets id.
     * 
     * @return Returns the id.
     */
    public final int getId() {
        return this.id;
    }

    /**
     * Gets order.
     * 
     * @return Returns the order.
     */
    public final int getOrder() {
        return this.order;
    }

    /**
     * gets score
     * 
     * @return Returns the score.
     */
    public float getScore() {
        return this.score;
    }

    /**
     * na
     * 
     * @return
     */
    public String getScoreString() {
        return "" + ((float) ((int) (this.getScore() * 10000)) / 100) + "%"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * na
     * 
     * @return
     */
    public String getSearchQuery() {
        return this.parent.getQueryText();
    }

    /**
     * gets word
     * 
     * @return Returns the word.
     */
    public String getSearchWord() {
        return this.parent.getWord();
    }

    /**
     * na
     * 
     * @param prefix na
     * @param suffix na
     * @param between na
     * 
     * @return
     * 
     * @throws ConvertorNotSupportedException ConvertorNotSupportedException
     * @throws IOException IOException
     * @throws ParseException ParseException
     * @throws InvalidTokenOffsetsException
     */
    public String preview(final String prefix, final String suffix, final String between) throws ConvertorNotSupportedException, IOException,
            ParseException, InvalidTokenOffsetsException {
        org.apache.lucene.analysis.Analyzer analyzer = Analyzer.valueOf(this.getLuceneDocument().get(ID.language.f())).analyzer();
        QueryParser queryParser = new QueryParser(Version.LUCENE_36, ID.text.f(), analyzer);
        queryParser.setAllowLeadingWildcard(true);

        Query query = this.rewriteQuery(queryParser.parse(this.parent.getQueryText()));
        QueryScorer scorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(new Formatter() {
            @Override
            public String highlightTerm(String originalText, TokenGroup group) {
                if (group.getTotalScore() <= 0) {
                    return originalText;
                }

                return prefix + originalText + suffix;
            }
        }, scorer);

        highlighter.setTextFragmenter(new SimpleFragmenter(40));

        final String text = this.getDocumentText();
        TokenStream stream = analyzer.tokenStream(ID.text.f(), new StringReader(text));
        String fragment = highlighter.getBestFragments(stream, text, 5, between);

        if (fragment == null) {
            return null;
        }

        fragment = fragment.replace('\n', ' ').replace('\r', ' ');

        while (fragment.indexOf("  ") != -1) { //$NON-NLS-1$
            fragment = fragment.replaceAll("  ", " "); //$NON-NLS-1$ //$NON-NLS-2$
        }

        System.out.println(fragment);

        return fragment;
    }

    /**
     * returns html preview string
     * 
     * @return preview string
     * 
     * @throws ConvertorNotSupportedException ConvertorNotSupportedException
     * @throws ParseException ParseException
     * @throws IOException IOException
     * @throws InvalidTokenOffsetsException
     */
    public String previewHtml() throws ConvertorNotSupportedException, ParseException, IOException, InvalidTokenOffsetsException {
        return "\u2026 " + this.preview("<u>", "</u>", " \u2026 ") + " \u2026"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    }

    /**
     * returns html preview string
     * 
     * @return preview string
     * 
     * @throws ConvertorNotSupportedException ConvertorNotSupportedException
     * @throws ParseException ParseException
     * @throws IOException IOException
     * @throws InvalidTokenOffsetsException
     */
    public String previewText() throws ConvertorNotSupportedException, ParseException, IOException, InvalidTokenOffsetsException {
        return this.preview("", "", " \u2026 "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * na
     * 
     * @param query na
     * 
     * @return
     * 
     * @throws IOException na
     */
    private Query rewriteQuery(final Query query) throws IOException {
        // if fuzzy, wildcard etc searching: you need to rewrite the query using
        // this low-level function that needs a IndexReader
        // IndexReader via parent of SearchResult = ResultList
        // via parent of ResultList = LuceneHelper
        // via initialized lucent Directory of LuceneHelper
        // will close this IndexReader immediate after rewriting of query
        Query tmp = query;
        IndexReader reader = IndexReader.open(new NIOFSDirectory(this.parent.parent.getIndexLocation()));
        tmp = query.rewrite(reader);
        reader.close();

        return tmp;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("score", this.getScoreString()).append("url", this.getURL().toString()).append("lastModified", this.getLastModified()).append("description", this.getDescription()).append("keywords", this.getKeywords()).toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    }
}
