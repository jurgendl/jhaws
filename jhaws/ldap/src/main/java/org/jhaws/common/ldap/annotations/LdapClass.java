package org.jhaws.common.ldap.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ldap class
 *
 * @author Jurgen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface LdapClass {
    /**
     * find properties via class properties instead of via LdapField annotation (default false)
     *
     * @return boolean
     */
    boolean autoFields() default false;

    /**
     * ordered key array
     *
     * @return LdapKeyValue[]
     */
    LdapKeyValue[] dn() default @LdapKeyValue(key = "", value = "");

    /**
     * processes superclass if it's a @LdapClass itself and merges info
     *
     * @return boolean
     */
    boolean inherited() default false;

    /**
     * wordt gebruikt bij findAll, update en create
     *
     * @return String[]
     */
    String[] objectClass() default "";
}
