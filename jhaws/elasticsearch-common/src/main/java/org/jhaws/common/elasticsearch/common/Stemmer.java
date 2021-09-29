package org.jhaws.common.elasticsearch.common;

//https://www.elastic.co/guide/en/elasticsearch/reference/6.8/analysis-stemmer-tokenfilter.html
public enum Stemmer {
	arabic, //
	armenian, //
	basque, //
	bengali, //
	light_bengali, //
	brazilian, //
	bulgarian, //
	catalan, //
	czech, //
	danish, //
	dutch, //
	dutch_kp, //
	english, //
	light_english, //
	minimal_english, //
	possessive_english, //
	porter2, //
	lovins, //
	finnish, //
	light_finnish, //
	french, //
	light_french, //
	minimal_french, //
	galician, //
	minimal_galician, //
	german, //
	german2, //
	light_german, //
	minimal_german, //
	greek, //
	hindi, //
	hungarian, //
	light_hungarian, //
	indonesian, //
	irish, //
	italian, //
	light_italian, //
	sorani, //
	latvian, //
	lithuanian, //
	norwegian, //
	light_norwegian, //
	minimal_norwegian, //
	light_nynorsk, //
	minimal_nynorsk, //
	portuguese, //
	light_portuguese, //
	minimal_portuguese, //
	portuguese_rslp, //
	romanian, //
	russian, //
	light_russian, //
	spanish, //
	light_spanish, //
	swedish, //
	light_swedish, //
	turkish,//
	;

	private String id;

	private Stemmer() {
		this.id = name();
	}

	public String id() {
		return id;
	}
}
