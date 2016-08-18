package org.jhaws.common.ldap.tests.local;

import java.util.Properties;

import org.jhaws.common.ldap.standalone.ContextSource;

/**
 * @author Jurgen
 */
public class UserDaoMock extends UserDao {

	private static final long serialVersionUID = -8240875516107185780L;

	/**
	 * Creates a new UserDaoMock object.
	 */
	public UserDaoMock() {
		super();

		ContextSource contextSource = new ContextSource();
		Properties baseEnvironmentProperties = new Properties();

		baseEnvironmentProperties.setProperty("ldapVersion", "3"); //$NON-NLS-1$ //$NON-NLS-2$
		contextSource.setUrl("ldap://127.0.0.1:10389"); //$NON-NLS-1$
		contextSource.setBase("ou=system"); //$NON-NLS-1$
		contextSource.setBaseEnvironmentProperties(baseEnvironmentProperties);
		contextSource.setUserName("uid=admin,ou=system"); //$NON-NLS-1$
		contextSource.setPassword("secret"); //$NON-NLS-1$

		contextSource.init();
		this.setContextSource(contextSource);
	}
}
