package org.jhaws.common.ldap.tests.mock;

import org.jhaws.common.ldap.tests.dao.CADao;

/**
 * @author Jurgen
 */
public class CADaoMock extends CADao {
    /** serialVersionUID */
    private static final long serialVersionUID = 9201760704127683097L;

    /**
     * Creates a new CADaoMock object.
     * 
     * @param b na
     */
    public CADaoMock() {
        this.setLdapOperations(LdapTemplateMock.singleton());
    }
}
