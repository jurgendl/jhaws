package org.jhaws.common.ldap.tests.pojo;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;


/**
 * na
 *
 * @author Jurgen De Landsheer
 */
public class Cert {
    /**
     * na
     *
     * @param cert na
     *
     * @return
     *
     * @throws CertificateException
     */
    public static Certificate certificate(byte[] cert) throws CertificateException {
        ByteArrayInputStream fis = new ByteArrayInputStream(cert);
        BufferedInputStream bis = new BufferedInputStream(fis);
        CertificateFactory cf = CertificateFactory.getInstance("X.509"); //$NON-NLS-1$

        return cf.generateCertificate(bis);
    }

    /**
     * na
     *
     * @param crl na
     *
     * @return
     *
     * @throws CertificateException na
     * @throws CRLException
     * @throws IOException
     */
    public static X509CRL crl(byte[] crl) throws CertificateException, CRLException, IOException {
        if (crl == null) {
            return null;
        }

        ByteArrayInputStream fis = new ByteArrayInputStream(crl);
        BufferedInputStream bis = new BufferedInputStream(fis);
        CertificateFactory cf = CertificateFactory.getInstance("X.509"); //$NON-NLS-1$
        X509CRL c = (X509CRL) cf.generateCRL(bis);
        bis.close();

        return c;
    }
}
