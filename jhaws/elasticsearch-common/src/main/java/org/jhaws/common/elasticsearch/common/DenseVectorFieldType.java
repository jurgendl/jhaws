package org.jhaws.common.elasticsearch.common;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

@Documented
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
public @interface DenseVectorFieldType {
	@AliasFor("value")
	int dims() default -1;

	int value() default -1;
}
