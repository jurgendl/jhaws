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
@LdapClass(autoFields = true, objectClass = "person")
public class Person implements Serializable, Comparable<Person> {
    /** serialVersionUID */
    private static final long serialVersionUID = -7958520557674896804L;

    /** field */
    private String c;

    /** field */
    private String cn;

    /** field */
    private String givenName;

    /** field */
    private String serialNumber;

    /** field */
    private String sn;

    /** field */
    @LdapKey
    private String uid;

    /** field */
    private String[] objectClass;

    /** field */
    private byte[] userCertificate;

    /**
     * Creates a new Person object.
     */
    public Person() {
        super();
    }

    /**
     * Setter voor c
     * 
     * @param c The c to set.
     */
    public void setC(String c) {
        this.c = c;
    }

    /**
     * Getter voor c
     * 
     * @return Returns the c.
     */
    public String getC() {
        return this.c;
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
     * Setter voor cn
     * 
     * @param cn The cn to set.
     */
    public void setCn(String cn) {
        this.cn = cn;
    }

    /**
     * Getter voor cn
     * 
     * @return Returns the cn.
     */
    public String getCn() {
        return this.cn;
    }

    /**
     * Setter voor givenName
     * 
     * @param givenName The givenName to set.
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * Getter voor givenName
     * 
     * @return Returns the givenName.
     */
    public String getGivenName() {
        return this.givenName;
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
     * Setter voor serialNumber
     * 
     * @param serialNumber The serialNumber to set.
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Getter voor serialNumber
     * 
     * @return Returns the serialNumber.
     */
    public String getSerialNumber() {
        return this.serialNumber;
    }

    /**
     * Setter voor sn
     * 
     * @param sn The sn to set.
     */
    public void setSn(String sn) {
        this.sn = sn;
    }

    /**
     * Getter voor sn
     * 
     * @return Returns the sn.
     */
    public String getSn() {
        return this.sn;
    }

    /**
     * Setter voor uid
     * 
     * @param uid The uid to set.
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Getter voor uid
     * 
     * @return Returns the uid.
     */
    public String getUid() {
        return this.uid;
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
    public int compareTo(Person o) {
        return this.uid.compareTo(o.uid);
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
        return new ToStringBuilder(this)
                .append("c", this.c).append("uid", this.uid).append("sn", this.sn).append("serialNumber", this.serialNumber).append("givenName", this.givenName).append("cn", this.cn).append("userCertificate", (userCertificate != null) && (userCertificate.length > 0)).append("objectClass", this.objectClass).toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
    }
}
