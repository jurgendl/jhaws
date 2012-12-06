package org.jhaws.common.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.*;

import java.io.IOException;
import java.io.StringReader;

import java.util.*;


/**
 * general utilities for a Lucene Analyzer
 *
 * @author Jurgen De Landsheer
 * @version 1.0.0 - 22 June 2006
 *
 * @since 1.5
 */
public class AnalyzerUtils {
    /**
     * na
     *
     * @param analyzer na
     * @param text na
     *
     * @return
     *
     * @throws IOException na
     */
    public static Token[] tokensFromAnalysis(final Analyzer analyzer, final String text) throws IOException {
        TokenStream stream = analyzer.tokenStream("contents", new StringReader(text)); //$NON-NLS-1$
        ArrayList<Token> tokenList = new ArrayList<Token>();

        while (stream.incrementToken()) {
            Iterator<AttributeImpl> attributeImplsIterator = stream.getAttributeImplsIterator();
            Token token = new Token();

            while (attributeImplsIterator.hasNext()) {
                attributeImplsIterator.next().copyTo(token);
            }

            System.out.println(token);
        }

        return tokenList.toArray(new Token[0]);
    }

    /**
     * na
     *
     * @param analyzer na
     * @param text na
     *
     * @throws IOException na
     */
    public static void displayTokens(final Analyzer analyzer, final String text) throws IOException {
        Token[] tokens = tokensFromAnalysis(analyzer, text);

        for (int i = 0; i < tokens.length; i++) {
            Token token = tokens[i];

            System.out.print("[" + token.term() + "] "); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * na
     *
     * @param analyzer na
     * @param text na
     *
     * @throws IOException na
     */
    public static void displayTokensWithPositions(final Analyzer analyzer, final String text) throws IOException {
        Token[] tokens = tokensFromAnalysis(analyzer, text);

        int position = 0;

        for (int i = 0; i < tokens.length; i++) {
            Token token = tokens[i];

            int increment = token.getPositionIncrement();

            if (increment > 0) {
                position = position + increment;
                System.out.println();
                System.out.print(position + ": "); //$NON-NLS-1$
            }

            System.out.print("[" + token.term() + "] "); //$NON-NLS-1$ //$NON-NLS-2$
        }

        System.out.println();
    }

    /**
     * na
     *
     * @param analyzer na
     * @param text na
     *
     * @throws IOException na
     */
    public static void displayTokensWithFullDetails(final Analyzer analyzer, final String text) throws IOException {
        Token[] tokens = tokensFromAnalysis(analyzer, text);

        int position = 0;

        for (int i = 0; i < tokens.length; i++) {
            Token token = tokens[i];

            int increment = token.getPositionIncrement();

            if (increment > 0) {
                position = position + increment;
                System.out.println();
                System.out.print(position + ": "); //$NON-NLS-1$
            }

            System.out.print("[" + token.term() + ":" + token.startOffset() + "->" + token.endOffset() + ":" + token.type() + "] "); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        }

        System.out.println();
    }
}
