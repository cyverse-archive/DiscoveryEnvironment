package org.iplantc.de.server.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Generates a keystore using the keystore specification provided in the properties file listed on the
 * command line. For more information about how to create the keystore specification properties file, see
 * the documentation for org.iplantc.de.server.util.KeystoreGenerator.
 * 
 * @author Dennis Roberts
 */
public class GenerateKeystore {

    /**
     * Main subroutine.
     * 
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        try {
            validateUsage(args);
            Properties properties = loadProperties(args[0]);
            KeystoreGenerator keystoreGenerator = new KeystoreGenerator(properties);
            keystoreGenerator.generateKeystoreFile();
        } catch (RuntimeException e) {
            System.err.println("unable to generate the keystore: " + e.getMessage()); //$NON-NLS-1$
            System.exit(1);
        }
    }

    /**
     * Loads the properties from the given properties file.
     * 
     * @param propertiesFilePath the path to the properties file.
     * @return the properties.
     * 
     */
    private static Properties loadProperties(String propertiesFilePath) {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(propertiesFilePath));
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("unable to open the properties file: " + e.getMessage()); //$NON-NLS-1$
        }
    }

    /**
     * Validates the usage of this utility.
     * 
     * @param args the command-line arguments.
     */
    private static void validateUsage(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java GenerateKeystore properties_file"); //$NON-NLS-1$
            System.exit(1);
        }
    }
}
