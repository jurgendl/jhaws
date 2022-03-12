package org.jhaws.common.elasticsearch.common;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

// https://www.elastic.co/guide/en/elasticsearch/reference/current/dense-vector.html
@Documented
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
public @interface DenseVector {
	int dims();

	DenseVectorSimilarity similarity() default DenseVectorSimilarity.uninitialized;

	/* index_options.type */
	DenseVectorType type() default DenseVectorType.uninitialized;

	/* index_options.m */
	int m() default 16;

	/* index_options.ef_construction */
	int ef_construction() default 100;
}
