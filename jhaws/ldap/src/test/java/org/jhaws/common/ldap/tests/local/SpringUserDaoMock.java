package org.jhaws.common.ldap.tests.local;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * @author Jurgen
 */
public class SpringUserDaoMock extends SpringUserDao {

    private static final long serialVersionUID = 647009288639148718L;

    /**
     * Creates a new SpringUserDaoMock object.
     */
    public SpringUserDaoMock() {
        Map<String, Object> env = new HashMap<>();
        env.put("ldapVersion", "3"); //$NON-NLS-1$ //$NON-NLS-2$

        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://127.0.0.1:10389"); //$NON-NLS-1$
        contextSource.setBase("ou=system"); //$NON-NLS-1$
        contextSource.setUserDn("uid=admin,ou=system"); //$NON-NLS-1$
        contextSource.setPassword("secret"); //$NON-NLS-1$
        contextSource.setBaseEnvironmentProperties(env);

        try {
            contextSource.afterPropertiesSet();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.setLdapOperations(new LdapTemplate(contextSource));
    }
}
