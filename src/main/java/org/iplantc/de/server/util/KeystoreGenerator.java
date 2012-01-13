package org.iplantc.de.server.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * <p>
 * This class can be used to generate a keystore from a set of properties. The properties provide the
 * path to the keystore, the keystore type, the keystore password, and zero or more key pair
 * specifications. The following property names are recognized by this class.
 * </p>
 * <table border=1>
 * <tr>
 * <th>Property Name</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>org.iplantc.keystore.path</td>
 * <td>The path to the keystore file, relative to the working directory.</td>
 * </tr>
 * <tr>
 * <td>org.iplantc.keystore.type</td>
 * <td>The type of keystore being used.</td>
 * </tr>
 * <tr>
 * <td>org.iplantc.keystore.password</td>
 * <td>The password used to protect the keystore.</td>
 * </tr>
 * <tr>
 * <td>org.iplantc.keystore.key.&lt;alias&gt;.algorithm</td>
 * <td>The algorithm used to generate the key pair with the given alias.</td>
 * </tr>
 * <tr>
 * <td>org.iplantc.keystore.key.&lt;alias&gt;.keysize</td>
 * <td>The size of the key with the given alias.</td>
 * </tr>
 * <tr>
 * <td>org.iplantc.keystore.key.&lt;alias&gt;.signatureAlgorithm</td>
 * <td>The algorithm used to sign the key pair with the given alias.</td>
 * </tr>
 * <tr>
 * <td>org.iplantc.keystore.key.&lt;alias&gt;.distinguishedName</td>
 * <td>The distinguished name to use for the self-signed certificate.</td>
 * </tr>
 * <tr>
 * <td>org.iplantc.keystore.key.&lt;alias&gt;.validity</td>
 * <td>The number of days the key should remain valid.</td>
 * </tr>
 * <tr>
 * <td>org.iplantc.keystore.key.&lt;alias&gt;.password</td>
 * <td>The password used to protect the key.</td>
 * </tr>
 * <tr>
 * <td>org.iplantc.keystore.import.&lt;alias&gt;.algorithm</td>
 * <td>The algorithm of the imported key pair with the given alias.</td>
 * </tr>
 * <tr>
 * <td>org.iplantc.keystore.import.&lt;alias&gt;.keyFile</td>
 * <td>The path to the file containing the private key, which must be in DER format.</td>
 * </tr>
 * <tr>
 * <td>org.iplantc.keystore.import.&lt;alias&gt;.certType</td>
 * <td>The type of certificate being imported.</td>
 * </tr>
 * <tr>
 * <td>org.iplantc.keystore.import.&lt;alias&gt;.certFile</td>
 * <td>The path to the file containing the certificates, which must be in DER format.</td>
 * </tr>
 * </table>
 * <p>
 * For example, the following properties file could be used to generate a keystore containing two key
 * pairs.
 * </p>
 * <code><pre>
 * org.iplantc.keystore.path = WEB-INF/classes/new-keystore.jceks
 * org.iplantc.keystore.type = JCEKS
 * org.iplantc.keystore.password = changeit
 * 
 * org.iplantc.keystore.key.signing.algorithm = RSA
 * org.iplantc.keystore.key.signing.keysize = 1024
 * org.iplantc.keystore.key.signing.signatureAlgorithm = SHA512WithRSAEncryption
 * org.iplantc.keystore.key.signing.distinguishedName = CN=iPlant
 * org.iplantc.keystore.key.signing.validity = 3650
 * org.iplantc.keystore.key.signing.password = changeit
 * 
 * org.iplantc.keystore.key.encrypting.algorithm = RSA
 * org.iplantc.keystore.key.encrypting.keysize = 1024
 * org.iplantc.keystore.key.encrypting.signatureAlgorithm = SHA512WithRSAEncryption
 * org.iplantc.keystore.key.encrypting.distinguishedName = CN=iPlant
 * org.iplantc.keystore.key.encrypting.validity = 3650
 * org.iplantc.keystore.key.encrypting.password = changeit
 * 
 * org.iplantc.keystore.import.foundational.algorithm = RSA
 * org.iplantc.keystore.import.foundational.keyFile = iplant_key.der
 * org.iplantc.keystore.import.foundational.certType = X.509
 * org.iplantc.keystore.import.foundational.certFile = iplant_cert.der
 * org.iplantc.keystore.import.foundational.password = changeit
 * </pre></code>
 * 
 * @author Dennis Roberts
 */
public class KeystoreGenerator {
    /**
     * The prefix used for all property names.
     */
    private static final String PROPERTY_NAME_PREFIX = "org.iplantc.keystore"; //$NON-NLS-1$

    /**
     * The prefix used for all property names used in key pair specifications.
     */
    private static final String KEY_PROPERTY_NAME_PREFIX = PROPERTY_NAME_PREFIX + ".key"; //$NON-NLS-1$

    /**
     * The prefix used for all property names used to specify key pairs that are imported.
     */
    private static final String KEY_IMPORT_PROPERTY_NAME_PREFIX = PROPERTY_NAME_PREFIX + ".import"; //$NON-NLS-1$

    /**
     * The path to the keystore, relative to the working directory.
     */
    private String path;

    /**
     * The keystore type.
     */
    private String type;

    /**
     * The keystore password.
     */
    private String password;

    /**
     * The list of key pair specifications.
     */
    private List<KeyPairSpec> keyPairSpecs;

    /**
     * The list of key store entry import specifications.
     */
    private List<KeyPairImportSpec> keyStoreEntryImportSpecs;

    /**
     * Creates a new keystore generator from the given set of properties.
     * 
     * @param properties the properties.
     */
    public KeystoreGenerator(Properties properties) {
        extractProperties(properties);
    }

    /**
     * Gets the path to the keystore file, relative to the working directory.
     * 
     * @return the path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the keystore type.
     * 
     * @return the keystore type.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the keystore password.
     * 
     * @return the keystore password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the list of key pair specifications.
     * 
     * @return the list of key pair specifications.
     */
    public List<KeyPairSpec> getKeyPairSpecs() {
        return keyPairSpecs;
    }

    /**
     * Gets the list of key store entry import specifications.
     * 
     * @return the list of key store entry import specifications.
     */
    public List<KeyPairImportSpec> getKeyStoreEntryImportSpec() {
        return keyStoreEntryImportSpecs;
    }

    /**
     * Extracts the keystore specification properties from the given set of properties.
     * 
     * @param properties the set of properties.
     * @throws RuntimeException if any required property doesn't exist.
     */
    private void extractProperties(Properties properties) {
        path = extractProperty(properties, "path"); //$NON-NLS-1$
        type = extractProperty(properties, "type"); //$NON-NLS-1$
        password = extractProperty(properties, "password"); //$NON-NLS-1$
        keyPairSpecs = new LinkedList<KeyPairSpec>();
        extractKeyPairSpecs(properties);
        extractKeyPairImportSpecs(properties);
    }

    private void extractKeyPairImportSpecs(Properties properties) {
        for (String alias : getAliases(properties, KEY_IMPORT_PROPERTY_NAME_PREFIX)) {
            keyStoreEntryImportSpecs.add(new KeyPairImportSpec(properties, alias));
        }
    }

    /**
     * Extracts the key pair specifications from the given set of properties.
     * 
     * @param properties the set of properties.
     * @throws RuntimeException if a property required for a key pair specification doesn't exist.
     */
    private void extractKeyPairSpecs(Properties properties) {
        for (String alias : getAliases(properties, KEY_PROPERTY_NAME_PREFIX)) {
            keyPairSpecs.add(new KeyPairSpec(properties, alias));
        }
    }

    /**
     * Gets the list of aliases for the key groups defined by the given property name prefix.
     * 
     * @param properties the set of properties.
     * @param prefix the property name prefix.
     * @return the list of aliases.
     */
    private Set<String> getAliases(Properties properties, String prefix) {
        int index = prefix.split("\\.").length; //$NON-NLS-1$
        Set<String> aliases = new HashSet<String>();
        for (String propertyName : properties.stringPropertyNames()) {
            if (propertyName.startsWith(prefix)) {
                aliases.add(propertyName.split("\\.")[index]); //$NON-NLS-1$
            }
        }
        return aliases;
    }

    /**
     * Extracts the value of the property with the given name from the given set of properties. If the
     * property with the given name doesn't exist then a RuntimeException is thrown.
     * 
     * @param properties the set of properties.
     * @param shortPropertyName the name of the property without the prefix.
     * @return the property value.
     * @throws RuntimeException if the property isn't defined.
     */
    private String extractProperty(Properties properties, String shortPropertyName) {
        String fullPropertyName = PROPERTY_NAME_PREFIX + "." + shortPropertyName; //$NON-NLS-1$
        String propertyValue = properties.getProperty(fullPropertyName);
        if (propertyValue == null) {
            throw new RuntimeException("missing required property: " + fullPropertyName); //$NON-NLS-1$
        }
        return propertyValue;
    }

    /**
     * Generates the keystore file according to the in-memory specification for this class.
     * 
     * This method throws RuntimeException if the keystore file can't be generated.
     * 
     * @see java.lang.RuntimeException
     */
    public void generateKeystoreFile() {
        try {
            KeyStore keystore = KeyStore.getInstance(type);
            keystore.load(null);
            generateKeys(keystore);
            keystore.store(new FileOutputStream(path), password.toCharArray());
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates all of the keys in the keystore specification and stores them in the keystore.
     * 
     * This method throws RuntimeException if any of the keys can't be generated.
     * 
     * @see java.lang.RuntimeException
     * @param keystore the keystore.
     */
    private void generateKeys(KeyStore keystore) {
        try {
            for (KeyPairSpec keyPairSpec : keyPairSpecs) {
                KeyStore.Entry entry = keyPairSpec.generateKeystoreEntry();
                KeyStore.ProtectionParameter protectionParameter = keyPairSpec
                        .generateProtectionParameter();
                keystore.setEntry(keyPairSpec.getAlias(), entry, protectionParameter);
            }
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
