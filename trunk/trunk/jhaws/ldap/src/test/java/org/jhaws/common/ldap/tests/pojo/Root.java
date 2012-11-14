package org.jhaws.common.ldap.tests.pojo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jhaws.common.ldap.annotations.LdapClass;
import org.jhaws.common.ldap.annotations.LdapKey;

import java.io.Serializable;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * na
 * 
 * @author Jurgen De Landsheer
 */
@LdapClass(autoFields = true)
public class Root implements Serializable, Comparable<Root> {
    /** serialVersionUID */
    private static final long serialVersionUID = -8516563104517026221L;

    /** field */
    @LdapKey
    private String ou;

    /** field */
    private String[] objectClass;

    /** field */
    private byte[] userCertificate;

    /**
     * Creates a new Root object.
     */
    public Root() {
        super();
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws CertificateException na
     */
    public X509Certificate getCertificate() throws CertificateException {
        return (X509Certificate) Cert.certificate(getUserCertificate());
    }

    /**
     * Setter voor objectClass
     * 
     * @param objectClass The objectClass to set.
     */
    public void setObjectClass(String[] objectClass) {
        this.objectClass = objectClass;
    }

    /**
     * Getter voor objectClass
     * 
     * @return Returns the objectClass.
     */
    public String[] getObjectClass() {
        return this.objectClass;
    }

    /**
     * Setter voor ou
     * 
     * @param ou The ou to set.
     */
    public void setOu(String ou) {
        this.ou = ou;
    }

    /**
     * Getter voor ou
     * 
     * @return Returns the ou.
     */
    public String getOu() {
        return this.ou;
    }

    /**
     * Setter voor userCertificate
     * 
     * @param userCertificate The userCertificate to set.
     */
    public void setUserCertificate(byte[] userCertificate) {
        this.userCertificate = userCertificate;
    }

    /**
     * Getter voor userCertificate
     * 
     * @return Returns the userCertificate.
     */
    public byte[] getUserCertificate() {
        return this.userCertificate;
    }

    /**
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Root o) {
        return this.ou.compareTo(o.ou);
    }

    /**
     * na
     */
    public void print() {
        System.out.println(this);

        try {
            System.out.println(getCertificate());
        } catch (CertificateException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("objectClass", this.objectClass).append("ou", this.ou).toString(); //$NON-NLS-1$//$NON-NLS-2$
    }
}
