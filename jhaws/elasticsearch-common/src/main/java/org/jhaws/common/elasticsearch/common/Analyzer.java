package org.jhaws.common.elasticsearch.common;

import java.util.Arrays;
import java.util.Optional;

/**
 * maak er zelf een
 * 
 * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-custom-analyzer.html
 */
public enum Analyzer {
	uninitialized(null),
	//
	//
	/**
	 * lowercase, verwijdert leestekens, laat cijfers staan, laat accenten staan<br>
	 * <br>
	 * "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone."<br>
	 * [ the, 2, quick, brown, foxes, jumped, over, the, lazy, dog's, bone ] <br>
	 * max_token_length <br>
	 * stopwords (array, default _none_, bv _english_) <br>
	 * stopwords_path <br>
	 * 
	 * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-standard-analyzer.html
	 */
	standard("standard"),
	//
	//
	/**
	 * lowercase, verwijdert leestekens, verwijdert cijfers, laat accenten staan<br>
	 * <br>
	 * "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone."<br>
	 * "[ the, quick, brown, foxes, jumped, over, the, lazy, dog, s, bone ] <br>
	 * 
	 * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-simple-analyzer.html
	 */
	simple("simple"),
	//
	//
	/**
	 * laat case ongemoeid, laat leestekens staan, laat cijfers staan, laat accenten staan<br>
	 * <br>
	 * "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone."<br>
	 * [ The, 2, QUICK, Brown-Foxes, jumped, over, the, lazy, dog's, bone. ] <br>
	 * 
	 * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-whitespace-analyzer.html
	 */
	whitespace("whitespace"),
	//
	//
	/**
	 * lowercase, verwijdert leestekens, verwijdert cijfers, laat accenten staan<br>
	 * <br>
	 * "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone."<br>
	 * [ quick, brown, foxes, jumped, over, lazy, dog, s, bone ] <br>
	 * stopwords (array, default _english_) <br>
	 * stopwords_path <br>
	 * 
	 * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-stop-analyzer.html
	 */
	stop("stop"),
	//
	//
	/**
	 * laat alles ongemoeid en splits niet op, laat accenten staan<br>
	 * <br>
	 * "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone.""<br>
	 * [ The 2 QUICK Brown-Foxes jumped over the lazy dog's bone. ] <br>
	 * 
	 * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-keyword-analyzer.html
	 */
	keyword("keyword"),
	//
	//
	/**
	 * lowercase, behoudt cijfers, verwijdert letters met accenten, verwijdert leestekens<br>
	 * <br>
	 * "The 2 QUICK Brown-Foxes jumped over the lazy dog's bone.""<br>
	 * [ the, 2, quick, brown, foxes, jumped, over, the, lazy, dog, s, bone ] <br>
	 * pattern (regular expression, default \W+.) <br>
	 * flags (bv CASE_INSENSITIVE|COMMENTS) <br>
	 * lowercase (default true) <br>
	 * stopwords (array, default _none_, bv _english_) <br>
	 * stopwords_path <br>
	 * 
	 * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-pattern-analyzer.html
	 */
	pattern("pattern"),
	//
	//
	/**
	 * vereist be.ugent.gismo.elasticsearch.model.Language language<br>
	 * op be.ugent.gismo.elasticsearch.model.Field<br>
	 * of op be.ugent.gismo.elasticsearch.model.NestedField<br>
	 *
	 * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html#hungarian-analyzer
	 */
	language("language"),
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "arabic_stop": {
	      "type":       "stop",
	      "stopwords":  "_arabic_" 
	    },
	    "arabic_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["مثال"] 
	    },
	    "arabic_stemmer": {
	      "type":       "stemmer",
	      "language":   "arabic"
	    }
	  },
	  "analyzer": {
	    "rebuilt_arabic": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "decimal_digit",
	        "arabic_stop",
	        "arabic_normalization",
	        "arabic_keywords",
	        "arabic_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_arabic("arabic"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "armenian_stop": {
	      "type":       "stop",
	      "stopwords":  "_armenian_" 
	    },
	    "armenian_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["օրինակ"] 
	    },
	    "armenian_stemmer": {
	      "type":       "stemmer",
	      "language":   "armenian"
	    }
	  },
	  "analyzer": {
	    "rebuilt_armenian": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "armenian_stop",
	        "armenian_keywords",
	        "armenian_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_armenian("armenian"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "basque_stop": {
	      "type":       "stop",
	      "stopwords":  "_basque_" 
	    },
	    "basque_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["Adibidez"] 
	    },
	    "basque_stemmer": {
	      "type":       "stemmer",
	      "language":   "basque"
	    }
	  },
	  "analyzer": {
	    "rebuilt_basque": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "basque_stop",
	        "basque_keywords",
	        "basque_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_basque("basque"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "bengali_stop": {
	      "type":       "stop",
	      "stopwords":  "_bengali_" 
	    },
	    "bengali_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["উদাহরণ"] 
	    },
	    "bengali_stemmer": {
	      "type":       "stemmer",
	      "language":   "bengali"
	    }
	  },
	  "analyzer": {
	    "rebuilt_bengali": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "decimal_digit",
	        "bengali_keywords",
	        "indic_normalization",
	        "bengali_normalization",
	        "bengali_stop",
	        "bengali_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_bengali("bengali"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "brazilian_stop": {
	      "type":       "stop",
	      "stopwords":  "_brazilian_" 
	    },
	    "brazilian_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["exemplo"] 
	    },
	    "brazilian_stemmer": {
	      "type":       "stemmer",
	      "language":   "brazilian"
	    }
	  },
	  "analyzer": {
	    "rebuilt_brazilian": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "brazilian_stop",
	        "brazilian_keywords",
	        "brazilian_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_brazilian("brazilian"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "bulgarian_stop": {
	      "type":       "stop",
	      "stopwords":  "_bulgarian_" 
	    },
	    "bulgarian_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["пример"] 
	    },
	    "bulgarian_stemmer": {
	      "type":       "stemmer",
	      "language":   "bulgarian"
	    }
	  },
	  "analyzer": {
	    "rebuilt_bulgarian": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "bulgarian_stop",
	        "bulgarian_keywords",
	        "bulgarian_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_bulgarian("bulgarian"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "catalan_elision": {
	      "type":       "elision",
	      "articles":   [ "d", "l", "m", "n", "s", "t"],
	      "articles_case": true
	    },
	    "catalan_stop": {
	      "type":       "stop",
	      "stopwords":  "_catalan_" 
	    },
	    "catalan_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["example"] 
	    },
	    "catalan_stemmer": {
	      "type":       "stemmer",
	      "language":   "catalan"
	    }
	  },
	  "analyzer": {
	    "rebuilt_catalan": {
	      "tokenizer":  "standard",
	      "filter": [
	        "catalan_elision",
	        "lowercase",
	        "catalan_stop",
	        "catalan_keywords",
	        "catalan_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_catalan("catalan"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "english_stop": {
	      "type":       "stop",
	      "stopwords":  [ 
	        "a", "and", "are", "as", "at", "be", "but", "by", "for",
	        "if", "in", "into", "is", "it", "no", "not", "of", "on",
	        "or", "s", "such", "t", "that", "the", "their", "then",
	        "there", "these", "they", "this", "to", "was", "will",
	        "with", "www"
	      ]
	    }
	  },
	  "analyzer": {
	    "rebuilt_cjk": {
	      "tokenizer":  "standard",
	      "filter": [
	        "cjk_width",
	        "lowercase",
	        "cjk_bigram",
	        "english_stop"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_cjk("cjk"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "czech_stop": {
	      "type":       "stop",
	      "stopwords":  "_czech_" 
	    },
	    "czech_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["příklad"] 
	    },
	    "czech_stemmer": {
	      "type":       "stemmer",
	      "language":   "czech"
	    }
	  },
	  "analyzer": {
	    "rebuilt_czech": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "czech_stop",
	        "czech_keywords",
	        "czech_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_czech("czech"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "danish_stop": {
	      "type":       "stop",
	      "stopwords":  "_danish_" 
	    },
	    "danish_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["eksempel"] 
	    },
	    "danish_stemmer": {
	      "type":       "stemmer",
	      "language":   "danish"
	    }
	  },
	  "analyzer": {
	    "rebuilt_danish": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "danish_stop",
	        "danish_keywords",
	        "danish_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_danish("danish"), //
	/**
	 * stopwords (array, default _none_, bv _english_) <br>
	 * stopwords_path <br>
	 * stem_exclusion: arabic, armenian, basque, bengali, bulgarian, catalan, czech, dutch, english, finnish, french, galician, german, hindi, hungarian, indonesian, irish,
	 * italian, latvian, lithuanian, norwegian, portuguese, romanian, russian, sorani, spanish, swedish, turkish <br>
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "dutch_stop": {
	      "type":       "stop",
	      "stopwords":  "_dutch_" 
	    },
	    "dutch_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["voorbeeld"] 
	    },
	    "dutch_stemmer": {
	      "type":       "stemmer",
	      "language":   "dutch"
	    },
	    "dutch_override": {
	      "type":       "stemmer_override",
	      "rules": [
	        "fiets=>fiets",
	        "bromfiets=>bromfiets",
	        "ei=>eier",
	        "kind=>kinder"
	      ]
	    }
	  },
	  "analyzer": {
	    "rebuilt_dutch": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "dutch_stop",
	        "dutch_keywords",
	        "dutch_override",
	        "dutch_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 * 
	 * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html
	 */
	language_dutch("dutch"), //
	/**
	 * stopwords (array, default _none_, bv _english_) <br>
	 * stopwords_path <br>
	 * stem_exclusion: arabic, armenian, basque, bengali, bulgarian, catalan, czech, dutch, english, finnish, french, galician, german, hindi, hungarian, indonesian, irish,
	 * italian, latvian, lithuanian, norwegian, portuguese, romanian, russian, sorani, spanish, swedish, turkish <br>
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "english_stop": {
	      "type":       "stop",
	      "stopwords":  "_english_" 
	    },
	    "english_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["example"] 
	    },
	    "english_stemmer": {
	      "type":       "stemmer",
	      "language":   "english"
	    },
	    "english_possessive_stemmer": {
	      "type":       "stemmer",
	      "language":   "possessive_english"
	    }
	  },
	  "analyzer": {
	    "rebuilt_english": {
	      "tokenizer":  "standard",
	      "filter": [
	        "english_possessive_stemmer",
	        "lowercase",
	        "english_stop",
	        "english_keywords",
	        "english_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 * 
	 * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lang-analyzer.html
	 */
	language_english("english"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "estonian_stop": {
	      "type":       "stop",
	      "stopwords":  "_estonian_" 
	    },
	    "estonian_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["näide"] 
	    },
	    "estonian_stemmer": {
	      "type":       "stemmer",
	      "language":   "estonian"
	    }
	  },
	  "analyzer": {
	    "rebuilt_estonian": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "estonian_stop",
	        "estonian_keywords",
	        "estonian_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_estonian("estonian"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "finnish_stop": {
	      "type":       "stop",
	      "stopwords":  "_finnish_" 
	    },
	    "finnish_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["esimerkki"] 
	    },
	    "finnish_stemmer": {
	      "type":       "stemmer",
	      "language":   "finnish"
	    }
	  },
	  "analyzer": {
	    "rebuilt_finnish": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "finnish_stop",
	        "finnish_keywords",
	        "finnish_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_finnish("finnish"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "french_elision": {
	      "type":         "elision",
	      "articles_case": true,
	      "articles": [
	          "l", "m", "t", "qu", "n", "s",
	          "j", "d", "c", "jusqu", "quoiqu",
	          "lorsqu", "puisqu"
	        ]
	    },
	    "french_stop": {
	      "type":       "stop",
	      "stopwords":  "_french_" 
	    },
	    "french_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["Example"] 
	    },
	    "french_stemmer": {
	      "type":       "stemmer",
	      "language":   "light_french"
	    }
	  },
	  "analyzer": {
	    "rebuilt_french": {
	      "tokenizer":  "standard",
	      "filter": [
	        "french_elision",
	        "lowercase",
	        "french_stop",
	        "french_keywords",
	        "french_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_french("french"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "galician_stop": {
	      "type":       "stop",
	      "stopwords":  "_galician_" 
	    },
	    "galician_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["exemplo"] 
	    },
	    "galician_stemmer": {
	      "type":       "stemmer",
	      "language":   "galician"
	    }
	  },
	  "analyzer": {
	    "rebuilt_galician": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "galician_stop",
	        "galician_keywords",
	        "galician_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_galician("galician"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "german_stop": {
	      "type":       "stop",
	      "stopwords":  "_german_" 
	    },
	    "german_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["Beispiel"] 
	    },
	    "german_stemmer": {
	      "type":       "stemmer",
	      "language":   "light_german"
	    }
	  },
	  "analyzer": {
	    "rebuilt_german": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "german_stop",
	        "german_keywords",
	        "german_normalization",
	        "german_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_german("german"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "greek_stop": {
	      "type":       "stop",
	      "stopwords":  "_greek_" 
	    },
	    "greek_lowercase": {
	      "type":       "lowercase",
	      "language":   "greek"
	    },
	    "greek_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["παράδειγμα"] 
	    },
	    "greek_stemmer": {
	      "type":       "stemmer",
	      "language":   "greek"
	    }
	  },
	  "analyzer": {
	    "rebuilt_greek": {
	      "tokenizer":  "standard",
	      "filter": [
	        "greek_lowercase",
	        "greek_stop",
	        "greek_keywords",
	        "greek_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_greek("greek"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "hindi_stop": {
	      "type":       "stop",
	      "stopwords":  "_hindi_" 
	    },
	    "hindi_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["उदाहरण"] 
	    },
	    "hindi_stemmer": {
	      "type":       "stemmer",
	      "language":   "hindi"
	    }
	  },
	  "analyzer": {
	    "rebuilt_hindi": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "decimal_digit",
	        "hindi_keywords",
	        "indic_normalization",
	        "hindi_normalization",
	        "hindi_stop",
	        "hindi_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_hindi("hindi"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "hungarian_stop": {
	      "type":       "stop",
	      "stopwords":  "_hungarian_" 
	    },
	    "hungarian_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["példa"] 
	    },
	    "hungarian_stemmer": {
	      "type":       "stemmer",
	      "language":   "hungarian"
	    }
	  },
	  "analyzer": {
	    "rebuilt_hungarian": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "hungarian_stop",
	        "hungarian_keywords",
	        "hungarian_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_hungarian("hungarian"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "indonesian_stop": {
	      "type":       "stop",
	      "stopwords":  "_indonesian_" 
	    },
	    "indonesian_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["contoh"] 
	    },
	    "indonesian_stemmer": {
	      "type":       "stemmer",
	      "language":   "indonesian"
	    }
	  },
	  "analyzer": {
	    "rebuilt_indonesian": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "indonesian_stop",
	        "indonesian_keywords",
	        "indonesian_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_indonesian("indonesian"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "irish_hyphenation": {
	      "type":       "stop",
	      "stopwords":  [ "h", "n", "t" ],
	      "ignore_case": true
	    },
	    "irish_elision": {
	      "type":       "elision",
	      "articles":   [ "d", "m", "b" ],
	      "articles_case": true
	    },
	    "irish_stop": {
	      "type":       "stop",
	      "stopwords":  "_irish_" 
	    },
	    "irish_lowercase": {
	      "type":       "lowercase",
	      "language":   "irish"
	    },
	    "irish_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["sampla"] 
	    },
	    "irish_stemmer": {
	      "type":       "stemmer",
	      "language":   "irish"
	    }
	  },
	  "analyzer": {
	    "rebuilt_irish": {
	      "tokenizer":  "standard",
	      "filter": [
	        "irish_hyphenation",
	        "irish_elision",
	        "irish_lowercase",
	        "irish_stop",
	        "irish_keywords",
	        "irish_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_irish("irish"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "italian_elision": {
	      "type": "elision",
	      "articles": [
	            "c", "l", "all", "dall", "dell",
	            "nell", "sull", "coll", "pell",
	            "gl", "agl", "dagl", "degl", "negl",
	            "sugl", "un", "m", "t", "s", "v", "d"
	      ],
	      "articles_case": true
	    },
	    "italian_stop": {
	      "type":       "stop",
	      "stopwords":  "_italian_" 
	    },
	    "italian_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["esempio"] 
	    },
	    "italian_stemmer": {
	      "type":       "stemmer",
	      "language":   "light_italian"
	    }
	  },
	  "analyzer": {
	    "rebuilt_italian": {
	      "tokenizer":  "standard",
	      "filter": [
	        "italian_elision",
	        "lowercase",
	        "italian_stop",
	        "italian_keywords",
	        "italian_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_italian("italian"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "latvian_stop": {
	      "type":       "stop",
	      "stopwords":  "_latvian_" 
	    },
	    "latvian_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["piemērs"] 
	    },
	    "latvian_stemmer": {
	      "type":       "stemmer",
	      "language":   "latvian"
	    }
	  },
	  "analyzer": {
	    "rebuilt_latvian": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "latvian_stop",
	        "latvian_keywords",
	        "latvian_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_latvian("latvian"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "lithuanian_stop": {
	      "type":       "stop",
	      "stopwords":  "_lithuanian_" 
	    },
	    "lithuanian_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["pavyzdys"] 
	    },
	    "lithuanian_stemmer": {
	      "type":       "stemmer",
	      "language":   "lithuanian"
	    }
	  },
	  "analyzer": {
	    "rebuilt_lithuanian": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "lithuanian_stop",
	        "lithuanian_keywords",
	        "lithuanian_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_lithuanian("lithuanian"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "norwegian_stop": {
	      "type":       "stop",
	      "stopwords":  "_norwegian_" 
	    },
	    "norwegian_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["eksempel"] 
	    },
	    "norwegian_stemmer": {
	      "type":       "stemmer",
	      "language":   "norwegian"
	    }
	  },
	  "analyzer": {
	    "rebuilt_norwegian": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "norwegian_stop",
	        "norwegian_keywords",
	        "norwegian_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_norwegian("norwegian"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "char_filter": {
	    "zero_width_spaces": {
	        "type":       "mapping",
	        "mappings": [ "\\u200C=>\\u0020"] 
	    }
	  },
	  "filter": {
	    "persian_stop": {
	      "type":       "stop",
	      "stopwords":  "_persian_" 
	    }
	  },
	  "analyzer": {
	    "rebuilt_persian": {
	      "tokenizer":     "standard",
	      "char_filter": [ "zero_width_spaces" ],
	      "filter": [
	        "lowercase",
	        "decimal_digit",
	        "arabic_normalization",
	        "persian_normalization",
	        "persian_stop"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_persian("persian"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "portuguese_stop": {
	      "type":       "stop",
	      "stopwords":  "_portuguese_" 
	    },
	    "portuguese_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["exemplo"] 
	    },
	    "portuguese_stemmer": {
	      "type":       "stemmer",
	      "language":   "light_portuguese"
	    }
	  },
	  "analyzer": {
	    "rebuilt_portuguese": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "portuguese_stop",
	        "portuguese_keywords",
	        "portuguese_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_portuguese("portuguese"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "romanian_stop": {
	      "type":       "stop",
	      "stopwords":  "_romanian_" 
	    },
	    "romanian_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["exemplu"] 
	    },
	    "romanian_stemmer": {
	      "type":       "stemmer",
	      "language":   "romanian"
	    }
	  },
	  "analyzer": {
	    "rebuilt_romanian": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "romanian_stop",
	        "romanian_keywords",
	        "romanian_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_romanian("romanian"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "russian_stop": {
	      "type":       "stop",
	      "stopwords":  "_russian_" 
	    },
	    "russian_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["пример"] 
	    },
	    "russian_stemmer": {
	      "type":       "stemmer",
	      "language":   "russian"
	    }
	  },
	  "analyzer": {
	    "rebuilt_russian": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "russian_stop",
	        "russian_keywords",
	        "russian_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_russian("russian"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "sorani_stop": {
	      "type":       "stop",
	      "stopwords":  "_sorani_" 
	    },
	    "sorani_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["mînak"] 
	    },
	    "sorani_stemmer": {
	      "type":       "stemmer",
	      "language":   "sorani"
	    }
	  },
	  "analyzer": {
	    "rebuilt_sorani": {
	      "tokenizer":  "standard",
	      "filter": [
	        "sorani_normalization",
	        "lowercase",
	        "decimal_digit",
	        "sorani_stop",
	        "sorani_keywords",
	        "sorani_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_sorani("sorani"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "spanish_stop": {
	      "type":       "stop",
	      "stopwords":  "_spanish_" 
	    },
	    "spanish_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["ejemplo"] 
	    },
	    "spanish_stemmer": {
	      "type":       "stemmer",
	      "language":   "light_spanish"
	    }
	  },
	  "analyzer": {
	    "rebuilt_spanish": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "spanish_stop",
	        "spanish_keywords",
	        "spanish_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_spanish("spanish"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "swedish_stop": {
	      "type":       "stop",
	      "stopwords":  "_swedish_" 
	    },
	    "swedish_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["exempel"] 
	    },
	    "swedish_stemmer": {
	      "type":       "stemmer",
	      "language":   "swedish"
	    }
	  },
	  "analyzer": {
	    "rebuilt_swedish": {
	      "tokenizer":  "standard",
	      "filter": [
	        "lowercase",
	        "swedish_stop",
	        "swedish_keywords",
	        "swedish_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_swedish("swedish"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "turkish_stop": {
	      "type":       "stop",
	      "stopwords":  "_turkish_" 
	    },
	    "turkish_lowercase": {
	      "type":       "lowercase",
	      "language":   "turkish"
	    },
	    "turkish_keywords": {
	      "type":       "keyword_marker",
	      "keywords":   ["örnek"] 
	    },
	    "turkish_stemmer": {
	      "type":       "stemmer",
	      "language":   "turkish"
	    }
	  },
	  "analyzer": {
	    "rebuilt_turkish": {
	      "tokenizer":  "standard",
	      "filter": [
	        "apostrophe",
	        "turkish_lowercase",
	        "turkish_stop",
	        "turkish_keywords",
	        "turkish_stemmer"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_turkish("turkish"), //
	/**
	 * <pre>
	{
	"settings": {
	"analysis": {
	  "filter": {
	    "thai_stop": {
	      "type":       "stop",
	      "stopwords":  "_thai_" 
	    }
	  },
	  "analyzer": {
	    "rebuilt_thai": {
	      "tokenizer":  "thai",
	      "filter": [
	        "lowercase",
	        "decimal_digit",
	        "thai_stop"
	      ]
	    }
	  }
	}
	}
	}
	 * </pre>
	 */
	language_thai("thai"), //
	//
	//
	/**
	 * lowercase, verwijdert leestekens, laat cijfers staan, verwijdert accenten, alfabetisch, verwijdert dubbels<br>
	 * <br>
	 * "Yes yes, Gödel said this sentence is consistent and."<br>
	 * [ and consistent godel is said sentence this yes ]<br>
	 * separator (default space ' ')<br>
	 * max_output_size (default 255)<br>
	 * stopwords (array, default _none_, bv _english_) <br>
	 * stopwords_path <br>
	 * 
	 * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-fingerprint-analyzer.html
	 */
	fingerprint("fingerprint"),
	//
	//
	// https://www.elastic.co/guide/en/elasticsearch/plugins/current/analysis-kuromoji.html
	// bin/elasticsearch-plugin install analysis-kuromoji
	// https://www.elastic.co/guide/en/elasticsearch/plugins/current/analysis-kuromoji-analyzer.html
	language_japanese("kuromoji"), //
	//
	// https://www.elastic.co/guide/en/elasticsearch/plugins/current/analysis-smartcn.html
	// bin/elasticsearch-plugin install analysis-smartcn
	language_chinese("smartcn"), //
	//
	// https://www.elastic.co/guide/en/elasticsearch/plugins/current/analysis-nori.html
	// bin/elasticsearch-plugin install analysis-nori
	// https://www.elastic.co/guide/en/elasticsearch/plugins/current/analysis-nori-analyzer.html
	language_korean("nori"), //
	//
	// https://www.elastic.co/guide/en/elasticsearch/plugins/current/analysis-ukrainian.html
	// bin/elasticsearch-plugin install analysis-ukrainian
	language_ukrainian("ukrainian"), //
	//
	// https://www.elastic.co/guide/en/elasticsearch/plugins/current/analysis-phonetic.html
	// https://www.elastic.co/guide/en/elasticsearch/plugins/current/analysis-phonetic-token-filter.html
	// bin/elasticsearch-plugin install analysis-stempel
	language_polish("polish"),//
	//
	//
	;

	private String id;

	private Analyzer(String id) {
		this.id = id;
	}

	public String id(Language lang) {
		return Analyzer.language != this ? id : forLanguage(lang).map(analyzer -> analyzer.id).orElse(null);
	}

	public static Optional<Analyzer> forLanguage(Language lang) {
		return Arrays.stream(Analyzer.values()).filter(analyzer -> analyzer.name().equals(Analyzer.language.name() + "_" + lang.name())).findAny();
	}

	// public static void main(String[] args) {
	// System.out.println(Analyzer.language.id(Language.forIso("zh").get()));
	// }
}
