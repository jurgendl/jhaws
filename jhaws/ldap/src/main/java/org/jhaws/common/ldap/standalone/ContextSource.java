package org.jhaws.common.ldap.standalone;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.directory.InitialDirContext;

/**
 * @author Jurgen
 */
public class ContextSource {
    /** field */
    private InitialDirContext context;

    /** field */
    private Properties baseEnvironmentProperties = new Properties();

    /** field */
    private String base;

    /** field */
    private String password;

    /** field */
    private String url;

    /** field */
    private String userName;

    /**
     * Creates a new ContextSource object.
     */
    public ContextSource() {
        super();
    }

    /**
     * gets context
     * 
     * @return Returns the context.
     */
    public final InitialDirContext getContext() {
        return this.context;
    }

    /**
     * maakt context aan
     * 
     * @return InitialDirContext
     */
    public final InitialDirContext init() {
        final Properties ldapEnvironment = new Properties();

        ldapEnvironment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory"); //$NON-NLS-1$
        ldapEnvironment.setProperty(Context.PROVIDER_URL, this.url + "/" + this.base.replace(" ", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        if (this.userName != null) {
            ldapEnvironment.setProperty(Context.SECURITY_PRINCIPAL, this.userName);
            ldapEnvironment.setProperty(Context.SECURITY_CREDENTIALS, this.password);
        }

        if (this.baseEnvironmentProperties.getProperty("ldapSecurityProtocol") != null) { //$NON-NLS-1$
            // eg 'ssl'
            ldapEnvironment.setProperty(Context.SECURITY_PROTOCOL, this.baseEnvironmentProperties.getProperty("ldapSecurityProtocol")); //$NON-NLS-1$
        }

        if (this.baseEnvironmentProperties.getProperty("ldapSecurity") != null) { //$NON-NLS-1$
            // eg 'none', 'simple', 'strong'
            ldapEnvironment.setProperty(Context.SECURITY_AUTHENTICATION, this.baseEnvironmentProperties.getProperty("ldapSecurity")); //$NON-NLS-1$
        }

        if (this.baseEnvironmentProperties.getProperty("ldapVersion") != null) { //$NON-NLS-1$
            // eg '3'
            ldapEnvironment.setProperty("java.naming.ldap.version", this.baseEnvironmentProperties.getProperty("ldapVersion")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        try {
            this.context = new InitialDirContext(ldapEnvironment);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.context;
    }

    /**
     * sets base
     * 
     * @param base The base to set.
     */
    public final void setBase(String base) {
        this.base = base;
    }

    /**
     * sets baseEnvironmentProperties
     * 
     * @param baseEnvironmentProperties The baseEnvironmentProperties to set.
     */
    public final void setBaseEnvironmentProperties(Properties baseEnvironmentProperties) {
        this.baseEnvironmentProperties = baseEnvironmentProperties;
    }

    /**
     * sets password
     * 
     * @param password The password to set.
     */
    public final void setPassword(String password) {
        this.password = password;
    }

    /**
     * sets url
     * 
     * @param url The url to set.
     */
    public final void setUrl(String url) {
        this.url = url;
    }

    /**
     * sets userName
     * 
     * @param name The userName to set.
     */
    public final void setUserName(String name) {
        this.userName = name;
    }
}
