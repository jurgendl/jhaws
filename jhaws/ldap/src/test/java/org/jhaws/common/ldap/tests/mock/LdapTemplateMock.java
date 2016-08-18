package org.jhaws.common.ldap.tests.mock;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * @author Jurgen
 */
public class LdapTemplateMock {
	/**
	 * na
	 *
	 * @return
	 */
	public static LdapTemplate singleton() {
		return LdapTemplateMock.singleton;
	}

	/** field */
	private static final LdapTemplate singleton;

	static {
		Map<String, Object> env = new HashMap<>();
		env.put("ldapVersion", "3"); //$NON-NLS-1$ //$NON-NLS-2$

		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl("ldap://ldap.eid.belgium.be:389"); //$NON-NLS-1$
		contextSource.setBase("dc=eid, dc=belgium, dc=be"); //$NON-NLS-1$

		// contextSource.setUserName("");
		// contextSource.setPassword("");
		contextSource.setBaseEnvironmentProperties(env);

		try {
			contextSource.afterPropertiesSet();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		singleton = new LdapTemplate(contextSource);
	}
}
