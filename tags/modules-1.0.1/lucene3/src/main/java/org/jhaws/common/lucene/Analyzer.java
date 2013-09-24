package org.jhaws.common.lucene;

import org.apache.lucene.analysis.LengthFilter;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.analysis.cz.CzechAnalyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.el.GreekAnalyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.nl.DutchAnalyzer;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.util.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * type og analyzer base on language
 *
 * @author Jurgen
 * @since 1.5
 * @version 1.0.0 - 22 June 2006
 */
public enum Analyzer {
/** cz */
    cz, 
/** br */
    br, 
/** zh */
    zh, 
/** nl */
    nl, 
/** fr */
    fr, 
/** de */
    de, 
/** el */
    el, 
/** ru */
    ru, 
/** en */
    en, 
/** da */
    da, 
/** fi */
    fi, 
/** it */
    it, 
/** nn */
    nn, 
/** pt */
    pt, 
/** es */
    es, 
/** sv */
    sv;
    /**
     * fetch Analyzer from locale
     *
     * @param locale Locale
     *
     * @return Analyzer
     */
    public static final Analyzer fetch(final Locale locale) {
        Analyzer tmp = valueOf(locale.getLanguage());

        return (tmp == null) ? en : tmp;
    }

    /**
     * na
     *
     * @return
     */
    public static final Analyzer fetch() {
        return fetch(Locale.getDefault());
    }

    /**
     * na
     *
     * @return
     */
    public String language() {
        return toString();
    }

    /**
     * na
     *
     * @return
     */
    public String languageName() {
        switch (this) {
            case en:
                return "English"; //$NON-NLS-1$

            case cz:
                return "Czech"; //$NON-NLS-1$

            case br:
                return "Brazilian"; //$NON-NLS-1$

            case zh:
                return "Chinese"; //$NON-NLS-1$

            case nl:
                return "Dutch"; //$NON-NLS-1$

            case fr:
                return "French"; //$NON-NLS-1$

            case de:
                return "German"; //$NON-NLS-1$

            case el:
                return "Greek"; //$NON-NLS-1$

            case ru:
                return "Russian"; //$NON-NLS-1$

            case da:
                return "Danish"; //$NON-NLS-1$

            case fi:
                return "Finnish"; //$NON-NLS-1$

            case it:
                return "Italian"; //$NON-NLS-1$

            case nn:
                return "Norwegian"; //$NON-NLS-1$

            case pt:
                return "Portuguese"; //$NON-NLS-1$

            case es:
                return "Spanish"; //$NON-NLS-1$

            case sv:
                return "Swedish"; //$NON-NLS-1$

            default:
                return "English"; //$NON-NLS-1$
        }
    }

    /** field */
    public static final int MIM_TERM_LEN = 3;

    /** field */
    public static final int MAX_TERM_LEN = 99;

    /**
     * English analyzer
     *
     * @author Jurgen
     */
    final    public static class English extends org.apache.lucene.analysis.Analyzer {
    	private SnowballAnalyzer sba = new SnowballAnalyzer(Version.LUCENE_CURRENT, "English");
/**
         * Creates a new EnglishAnalyzer object.
         */
        public English() {
            //StopAnalyzer.ENGLISH_STOP_WORDS_SET.toArray(new String[0])); //$NON-NLS-1$
        }

        /**
         * 
         * @see org.apache.lucene.analysis.snowball.SnowballAnalyzer#tokenStream(java.lang.String,
         *      java.io.Reader)
         */
        @Override
        public TokenStream tokenStream(String arg0, Reader arg1) {
            return new LengthFilter(sba.tokenStream(arg0, arg1), MIM_TERM_LEN, MAX_TERM_LEN);
        }
    }

    /**
     * Czech analyzer
     *
     * @author Jurgen
     */
    final   public static class Czech extends org.apache.lucene.analysis.Analyzer {
/**
         * Creates a new Czech object.
         */
        public Czech() {
            super();
        }

        /**
         * 
         * @see org.apache.lucene.analysis.Analyzer#tokenStream(java.lang.String,
         *      java.io.Reader)
         */
        @Override
        public TokenStream tokenStream(String arg0, Reader arg1) {
            return new LengthFilter(new CzechAnalyzer(Version.LUCENE_CURRENT).tokenStream(arg0, arg1), MIM_TERM_LEN, MAX_TERM_LEN);
        }
    }

    /**
     * Brazilian analyzer
     *
     * @author Jurgen
     */
    final   public static class Brazilian extends org.apache.lucene.analysis.Analyzer {
/**
         * Creates a new Brazilian object.
         */
        public Brazilian() {
            super();
        }

        /**
         * 
         * @see org.apache.lucene.analysis.Analyzer#tokenStream(java.lang.String,
         *      java.io.Reader)
         */
        @Override
        public TokenStream tokenStream(String arg0, Reader arg1) {
            return new LengthFilter(new BrazilianAnalyzer(Version.LUCENE_CURRENT).tokenStream(arg0, arg1), MIM_TERM_LEN, MAX_TERM_LEN);
        }
    }

    /**
     * Chinese analyzer
     *
     * @author Jurgen
     */
    final    public static class Chinese extends org.apache.lucene.analysis.Analyzer {
/**
         * Creates a new Chinese object.
         */
        public Chinese() {
            super();
        }

        /**
         * 
         * @see org.apache.lucene.analysis.Analyzer#tokenStream(java.lang.String,
         *      java.io.Reader)
         */
        @Override
        public TokenStream tokenStream(String arg0, Reader arg1) {
            return new LengthFilter(new ChineseAnalyzer().tokenStream(arg0, arg1), MIM_TERM_LEN, MAX_TERM_LEN);
        }
    }

    /**
     * Dutch analyzer
     *
     * @author Jurgen
     */
    final    public static class Dutch extends org.apache.lucene.analysis.Analyzer {
    	private SnowballAnalyzer sba = new SnowballAnalyzer(Version.LUCENE_CURRENT, "Dutch");
    	
/**
         * Creates a new Dutch object.
         */
        public Dutch() {
            // DutchAnalyzer.getDefaultStopSet().toArray(new String[0]));
        }

        /**
         * 
         * @see org.apache.lucene.analysis.snowball.SnowballAnalyzer#tokenStream(java.lang.String,
         *      java.io.Reader)
         */
        @Override
        public TokenStream tokenStream(String arg0, Reader arg1) {
            return new LengthFilter(sba.tokenStream(arg0, arg1), MIM_TERM_LEN, MAX_TERM_LEN);
        }
    }

    /**
     * French analyzer
     *
     * @author Jurgen
     */
    final    public static class French extends org.apache.lucene.analysis.Analyzer {
    	private SnowballAnalyzer sba = new SnowballAnalyzer(Version.LUCENE_CURRENT, "French");
/**
         * Creates a new French object.
         */
        public French() {
            //FrenchAnalyzer.getDefaultStopSet().toArray(new String[0])); //$NON-NLS-1$
        }

        /**
         * 
         * @see org.apache.lucene.analysis.snowball.SnowballAnalyzer#tokenStream(java.lang.String,
         *      java.io.Reader)
         */
        @Override
        public TokenStream tokenStream(String arg0, Reader arg1) {
            return new LengthFilter(sba.tokenStream(arg0, arg1), MIM_TERM_LEN, MAX_TERM_LEN);
        }
    }

    /**
     * german analyzer
     *
     * @author Jurgen
     */
    final   public static class German extends org.apache.lucene.analysis.Analyzer {
    	private SnowballAnalyzer sba = new SnowballAnalyzer(Version.LUCENE_CURRENT, "German");
/**
         * Creates a new German object.
         */
        public German() {
            // GermanAnalyzer.getDefaultStopSet().toArray(new String[0])); //$NON-NLS-1$
        }

        /**
         * 
         * @see org.apache.lucene.analysis.snowball.SnowballAnalyzer#tokenStream(java.lang.String,
         *      java.io.Reader)
         */
        @Override
        public TokenStream tokenStream(String arg0, Reader arg1) {
            return new LengthFilter(sba.tokenStream(arg0, arg1), MIM_TERM_LEN, MAX_TERM_LEN);
        }
    }

    /**
     * Greek analyzer
     *
     * @author Jurgen
     */
    final   public static class Greek extends org.apache.lucene.analysis.Analyzer {
/**
         * Creates a new Greek object.
         */
        public Greek() {
            super();
        }

        /**
         * 
         * @see org.apache.lucene.analysis.Analyzer#tokenStream(java.lang.String,
         *      java.io.Reader)
         */
        @Override
        public TokenStream tokenStream(String arg0, Reader arg1) {
            return new LengthFilter(new GreekAnalyzer(Version.LUCENE_CURRENT).tokenStream(arg0, arg1), MIM_TERM_LEN, MAX_TERM_LEN);
        }
    }

    /**
     * Russian analyzer
     *
     * @author Jurgen
     */
    final   public static class Russian extends org.apache.lucene.analysis.Analyzer {
    	private SnowballAnalyzer sba = new SnowballAnalyzer(Version.LUCENE_CURRENT, "Russian");
/**
         * Creates a new Russian object.
         */
        public Russian() {
        	//
                    }

        /**
         * 
         * @see org.apache.lucene.analysis.snowball.SnowballAnalyzer#tokenStream(java.lang.String,
         *      java.io.Reader)
         */
        @Override
        public TokenStream tokenStream(String arg0, Reader arg1) {
            return new LengthFilter(sba.tokenStream(arg0, arg1), MIM_TERM_LEN, MAX_TERM_LEN);
        }
    }

    /**
     * Danish analyzer
     *
     * @author Jurgen
     */
    final   public static class Danish extends org.apache.lucene.analysis.Analyzer {
    	private SnowballAnalyzer sba = new SnowballAnalyzer(Version.LUCENE_CURRENT, "Danish");
/**
         * Creates a new Danish object.
         */
        public Danish() {
            // getStopwords("DANISH")); //$NON-NLS-1$

        }

        /**
         * 
         * @see org.apache.lucene.analysis.snowball.SnowballAnalyzer#tokenStream(java.lang.String,
         *      java.io.Reader)
         */
        @Override
        public TokenStream tokenStream(String arg0, Reader arg1) {
            return new LengthFilter(sba.tokenStream(arg0, arg1), MIM_TERM_LEN, MAX_TERM_LEN);
        }
    }

    /**
     * Finnish analyzer
     *
     * @author Jurgen
     */
    final    public static class Finnish extends org.apache.lucene.analysis.Analyzer {
    	private SnowballAnalyzer sba = new SnowballAnalyzer(Version.LUCENE_CURRENT, "Finnish");
/**
         * Creates a new Finnish object.
         */
        public Finnish() {
            //
        }

        /**
         * 
         * @see org.apache.lucene.analysis.snowball.SnowballAnalyzer#tokenStream(java.lang.String,
         *      java.io.Reader)
         */
        @Override
        public TokenStream tokenStream(String arg0, Reader arg1) {
            return new LengthFilter(sba.tokenStream(arg0, arg1), MIM_TERM_LEN, MAX_TERM_LEN);
        }
    }

    /**
     * Italian analyzer
     *
     * @author Jurgen
     */
    final    public static class Italian extends org.apache.lucene.analysis.Analyzer {
    	private SnowballAnalyzer sba = new SnowballAnalyzer(Version.LUCENE_CURRENT, "Italian");
/**
         * Creates a new Italian object.
         */
        public Italian() {
            // getStopwords("ITALIAN")); //$NON-NLS-1$
        }

        /**
         * 
         * @see org.apache.lucene.analysis.snowball.SnowballAnalyzer#tokenStream(java.lang.String,
         *      java.io.Reader)
         */
        @Override
        public TokenStream tokenStream(String arg0, Reader arg1) {
            return new LengthFilter(sba.tokenStream(arg0, arg1), MIM_TERM_LEN, MAX_TERM_LEN);
        }
    }

    /**
     * Norwegian analyzer
     *
     * @author Jurgen
     */
    final   public static class Norwegian extends org.apache.lucene.analysis.Analyzer {
    	private SnowballAnalyzer sba = new SnowballAnalyzer(Version.LUCENE_CURRENT, "Norwegian");
    	
/**
         * Creates a new Norwegian object.
         */
        public Norwegian() {
            // getStopwords("NORWEGIAN")); //$NON-NLS-1$
        }

        /**
         * 
         * @see org.apache.lucene.analysis.snowball.SnowballAnalyzer#tokenStream(java.lang.String,
         *      java.io.Reader)
         */
        @Override
        public TokenStream tokenStream(String arg0, Reader arg1) {
            return new LengthFilter(sba.tokenStream(arg0, arg1), MIM_TERM_LEN, MAX_TERM_LEN);
        }
    }

    /**
     * Portuguese analayzer
     *
     * @author Jurgen
     */
    final    public static class Portuguese extends org.apache.lucene.analysis.Analyzer {
    	private SnowballAnalyzer sba = new SnowballAnalyzer(Version.LUCENE_CURRENT, "Portuguese");
/**
         * Creates a new Portuguese object.
         */
        public Portuguese() {
            // getStopwords("PORTUGUESE")); //$NON-NLS-1$

        }

        /**
         * 
         * @see org.apache.lucene.analysis.snowball.SnowballAnalyzer#tokenStream(java.lang.String,
         *      java.io.Reader)
         */
        @Override
        public TokenStream tokenStream(String arg0, Reader arg1) {
            return new LengthFilter(sba.tokenStream(arg0, arg1), MIM_TERM_LEN, MAX_TERM_LEN);
        }
    }

    /**
     * Spanish analyzer
     *
     * @author Jurgen
     */
    final   public static class Spanish extends org.apache.lucene.analysis.Analyzer {
    	private SnowballAnalyzer sba = new SnowballAnalyzer(Version.LUCENE_CURRENT, "Spanish");
    	
/**
         * Creates a new Spanish object.
         */
        public Spanish() {
// getStopwords("SPANISH")); //$NON-NLS-1$
        }

        /**
         * 
         * @see org.apache.lucene.analysis.snowball.SnowballAnalyzer#tokenStream(java.lang.String,
         *      java.io.Reader)
         */
        @Override
        public TokenStream tokenStream(String arg0, Reader arg1) {
            return new LengthFilter(sba.tokenStream(arg0, arg1), MIM_TERM_LEN, MAX_TERM_LEN);
        }
    }

    /**
     * Swedish analyzer
     *
     * @author Jurgen
     */
  final  public static class Swedish extends org.apache.lucene.analysis.Analyzer {
    	private SnowballAnalyzer sba = new SnowballAnalyzer(Version.LUCENE_CURRENT, "Swedish");
    	
/**
         * Creates a new Swedish object.
         */
        public Swedish() {
            //
        }

        /**
         * 
         * @see org.apache.lucene.analysis.snowball.SnowballAnalyzer#tokenStream(java.lang.String,
         *      java.io.Reader)
         */
        @Override
        public TokenStream tokenStream(String arg0, Reader arg1) {
            return new LengthFilter(sba.tokenStream(arg0, arg1), MIM_TERM_LEN, MAX_TERM_LEN);
        }
    }

    /**
     * gets apropriate Lucene Analyzer
     *
     * @return Lucene Analyzer
     */
    public org.apache.lucene.analysis.Analyzer analyzer() {
        // missing stopwords taken from http://www.ranks.nl/stopwords/
        switch (this) {
            case en:
                return new English();

            case cz:
                return new Czech();

            case br:
                return new Brazilian();

            case zh:
                return new Chinese();

            case nl:
                return new Dutch();

            case fr:
                return new French();

            case de:
                return new German();

            case el:
                return new Greek();

            case ru:
                return new Russian();

            case da:
                return new Danish();

            case fi:
                return new Finnish();

            case it:
                return new Italian();

            case nn:
                return new Norwegian();

            case pt:
                return new Portuguese();

            case es:
                return new Spanish();

            case sv:
                return new Swedish();

            default:
                return new English();
        }
    }

    /**
     * na
     *
     * @param string
     *
     * @return
     */
    private static String[] getStopwords(String string) {
        try {
            InputStream in = Analyzer.class.getClassLoader().getResourceAsStream(string + "_STOPWORDS"); //$NON-NLS-1$ 
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            ArrayList<String> list = new ArrayList<String>();
            String line;

            while ((line = reader.readLine()) != null) {
                list.add(line.trim());
            }

            in.close();

            return list.toArray(new String[0]);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }

        return new String[0];
    }
}
