package org.iplantc.de.server.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for org.iplantc.de.server.util.KeyPairSpec.
 * 
 * @author Dennis Roberts
 */
public class KeyPairSpecTest {

    /**
     * The KeyPairSpec instance to use for most of the tests.
     */
    private KeyPairSpec instance;

    /**
     * Initializes each of the tests.
     */
    @Before
    public void initialize() {
        Properties properties = new Properties();
        addFooProperties(properties);
        addBarProperties(properties);
        instance = new KeyPairSpec(properties, "foo"); //$NON-NLS-1$
    }

    /**
     * Verifies that the constructor correctly sets the alias.
     */
    @Test
    public void shouldExtractAlias() {
        assertEquals("foo", instance.getAlias()); //$NON-NLS-1$
    }

    /**
     * Verifies that the constructor correctly sets the algorithm.
     */
    @Test
    public void shouldExtractAlgorithm() {
        assertEquals("RSA", instance.getAlgorithm()); //$NON-NLS-1$
    }

    /**
     * Verifies that the constructor correctly sets the key size.
     */
    @Test
    public void shouldExtractKeysize() {
        assertEquals(1024, instance.getKeysize());
    }

    /**
     * Verifies that the constructor correctly sets the signature algorithm.
     */
    @Test
    public void shouldExtractSignatureAlgorithm() {
        assertEquals("SHA512WithRSAEncryption", instance.getSignatureAlgorithm()); //$NON-NLS-1$
    }

    /**
     * Verifies that the constructor correctly sets the distinguished name.
     */
    @Test
    public void shouldExtractDistinguishedName() {
        assertEquals("CN=Foo", instance.getDistinguishedName()); //$NON-NLS-1$
    }

    /**
     * Verifies that the constructor correctly sets the validity period.
     */
    @Test
    public void shouldExtractValidity() {
        assertEquals(3650, instance.getValidity());
    }

    /**
     * Verifies that the constructor correctly sets the password.
     */
    @Test
    public void shouldExtractPassword() {
        assertEquals("oof", instance.getPassword()); //$NON-NLS-1$
    }

    /**
     * Verifies that we get an exception if a required property is missing.
     */
    @Test(expected = RuntimeException.class)
    public void shouldGetExceptionForMissingParameter() {
        Properties properties = new Properties();
        addFooProperties(properties);
        addBarProperties(properties);
        instance = new KeyPairSpec(properties, "bar"); //$NON-NLS-1$
    }

    /**
     * Verifies that we can generate an entry for a keystore.
     */
    @Test
    public void shouldGeneratePrivateKeyEntry() {
        assertNotNull(instance.generateKeystoreEntry());
    }

    /**
     * Verifies that we can generate a protection object for a keystore entry.
     */
    @Test
    public void shouldGenerateProtectionObject() {
        assertNotNull(instance.generateProtectionParameter());
    }

    /**
     * Verifies that we get an exception if we try to generate a private key entry using a bogus hash
     * algorithm.
     * 
     * @throws Exception if an error occurs.
     */
    @Test(expected = RuntimeException.class)
    public void shouldGetExceptionForBogusHashAlgorithm() throws Exception {
        Properties properties = new Properties();
        properties.put("org.iplantc.keystore.key.baz.algorithm", "BLARGSA"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.baz.keysize", "1024"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.baz.signatureAlgorithm", "SHA512WithRSAEncryption"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.baz.distinguishedName", "CN=Baz"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.baz.validity", "3650"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.baz.password", "zab"); //$NON-NLS-1$ //$NON-NLS-2$
        instance = new KeyPairSpec(properties, "baz"); //$NON-NLS-1$
        instance.generateKeystoreEntry();
    }

    /**
     * Verifies that we get an exception if we try to generate a private key entry using a bogus
     * signature algorithm.
     * 
     * @throws Exception if an error occurs.
     */
    @Test(expected = RuntimeException.class)
    public void shouldGetExceptionForBogusSignatureAlgorithm() throws Exception {
        Properties properties = new Properties();
        properties.put("org.iplantc.keystore.key.baz.algorithm", "RSA"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.baz.keysize", "1024"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.baz.signatureAlgorithm", "BLARGSA512WithRSAEncryption"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.baz.distinguishedName", "CN=Baz"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.baz.validity", "3650"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.baz.password", "zab"); //$NON-NLS-1$ //$NON-NLS-2$
        instance = new KeyPairSpec(properties, "baz"); //$NON-NLS-1$
        instance.generateKeystoreEntry();
    }

    /**
     * Adds the properties for the key with the alias, "foo."
     * 
     * @param properties the properties object.
     */
    private void addFooProperties(Properties properties) {
        properties.put("org.iplantc.keystore.key.foo.algorithm", "RSA"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.foo.keysize", "1024"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.foo.signatureAlgorithm", "SHA512WithRSAEncryption"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.foo.distinguishedName", "CN=Foo"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.foo.validity", "3650"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.foo.password", "oof"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Adds the properties for the key with the alias, "bar."
     * 
     * @param properties the properties object.
     */
    private void addBarProperties(Properties properties) {
        properties.put("org.iplantc.keystore.key.bar.algorithm", "DSA"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.bar.keysize", "512"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.bar.signatureAlgorithm", "SHA256WithRSAEncryption"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.bar.distinguishedName", "CN=Bar"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.bar.validity", "365"); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
