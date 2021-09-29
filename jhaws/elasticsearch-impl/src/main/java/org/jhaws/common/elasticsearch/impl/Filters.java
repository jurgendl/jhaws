package org.jhaws.common.elasticsearch.impl;

public interface Filters {
	public static final String CUSTOM_LENGTH_3_TO_20_CHAR_LENGTH_FILTER = "custom_length_3_to_20_char_length_filter";

	public static final String CUSTOM_EMAIL_DOMAIN_FILTER = "custom_email_domain_filter";

	public static final String CUSTOM_EMAIL_NAME_FILTER = "custom_email_name_filter";

	public static final String ENGLISH_STOP = "english_stop";

	public static final String ENGLISH_STEMMER = "english_stemmer";

	public static final String ENGLISH_POSSESSIVE_STEMMER = "english_possessive_stemmer";

	public static final String DUTCH_STOP = "dutch_stop";

	public static final String DUTCH_STEMMER = "dutch_stemmer";

	public static final String CUSTOM_TO_SPACE_FILTER = "custom_to_space_filter";

	public static final String CUSTOM_REMOVE_SPACE_FILTER = "custom_remove_space_filter";

	public static final String CUSTOM_ONLY_KEEP_ALPHANUMERIC_FILTER = "custom_only_keep_alphanumeric_filter";

	public static final String CUSTOM_ONLY_KEEP_ALPHA_FILTER = "custom_only_keep_alpha_filter";

	public static final String CUSTOM_FRENCH_ELISION_FILTER = "custom_french_elision_filter";

	public static final String CUSTOM_FRENCH_STOP_FILTER = "custom_french_stop_filter";

	public static final String CUSTOM_FRENCH_STEMMER_FILTER = "custom_french_stemmer_filter";
}
