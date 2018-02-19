package org.jhaws.common.ldap.tests.mock;

import org.jhaws.common.ldap.tests.dao.PersoonDao;

/**
 * @author Jurgen
 */
public class PersoonDaoMock extends PersoonDao {

    private static final long serialVersionUID = -6670301193215892963L;

    /**
     * Instantieer een nieuwe PersoonDaoMock
     * 
     * @param b
     */
    public PersoonDaoMock() {
        this.setLdapOperations(LdapTemplateMock.singleton());
    }
}
