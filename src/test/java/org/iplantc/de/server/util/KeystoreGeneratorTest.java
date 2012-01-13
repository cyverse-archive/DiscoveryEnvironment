package org.iplantc.de.server.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for org.iplantc.de.server.util.KeystoreGenerator.
 * 
 * @author Dennis Roberts
 */
public class KeystoreGeneratorTest {
    /**
     * The path to the test keystore.
     */
    private static final String TEST_KEYSTORE_PATH = "test-keystore.jceks"; //$NON-NLS-1$

    /**
     * The type of the test keystore.
     */
    private static final String TEST_KEYSTORE_TYPE = "JCEKS"; //$NON-NLS-1$

    /**
     * The password to use for testing.
     */
    private static final String TEST_KEYSTORE_PASSWORD = "I am the password...BEWARE!"; //$NON-NLS-1$

    /**
     * The KeystoreGenerator instance to use for testing.
     */
    private KeystoreGenerator instance;

    /**
     * Initializes each test.
     */
    @Before
    public void initialize() {
        Properties properties = new Properties();
        addCommonProperties(properties);
        addFooProperties(properties);
        addBarProperties(properties);
        instance = new KeystoreGenerator(properties);
    }

    /**
     * Verifies that the constructor correctly extracts the path.
     */
    @Test
    public void shouldExtractPath() {
        assertEquals(TEST_KEYSTORE_PATH, instance.getPath());
    }

    /**
     * Verifies that the constructor correctly extracts the keystore type.
     */
    @Test
    public void shouldExtractType() {
        assertEquals(TEST_KEYSTORE_TYPE, instance.getType());
    }

    /**
     * Verifies that the constructor correctly extracts the keystore password.
     */
    @Test
    public void shouldExtractPassword() {
        assertEquals(TEST_KEYSTORE_PASSWORD, instance.getPassword());
    }

    /**
     * Verifies that we get a RuntimeException if a required property is missing.
     */
    @Test(expected = RuntimeException.class)
    public void shouldGetExceptionForMissingProperty() {
        Properties properties = new Properties();
        instance = new KeystoreGenerator(properties);
    }

    /**
     * Verifies that the constructor correctly extracts the key pair specifications.
     */
    @Test
    public void shouldExtractKeyPairSpecs() {
        assertEquals(2, instance.getKeyPairSpecs().size());
        assertNotNull(findKeyPairSpec(instance.getKeyPairSpecs(), "foo")); //$NON-NLS-1$
        assertNotNull(findKeyPairSpec(instance.getKeyPairSpecs(), "bar")); //$NON-NLS-1$
    }

    /**
     * Verifies that we can successfully generate a keystore file.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldGenerateKeystoreFile() throws Exception {
        try {
            instance.generateKeystoreFile();
            assertTrue(new File(TEST_KEYSTORE_PATH).exists());
        } finally {
            removeTestKeystoreFile();
        }
    }

    /**
     * Verifies that we can successfully generate the keys in the keystore file.
     * 
     * @throws Exception if an error occurs.
     */
    @Test
    public void shouldGenerateKeys() throws Exception {
        try {
            instance.generateKeystoreFile();
            KeyStore keystore = KeyStore.getInstance(TEST_KEYSTORE_TYPE);
            FileInputStream in = new FileInputStream(TEST_KEYSTORE_PATH);
            keystore.load(in, TEST_KEYSTORE_PASSWORD.toCharArray());
            assertTrue(keystore.containsAlias("foo")); //$NON-NLS-1$
            assertTrue(keystore.containsAlias("bar")); //$NON-NLS-1$
        } finally {
            removeTestKeystoreFile();
        }
    }

    /**
     * Removes the test keystore file if it exists.
     */
    private void removeTestKeystoreFile() {
        File keystoreFile = new File(TEST_KEYSTORE_PATH);
        if (keystoreFile.exists()) {
            keystoreFile.delete();
        }
    }

    /**
     * Finds a key pair specification for the key with the given alias in the given list of key pair
     * specifications.
     * 
     * @param keyPairSpecs the list of key pair specifications.
     * @param alias the key pair alias to search for.
     * @return the key pair specification or null if a matching specification can't be found.
     */
    private Object findKeyPairSpec(List<KeyPairSpec> keyPairSpecs, String alias) {
        for (KeyPairSpec keyPairSpec : keyPairSpecs) {
            if (keyPairSpec.getAlias().equals(alias)) {
                return keyPairSpec;
            }
        }
        return null;
    }

    /**
     * Adds the key pair specification for the key pair with the alias, "bar."
     * 
     * @param properties the set of properties.
     */
    private void addBarProperties(Properties properties) {
        properties.put("org.iplantc.keystore.key.bar.algorithm", "RSA"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.bar.keysize", "512"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.bar.signatureAlgorithm", "SHA256WithRSAEncryption"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.bar.distinguishedName", "CN=Bar"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.bar.validity", "365"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.key.bar.password", "rab"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Adds the key pair specification for the key pair with the alias, "foo."
     * 
     * @param properties the set of properties.
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
     * Adds the common keystore properties to the set of properties.
     * 
     * @param properties the set of properties.
     */
    private void addCommonProperties(Properties properties) {
        properties.put("org.iplantc.keystore.path", TEST_KEYSTORE_PATH); //$NON-NLS-1$
        properties.put("org.iplantc.keystore.type", TEST_KEYSTORE_TYPE); //$NON-NLS-1$
        properties.put("org.iplantc.keystore.password", TEST_KEYSTORE_PASSWORD); //$NON-NLS-1$
    }
}
