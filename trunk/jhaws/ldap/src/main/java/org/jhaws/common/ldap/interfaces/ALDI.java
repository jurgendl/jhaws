package org.jhaws.common.ldap.interfaces;

import java.io.Serializable;

/**
 * hernoemd naar {@link LdapDAOCommonSuperclass}, behouden voor backwards compatibility
 * 
 * @author jdlandsh
 * 
 * @param <T>
 * 
 * @deprecated
 */
@Deprecated
public abstract class ALDI<T extends Serializable & Comparable<? super T>> extends LdapDAOCommonSuperclass<T> {
    /** serialVersionUID */
    private static final long serialVersionUID = -7411351913012966087L;
}
