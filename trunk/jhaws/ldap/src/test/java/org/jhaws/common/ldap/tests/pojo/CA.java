package org.jhaws.common.ldap.tests.pojo;

import java.io.IOException;
import java.io.Serializable;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jhaws.common.ldap.annotations.LdapClass;
import org.jhaws.common.ldap.annotations.LdapKey;

/**
 * @author Jurgen
 */
@LdapClass(autoFields = true)
public class CA implements Serializable, Comparable<CA> {
    /** serialVersionUID */
    private static final long serialVersionUID = 98653907059210519L;

    /** field */
    @LdapKey
    private String ou;

    /** field */
    private byte[] certificateRevocationList;

    /** field */
    private byte[] deltaRevocationList;

    /** field */
    private String[] objectClass;

    /** field */
    private byte[] userCertificate;

    /**
     * Creates a new CA object.
     */
    public CA() {
        super();
    }

    /**
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(CA o) {
        return this.ou.compareTo(o.ou);
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws CertificateException na
     */
    public X509Certificate getCertificate() throws CertificateException {
        return (X509Certificate) Cert.certificate(this.getUserCertificate());
    }

    /**
     * Getter voor certificateRevocationList
     * 
     * @return Returns the certificateRevocationList.
     */
    public byte[] getCertificateRevocationList() {
        return this.certificateRevocationList;
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws CertificateException na
     * @throws CRLException na
     * @throws IOException na
     */
    public X509CRL getCrl() throws CertificateException, CRLException, IOException {
        return Cert.crl(this.getCertificateRevocationList());
    }

    /**
     * na
     * 
     * @return
     * 
     * @throws CertificateException na
     * @throws CRLException na
     * @throws IOException na
     */
    public X509CRL getDeltaCrl() throws CertificateException, CRLException, IOException {
        return Cert.crl(this.getDeltaRevocationList());
    }

    /**
     * Getter voor deltaRevocationList
     * 
     * @return Returns the deltaRevocationList.
     */
    public byte[] getDeltaRevocationList() {
        return this.deltaRevocationList;
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
     * Getter voor ou
     * 
     * @return Returns the ou.
     */
    public String getOu() {
        return this.ou;
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
     * na
     */
    public void print() {
        System.out.println(this);

        try {
            System.out.println(this.getCertificate());
        } catch (CertificateException ex) {
            ex.printStackTrace();
        }

        try {
            System.out.println(this.getCrl());
        } catch (CertificateException ex) {
            ex.printStackTrace();
        } catch (CRLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            System.out.println(this.getDeltaCrl());
        } catch (CertificateException ex) {
            ex.printStackTrace();
        } catch (CRLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Setter voor certificateRevocationList
     * 
     * @param certificateRevocationList The certificateRevocationList to set.
     */
    public void setCertificateRevocationList(byte[] certificateRevocationList) {
        this.certificateRevocationList = certificateRevocationList;
    }

    /**
     * Setter voor deltaRevocationList
     * 
     * @param deltaRevocationList The deltaRevocationList to set.
     */
    public void setDeltaRevocationList(byte[] deltaRevocationList) {
        this.deltaRevocationList = deltaRevocationList;
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
     * Setter voor ou
     * 
     * @param ou The ou to set.
     */
    public void setOu(String ou) {
        this.ou = ou;
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
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("objectClass", this.objectClass).append("ou", this.ou).toString(); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
