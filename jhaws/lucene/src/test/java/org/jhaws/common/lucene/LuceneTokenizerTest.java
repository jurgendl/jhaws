package org.jhaws.common.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class LuceneTokenizerTest {
	public static void main(String[] args) {
		try {
			try (Analyzer analyzer = new LuceneIndexAnalyzer(); TokenStream tokenStream = analyzer.tokenStream("fieldName", "Some stuff that is in need of analysis, we love words.")) {
				tokenStream.reset();
				CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
				while (tokenStream.incrementToken()) {
					System.out.println(token.toString());
				}
				tokenStream.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
