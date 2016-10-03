package org.jhaws.common.lucene;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.miscellaneous.HyphenatedWordsFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;

public class LuceneIndexAnalyzer extends Analyzer {
	protected int maxLength = Integer.MAX_VALUE;

	protected int minLength = 3;

	protected CharArraySet defaultStopSet = EnglishAnalyzer.getDefaultStopSet();

	protected TokenStream createStemFilter(TokenStream tf) {
		// return new KStemFilter(tf);
		return new PorterStemFilter(tf);
	}

	protected TokenStream createStopFilter(TokenStream tf) {
		return new StopFilter(tf, defaultStopSet);
	}

	protected TokenStream createCleanupFilter(TokenStream tf) {
		tf = new EnglishPossessiveFilter(getVersion(), tf);// remove end 's
		// tf = new ElisionFilter(tf, new CharArraySet(ElisionFilterFactory.availableTokenFilters(), true)); // remove l' (french)
		return tf;
	}

	public CharArraySet getDefaultStopSet() {
		return this.defaultStopSet;
	}

	public void setDefaultStopSet(CharArraySet defaultStopSet) {
		this.defaultStopSet = defaultStopSet;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		Tokenizer source = new StandardTokenizer(reader);
		return new TokenStreamComponents(source, addFilters(source));
	}

	@SuppressWarnings("resource")
	protected TokenStream addFilters(TokenStream tf) {
		tf = new LowerCaseFilter(tf);
		tf = createCleanupFilter(tf);
		tf = new ClassicFilter(tf);
		tf = new ASCIIFoldingFilter(tf);
		tf = createStopFilter(tf);
		tf = createStemFilter(tf);
		tf = new HyphenatedWordsFilter(tf);
		tf = new LengthFilter(tf, minLength, maxLength);
		return tf;
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
