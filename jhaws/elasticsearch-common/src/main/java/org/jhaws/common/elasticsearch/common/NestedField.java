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
public @interface NestedField {
    @AliasFor("value")
    Language language() default Language.uninitialized;

    Language value() default Language.uninitialized;

    Class<?> type() default Class.class;

    /** https://www.elastic.co/guide/en/elasticsearch/reference/current/nested.html#nested-fields-array-objects */
    /** https://www.elastic.co/guide/en/elasticsearch/reference/current/search-request-body.html#nested-sorting */
    boolean nested() default false;
}
