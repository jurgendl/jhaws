package org.jhaws.common.elasticsearch8.impl;

import co.elastic.clients.elasticsearch._types.analysis.CustomAnalyzer;
import co.elastic.clients.elasticsearch._types.analysis.ElisionTokenFilter;
import co.elastic.clients.elasticsearch._types.analysis.LengthTokenFilter;
import co.elastic.clients.elasticsearch._types.analysis.PatternCaptureTokenFilter;
import co.elastic.clients.elasticsearch._types.analysis.PatternReplaceTokenFilter;
import co.elastic.clients.elasticsearch._types.analysis.StemmerTokenFilter;
import co.elastic.clients.elasticsearch._types.analysis.StopTokenFilter;
import co.elastic.clients.elasticsearch._types.analysis.TokenFilter;
import co.elastic.clients.elasticsearch._types.analysis.TokenFilterDefinition;
import co.elastic.clients.json.JsonpSerializable;
import co.elastic.clients.json.JsonpSerializer;
import co.elastic.clients.json.JsonpUtils;
import co.elastic.clients.json.SimpleJsonpMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.elasticsearch.common.Analyzer;
import org.jhaws.common.elasticsearch.common.Analyzers;
import org.jhaws.common.elasticsearch.common.Bool;
import org.jhaws.common.elasticsearch.common.CharacterFilter;
import org.jhaws.common.elasticsearch.common.DenseVectorSimilarity;
import org.jhaws.common.elasticsearch.common.DenseVectorType;
import org.jhaws.common.elasticsearch.common.ElasticDocument;
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
import org.jhaws.common.web.resteasy.CustomObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class ElasticCustomizer {
    static JsonpSerializer<?> configToJson_JsonpSerializer = (JsonpSerializer<Object>) (value, generator, mapper) -> {
        if (value == null) {
            generator.writeNull();
        } else {
            generator.write(value.toString());
        }
    };
    static SimpleJsonpMapper configToJson_SimpleJsonpMapper = new SimpleJsonpMapper() {
        @Override
        protected <T> JsonpSerializer<T> getDefaultSerializer(T value) {
            return (JsonpSerializer<T>) configToJson_JsonpSerializer;
        }
    };

    public static String serializeJsonp(JsonpSerializable o) {
        StringBuilder sb = new StringBuilder();
        JsonpUtils.toString(o, configToJson_SimpleJsonpMapper, sb);
        return sb.toString();
    }

    private static final String FILTER = "filter";

    private static final String ANALYZER = "analyzer";

    private static final String ANALYSIS = "analysis";

    private static final String CHAR_FILTER = "char_filter";

    private static final String TOKENIZER = "tokenizer";

    private static final String NORMALIZER = "normalizer";

    public static void main(String[] args) {
        try {
            ElasticCustomizer eb = new ElasticCustomizer();
            CustomObjectMapper om = new CustomObjectMapper();
            om.configure(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT, true);

            Map<String, Object> configMap = new LinkedHashMap<>();
            Map<String, Object> analysisMap = new LinkedHashMap<>();
            configMap.put(ANALYSIS, analysisMap);

            TypeReference<Map<String, Object>> valueTypeRef = new TypeReference<>() {
            };
            if (!eb.analyzers().isEmpty()) {
                Map<String, Map<String, Object>> analyzerMap = new LinkedHashMap<>();
                for (Map.Entry<String, co.elastic.clients.elasticsearch._types.analysis.Analyzer> entry : eb.analyzers().entrySet()) {
                    analyzerMap.put(entry.getKey(), om.readValue(serializeJsonp(entry.getValue()), valueTypeRef));
                }
                analysisMap.put(ANALYZER, analyzerMap);
            }
            if (!eb.tokenizers().isEmpty()) {
                Map<String, Map<String, Object>> tokenizerMap = new LinkedHashMap<>();
                for (Map.Entry<String, co.elastic.clients.elasticsearch._types.analysis.Tokenizer> entry : eb.tokenizers().entrySet()) {
                    tokenizerMap.put(entry.getKey(), om.readValue(serializeJsonp(entry.getValue()), valueTypeRef));
                }
                analysisMap.put(TOKENIZER, tokenizerMap);
            }
            if (!eb.tokenFilters().isEmpty()) {
                Map<String, Map<String, Object>> tokenFiltersMap = new LinkedHashMap<>();
                for (Map.Entry<String, co.elastic.clients.elasticsearch._types.analysis.TokenFilter> entry : eb.tokenFilters().entrySet()) {
                    tokenFiltersMap.put(entry.getKey(), om.readValue(serializeJsonp(entry.getValue()), valueTypeRef));
                }
                analysisMap.put(FILTER, tokenFiltersMap);
            }
            if (!eb.charFilters().isEmpty()) {
                Map<String, Map<String, Object>> charFiltersMap = new LinkedHashMap<>();
                for (Map.Entry<String, co.elastic.clients.elasticsearch._types.analysis.CharFilter> entry : eb.charFilters().entrySet()) {
                    charFiltersMap.put(entry.getKey(), om.readValue(serializeJsonp(entry.getValue()), valueTypeRef));
                }
                analysisMap.put(CHAR_FILTER, charFiltersMap);
            }
            if (!eb.normalizers().isEmpty()) {
                Map<String, Map<String, Object>> normalizersMap = new LinkedHashMap<>();
                for (Map.Entry<String, co.elastic.clients.elasticsearch._types.analysis.Normalizer> entry : eb.normalizers().entrySet()) {
                    normalizersMap.put(entry.getKey(), om.readValue(serializeJsonp(entry.getValue()), valueTypeRef));
                }
                analysisMap.put(NORMALIZER, normalizersMap);
            }
            System.out.println(om.writer().writeValueAsString(configMap));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final String INDEX_SETTINGS_BLOCKS_READ_ONLY = "blocks.read_only";

    public static final String INDEX_SETTINGS_HIGHLIGHT_MAX_ANALYZED_OFFSET = "highlight.max_analyzed_offset";

    protected final Logger LOGGER = LoggerFactory.getLogger(ElasticCustomizer.class);

    protected final Logger LOGGER_DETAILS = LoggerFactory.getLogger(ElasticCustomizer.class.getName() + ".details");

    public static final String ENABLED = "enabled";

    public static final String FIELDDATA = "fielddata";

    public static final String STORE = "store";

    public static final String FIELDS = "fields";

    public static final String PROPERTIES = "properties";

    public static final String TYPE = "type";


    public Map<String, co.elastic.clients.elasticsearch._types.analysis.Normalizer> normalizers() {
        Map<String, co.elastic.clients.elasticsearch._types.analysis.Normalizer> normalizers = new LinkedHashMap<>();
        return normalizers;
    }

    public Map<String, co.elastic.clients.elasticsearch._types.analysis.CharFilter> charFilters() {
        Map<String, co.elastic.clients.elasticsearch._types.analysis.CharFilter> charFilters = new LinkedHashMap<>();
        return charFilters;
    }

    public Map<String, co.elastic.clients.elasticsearch._types.analysis.Tokenizer> tokenizers() {
        Map<String, co.elastic.clients.elasticsearch._types.analysis.Tokenizer> tokenizers = new LinkedHashMap<>();
        return tokenizers;
    }

    public Map<String, co.elastic.clients.elasticsearch._types.analysis.TokenFilter> tokenFilters() {
        Map<String, co.elastic.clients.elasticsearch._types.analysis.TokenFilter> tokenFilters = new LinkedHashMap<>();
        tokenFilters.put(Filters.CUSTOM_LENGTH_3_TO_20_CHAR_LENGTH_FILTER, customCleanupFilter(Filters.CUSTOM_LENGTH_3_TO_20_CHAR_LENGTH_FILTER));
        tokenFilters.put(Filters.CUSTOM_EMAIL_NAME_FILTER, customEmailNameFilter(Filters.CUSTOM_EMAIL_NAME_FILTER));
        tokenFilters.put(Filters.CUSTOM_EMAIL_DOMAIN_FILTER, customEmailDomainFilter(Filters.CUSTOM_EMAIL_DOMAIN_FILTER));
        tokenFilters.put(Filters.ENGLISH_STOP, customEnglishStopFilter(Filters.ENGLISH_STOP));
        tokenFilters.put(Filters.ENGLISH_STEMMER, customEnglishStemmerFilter(Filters.ENGLISH_STEMMER));
        tokenFilters.put(Filters.ENGLISH_POSSESSIVE_STEMMER, customEnglishPossessiveStemmerFilter(Filters.ENGLISH_POSSESSIVE_STEMMER));
        tokenFilters.put(Filters.DUTCH_STOP, customDutchStopFilter(Filters.DUTCH_STOP));
        tokenFilters.put(Filters.DUTCH_STEMMER, customDutchStemmerFilter(Filters.DUTCH_STEMMER));
        tokenFilters.put(Filters.CUSTOM_TO_SPACE_FILTER, customToSpaceFilter(Filters.CUSTOM_TO_SPACE_FILTER));
        tokenFilters.put(Filters.CUSTOM_REMOVE_SPACE_FILTER, customRemoveSpaceFilter(Filters.CUSTOM_REMOVE_SPACE_FILTER));
        tokenFilters.put(Filters.CUSTOM_ONLY_KEEP_ALPHA_FILTER, customOnlyKeepAlphaFilter(Filters.CUSTOM_ONLY_KEEP_ALPHA_FILTER));
        tokenFilters.put(Filters.CUSTOM_ONLY_KEEP_ALPHANUMERIC_FILTER, customOnlyKeepAlphaNumericFilter(Filters.CUSTOM_ONLY_KEEP_ALPHANUMERIC_FILTER));
        tokenFilters.put(Filters.CUSTOM_ONLY_KEEP_EXTENDED_ALPHANUMERIC_FILTER, customOnlyKeepExtendedAlphaNumericFilter(Filters.CUSTOM_ONLY_KEEP_EXTENDED_ALPHANUMERIC_FILTER));
        tokenFilters.put(Filters.CUSTOM_FRENCH_ELISION_FILTER, customFrenchElisionFilter(Filters.CUSTOM_FRENCH_ELISION_FILTER));
        tokenFilters.put(Filters.CUSTOM_FRENCH_STOP_FILTER, customFrenchStopFilter(Filters.CUSTOM_FRENCH_STOP_FILTER));
        tokenFilters.put(Filters.CUSTOM_FRENCH_STEMMER_FILTER, customFrenchStemmerFilter(Filters.CUSTOM_FRENCH_STEMMER_FILTER));
        return tokenFilters;
    }

    public Map<String, co.elastic.clients.elasticsearch._types.analysis.Analyzer> analyzers() {
        Map<String, co.elastic.clients.elasticsearch._types.analysis.Analyzer> analyzers = new LinkedHashMap<>();
        analyzers.put(Analyzers.CUSTOM_CLEANUP_ANALYZER, customCleanupAnalyzer());
        analyzers.put(Analyzers.CUSTOM_DUTCH_HTML_ANALYZER, customDutchHtmlAnalyzer());
        analyzers.put(Analyzers.CUSTOM_ENGLISH_HTML_ANALYZER, customEnglishHtmlAnalyzer());
        analyzers.put(Analyzers.CUSTOM_ASCIIFOLDING_ANALYZER, customAsciiFoldingAnalyzer());
        analyzers.put(Analyzers.CUSTOM_EMAIL_NAME_ANALYZER, customEmailNameAnalyzer());
        analyzers.put(Analyzers.CUSTOM_EMAIL_NAME_KEEP_TOGETHER_ANALYZER, customEmailNameKeepTogetherAnalyzer());
        analyzers.put(Analyzers.CUSTOM_EMAIL_DOMAIN_ANALYZER, customEmailDomainAnalyzer());
        analyzers.put(Analyzers.CUSTOM_EMAIL_ANALYZER, customEmailAnalyzer());
        analyzers.put(Analyzers.CUSTOM_FILENAME_ANALYZER, customFilenameAnalyzer());
        analyzers.put(Analyzers.CUSTOM_WORD_DELIMITER_GRAPH_ANALYZER, customWordDelimiterGraphAnalyzer());
        analyzers.put(Analyzers.CUSTOM_WHITESPACE_LOWERCASE_ANALYZER, customWhitespaceAnalyzer());
        analyzers.put(Analyzers.CUSTOM_NAME_KEEP_TOGETHER_ANALYZER, customNameKeepTogetherAnalyzer());
        analyzers.put(Analyzers.CUSTOM_SORTABLE_ANALYZER, customSortableAnalyzer());
        analyzers.put(Analyzers.CUSTOM_SORTABLE_ONLY_ALPHA_ANALYZER, customSortableOnlyAlphaAnalyzer());
        analyzers.put(Analyzers.CUSTOM_SORTABLE_ONLY_ALPHANUMERIC_ANALYZER, customSortableOnlyAlphaNumericAnalyzer());
        analyzers.put(Analyzers.CUSTOM_SORTABLE_EXTENDED_ALPHANUMERIC_ANALYZER, customSortableExtendedAlphaNumericAnalyzer());
        analyzers.put(Analyzers.CUSTOM_ANY_LANGUAGE_ANALYZER, customAnyLanguageAnalyzer());
        analyzers.put(Analyzers.CUSTOM_FRENCH_LANGUAGE_ANALYZER, customFrenchLanguageAnalyzer());
        analyzers.put(Analyzers.CUSTOM_FOLDED_LOWERCASE_TOKENS_ANALYZER, customFoldedLowercaseTokensAnalyzer());
        return analyzers;
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customEnglishHtmlAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.standard.id()//
        ).charFilter(Arrays.asList(//
                CharacterFilter.html_strip.id()//
        )).filter(Arrays.asList(//
                Filters.ENGLISH_POSSESSIVE_STEMMER//
                , Filter.lowercase.id()//
                , Filters.ENGLISH_STOP//
                , Filters.ENGLISH_STEMMER//
        )).build()).build();
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customDutchHtmlAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.standard.id()//
        ).charFilter(Arrays.asList(//
                CharacterFilter.html_strip.id()//
        )).filter(Arrays.asList(//
                Filter.lowercase.id()//
                , Filters.DUTCH_STOP//
                , Filters.DUTCH_STEMMER//
        )).build()).build();
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customAsciiFoldingAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.standard.id()//
        ).filter(Arrays.asList(//
                Filter.asciifolding.id()//
        )).build()).build();
    }

    public TokenFilter customCleanupFilter(String name) {
        TokenFilter.Builder builder = new TokenFilter.Builder();
        builder.name(name);
        builder.definition(new TokenFilterDefinition.Builder().length(new LengthTokenFilter.Builder().min(3).max(20).build()).build());
        return builder.build();
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customCleanupAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.standard.id()//
        ).filter(Arrays.asList(//
                Filter.trim.id()//
                , Filter.asciifolding.id()//
                , Filter.lowercase.id()//
                , Filter.word_delimiter.id()//
                , Filters.CUSTOM_LENGTH_3_TO_20_CHAR_LENGTH_FILTER//
        )).build()).build();
    }

    public TokenFilter customEnglishPossessiveStemmerFilter(String name) {
        TokenFilter.Builder builder = new TokenFilter.Builder();
        builder.name(name);
        builder.definition(new TokenFilterDefinition.Builder().stemmer(new StemmerTokenFilter.Builder().language("possessive_" + Language.english.id()).build()).build());
        return builder.build();
    }

    public TokenFilter customDutchStemmerFilter(String name) {
        return customStemmerFilter(name, Stemmer.dutch);
    }

    public TokenFilter customEnglishStemmerFilter(String name) {
        return customStemmerFilter(name, Stemmer.english);
    }

    public TokenFilter customFrenchStemmerFilter(String name) {
        return customStemmerFilter(name, Stemmer.french);
    }

    public TokenFilter customDutchStopFilter(String name) {
        return customStopFilter(name, Language.dutch);
    }

    public TokenFilter customEnglishStopFilter(String name) {
        return customStopFilter(name, Language.english);
    }

    public TokenFilter customFrenchStopFilter(String name) {
        return customStopFilter(name, Language.french);
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customEmailAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.uax_url_email.id()//
        ).build()).build();
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customEmailDomainAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.uax_url_email.id()//
        ).filter(Arrays.asList(//
                Filters.CUSTOM_EMAIL_DOMAIN_FILTER, //
                Filter.lowercase.id()//
        )).build()).build();
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customEmailNameAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.uax_url_email.id()//
        ).filter(Arrays.asList(//
                Filters.CUSTOM_EMAIL_NAME_FILTER//
                , Filter.lowercase.id()//
                , Filter.word_delimiter.id()//
                , Filter.unique.id()//
        )).build()).build();
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customEmailNameKeepTogetherAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.uax_url_email.id()//
        ).filter(Arrays.asList(//
                Filters.CUSTOM_EMAIL_NAME_FILTER//
                , Filter.lowercase.id()//
        )).build()).build();
    }

    public TokenFilter customEmailDomainFilter(String name) {
        TokenFilter.Builder builder = new TokenFilter.Builder();
        builder.name(name);
        builder.definition(new TokenFilterDefinition.Builder().patternCapture(new PatternCaptureTokenFilter.Builder().preserveOriginal(false).patterns(Arrays.asList("@(.+)")).build()).build());
        return builder.build();
    }

    public TokenFilter customEmailNameFilter(String name) {
        TokenFilter.Builder builder = new TokenFilter.Builder();
        builder.name(name);
        builder.definition(new TokenFilterDefinition.Builder().patternCapture(new PatternCaptureTokenFilter.Builder().preserveOriginal(false).patterns(Arrays.asList("(.+)@")).build()).build());
        return builder.build();
    }

    public TokenFilter customStemmerFilter(String name, Stemmer language) {
        TokenFilter.Builder builder = new TokenFilter.Builder();
        builder.name(name);
        builder.definition(new TokenFilterDefinition.Builder().stemmer(new StemmerTokenFilter.Builder().language(language.id()).build()).build());
        return builder.build();
    }

    public TokenFilter customStopFilter(String name, Language language) {
        TokenFilter.Builder builder = new TokenFilter.Builder();
        builder.name(name);
        builder.definition(new TokenFilterDefinition.Builder().stop(new StopTokenFilter.Builder().stopwords("_" + language.id() + "_").build()).build());
        return builder.build();
    }

    public Map<String, Object> getObjectMapping(Class<?> annotatedType, MappingListener listener) {
        Map<String, Object> typeMapping = new TreeMap<>();
        $mappings_fields(null, annotatedType, typeMapping, Collections.emptyList(), Collections.emptyList(), listener);
        Map<String, Object> mappings = new TreeMap<>();
        mappings.put(PROPERTIES, typeMapping);
        return mappings;
    }

    protected void $mappings_fields(Language language, Class<?> annotatedType, Map<String, Object> typeMapping, List<String> prefixList, List<String> actualNestingList, MappingListener listener) {
        List<java.lang.reflect.Field> list = new ArrayList<>();
        Class<?> _annotatedType = annotatedType;
        while (!ElasticDocument.class.equals(_annotatedType) && !Object.class.equals(_annotatedType)) {
            list.addAll(Arrays.asList(_annotatedType.getDeclaredFields()));
            _annotatedType = _annotatedType.getSuperclass();
        }
        list//
                .stream().filter(f -> !Modifier.isTransient(f.getModifiers()))//
                .filter(f -> !Modifier.isStatic(f.getModifiers()))//
                .filter(f -> f.getAnnotation(JsonIgnore.class) == null)//
                .filter(f -> f.getAnnotation(Ignore.class) == null)//
                .forEach(field -> $mappings_field(language, typeMapping, prefixList, actualNestingList, field, listener));
    }

    protected void $mappings_field(Language language, Map<String, Object> mapping, List<String> prefixList, List<String> actualNestingList, java.lang.reflect.Field reflectField, MappingListener listener) {
        String msgPrefix = "field: " + (prefixList.isEmpty() ? "" : "[" + prefixList + "] ") + (actualNestingList.isEmpty() ? "" : "<" + actualNestingList + "> ") + reflectField;
        LOGGER.trace(msgPrefix);
        LOGGER_DETAILS.debug(msgPrefix);

        JsonIgnore jsonIgnore = reflectField.getAnnotation(JsonIgnore.class);
        Ignore ignore = reflectField.getAnnotation(Ignore.class);
        OnlySave onlySave = reflectField.getAnnotation(OnlySave.class);
        Field field = reflectField.getAnnotation(Field.class);
        JsonUnwrapped jsonUnwrapped = reflectField.getAnnotation(JsonUnwrapped.class);
        NestedField nestedField = reflectField.getAnnotation(NestedField.class);

        {
            if (jsonIgnore != null) LOGGER_DETAILS.debug("   jsonIgnore=", jsonIgnore);
            if (ignore != null) LOGGER_DETAILS.debug("   ignore=", ignore);
            if (onlySave != null) LOGGER_DETAILS.debug("   onlySave=", onlySave);
            if (field != null) LOGGER_DETAILS.debug("   field=", field);
            if (jsonUnwrapped != null) LOGGER_DETAILS.debug("   jsonUnwrapped=", jsonUnwrapped);
            if (nestedField != null) LOGGER_DETAILS.debug("   nestedField=", nestedField);
        }

        String exceptionErrorMsgPrefix = "\n\t" + msgPrefix + "\n\t\t" + "error: ";
        {
            if ((ignore != null || jsonIgnore != null) && (onlySave != null && field != null && jsonUnwrapped != null && nestedField != null)) {
                throw new IllegalArgumentException(exceptionErrorMsgPrefix + "(ignore != null || jsonIgnore != null) && (onlySave != null && field != null && jsonUnwrapped != null && nestedField != null)");
            }
            if ((ignore == null || jsonIgnore == null) && onlySave == null && field == null && jsonUnwrapped == null && nestedField == null) {
                throw new IllegalArgumentException(exceptionErrorMsgPrefix + "(ignore == null || jsonIgnore == null) && onlySave == null && field == null && jsonUnwrapped == null && nestedField == null");
            }
            if (onlySave != null && field != null) {
                throw new IllegalArgumentException(exceptionErrorMsgPrefix + "onlySave != null && field != null");
            }
            if (onlySave != null && jsonUnwrapped != null) {
                throw new IllegalArgumentException(exceptionErrorMsgPrefix + "onlySave != null && jsonUnwrapped != null");
            }
            if (onlySave != null && nestedField != null) {
                throw new IllegalArgumentException(exceptionErrorMsgPrefix + "onlySave != null && nestedField != null");
            }
            if (field != null && jsonUnwrapped != null) {
                throw new IllegalArgumentException(exceptionErrorMsgPrefix + "field != null && jsonUnwrapped != null");
            }
            if (field != null && nestedField != null) {
                throw new IllegalArgumentException(exceptionErrorMsgPrefix + "field != null && nestedField != null");
            }
            // @JsonUnwrapped op Collection werkt niet
            if (jsonUnwrapped != null && Collection.class.isAssignableFrom(reflectField.getType())) {
                throw new IllegalArgumentException(exceptionErrorMsgPrefix + "jsonUnwrapped != null && reflectField instanceof Collection");
            }
            if (nestedField != null && Collection.class.isAssignableFrom(reflectField.getType()) && nestedField.type() == Class.class) {
                throw new IllegalArgumentException(exceptionErrorMsgPrefix + "geef nestedField.type op");
            }
        }

        if (jsonIgnore != null) {
            //
        } else if (ignore != null) {
            //
        } else if (onlySave != null) {
            $mappings_field_onlySave(mapping, prefixList, actualNestingList, reflectField, onlySave, listener);
        } else if (field != null) {
            $mappings_field_field(language, mapping, prefixList, actualNestingList, reflectField, field, listener);
        } else if (jsonUnwrapped != null) {
            $mappings_field_jsonUnwrapped(language, mapping, prefixList, actualNestingList, reflectField, jsonUnwrapped, nestedField, listener);
        } else if (nestedField != null) {
            $mappings_field_nestedField(language, mapping, prefixList, actualNestingList, reflectField, nestedField, listener);
        } else {
            throw new IllegalArgumentException(exceptionErrorMsgPrefix + "incomplete switch, use OnlySave");
        }
    }

    protected void $mappings_field_onlySave(Map<String, Object> mapping, List<String> prefixList, @SuppressWarnings("unused") List<String> actualNestingList, java.lang.reflect.Field reflectField, OnlySave onlySave,
                                            @SuppressWarnings("unused") MappingListener listener) {
        // https://www.elastic.co/guide/en/elasticsearch/reference/current/enabled.html
        String fullName = prefixList.stream().collect(Collectors.joining()) + (StringUtils.isBlank(onlySaveName(onlySave)) ? reflectField.getName() : onlySaveName(onlySave));
        Map<String, Object> fieldMapping = new TreeMap<>();
        fieldMapping.put(TYPE, FieldType.OBJECT.id());
        fieldMapping.put(ENABLED, Boolean.FALSE);
        mapping.put(fullName, fieldMapping);
    }

    protected void $mappings_field_field(Language language, Map<String, Object> mapping, List<String> prefixList, List<String> actualNestingList, java.lang.reflect.Field reflectField, Field field, MappingListener listener) {
        FieldType defaultFieldType = $defaultFieldType(reflectField);
        FieldMapping fieldMapping = $mappings_field(reflectField, language, defaultFieldType, field);
        String fieldname = StringUtils.isBlank(field.name()) ? reflectField.getName() : field.name();
        String fullName = prefixList.stream().collect(Collectors.joining()) + fieldname;
        String fullNameForListener = actualNestingList.stream().collect(Collectors.joining()) + fieldname;
        mapping.put(fullName, fieldMapping.fieldMapping);
        if (listener != null) {
            listener.map(fullNameForListener, field, fieldMapping);
        }
        if (reflectField.getAnnotation(FieldExtra.class) != null) {
            Field[] fieldExtraFields = reflectField.getAnnotation(FieldExtra.class).value();
            if (fieldExtraFields != null && fieldExtraFields.length > 0) {
                Map<String, Object> extraFields = new TreeMap<>();
                fieldMapping.put(FIELDS, extraFields);
                Arrays.stream(fieldExtraFields).forEach(fieldExtraField -> {
                    extraFields.put(nestedFieldName(fieldExtraField), $mappings_field(reflectField, fieldMapping.language, fieldMapping.fieldType, fieldExtraField).fieldMapping);
                    if (listener != null) {
                        listener.map(fullNameForListener + "." + nestedFieldName(fieldExtraField), fieldExtraField, fieldMapping);
                    }
                });
            }
        }
    }

    protected void $mappings_field_jsonUnwrapped(Language language, Map<String, Object> mapping, List<String> prefixList, List<String> actualNestingList, java.lang.reflect.Field reflectField, JsonUnwrapped jsonUnwrapped, NestedField nestedField,
                                                 MappingListener listener) {
        String exceptionErrorMsgPrefix = "\n\t" + "field: " + (prefixList.stream().collect(Collectors.joining()) + " " + reflectField).trim() + "\n\t\t" + "error: ";
        if (actualNestingList.size() > 25) {
            throw new IllegalArgumentException(exceptionErrorMsgPrefix + "depth > 25: " + actualNestingList);
        }
        if (StringUtils.isNotBlank(jsonUnwrapped.suffix())) {
            throw new IllegalArgumentException(exceptionErrorMsgPrefix + "suffix unsupported");
        }
        String nestedPrefix = jsonUnwrapped.prefix();
        if (StringUtils.isBlank(nestedPrefix)) {
            nestedPrefix = "";
            LOGGER_DETAILS.warn("field: " + (prefixList.stream().collect(Collectors.joining()) + " " + reflectField).trim() + ": JsonUnwrapped.nestedPrefix=\"\" is supported but dangerous");
            // throw new IllegalArgumentException(exceptionErrorMsgPrefix + "prefix verplicht");
        }

        if (nestedField != null) {
            Language nestedFieldLanguage = nestedFieldLanguage(nestedField);
            if (nestedFieldLanguage != null && nestedFieldLanguage != Language.uninitialized) {
                language = nestedFieldLanguage;
            }
        }

        $mappings_fields(language, reflectField.getType(), mapping, join(prefixList, nestedPrefix), join(actualNestingList, nestedPrefix), listener);
    }

    public List<String> join(List<String> list, String element) {
        List<String> merged = new ArrayList<>(list);
        merged.add(element);
        return Collections.unmodifiableList(merged);
    }

    protected void $mappings_field_nestedField(Language language, Map<String, Object> mapping, List<String> prefixList, List<String> actualNestingList, java.lang.reflect.Field reflectField, NestedField nestedField, MappingListener listener) {
        Language nestedFieldLanguage = nestedFieldLanguage(nestedField);
        if (nestedFieldLanguage != null && nestedFieldLanguage != Language.uninitialized) {
            language = nestedFieldLanguage;
        }
        // if (nestedField.nested()) {
        // co.elastic.clients.elasticsearch._types.analysis.Analyzer mappings = new TreeMap<>();
        // mappings.put(TYPE, "nested"/* FieldType.NESTED.id() */);
        // mapping.put(reflectField.getName(), mappings);
        // } else {
        Map<String, Object> typeMapping = new TreeMap<>();
        $mappings_fields(language, //
                nestedField.type() != Class.class ? nestedField.type() : reflectField.getType(), //
                typeMapping, //
                prefixList, //
                join(actualNestingList, reflectField.getName() + "."), //
                listener);
        Map<String, Object> mappings = new TreeMap<>();
        mappings.put(PROPERTIES, typeMapping);
        if (nestedField.nested()) {
            mappings.put(TYPE, "nested"/* FieldType.NESTED.id() */);
        }
        mapping.put(reflectField.getName(), mappings);
        // }
    }

    public String nestedFieldName(Field nestedField) {
        return StringUtils.isBlank(nestedField.value()) ? nestedField.name() : nestedField.value();
    }

    public Language nestedFieldLanguage(NestedField nestedField) {
        if (nestedField.language() != Language.uninitialized) return nestedField.language();
        if (nestedField.value() != Language.uninitialized) return nestedField.value();
        return Language.uninitialized;
    }

    public String onlySaveName(OnlySave onlySave) {
        return StringUtils.isBlank(onlySave.value()) ? onlySave.name() : onlySave.value();
    }

    protected FieldMapping $mappings_field(java.lang.reflect.Field reflectField, Language language, FieldType fieldType, Field field) {
        String log = "\t" + language + " / " + fieldType + " / " + field;
        LOGGER.trace(log);
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

    public TokenFilter customToSpaceFilter(String name) {
        TokenFilter.Builder builder = new TokenFilter.Builder();
        builder.name(name);
        builder.definition(new TokenFilterDefinition.Builder().patternReplace(new PatternReplaceTokenFilter.Builder().pattern("[" + ".,!?" + "]").replacement(" ").build()).build());
        return builder.build();
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customWordDelimiterGraphAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.keyword.id()//
        ).filter(Arrays.asList(//
                Filter.word_delimiter_graph.id()//
        )).build()).build();

    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customFilenameAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.keyword.id()//
        ).filter(Arrays.asList(//
                Filter.word_delimiter_graph.id()//
                , Filter.asciifolding.id()//
                , Filter.lowercase.id()//
        )).build()).build();
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customWhitespaceAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.whitespace.id()//
        ).filter(Arrays.asList(//
                Filter.lowercase.id()//
        )).build()).build();
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customNameKeepTogetherAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.keyword.id()//
        ).filter(Arrays.asList(//
                Filter.asciifolding.id()//
                , Filter.lowercase.id()//
        )).build()).build();
    }

    public TokenFilter customRemoveSpaceFilter(String name) {
        TokenFilter.Builder builder = new TokenFilter.Builder();
        builder.name(name);
        builder.definition(new TokenFilterDefinition.Builder().patternReplace(new PatternReplaceTokenFilter.Builder().pattern("[" + " " + "]").replacement("").build()).build());
        return builder.build();
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customSortableAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.keyword.id()//
        ).filter(Arrays.asList(//
                Filter.asciifolding.id()//
                , Filters.CUSTOM_REMOVE_SPACE_FILTER//
                , Filter.uppercase.id()//
        )).build()).build();
    }

    public TokenFilter customOnlyKeepAlphaFilter(String name) {
        TokenFilter.Builder builder = new TokenFilter.Builder();
        builder.name(name);
        builder.definition(new TokenFilterDefinition.Builder().patternReplace(new PatternReplaceTokenFilter.Builder().pattern("[^A-Za-z]").replacement("").build()).build());
        return builder.build();
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customSortableOnlyAlphaAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.keyword.id()//
        ).filter(Arrays.asList(//
                Filter.asciifolding.id()//
                , Filter.uppercase.id()//
                , Filters.CUSTOM_ONLY_KEEP_ALPHA_FILTER//
        )).build()).build();
    }

    public TokenFilter customOnlyKeepAlphaNumericFilter(String name) {
        TokenFilter.Builder builder = new TokenFilter.Builder();
        builder.name(name);
        builder.definition(new TokenFilterDefinition.Builder().patternReplace(new PatternReplaceTokenFilter.Builder().pattern("[^A-Za-z0-9]").replacement("").build()).build());
        return builder.build();
    }

    public TokenFilter customOnlyKeepExtendedAlphaNumericFilter(String name) {
        TokenFilter.Builder builder = new TokenFilter.Builder();
        builder.name(name);
        builder.definition(new TokenFilterDefinition.Builder().patternReplace(new PatternReplaceTokenFilter.Builder().pattern("[^\\p{L}\\p{Nd}]").replacement("").build()).build());
        return builder.build();
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customSortableOnlyAlphaNumericAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.keyword.id()//
        ).filter(Arrays.asList(//
                Filter.asciifolding.id()//
                , Filter.uppercase.id()//
                , Filters.CUSTOM_ONLY_KEEP_ALPHANUMERIC_FILTER//
        )).build()).build();
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customSortableExtendedAlphaNumericAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.keyword.id()//
        ).filter(Arrays.asList(//
                Filter.asciifolding.id()//
                , Filter.uppercase.id()//
                , Filters.CUSTOM_ONLY_KEEP_EXTENDED_ALPHANUMERIC_FILTER//
        )).build()).build();
    }

    @Value("${elasticCustomizer.index.highlightMaxAnalyzedOffset:1000000}") // 10_000_000
    public Integer highlightMaxAnalyzedOffset = 1_000_000;

    @Value("${elasticCustomizer.index.maxClauses:1024}") // 1024
    public Integer maxClauses = 1024;

    @Value("${elasticCustomizer.index.maxResultWindow:10000}") // 100_000
    public Integer maxResultWindow = 10_000;

    @Value("${elasticCustomizer.cluster.searchMaxBuckets:10000}") // 100_000
    public Integer searchMaxBuckets = 10_000;

    @Value("${elasticCustomizer.trackTotalHits:10000}") // 10_000
    public Integer trackTotalHits = 10_000;

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

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customAnyLanguageAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.standard.id()//
        ).filter(Arrays.asList(//
                Filter.apostrophe.id()//
                , Filter.asciifolding.id()//
                , Filter.lowercase.id()//
        )).build()).build();
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customFoldedLowercaseTokensAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.standard.id()//
        ).filter(Arrays.asList(//
                Filter.asciifolding.id()//
                , Filter.lowercase.id()//
        )).build()).build();
    }

    public static final List<Analyzer> ANALYZER_EXCLUDE_TRYOUTS = Arrays.asList(//
            Analyzer.language_japanese, //
            Analyzer.language_chinese, //
            Analyzer.language_korean, //
            Analyzer.language_ukrainian, //
            Analyzer.language_polish//
    );

    public TokenFilter customFrenchElisionFilter(String name) {
        TokenFilter.Builder builder = new TokenFilter.Builder();
        builder.name(name);
        builder.definition(new TokenFilterDefinition.Builder().elision(new ElisionTokenFilter.Builder().articlesCase(true).articles("l", "m", "t", "qu", "n", "s", "j", "d", "c", "jusqu", "quoiqu", "lorsqu", "puisqu").build()).build());
        return builder.build();
    }

    public co.elastic.clients.elasticsearch._types.analysis.Analyzer customFrenchLanguageAnalyzer() {
        return new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder().custom(new CustomAnalyzer.Builder().tokenizer(//
                Tokenizer.standard.id()//
        ).filter(Arrays.asList(//
                Filters.CUSTOM_FRENCH_ELISION_FILTER, //
                Filter.lowercase.id(), //
                Filter.asciifolding.id(), //
                Filters.CUSTOM_FRENCH_STOP_FILTER, //
                Filters.CUSTOM_FRENCH_STEMMER_FILTER//
        )).build()).build();
    }

    public Integer getTrackTotalHits() {
        return this.trackTotalHits;
    }

    public void setTrackTotalHits(Integer trackTotalHits) {
        this.trackTotalHits = trackTotalHits;
    }

    /**
     * in milliseconds
     */
    @Value("${elasticCustomizer.client.connection.timeout:1000}") // 1_000
    public Integer connectionTimeout = 1_000;

    /**
     * in milliseconds
     */
    @Value("${elasticCustomizer.client.connection.socketTimeout:30000}") // 30_000
    public Integer socketTimeout = 30_000;

    public Integer getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getSocketTimeout() {
        return this.socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public Integer getMaxClauses() {
        return this.maxClauses;
    }

    public void setMaxClauses(Integer maxClauses) {
        this.maxClauses = maxClauses;
    }

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
}
