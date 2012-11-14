package org.jhaws.common.ldap.tests.mock;

import org.jhaws.common.ldap.tests.dao.PersoonDao;

/**
 * na
 * 
 * @author Jurgen De Landsheer
 */
public class PersoonDaoMock extends PersoonDao {
    /** serialVersionUID */
    private static final long serialVersionUID = -6670301193215892963L;

    /**
     * Instantieer een nieuwe PersoonDaoMock
     * 
     * @param b
     */
    public PersoonDaoMock() {
        setLdapOperations(LdapTemplateMock.singleton());
    }
}
