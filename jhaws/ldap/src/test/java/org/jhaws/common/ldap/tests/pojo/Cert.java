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
 * @author Jurgen
 */
public class Cert {
    public static Certificate certificate(byte[] cert) throws CertificateException {
        ByteArrayInputStream fis = new ByteArrayInputStream(cert);
        BufferedInputStream bis = new BufferedInputStream(fis);
        CertificateFactory cf = CertificateFactory.getInstance("X.509"); //$NON-NLS-1$

        return cf.generateCertificate(bis);
    }

    public static X509CRL crl(byte[] crl) throws CertificateException, CRLException, IOException {
        if (crl == null) {
            return null;
        }

        try (ByteArrayInputStream fis = new ByteArrayInputStream(crl); BufferedInputStream bis = new BufferedInputStream(fis)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509"); //$NON-NLS-1$
            X509CRL c = (X509CRL) cf.generateCRL(bis);
            bis.close();

            return c;
        }
    }
}
