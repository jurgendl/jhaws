package org.jhaws.common.elasticsearch.impl;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.elasticsearch.common.Analyzer;
import org.jhaws.common.elasticsearch.common.Analyzers;
import org.jhaws.common.elasticsearch.common.Bool;
import org.jhaws.common.elasticsearch.common.CharacterFilter;
import org.jhaws.common.elasticsearch.common.DenseVectorType;
import org.jhaws.common.elasticsearch.common.DenseVectorSimilarity;
import org.jhaws.common.elasticsearch.common.Field;
import org.jhaws.common.elasticsearch.common.FieldExtra;
import org.jhaws.common.elasticsearch.common.FieldType;
import org.jhaws.common.elasticsearch.common.Filter;
import org.jhaws.common.elasticsearch.common.Ignore;
import org.jhaws.common.elasticsearch.common.Language;
import org.jhaws.common.elasticsearch.common.NestedField;
import org.jhaws.common.elasticsearch.common.OnlySave;
import org.jhaws.common.elasticsearch.common.Stemmer;
import org.jhaws.common.elasticsearch.common.Tokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@Component
public class ElasticCustomizer {
    protected final Logger LOGGER = LoggerFactory.getLogger(ElasticCustomizer.class);

    /**
     * needs cluster restart!<br>
     * client.updateClusterSetting(config -> config.put(ElasticCustomizer.CLUSER_SETTING_SEARCH_MAX_BUCKETS, ElasticCustomizer.CLUSER_SETTING_SEARCH_MAX_BUCKETS_VALUE));
     */
    public static final String CLUSER_SETTING_SEARCH_MAX_BUCKETS = "search.max_buckets";

    /**
     * client.updateIndexSetting(${index}, config -> config.put(ElasticCustomizer.INDEX_SETTINGS_MAX_RESULTS, ElasticCustomizer.INDEX_SETTINGS_MAX_RESULTS_VALUE));
     */
    public static final String INDEX_SETTINGS_MAX_RESULTS = "max_result_window";

    public static final String INDEX_SETTINGS_HIGHLIGHT_MAX_ANALYZED_OFFSET = "highlight.max_analyzed_offset";

    public static final String INDEX_SETTINGS_BLOCKS_READ_ONLY = "blocks.read_only";

    private static final String CUSTOM = "custom";

    private static final String REPLACEMENT = "replacement";

    private static final String PATTERN = "pattern";

    private static final String ENABLED = "enabled";

    private static final String INDEX = "index";

    private static final String LANGUAGE = "language";

    private static final String STOPWORDS = "stopwords";

    private static final String FIELDDATA = "fielddata";

    private static final String STORE = "store";

    private static final String FIELDS = "fields";

    private static final String PROPERTIES = "properties";

    private static final String MAX = "max";

    private static final String MIN = "min";

    private static final String PATTERNS = "patterns";

    private static final String PRESERVE_ORIGINAL = "preserve_original";

    private static final String TYPE = "type";

    private static final String FILTER = "filter";

    private static final String ANALYZER = "analyzer";

    private static final String ANALYSIS = "analysis";

    private static final String CHAR_FILTER = "char_filter";

    private static final String TOKENIZER = "tokenizer";

    private static final String ARTICLES = "articles";

    private static final String ARTICLES_CASE = "articles_case";

    public Map<String, Object> settings() {
        // "mappings": {
        // "_source": {
        // "enabled": false
        // }
        // }

        Map<String, Object> analysis = new LinkedHashMap<>();
        Map<String, Object> filter = new LinkedHashMap<>();
        filter.put(Filters.CUSTOM_LENGTH_3_TO_20_CHAR_LENGTH_FILTER, customCleanupFilter());
        filter.put(Filters.CUSTOM_EMAIL_NAME_FILTER, customEmailNameFilter());
        filter.put(Filters.CUSTOM_EMAIL_DOMAIN_FILTER, customEmailDomainFilter());
        filter.put(Filters.ENGLISH_STOP, customEnglishStopFilter());
        filter.put(Filters.ENGLISH_STEMMER, customEnglishStemmerFilter());
        filter.put(Filters.ENGLISH_POSSESSIVE_STEMMER, customEnglishPossessiveStemmerFilter());
        filter.put(Filters.DUTCH_STOP, customDutchStopFilter());
        filter.put(Filters.DUTCH_STEMMER, customDutchStemmerFilter());
        filter.put(Filters.CUSTOM_TO_SPACE_FILTER, customToSpaceFilter());
        filter.put(Filters.CUSTOM_REMOVE_SPACE_FILTER, customRemoveSpaceFilter());
        filter.put(Filters.CUSTOM_ONLY_KEEP_ALPHA_FILTER, customOnlyKeepAlphaFilter());
        filter.put(Filters.CUSTOM_ONLY_KEEP_ALPHANUMERIC_FILTER, customOnlyKeepAlphaNumericFilter());
        filter.put(Filters.CUSTOM_FRENCH_ELISION_FILTER, customFrenchElisionFilter());
        filter.put(Filters.CUSTOM_FRENCH_STOP_FILTER, customFrenchStopFilter());
        filter.put(Filters.CUSTOM_FRENCH_STEMMER_FILTER, customFrenchStemmerFilter());
        analysis.put(FILTER, filter);
        Map<String, Object> tokenizer = new LinkedHashMap<>();
        // tokenizer.put(Tokenizers.CUSTOM_PUNCTUATION_TOKENIZER, customPunctuationTokenizer());
        // tokenizer.put(Tokenizers.CUSTOM_FILENAME_TOKENIZER, customFilenameTokenizer());
        analysis.put(TOKENIZER, tokenizer);
        Map<String, Object> analyzer = new LinkedHashMap<>();
        analyzer.put(Analyzers.CUSTOM_CLEANUP_ANALYZER, customCleanupAnalyzer());
        analyzer.put(Analyzers.CUSTOM_DUTCH_HTML_ANALYZER, customDutchHtmlAnalyzer());
        analyzer.put(Analyzers.CUSTOM_ENGLISH_HTML_ANALYZER, customEnglishHtmlAnalyzer());
        analyzer.put(Analyzers.CUSTOM_ASCIIFOLDING_ANALYZER, customAsciiFoldingAnalyzer());
        analyzer.put(Analyzers.CUSTOM_EMAIL_NAME_ANALYZER, customEmailNameAnalyzer());
        analyzer.put(Analyzers.CUSTOM_EMAIL_NAME_KEEP_TOGETHER_ANALYZER, customEmailNameKeepTogetherAnalyzer());
        analyzer.put(Analyzers.CUSTOM_EMAIL_DOMAIN_ANALYZER, customEmailDomainAnalyzer());
        analyzer.put(Analyzers.CUSTOM_EMAIL_ANALYZER, customEmailAnalyzer());
        analyzer.put(Analyzers.CUSTOM_FILENAME_ANALYZER, customFilenameAnalyzer());
        analyzer.put(Analyzers.CUSTOM_WORD_DELIMITER_GRAPH_ANALYZER, customWordDelimiterGraphAnalyzer());
        analyzer.put(Analyzers.CUSTOM_WHITESPACE_LOWERCASE_ANALYZER, customWhitespaceAnalyzer());
        analyzer.put(Analyzers.CUSTOM_NAME_KEEP_TOGETHER_ANALYZER, customNameKeepTogetherAnalyzer());
        analyzer.put(Analyzers.CUSTOM_SORTABLE_ANALYZER, customSortableAnalyzer());
        analyzer.put(Analyzers.CUSTOM_SORTABLE_ONLY_ALPHA_ANALYZER, customSortableOnlyAlphaAnalyzer());
        analyzer.put(Analyzers.CUSTOM_SORTABLE_ONLY_ALPHANUMERIC_ANALYZER, customSortableOnlyAlphaNumericAnalyzer());
        analyzer.put(Analyzers.CUSTOM_ANY_LANGUAGE_ANALYZER, customAnyLanguageAnalyzer());
        analyzer.put(Analyzers.CUSTOM_FRENCH_LANGUAGE_ANALYZER, customFrenchLanguageAnalyzer());
        analysis.put(ANALYZER, analyzer);
        Map<String, Object> settings = new LinkedHashMap<>();
        settings.put(ANALYSIS, analysis);
        {
            Map<String, Object> indexSettings = new LinkedHashMap<>();
            indexSettings.put(INDEX_SETTINGS_HIGHLIGHT_MAX_ANALYZED_OFFSET, highlightMaxAnalyzedOffset);
            settings.put(INDEX, indexSettings);
        }
        {
            settings.put(INDEX_SETTINGS_MAX_RESULTS, maxResultWindow);
        }
        return settings;
    }

    public Map<String, Object> customEnglishHtmlAnalyzer() {
        // https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-htmlstrip-charfilter.html
        // standardTokenizer:
        // https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-tokenizers.html
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.standard.id());
        analyzerConfig.put(CHAR_FILTER, Arrays.asList(CharacterFilter.html_strip.id()));
        analyzerConfig.put(FILTER, Arrays.asList(Filters.ENGLISH_POSSESSIVE_STEMMER, Filter.lowercase.id(), Filters.ENGLISH_STOP, Filters.ENGLISH_STEMMER));
        return analyzerConfig;
    }

    public Map<String, Object> customDutchHtmlAnalyzer() {
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.standard.id());
        analyzerConfig.put(CHAR_FILTER, Arrays.asList(CharacterFilter.html_strip.id()));
        analyzerConfig.put(FILTER, Arrays.asList(Filter.lowercase.id(), Filters.DUTCH_STOP, Filters.DUTCH_STEMMER));
        return analyzerConfig;
    }

    public Map<String, Object> customAsciiFoldingAnalyzer() {
        // https: //
        // www.elastic.co/guide/en/elasticsearch/reference/current/analysis-asciifolding-tokenfilter.html
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.standard.id());
        analyzerConfig.put(FILTER, Arrays.asList(Filter.asciifolding.id()));
        return analyzerConfig;
    }

    public Map<String, Object> customCleanupFilter() {
        Map<String, Object> filterConfig = new LinkedHashMap<>();
        filterConfig.put(TYPE, Filter.length.id());
        filterConfig.put(MIN, 3);
        filterConfig.put(MAX, 20);
        return filterConfig;
    }

    public Map<String, Object> customCleanupAnalyzer() {
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.standard.id());
        analyzerConfig.put(FILTER, Arrays.asList(//
                Filter.trim.id()//
                , Filter.asciifolding.id()//
                , Filter.lowercase.id()//
                , Filter.word_delimiter.id()//
                , Filters.CUSTOM_LENGTH_3_TO_20_CHAR_LENGTH_FILTER//
        ));
        return analyzerConfig;
    }

    public Map<String, Object> customEnglishPossessiveStemmerFilter() {
        Map<String, Object> filterConfig = new LinkedHashMap<>();
        filterConfig.put(TYPE, Filter.stemmer.id());
        filterConfig.put(LANGUAGE, "possessive_" + Language.english.id());
        return filterConfig;
    }

    public Map<String, Object> customDutchStemmerFilter() {
        return customStemmerFilter(Stemmer.dutch);
    }

    public Map<String, Object> customEnglishStemmerFilter() {
        return customStemmerFilter(Stemmer.english);
    }

    public Map<String, Object> customFrenchStemmerFilter() {
        return customStemmerFilter(Stemmer.french);
    }

    public Map<String, Object> customDutchStopFilter() {
        return customStopFilter(Language.dutch);
    }

    public Map<String, Object> customEnglishStopFilter() {
        return customStopFilter(Language.english);
    }

    public Map<String, Object> customFrenchStopFilter() {
        return customStopFilter(Language.french);
    }

    public Map<String, Object> customEmailAnalyzer() {
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.uax_url_email.id());
        return analyzerConfig;
    }

    public Map<String, Object> customEmailDomainAnalyzer() {
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.uax_url_email.id());
        analyzerConfig.put(FILTER, Arrays.asList(//
                Filters.CUSTOM_EMAIL_DOMAIN_FILTER//
                , Filter.lowercase.id()//
        ));
        return analyzerConfig;
    }

    public Map<String, Object> customEmailNameAnalyzer() {
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.uax_url_email.id());
        analyzerConfig.put(FILTER, Arrays.asList(//
                Filters.CUSTOM_EMAIL_NAME_FILTER//
                , Filter.lowercase.id()//
                , Filter.word_delimiter.id()//
                , Filter.unique.id()//
        ));
        return analyzerConfig;
    }

    public Map<String, Object> customEmailNameKeepTogetherAnalyzer() {
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.uax_url_email.id());
        analyzerConfig.put(FILTER, Arrays.asList(//
                Filters.CUSTOM_EMAIL_NAME_FILTER//
                , Filter.lowercase.id()//
        ));
        return analyzerConfig;
    }

    public Map<String, Object> customEmailDomainFilter() {
        Map<String, Object> filterConfig = new LinkedHashMap<>();
        filterConfig.put(TYPE, Filter.pattern_capture.id());
        filterConfig.put(PRESERVE_ORIGINAL, false);
        filterConfig.put(PATTERNS, Arrays.asList("@(.+)"));
        return filterConfig;
    }

    public Map<String, Object> customEmailNameFilter() {
        Map<String, Object> filterConfig = new LinkedHashMap<>();
        filterConfig.put(TYPE, Filter.pattern_capture.id());
        filterConfig.put(PRESERVE_ORIGINAL, false);
        filterConfig.put(PATTERNS, Arrays.asList("(.+)@"));
        return filterConfig;
    }

    public Map<String, Object> customStemmerFilter(Stemmer language) {
        Map<String, Object> filterConfig = new LinkedHashMap<>();
        filterConfig.put(TYPE, Filter.stemmer.id());
        filterConfig.put(LANGUAGE, language.id());
        return filterConfig;
    }

    public Map<String, Object> customStopFilter(Language language) {
        Map<String, Object> filterConfig = new LinkedHashMap<>();
        filterConfig.put(TYPE, Filter.stop.id());
        filterConfig.put(STOPWORDS, "_" + language.id() + "_");
        return filterConfig;
    }

    public Map<String, Object> getObjectMapping(Class<?> annotatedType, MappingListener listener) {
        Map<String, Object> typeMapping = new TreeMap<>();
        $mappings_fields(null, annotatedType, typeMapping, "", listener);
        Map<String, Object> mappings = new TreeMap<>();
        mappings.put(PROPERTIES, typeMapping);
        return mappings;
    }

    protected void $mappings_fields(Language language, Class<?> annotatedType, Map<String, Object> typeMapping, String prefix, MappingListener listener) {
        Arrays.stream(annotatedType.getDeclaredFields())//
                .filter(f -> !Modifier.isTransient(f.getModifiers()))//
                .filter(f -> !Modifier.isStatic(f.getModifiers()))//
                .filter(f -> f.getAnnotation(JsonIgnore.class) == null)//
                .filter(f -> f.getAnnotation(Ignore.class) == null)//
                .forEach(field -> $mappings_field(language, typeMapping, prefix, field, listener));
    }

    protected void $mappings_field(Language language, Map<String, Object> mapping, String prefix, java.lang.reflect.Field reflectField, MappingListener listener) {
        LOGGER.trace("\n" + reflectField);
        OnlySave onlySave = reflectField.getAnnotation(OnlySave.class);
        if (onlySave != null) {
            // https://www.elastic.co/guide/en/elasticsearch/reference/current/enabled.html
            String fullName = prefix + (StringUtils.isBlank(onlySaveName(onlySave)) ? reflectField.getName() : onlySaveName(onlySave));
            Map<String, Object> fieldMapping = new TreeMap<>();
            fieldMapping.put(TYPE, FieldType.OBJECT.id());
            fieldMapping.put(ENABLED, Boolean.FALSE);
            mapping.put(fullName, fieldMapping);
        } else {
            Field field = reflectField.getAnnotation(Field.class);
            if (field != null) {
                FieldType defaultFieldType = $defaultFieldType(reflectField);
                FieldMapping fieldMapping = $mappings_field(reflectField, language, defaultFieldType, field);
                String fullName = prefix + (StringUtils.isBlank(field.name()) ? reflectField.getName() : field.name());
                mapping.put(fullName, fieldMapping.fieldMapping);
                if (listener != null) listener.map(fullName, field, fieldMapping);
                if (reflectField.getAnnotation(FieldExtra.class) != null) {
                    Field[] fef = reflectField.getAnnotation(FieldExtra.class).value();
                    if (fef != null && fef.length > 0) {
                        Map<String, Object> extraFields = new TreeMap<>();
                        fieldMapping.put(FIELDS, extraFields);
                        Arrays.stream(fef).forEach(nestedField -> {
                            extraFields.put(nestedFieldName(nestedField), $mappings_field(reflectField, fieldMapping.language, fieldMapping.fieldType, nestedField).fieldMapping);
                            if (listener != null) listener.map(fullName + "." + nestedFieldName(nestedField), nestedField, fieldMapping);
                        });
                    }
                }
            } else {
                JsonUnwrapped nested = reflectField.getAnnotation(JsonUnwrapped.class);
                if (nested != null) {
                    if (StringUtils.isNotBlank(nested.suffix())) {
                        throw new IllegalArgumentException("suffix nog niet ondersteund");
                    }
                    String nestedPrefix = nested.prefix();
                    if (StringUtils.isBlank(nestedPrefix)) {
                        throw new IllegalArgumentException("prefix verplicht");
                    }
                    NestedField nestedField = reflectField.getAnnotation(NestedField.class);
                    if (nestedField != null && nestedFieldLanguage(nestedField) != null && nestedFieldLanguage(nestedField) != Language.uninitialized) {
                        $mappings_fields(nestedFieldLanguage(nestedField), reflectField.getType(), mapping, prefix + nestedPrefix, listener);
                    } else {
                        $mappings_fields(language, reflectField.getType(), mapping, prefix + nestedPrefix, listener);
                    }
                } else {
                    NestedField nestedField = reflectField.getAnnotation(NestedField.class);
                    if (nestedField != null && nestedFieldLanguage(nestedField) != null && nestedFieldLanguage(nestedField) != Language.uninitialized) {
                        Map<String, Object> typeMapping = new TreeMap<>();
                        $mappings_fields(nestedFieldLanguage(nestedField), reflectField.getType(), typeMapping, "", listener);
                        Map<String, Object> mappings = new TreeMap<>();
                        mappings.put(PROPERTIES, typeMapping);
                        mapping.put(reflectField.getName(), mappings);
                    } else {
                        // $mappings_fields(language, reflectField.getType(), typeMapping, "", listener);
                    }
                }
            }
        }
    }

    private String nestedFieldName(Field nestedField) {
        return StringUtils.isBlank(nestedField.value()) ? nestedField.name() : nestedField.value();
    }

    private Language nestedFieldLanguage(NestedField nestedField) {
        if (nestedField.language() != Language.uninitialized) return nestedField.language();
        if (nestedField.value() != Language.uninitialized) return nestedField.value();
        return Language.uninitialized;
    }

    private String onlySaveName(OnlySave onlySave) {
        return StringUtils.isBlank(onlySave.value()) ? onlySave.name() : onlySave.value();
    }

    protected FieldMapping $mappings_field(java.lang.reflect.Field reflectField, Language language, FieldType fieldType, Field field) {
        LOGGER.trace("\t" + language + " / " + fieldType + " / " + field);
        FieldMapping fieldMapping = new FieldMapping();
        fieldMapping.language = field.language() == null || field.language() == Language.uninitialized ? language : field.language();
        fieldMapping.fieldType = field.type() == null || field.type() == FieldType.uninitialized ? fieldType : field.type();
        fieldMapping.put(TYPE, fieldMapping.fieldType.id());
        Map<String, Object> options = new LinkedHashMap<>(fieldMapping.fieldType.options());
        if (field.type() == FieldType.DENSE_VECTOR) {
            if (field.denseVector().dims() > 0) {
                options.put("dims", field.denseVector().dims());
                if (field.denseVector().similarity() != null && field.denseVector().similarity() != DenseVectorSimilarity.uninitialized) {
                    options.put("similarity", field.denseVector().similarity().id());
                }
                if (field.denseVector().type() != null && field.denseVector().type() != DenseVectorType.uninitialized) {
                    options.put("index_options.type", field.denseVector().type().id());
                    options.put("index_options.m", field.denseVector().m());
                    options.put("index_options.ef_construction", field.denseVector().ef_construction());
                }
            }
        }
        options.entrySet().forEach(option -> fieldMapping.fieldMapping.put(option.getKey(), option.getValue()));
        if (StringUtils.isNotBlank(field.customAnalyzer())) {
            if (fieldMapping.fieldType != FieldType.TEXT//
                    && fieldMapping.fieldType != FieldType.TEXT_VECTOR//
                    && fieldMapping.fieldType != FieldType.TEXT_VECTOR_PAYLOADS//
                    && fieldMapping.fieldType != FieldType.TEXT_VECTOR_POSITIONS//
                    && fieldMapping.fieldType != FieldType.TEXT_VECTOR_POSITIONS_OFFSETS//
                    && fieldMapping.fieldType != FieldType.TEXT_VECTOR_POSITIONS_OFFSETS_PAYLOADS//
            ) {
                LOGGER.warn("custom analyzer on non text-field");
            }
            if (fieldMapping.language == null) {
                fieldMapping.put(ANALYZER, replaceAnalyze(field.customAnalyzer()));
            } else {
                fieldMapping.put(ANALYZER, replaceAnalyze(field.customAnalyzer().replace(Analyzers.LANGUAGE_PARAMETER, fieldMapping.language.id())));
            }
        } else if (field.analyzer() != null && field.analyzer() != Analyzer.uninitialized) {
            if (field.analyzer() == Analyzer.language && fieldMapping.language == null) {
                throw new IllegalArgumentException("Analyzer.language requires specific language te be set on this field or parent object field" + "\n\t" + reflectField + "\n\t" + field);
            }
            fieldMapping.put(ANALYZER, replaceAnalyze(field.analyzer().id(fieldMapping.language)));
            // } else {
            // fieldMapping.put(ANALYZER, Analyzer.standard.id(null));
        }
        if (field.store() != null && field.store() != Bool.uninitialized) {
            fieldMapping.put(STORE, field.store().value());
        }
        if (field.fielddata() != null && field.fielddata() != Bool.uninitialized) {
            fieldMapping.put(FIELDDATA, field.fielddata().value());
        }
        return fieldMapping;
    }

    public FieldType $defaultFieldType(java.lang.reflect.Field field) {
        if (Collection.class.isAssignableFrom(field.getType())) {
            Class<?> collectionType = ElasticHelper.getCollectionType(field);
            if (collectionType == null) {
                return FieldType.TEXT;
            }
            return $defaultFieldType(collectionType);
        }

        return $defaultFieldType(field.getType());
    }

    protected FieldType $defaultFieldType(Class<?> ft) {
        if (byte[].class.isAssignableFrom(ft)) {
            return FieldType.BYTES;
        }

        if (ft.isArray()) {
            return $defaultFieldType(ft.getComponentType());
        }

        if (Short.class.isAssignableFrom(ft) || Short.TYPE.isAssignableFrom(ft)) {
            return FieldType.SHORT;
        }
        if (Integer.class.isAssignableFrom(ft) || Integer.TYPE.isAssignableFrom(ft)) {
            return FieldType.INT;
        }
        if (Long.class.isAssignableFrom(ft) || Long.TYPE.isAssignableFrom(ft)) {
            return FieldType.LONG;
        }
        if (Float.class.isAssignableFrom(ft) || Float.TYPE.isAssignableFrom(ft)) {
            return FieldType.FLOAT;
        }
        if (Double.class.isAssignableFrom(ft) || Double.TYPE.isAssignableFrom(ft)) {
            return FieldType.DOUBLE;
        }
        if (Boolean.class.isAssignableFrom(ft) || Boolean.TYPE.isAssignableFrom(ft)) {
            return FieldType.BOOL;
        }
        if (Byte.class.isAssignableFrom(ft) || Byte.TYPE.isAssignableFrom(ft)) {
            return FieldType.BYTE;
        }

        if (java.util.Date.class.isAssignableFrom(ft)) {
            return FieldType.DATE;
        }
        if (java.time.LocalDate.class.isAssignableFrom(ft)) {
            return FieldType.DATE;
        }
        if (java.time.LocalDateTime.class.isAssignableFrom(ft)) {
            return FieldType.DATE;
        }
        if (org.joda.time.LocalDate.class.isAssignableFrom(ft)) {
            return FieldType.DATE;
        }
        if (org.joda.time.LocalDateTime.class.isAssignableFrom(ft)) {
            return FieldType.DATE;
        }

        return FieldType.TEXT;
    }

    public Map<String, Object> customToSpaceFilter() {
        Map<String, Object> filterConfig = new LinkedHashMap<>();
        filterConfig.put(TYPE, Filter.pattern_replace.id());
        String pattern = "["//
                + ".,!?"//
                + "]";
        filterConfig.put(PATTERN, pattern);
        filterConfig.put(REPLACEMENT, " ");
        return filterConfig;
    }

    // public Map<String, Object> customFilenameTokenizer() {
    // Map<String, Object> cfg = new LinkedHashMap<>();
    // cfg.put(TYPE, PATTERN);
    // String pattern = "["//
    // + " "//
    // + ".,!?"//
    // + "_-"//
    // + "]";
    // System.out.println(pattern);
    // cfg.put(PATTERN, pattern);
    // return cfg;
    // }
    //
    // public Map<String, Object> customPunctuationTokenizer() {
    // Map<String, Object> cfg = new LinkedHashMap<>();
    // cfg.put(TYPE, PATTERN);
    // cfg.put(PATTERN, "["//
    // + " "//
    // + ".,!?"//
    // + "]");
    // return cfg;
    // }

    public Map<String, Object> customWordDelimiterGraphAnalyzer() {
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.keyword.id());
        analyzerConfig.put(FILTER, Arrays.asList(Filter.word_delimiter_graph.id()));
        return analyzerConfig;
    }

    public Map<String, Object> customFilenameAnalyzer() {
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.keyword.id());
        analyzerConfig.put(FILTER, Arrays.asList(Filter.word_delimiter_graph.id(), Filter.asciifolding.id(), Filter.lowercase.id()));
        return analyzerConfig;
    }

    public Map<String, Object> customWhitespaceAnalyzer() {
        // https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-lowercase-tokenfilter.html
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.whitespace.id());
        analyzerConfig.put(FILTER, Filter.lowercase.id());
        return analyzerConfig;
    }

    public Map<String, Object> customNameKeepTogetherAnalyzer() {
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.keyword.id());
        analyzerConfig.put(FILTER, Arrays.asList(Filter.asciifolding.id(), Filter.lowercase.id()));
        return analyzerConfig;
    }

    public Map<String, Object> customRemoveSpaceFilter() {
        Map<String, Object> filterConfig = new LinkedHashMap<>();
        filterConfig.put(TYPE, Filter.pattern_replace.id());
        String pattern = "["//
                + " "//
                + "]";
        filterConfig.put(PATTERN, pattern);
        filterConfig.put(REPLACEMENT, "");
        return filterConfig;
    }

    public Map<String, Object> customSortableAnalyzer() {
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.keyword.id());
        analyzerConfig.put(FILTER, Arrays.asList(Filter.asciifolding.id(), Filters.CUSTOM_REMOVE_SPACE_FILTER, Filter.uppercase.id()));
        return analyzerConfig;
    }

    public Map<String, Object> customOnlyKeepAlphaFilter() {
        Map<String, Object> filterConfig = new LinkedHashMap<>();
        filterConfig.put(TYPE, Filter.pattern_replace.id());
        String pattern = "[^A-Za-z]";
        filterConfig.put(PATTERN, pattern);
        filterConfig.put(REPLACEMENT, "");
        return filterConfig;
    }

    public Map<String, Object> customSortableOnlyAlphaAnalyzer() {
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.keyword.id());
        analyzerConfig.put(FILTER, Arrays.asList(Filter.asciifolding.id(), Filter.uppercase.id(), Filters.CUSTOM_ONLY_KEEP_ALPHA_FILTER));
        return analyzerConfig;
    }

    public Map<String, Object> customOnlyKeepAlphaNumericFilter() {
        Map<String, Object> filterConfig = new LinkedHashMap<>();
        filterConfig.put(TYPE, Filter.pattern_replace.id());
        String pattern = "[^A-Za-z0-9]";
        filterConfig.put(PATTERN, pattern);
        filterConfig.put(REPLACEMENT, "");
        return filterConfig;
    }

    public Map<String, Object> customSortableOnlyAlphaNumericAnalyzer() {
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.keyword.id());
        analyzerConfig.put(FILTER, Arrays.asList(Filter.asciifolding.id(), Filter.uppercase.id(), Filters.CUSTOM_ONLY_KEEP_ALPHANUMERIC_FILTER));
        return analyzerConfig;
    }

    @Value("${elasticCustomizer.index.highlightMaxAnalyzedOffset:1000000}") // 10_000_000
    private Integer highlightMaxAnalyzedOffset = 1_000_000;

    @Value("${elasticCustomizer.index.maxResultWindow:10000}") // 100_000
    private Integer maxResultWindow = 10_000;

    @Value("${elasticCustomizer.cluster.searchMaxBuckets:10000}") // 100_000
    private Integer searchMaxBuckets = 10_000;

    public Integer getHighlightMaxAnalyzedOffset() {
        return highlightMaxAnalyzedOffset;
    }

    public void setHighlightMaxAnalyzedOffset(Integer highlightMaxAnalyzedOffset) {
        this.highlightMaxAnalyzedOffset = highlightMaxAnalyzedOffset;
    }

    public Integer getMaxResultWindow() {
        return maxResultWindow;
    }

    public void setMaxResultWindow(Integer maxResultWindow) {
        this.maxResultWindow = maxResultWindow;
    }

    public Integer getSearchMaxBuckets() {
        return searchMaxBuckets;
    }

    public void setSearchMaxBuckets(Integer searchMaxBuckets) {
        this.searchMaxBuckets = searchMaxBuckets;
    }

    public Map<String, Object> customAnyLanguageAnalyzer() {
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.standard.id());
        analyzerConfig.put(FILTER, Arrays.asList(Filter.apostrophe.id(), Filter.asciifolding.id(), Filter.lowercase.id()));
        return analyzerConfig;
    }

    public static final List<Analyzer> ANALYZER_EXCLUDE_TRYOUTS = Arrays.asList(//
            Analyzer.language_japanese, //
            Analyzer.language_chinese, //
            Analyzer.language_korean, //
            Analyzer.language_ukrainian, //
            Analyzer.language_polish//
    );

    private Map<String, String> replaceAnalyzers = new LinkedHashMap<>();

    public Map<String, String> getReplaceAnalyzers() {
        return this.replaceAnalyzers;
    }

    public void setReplaceAnalyzers(Map<String, String> replaceAnalyzers) {
        this.replaceAnalyzers = replaceAnalyzers;
    }

    public String replaceAnalyze(String analyzer) {
        return Optional.ofNullable(replaceAnalyzers.get(analyzer)).orElse(analyzer);
    }

    public Map<String, Object> customFrenchElisionFilter() {
        Map<String, Object> filterConfig = new LinkedHashMap<>();
        filterConfig.put(TYPE, Filter.elision.id());
        filterConfig.put(ARTICLES_CASE, true);
        filterConfig.put(ARTICLES, Arrays.asList("l", "m", "t", "qu", "n", "s", "j", "d", "c", "jusqu", "quoiqu", "lorsqu", "puisqu"));
        return filterConfig;
    }

    public Map<String, Object> customFrenchLanguageAnalyzer() {
        Map<String, Object> analyzerConfig = new LinkedHashMap<>();
        analyzerConfig.put(TYPE, CUSTOM);
        analyzerConfig.put(TOKENIZER, Tokenizer.standard.id());
        analyzerConfig.put(FILTER, Arrays.asList(//
                Filters.CUSTOM_FRENCH_ELISION_FILTER, //
                Filter.lowercase.id(), //
                Filter.asciifolding.id(), //
                Filters.CUSTOM_FRENCH_STOP_FILTER, //
                Filters.CUSTOM_FRENCH_STEMMER_FILTER//
        ));
        return analyzerConfig;
    }

}
