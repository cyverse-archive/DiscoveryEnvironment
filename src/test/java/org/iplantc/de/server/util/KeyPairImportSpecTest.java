package org.iplantc.de.server.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for org.iplantc.de.server.util.KeyPairImportSpec.
 * 
 * @author Dennis Roberts
 */
public class KeyPairImportSpecTest {
    private Properties properties;

    private KeyPairImportSpec instance;

    /**
     * Sets up each unit test.
     */
    @Before
    public void setUp() {
        properties = new Properties();
        addFooProperties(properties);
        addBarProperties(properties);
        instance = new KeyPairImportSpec(properties, "foo"); //$NON-NLS-1$
    }

    /**
     * Adds some properties to the set of properties for testing.
     * 
     * @param properties the set of properties.
     */
    private void addBarProperties(Properties properties) {
        properties.put("org.iplantc.keystore.import.bar.algorithm", "RSA"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.import.bar.keyFile", "bar.key"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.import.bar.certType", "X.509"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.import.bar.certFile", "bar.crt"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Adds some properties to the set of properties for testing.
     * 
     * @param properties the set of properties.
     */
    private void addFooProperties(Properties properties) {
        properties.put("org.iplantc.keystore.import.foo.algorithm", "RSA"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.import.foo.keyFile", "foo.key"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.import.foo.certType", "X.509"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.import.foo.certFile", "foo.crt"); //$NON-NLS-1$ //$NON-NLS-2$
        properties.put("org.iplantc.keystore.import.foo.password", "oof"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Verifies that the key algorithm is successfully extracted.
     */
    @Test
    public void shouldExtractAlgorithm() {
        assertEquals("RSA", instance.getAlgorithm()); //$NON-NLS-1$
    }

    /**
     * Verifies that the key file is successfully extracted.
     */
    @Test
    public void shouldExtractKeyFile() {
        assertEquals("foo.key", instance.getKeyFile()); //$NON-NLS-1$
    }

    /**
     * Verifies that the certificate type is successfully extracted.
     */
    @Test
    public void shouldExtractCertType() {
        assertEquals("X.509", instance.getCertType()); //$NON-NLS-1$
    }

    /**
     * Verifies that the certificate file is successfully extracted.
     */
    @Test
    public void shouldExtractCertificateFile() {
        assertEquals("foo.crt", instance.getCertFile()); //$NON-NLS-1$
    }

    /**
     * Verifies that the password is successfully extracted.
     */
    @Test
    public void shouldExtractPassword() {
        assertArrayEquals("oof".toCharArray(), instance.getPassword()); //$NON-NLS-1$
    }

    /**
     * Verifies that we get an exception for a missing property.
     */
    @Test(expected = RuntimeException.class)
    public void shouldGetExceptionForMissingProperty() {
        instance = new KeyPairImportSpec(properties, "bar"); //$NON-NLS-1$
    }
}
