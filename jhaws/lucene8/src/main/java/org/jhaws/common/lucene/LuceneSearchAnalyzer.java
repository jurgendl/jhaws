package org.jhaws.common.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.FilteringTokenFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.miscellaneous.HyphenatedWordsFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class LuceneSearchAnalyzer extends Analyzer {
    protected int maxLength = Integer.MAX_VALUE;

    protected int minLength = 3;

    protected CharArraySet defaultStopSet = EnglishAnalyzer.getDefaultStopSet();

    public CharArraySet getDefaultStopSet() {
        return this.defaultStopSet;
    }

    public void setDefaultStopSet(CharArraySet defaultStopSet) {
        this.defaultStopSet = defaultStopSet;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName/* , Reader reader */) {
        Tokenizer source = new StandardTokenizer(/* reader */);
        return new TokenStreamComponents(source, addFilters(source));
    }

    protected TokenStream addFilters(TokenStream tf) {
        tf = new LowerCaseFilter(tf);
        tf = new EnglishPossessiveFilter(tf);
        tf = new ClassicFilter(tf);
        tf = new ASCIIFoldingFilter(tf);
        tf = new StopFilter(tf, defaultStopSet);
        tf = new KStemFilter(tf); // PorterStemFilter/KStemFilter(;
        tf = new HyphenatedWordsFilter(tf);
        tf = isAlphabetic(tf);
        tf = onlyAlphabetic(tf);
        tf = new LengthFilter(tf, minLength, maxLength);
        return tf;
    }

    public TokenFilter onlyAlphabetic(TokenStream stream) {
        return new TokenFilter(stream) {
            final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

            @Override
            public final boolean incrementToken() throws IOException {
                if (input.incrementToken()) {
                    char[] buffer = termAtt.buffer();
                    int p = 0;
                    for (int i = 0; i < termAtt.length(); i++) {
                        if (Character.isAlphabetic(buffer[i])) {
                            buffer[p++] = buffer[i];
                        }
                    }
                    for (int i = p; i < termAtt.length(); i++) {
                        buffer[i] = 0;
                    }
                    termAtt.setLength(p);
                    return true;
                }
                return false;
            }
        };
    }

    public FilteringTokenFilter isAlphabetic(TokenStream stream) {
        return new FilteringTokenFilter(stream) {
            final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

            @Override
            protected boolean accept() throws IOException {
                return termAtt.chars().parallel().filter(Character::isAlphabetic).findAny().isPresent();
            }
        };
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMinLength() {
        return this.minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }
}
