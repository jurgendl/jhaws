package org.jhaws.common.ldap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * key/value pair
 *
 * @author Jurgen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface LdapKeyValue {
    /**
     * key
     *
     * @return key
     */
    String key();

    /**
     * value
     *
     * @return value
     */
    String value();
}
