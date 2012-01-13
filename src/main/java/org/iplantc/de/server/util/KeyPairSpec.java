package org.iplantc.de.server.util;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.Properties;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;

/**
 * Represents a key pair specification, which can be used to generate a public/private key pair. The key
 * pair specification comes from a set of properties that can appear in a properties file. For more
 * information on how to define a key pair specification, see the documentation for
 * org.iplantc.de.server.util.KeystoreGenerator.
 * 
 * @author Dennis Roberts
 */
public class KeyPairSpec {
    {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * The name of the cryptography provider to use when generating certificates.
     */
    private static final String PROVIDER = "BC"; //$NON-NLS-1$

    /**
     * The prefix to use in all property names.
     */
    private static final String PROPERTY_NAME_PREFIX = "org.iplantc.keystore.key"; //$NON-NLS-1$

    /**
     * The number of milliseconds in a day.
     */
    private static final long MILLISECONDS_PER_DAY = 24 * 3600 * 1000;

    /**
     * The alias to use when storing the key in the keystore.
     */
    private String alias;

    /**
     * The hash algorithm to use when generating the key.
     */
    private String algorithm;

    /**
     * The size of the key to generate.
     */
    private int keysize;

    /**
     * The algorithm to use when generating the self-signed certificate.
     */
    private String signatureAlgorithm;

    /**
     * The distinguished name to use in the certificate.
     */
    private String distinguishedName;

    /**
     * The number of days that the certificate is valid.
     */
    private int validity;

    /**
     * The key password to use when storing the key pair in the keystore.
     */
    private String password;

    /**
     * Uses the given set of properties to extract the key pair specification for the key with the given
     * alias.
     * 
     * @param properties the properties to extract the key pair specification from.
     * @param alias the key alias.
     */
    public KeyPairSpec(Properties properties, String alias) {
        extractProperties(properties, alias);
    }

    /**
     * Gets the alias to use when storing the key in the keystore.
     * 
     * @return the alias.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Gets the name of the hash algorithm to use when generating the key.
     * 
     * @return the algorithm name.
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Gets the size of the key to generate.
     * 
     * @return the key size.
     */
    public int getKeysize() {
        return keysize;
    }

    /**
     * Gets the name of the signature algorithm to use when generating the self-signed certificate.
     * 
     * @return the signature algorithm name.
     */
    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    /**
     * Gets the distinguished name to use in the self-signed certificate.
     * 
     * @return the distinguished name.
     */
    public String getDistinguishedName() {
        return distinguishedName;
    }

    /**
     * Gets the number of days that the certificate is valid.
     * 
     * @return the validity period in days.
     */
    public int getValidity() {
        return validity;
    }

    /**
     * Gets the key password to use when storing the key pair in the keystore.
     * 
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Extracts the key pair specification properties for the key with the given alias.
     * 
     * @param properties the properties to extract the specification from.
     * @param keyAlias the key alias.
     * @throws RuntimeException if a required parameter is missing.
     */
    private void extractProperties(Properties properties, String keyAlias) {
        this.alias = keyAlias;
        algorithm = extractProperty(properties, keyAlias, "algorithm"); //$NON-NLS-1$
        keysize = Integer.parseInt(extractProperty(properties, keyAlias, "keysize")); //$NON-NLS-1$
        signatureAlgorithm = extractProperty(properties, keyAlias, "signatureAlgorithm"); //$NON-NLS-1$
        distinguishedName = extractProperty(properties, keyAlias, "distinguishedName"); //$NON-NLS-1$
        validity = Integer.parseInt(extractProperty(properties, keyAlias, "validity")); //$NON-NLS-1$
        password = extractProperty(properties, keyAlias, "password"); //$NON-NLS-1$
    }

    /**
     * Extracts a property from the given set of properties, throwing an exception if the property
     * doesn't exist.
     * 
     * @param properties the set of properties.
     * @param keyAlias the key alias.
     * @param shortPropertyName the abbreviated property name.
     * @return the key value.
     * @throws RuntimeException if the property isn't defined.
     */
    private String extractProperty(Properties properties, String keyAlias, String shortPropertyName) {
        String fullPropertyName = PROPERTY_NAME_PREFIX + "." + keyAlias + "." + shortPropertyName; //$NON-NLS-1$ //$NON-NLS-2$
        Object propertyValue = properties.get(fullPropertyName);
        if (propertyValue == null) {
            throw new RuntimeException("missing required property: " + fullPropertyName); //$NON-NLS-1$
        }
        return propertyValue.toString();
    }

    /**
     * Generates the keystore entry for a new self-signed certificate.
     * 
     * @return the certificate.
     */
    public KeyStore.Entry generateKeystoreEntry() {
        try {
            KeyPair keyPair = generateKeyPair();
            Certificate certificate = generateCertificate(keyPair);
            return new KeyStore.PrivateKeyEntry(keyPair.getPrivate(), new Certificate[] {certificate});
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates the protection parameter object to use when storing the key pair in the keystore.
     * 
     * @return the protection parameter object.
     */
    public KeyStore.ProtectionParameter generateProtectionParameter() {
        return new KeyStore.PasswordProtection(password.toCharArray());
    }

    /**
     * Generates a self-signed certificate for the given key pair.
     * 
     * @param keyPair the key pair.
     * @return the certificate.
     * @throws GeneralSecurityException if the certificate can't be generated.
     */
    private Certificate generateCertificate(KeyPair keyPair) throws GeneralSecurityException {
        X509V3CertificateGenerator certificateGenerator = new X509V3CertificateGenerator();
        certificateGenerator.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        certificateGenerator.setIssuerDN(new X500Principal(distinguishedName));
        certificateGenerator.setNotBefore(new Date(System.currentTimeMillis() - 10000));
        certificateGenerator.setNotAfter(new Date(calculateExpirationTime()));
        certificateGenerator.setSubjectDN(new X500Principal(distinguishedName));
        certificateGenerator.setPublicKey(keyPair.getPublic());
        certificateGenerator.setSignatureAlgorithm(signatureAlgorithm);
        certificateGenerator.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(
                false));
        certificateGenerator.addExtension(X509Extensions.KeyUsage, true, getValidKeyUsage());
        certificateGenerator.addExtension(X509Extensions.ExtendedKeyUsage, true, getExtendedKeyUsage());
        return certificateGenerator.generate(keyPair.getPrivate(), PROVIDER);
    }

    /**
     * Creates the extended key usage options for our self-signed certificates.
     * 
     * @return the extended key usage options.
     */
    private ExtendedKeyUsage getExtendedKeyUsage() {
        return new ExtendedKeyUsage(KeyPurposeId.id_kp_serverAuth);
    }

    /**
     * Creates the key usage options for our self-signed certificates.
     * 
     * @return the key usage options.
     */
    private KeyUsage getValidKeyUsage() {
        return new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment);
    }

    /**
     * Calculates the expiration time for our self-signed certificates.
     * 
     * @return the expiration time.
     */
    private long calculateExpirationTime() {
        return System.currentTimeMillis() + validity * MILLISECONDS_PER_DAY;
    }

    /**
     * Generates the key pairs for our self-signed certificates.
     * 
     * @return the key pair.
     * @throws NoSuchAlgorithmException if the specified hash algorithm is unknown.
     */
    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(keysize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }
}
