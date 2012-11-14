package org.jhaws.common.ldap.tests;

import java.net.URI;
import java.security.Security;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreParameters;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.jhaws.common.ldap.tests.dao.CADao;
import org.jhaws.common.ldap.tests.dao.CADao0;
import org.jhaws.common.ldap.tests.dao.PersoonDao;
import org.jhaws.common.ldap.tests.dao.PersoonDao0;
import org.jhaws.common.ldap.tests.dao.RootDao;
import org.jhaws.common.ldap.tests.dao.RootDao0;
import org.jhaws.common.ldap.tests.mock.CADaoMock;
import org.jhaws.common.ldap.tests.mock.PersoonDaoMock;
import org.jhaws.common.ldap.tests.mock.RootDaoMock;
import org.jhaws.common.ldap.tests.pojo.CA;
import org.jhaws.common.ldap.tests.pojo.Person;
import org.jhaws.common.ldap.tests.pojo.Root;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * test ldap
 * 
 * @author Jurgen De Landsheer
 */
public class Test {
    /**
     * TESTING
     */
    @SuppressWarnings("unused")
    public static void main(final String[] args) {
        try {
            ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml"); //$NON-NLS-1$

            PersoonDao pdao1 = new PersoonDaoMock();
            PersoonDao pdao2 = (PersoonDao) ctx.getBean("persoonDao"); //$NON-NLS-1$
            PersoonDao0 pdao3 = (PersoonDao0) ctx.getBean("persoonDao0"); //$NON-NLS-1$

            System.out.println("classes-1-" + pdao1.findByPrimaryKey("1003059162")); //$NON-NLS-1$ //$NON-NLS-2$
            System.out.println("classes-2-" + pdao2.findByPrimaryKey("1003059162")); //$NON-NLS-1$ //$NON-NLS-2$
            System.out.println("classes-3-" + pdao3.findByPrimaryKey("1003059162")); //$NON-NLS-1$ //$NON-NLS-2$

            if (false) {
                System.exit(0);
            }

            System.out.println(pdao1.findAllBeans().size());
            System.out.println(pdao2.findAllBeans().size());
            System.out.println(pdao3.findAllBeans().size());

            if (true) {
                System.exit(0);
            }

            RootDao rdao1 = new RootDaoMock();
            CADao cdao1 = new CADaoMock();

            RootDao rdao2 = (RootDao) ctx.getBean("rootDao"); //$NON-NLS-1$
            CADao cdao2 = (CADao) ctx.getBean("caDao"); //$NON-NLS-1$

            RootDao0 rdao3 = (RootDao0) ctx.getBean("rootDao0"); //$NON-NLS-1$
            CADao0 cdao3 = (CADao0) ctx.getBean("caDao0"); //$NON-NLS-1$

            if (true) {
                pdao1 = pdao2;
                rdao1 = rdao2;
                cdao1 = cdao2;
            }

            X509Certificate cert = null;
            X509Certificate ROOT_CA_CERT = null;
            X509Certificate OCSP_SERVER_CERT = null;
            System.out.println("validate:" + Test.validate(cert, ROOT_CA_CERT, OCSP_SERVER_CERT));

            Person p = pdao1.findByPrimaryKey("1003059162");
            p.print();

            boolean debug = true;

            if (debug) {
                p.print();
                System.out.println("\n\n\n\n\n\n\n\n");

                X509Certificate x509 = p.getCertificate();
                System.out.println(p.getSerialNumber());
                System.out.println("------------------------------------");
                System.out.println(x509.getBasicConstraints());
                System.out.println(x509.getSigAlgName());
                System.out.println(x509.getSigAlgOID());
                System.out.println(x509.getType());
                System.out.println(x509.getVersion());

                for (String ext : x509.getCriticalExtensionOIDs()) {
                    System.out.println(ext + "=" + new String(x509.getExtensionValue(ext)));
                }

                try {
                    for (String extk : x509.getExtendedKeyUsage()) {
                        System.out.println(extk);
                    }
                } catch (NullPointerException e) {
                    //
                }

                System.out.println(x509.getIssuerDN().getName());
                System.out.println(x509.getIssuerX500Principal().getName());

                for (String ext : x509.getNonCriticalExtensionOIDs()) {
                    System.out.println(ext + "=" + new String(x509.getExtensionValue(ext)));
                }

                System.out.println(x509.getNotAfter());
                System.out.println(x509.getNotBefore());

                RSAPublicKey pk = (RSAPublicKey) x509.getPublicKey();
                System.out.println(pk.getAlgorithm());
                System.out.println(pk.getFormat());
                System.out.println(new String(pk.getEncoded()));
                System.out.println(pk.getModulus());
                System.out.println(pk.getPublicExponent());
                System.out.println(x509.getSigAlgName());
                System.out.println(x509.getSigAlgOID());
                System.out.println(x509.getSerialNumber());
                System.out.println(new String(x509.getSignature()));
                System.out.println(x509.getSubjectDN().getName());
                System.out.println(x509.getSubjectX500Principal().getName());
                System.out.println(x509.getType());
                System.out.println(x509.getVersion());
            }

            Root r = rdao2.findByPrimaryKey("Belgium Root CA");

            // r.print();
            CA c1 = cdao3.findByPrimaryKey("Government CA");
            // c1.print();
            System.out.println(c1.getCrl().isRevoked(p.getCertificate()));
            System.out.println(c1.getDeltaCrl().isRevoked(p.getCertificate()));

            CA c2 = cdao3.findByPrimaryKey("Government CA 2004-1");
            // c2.print();
            System.out.println(c2.getCrl().isRevoked(p.getCertificate()));
            System.out.println(c2.getDeltaCrl().isRevoked(p.getCertificate()));

            CA c3 = cdao3.findByPrimaryKey("Citizen CA");
            // c3.print();
            System.out.println(c3.getCrl().isRevoked(p.getCertificate()));
            System.out.println(c3.getDeltaCrl().isRevoked(p.getCertificate()));

            CA c4 = cdao3.findByPrimaryKey("Citizen CA 2004-1");
            // c4.print();
            System.out.println(c4.getCrl().isRevoked(p.getCertificate()));
            System.out.println(c4.getDeltaCrl().isRevoked(p.getCertificate()));
        } catch (final Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Creates a new validate object.
     * 
     * @param cert na
     * @param ROOT_CA_CERT na
     * @param OCSP_SERVER_CERT na
     * 
     * @return
     */
    @SuppressWarnings({ "nls", "rawtypes", "unchecked" })
    protected static int validate(X509Certificate cert, X509Certificate ROOT_CA_CERT, X509Certificate OCSP_SERVER_CERT) {
        try {
            CertPath cp = null;
            Vector certs = new Vector();
            @SuppressWarnings("unused")
            URI ocspServer = new URI("http://ocsp.eid.belgium.be"); //$NON-NLS-1$

            // load the cert to be checked
            certs.add(cert);

            // init cert path
            CertificateFactory cf = CertificateFactory.getInstance("X509"); //$NON-NLS-1$
            cp = cf.generateCertPath(certs);

            // load the root CA cert for the OCSP server cert
            X509Certificate rootCACert = ROOT_CA_CERT;

            // init trusted certs
            TrustAnchor ta = new TrustAnchor(rootCACert, null);
            Set trustedCertsSet = new HashSet();
            trustedCertsSet.add(ta);

            // init cert store
            Set certSet = new HashSet();
            X509Certificate ocspCert = OCSP_SERVER_CERT;
            certSet.add(ocspCert);

            CertStoreParameters storeParams = new CollectionCertStoreParameters(certSet);
            CertStore store = CertStore.getInstance("Collection", storeParams); //$NON-NLS-1$

            // init PKIX parameters
            PKIXParameters params = null;
            params = new PKIXParameters(trustedCertsSet);
            params.addCertStore(store);

            // enable OCSP
            Security.setProperty("ocsp.enable", "true"); //$NON-NLS-1$ //$NON-NLS-2$

            Security.setProperty("ocsp.responderCertSubjectName", ocspCert.getSubjectX500Principal().getName()); //$NON-NLS-1$

            // perform validation
            CertPathValidator cpv = CertPathValidator.getInstance("PKIX"); //$NON-NLS-1$
            PKIXCertPathValidatorResult cpv_result = (PKIXCertPathValidatorResult) cpv.validate(cp, params);
            X509Certificate trustedCert = cpv_result.getTrustAnchor().getTrustedCert();

            if (trustedCert == null) {
                System.out.println("Trsuted Cert = NULL"); //$NON-NLS-1$
            } else {
                System.out.println("Trusted CA DN = " + trustedCert.getSubjectDN()); //$NON-NLS-1$
            }
        } catch (CertPathValidatorException e) {
            e.printStackTrace();

            return 1;
        } catch (Exception e) {
            e.printStackTrace();

            return -1;
        }

        System.out.println("CERTIFICATE VALIDATION SUCCEEDED"); //$NON-NLS-1$

        return 0;
    }
}
