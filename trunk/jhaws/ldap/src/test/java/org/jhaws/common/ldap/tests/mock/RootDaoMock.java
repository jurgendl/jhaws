package org.jhaws.common.ldap.tests.mock;

import org.jhaws.common.ldap.tests.dao.RootDao;

/**
 * @author Jurgen De Landsheer
 */
public class RootDaoMock extends RootDao {
    /** serialVersionUID */
    private static final long serialVersionUID = 431289823507041339L;

    /**
     * Creates a new RootDaoMock object.
     * 
     * @param b na
     */
    public RootDaoMock() {
        this.setLdapOperations(LdapTemplateMock.singleton());
    }
}
