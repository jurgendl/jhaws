package org.jhaws.common.elasticsearch.common;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface Field {
    @AliasFor("value")
    String name() default "";

    String value() default "";

    FieldType type() default FieldType.uninitialized;

    /**
     * standard, english, dutch
     * 
     * @see https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-analyzers.html
     */
    Analyzer analyzer() default Analyzer.uninitialized;

    String customAnalyzer() default "";

    Bool store() default Bool.uninitialized;

    Bool fielddata() default Bool.uninitialized;

    Language language() default Language.uninitialized;
}
