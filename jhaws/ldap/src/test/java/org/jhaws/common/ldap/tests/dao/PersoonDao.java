package org.jhaws.common.ldap.tests.dao;

import org.jhaws.common.ldap.spring.AbstractSpringLdapDao;
import org.jhaws.common.ldap.tests.pojo.Person;

/**
 * @author Jurgen
 */
public class PersoonDao extends AbstractSpringLdapDao<Person> {

    private static final long serialVersionUID = -1960081318189996183L;

    /**
     * Creates a new PersoonDao object.
     */
    public PersoonDao() {
        super();
    }
}
