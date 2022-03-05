package org.jhaws.common.elasticsearch.common;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

// https://www.elastic.co/guide/en/elasticsearch/reference/current/enabled.html
/**
 * veld wordt enkel bewaard en niet geindexeerd, heeft voorrang en maakt andere annotatie config nutteloos
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface OnlySave {
    @AliasFor("value")
    String name() default "";

    String value() default "";
}
