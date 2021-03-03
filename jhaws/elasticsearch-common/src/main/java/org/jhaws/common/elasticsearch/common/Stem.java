package org.jhaws.common.elasticsearch.common;

public enum Stem {
	arabic, //
	armenian, //
	basque, //
	bengali, //
	bulgarian, //
	catalan, //
	czech, //
	dutch, //
	english, //
	finnish, //
	french, //
	galician, //
	german, //
	hindi, //
	hungarian, //
	indonesian, //
	irish, //
	italian, //
	latvian, //
	lithuanian, //
	norwegian, //
	portuguese, //
	romanian, //
	russian, //
	sorani, //
	spanish, //
	swedish, //
	turkish,//
	;

	private String id;

	private Stem() {
		this.id = name();
	}

	public String id() {
		return id;
	}
}
