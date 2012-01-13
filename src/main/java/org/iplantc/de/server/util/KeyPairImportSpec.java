package org.iplantc.de.server.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

/**
 * Specifications for importing a key pair from a pair of files in DER format.
 * 
 * @author Dennis Roberts
 */
public class KeyPairImportSpec {
    /**
     * The prefix to use for all property names.
     */
    private static final String PROPERTY_NAME_PREFIX = "org.iplantc.keystore.import"; //$NON-NLS-1$

    /**
     * The key store entry alias.
     */
    private String alias;

    /**
     * The public key encryption algorithm.
     */
    private String algorithm;

    /**
     * The path to the file containing the private key (must be in DER format).
     */
    private String keyFile;

    /**
     * The certificate type.
     */
    private String certType;

    /**
     * The path to the file containing the certificate chain (must be in DER format).
     */
    private String certFile;

    /**
     * The password to use to protect the key.
     */
    private char[] password;

    /**
     * Initializes a new keystore entry import specification from the given set of properties and alias.
     * 
     * @param properties the set of properties.
     * @param entryAlias the keystore entry alias.
     */
    public KeyPairImportSpec(Properties properties, String entryAlias) {
        alias = entryAlias;
        algorithm = extractProperty(properties, "algorithm"); //$NON-NLS-1$
        keyFile = extractProperty(properties, "keyFile"); //$NON-NLS-1$
        certType = extractProperty(properties, "certType"); //$NON-NLS-1$
        certFile = extractProperty(properties, "certFile"); //$NON-NLS-1$
        password = extractProperty(properties, "password").toCharArray(); //$NON-NLS-1$
    }

    /**
     * @return the key algorithm.
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * @return the path to the file containing the key.
     */
    public String getKeyFile() {
        return keyFile;
    }

    /**
     * @return the certificate type.
     */
    public String getCertType() {
        return certType;
    }

    /**
     * @return the path to the file containing the certificate.
     */
    public String getCertFile() {
        return certFile;
    }

    /**
     * @return the password used to protect the private key in the keystore.
     */
    public char[] getPassword() {
        return password;
    }

    /**
     * Extracts a property from the given set of properties.
     * 
     * @param properties the set of properties.
     * @param shortPropertyName the abbreviated property name.
     * @return the property value.
     * @throws RuntimeException if a property is missing.
     */
    private String extractProperty(Properties properties, String shortPropertyName)
            throws RuntimeException {
        String fullPropertyName = PROPERTY_NAME_PREFIX + "." + alias + "." + shortPropertyName; //$NON-NLS-1$ //$NON-NLS-2$
        Object propertyValue = properties.get(fullPropertyName);
        if (propertyValue == null) {
            throw new RuntimeException("missing required property: " + fullPropertyName); //$NON-NLS-1$
        }
        return propertyValue.toString();
    }

    /**
     * Generates the keystore entry for this key pair import specification.
     * 
     * @return the keystore entry.
     */
    public KeyStore.Entry generateKeystoreEntry() {
        try {
            PrivateKey privateKey = importPrivateKey();
            X509Certificate[] certs = importCertificates();
            return new KeyStore.PrivateKeyEntry(privateKey, certs);
        } catch (Exception e) {
            throw new RuntimeException("unable to import " + alias, e); //$NON-NLS-1$
        }
    }

    /**
     * Imports a private key.
     * 
     * @return the private key.
     * @throws IOException if the file can't be read.
     * @throws GeneralSecurityException if the algorithm is unknown or the key is invalid.
     */
    private PrivateKey importPrivateKey() throws IOException, GeneralSecurityException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] key = IOUtils.toByteArray(new FileInputStream(keyFile));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * Imports a certificate chain.
     * 
     * @return the certificate chain.
     * @throws IOException if the file can't be read.
     * @throws GeneralSecurityException if the certificate type is unknown or the input is invalid.
     */
    private X509Certificate[] importCertificates() throws IOException, GeneralSecurityException {
        byte[] certSpec = IOUtils.toByteArray(new FileInputStream(certFile));
        return certSpecToCertChain(certSpec);
    }

    /**
     * Converts a certificate specification to an array of certificates.
     * 
     * @param certSpec the certificate specification.
     * @return the array of certificates.
     * @throws GeneralSecurityException if the certificate chain is invalid.
     */
    @SuppressWarnings("unchecked")
    private X509Certificate[] certSpecToCertChain(byte[] certSpec) throws GeneralSecurityException {
        CertificateFactory certFactory = CertificateFactory.getInstance(certType);
        ByteArrayInputStream in = new ByteArrayInputStream(certSpec);
        Collection<X509Certificate> certCollection = (Collection<X509Certificate>)certFactory
                .generateCertificates(in);
        return certCollectionToCertArray(certFactory, certCollection, certSpec);
    }

    /**
     * Converts a collection of certificates to an array of certificates. Apparently, the certificate
     * factory doesn't import a single certificate correctly, so a certificate chain length of one is a
     * special case.
     * 
     * @param certFactory the certificate factory.
     * @param certCollection the collection of certificates.
     * @param certSpec the certificate specification.
     * @return the array of certificates.
     * @throws CertificateException if the certificate specification is invalid.
     */
    private X509Certificate[] certCollectionToCertArray(CertificateFactory certFactory,
            Collection<X509Certificate> certCollection, byte[] certSpec) throws CertificateException {
        X509Certificate[] certs = new X509Certificate[certCollection.size()];
        if (certs.length == 1) {
            ByteArrayInputStream in = new ByteArrayInputStream(certSpec);
            certs[0] = (X509Certificate)certFactory.generateCertificate(in);
        } else {
            certs = certCollection.toArray(new X509Certificate[0]);
        }
        return certs;
    }
}
