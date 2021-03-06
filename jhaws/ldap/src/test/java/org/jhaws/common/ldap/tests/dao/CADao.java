package org.jhaws.common.ldap.tests.dao;

import org.jhaws.common.ldap.spring.AbstractSpringLdapDao;
import org.jhaws.common.ldap.tests.pojo.CA;

/**
 * @author Jurgen
 */
public class CADao extends AbstractSpringLdapDao<CA> {

    private static final long serialVersionUID = -6411318567032859524L;

    /**
     * Creates a new CADao object.
     */
    public CADao() {
        super();
    }
}
