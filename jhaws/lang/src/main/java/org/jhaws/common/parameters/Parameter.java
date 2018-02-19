package org.jhaws.common.parameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {
    String value() default "";

    String prefix() default "-";

    ParameterOption[] options() default {};

    int min() default -1;

    int max() default -1;

    Class<?> type() default Object.class;

    boolean secure() default false;
}
