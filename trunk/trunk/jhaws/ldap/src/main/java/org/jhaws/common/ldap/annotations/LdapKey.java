package org.jhaws.common.ldap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ldap fieldname = key
 * 
 * @author Jurgen De Landsheer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface LdapKey {
    /**
     * 
     * key order index, default 0
     * 
     * @return index
     */
    int index() default 0;

    /**
     * fieldname or same as class property when not set
     * 
     * @return fieldname
     */
    String value() default "";
}
