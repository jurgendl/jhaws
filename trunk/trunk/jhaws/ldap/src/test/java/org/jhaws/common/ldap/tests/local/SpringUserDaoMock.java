package org.jhaws.common.ldap.tests.local;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import java.util.HashMap;
import java.util.Map;

/**
 * na
 * 
 * @author Jurgen De Landsheer
 */
public class SpringUserDaoMock extends SpringUserDao {
    /** serialVersionUID */
    private static final long serialVersionUID = 647009288639148718L;

    /**
     * Creates a new SpringUserDaoMock object.
     */
    @SuppressWarnings("deprecation")
    public SpringUserDaoMock() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("ldapVersion", "3"); //$NON-NLS-1$ //$NON-NLS-2$

        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://127.0.0.1:10389"); //$NON-NLS-1$
        contextSource.setBase("ou=system"); //$NON-NLS-1$
        contextSource.setUserName("uid=admin,ou=system"); //$NON-NLS-1$
        contextSource.setPassword("secret"); //$NON-NLS-1$
        contextSource.setBaseEnvironmentProperties(env);

        try {
            contextSource.afterPropertiesSet();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setLdapOperations(new LdapTemplate(contextSource));
    }
}
