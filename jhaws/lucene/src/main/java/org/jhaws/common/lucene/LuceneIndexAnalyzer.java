package org.jhaws.common.lucene;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
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
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;

public class LuceneIndexAnalyzer extends Analyzer {
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
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
		Tokenizer source = new StandardTokenizer(reader);
		return new TokenStreamComponents(source, addFilters(source));
	}

	protected TokenStream addFilters(TokenStream tf) {
		tf = new StandardFilter(tf);
		tf = new LowerCaseFilter(tf);
		tf = new EnglishPossessiveFilter(getVersion(), tf);
		tf = new ClassicFilter(tf);
		tf = new ASCIIFoldingFilter(tf);
		tf = new StopFilter(tf, defaultStopSet);
		tf = new KStemFilter(tf); // PorterStemFilter/KStemFilter(;
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
