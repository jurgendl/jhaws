package org.jhaws.common.elasticsearch.common;

// String index = "altijdnieuw";
// client.deleteIndex(index);
// client.createIndex(index);
// String test = "The qu!ck brown-fox jumps 0ver thé lazy dog.";
// Arrays.stream(Analyzers.class.getFields()).forEach(f -> {
// try {
// String analyzer = f.get(Analyzers.class).toString();
// if (!analyzer.contains("{language}")) {
// System.out.println(f.getName() + " -> " + analyzer + " -> " + client.analyzeIndex(index, analyzer, test));
// }
// } catch (IllegalArgumentException | IllegalAccessException ex) {
// ex.printStackTrace();
// }
// });
//
//
// The qu!ck brown-fox jumps 0ver thé lazy dog.
//
// CUSTOM_WHITESPACE_LOWERCASE_ANALYZER -> custom_whitespace_lowercase_analyzer -> [the, qu!ck, brown-fox, jumps, 0ver, thé, lazy, dog.]
// CUSTOM_ENGLISH_HTML_ANALYZER -> custom_english_html_analyzer -> [qu, ck, brown, fox, jump, 0ver, thé, lazi, dog]
// CUSTOM_DUTCH_HTML_ANALYZER -> custom_dutch_html_analyzer -> [the, qu, ck, brown, fox, jump, 0ver, the, lazy, dog]
// CUSTOM_CLEANUP_ANALYZER -> custom_cleanup_analyzer -> [the, brown, fox, jumps, ver, the, lazy, dog]
// CUSTOM_ASCIIFOLDING_ANALYZER -> custom_asciifolding_analyzer -> [The, qu, ck, brown, fox, jumps, 0ver, the, lazy, dog]
// CUSTOM_EMAIL_ANALYZER -> custom_email_analyzer -> [The, qu, ck, brown, fox, jumps, 0ver, thé, lazy, dog]
// CUSTOM_EMAIL_DOMAIN_ANALYZER -> custom_email_domain_analyzer -> [the, qu, ck, brown, fox, jumps, 0ver, thé, lazy, dog]
// CUSTOM_EMAIL_NAME_ANALYZER -> custom_email_name_analyzer -> [the, qu, ck, brown, fox, jumps, 0, ver, thé, lazy, dog]
// CUSTOM_FILENAME_ANALYZER -> custom_filename_analyzer -> [the, qu, ck, brown, fox, jumps, 0, ver, the, lazy, dog]
// CUSTOM_WORD_DELIMITER_GRAPH_ANALYZER -> custom_word_delimiter_graph_analyzer -> [The, qu, ck, brown, fox, jumps, 0, ver, thé, lazy, dog]
// CUSTOM_NAME_KEEP_TOGETHER_ANALYZER -> custom_name_keep_together_analyzer -> [the qu!ck brown-fox jumps 0ver the lazy dog.]
// CUSTOM_SORTABLE_ANALYZER -> custom_sortable_analyzer -> [THEQU!CKBROWN-FOXJUMPS0VERTHELAZYDOG.]
// CUSTOM_SORTABLE_ONLY_ALPHA_ANALYZER -> custom_sortable_only_alpha_analyzer -> [THEQUCKBROWNFOXJUMPSVERTHELAZYDOG]
// CUSTOM_SORTABLE_ONLY_ALPHANUMERIC_ANALYZER -> custom_sortable_only_alphanumeric_analyzer -> [THEQUCKBROWNFOXJUMPS0VERTHELAZYDOG]
public interface Analyzers {
    public static final String LANGUAGE_PARAMETER = "{language}";

    public static final String CUSTOM_WHITESPACE_LOWERCASE_ANALYZER = "custom_whitespace_lowercase_analyzer";

    public static final String CUSTOM_HTML_ANALYZER = "custom_" + LANGUAGE_PARAMETER + "_html_analyzer";

    /** see {@link #CUSTOM_HTML_ANALYZER} */
    public static final String CUSTOM_ENGLISH_HTML_ANALYZER = "custom_english_html_analyzer";

    /** see {@link #CUSTOM_HTML_ANALYZER} */
    public static final String CUSTOM_DUTCH_HTML_ANALYZER = "custom_dutch_html_analyzer";

    public static final String CUSTOM_CLEANUP_ANALYZER = "custom_cleanup_analyzer";

    public static final String CUSTOM_ASCIIFOLDING_ANALYZER = "custom_asciifolding_analyzer";

    public static final String CUSTOM_EMAIL_ANALYZER = "custom_email_analyzer";

    public static final String CUSTOM_EMAIL_DOMAIN_ANALYZER = "custom_email_domain_analyzer";

    public static final String CUSTOM_EMAIL_NAME_ANALYZER = "custom_email_name_analyzer";

    public static final String CUSTOM_EMAIL_NAME_KEEP_TOGETHER_ANALYZER = "custom_email_name_keep_together_analyzer";

    public static final String CUSTOM_FILENAME_ANALYZER = "custom_filename_analyzer";

    public static final String CUSTOM_WORD_DELIMITER_GRAPH_ANALYZER = "custom_word_delimiter_graph_analyzer";

    public static final String CUSTOM_NAME_KEEP_TOGETHER_ANALYZER = "custom_name_keep_together_analyzer";

    public static final String CUSTOM_SORTABLE_ANALYZER = "custom_sortable_analyzer";

    public static final String CUSTOM_SORTABLE_ONLY_ALPHA_ANALYZER = "custom_sortable_only_alpha_analyzer";

    public static final String CUSTOM_SORTABLE_ONLY_ALPHANUMERIC_ANALYZER = "custom_sortable_only_alphanumeric_analyzer";

    public static final String CUSTOM_ANY_LANGUAGE_ANALYZER = "custom_any_language_analyzer";

    public static final String CUSTOM_FRENCH_LANGUAGE_ANALYZER = "custom_french_language_analyzer";
}
