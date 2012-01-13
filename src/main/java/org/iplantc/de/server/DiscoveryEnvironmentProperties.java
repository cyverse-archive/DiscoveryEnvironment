package org.iplantc.de.server;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Provides access to configuration properties for the Discovery Environment.
 * 
 * @author Donald A. Barre
 */
public class DiscoveryEnvironmentProperties {
    private static final Logger LOGGER = Logger.getLogger(DiscoveryEnvironmentProperties.class);

    // The name of the properties file.
    public static final String PROPERTIES_FILE = "/discoveryenvironment.properties"; //$NON-NLS-1$

    /**
     * The prefix used in each of the data management property names.
     */
    private static final String DATA_MGMT_PROPERTY_PREFIX = "org.iplantc.services.de-data-mgmt."; //$NON-NLS-1$

    /**
     * Properties key of the base data management URL.
     */
    private static final String DATA_MGMT_SERVICE_BASE_URL = DATA_MGMT_PROPERTY_PREFIX + "base"; //$NON-NLS-1$

    /**
     * Properties key of the base URL used to upload user's data files.
     */
    private static final String UPLOAD_SERVICE_URL = DATA_MGMT_PROPERTY_PREFIX + "file-upload"; //$NON-NLS-1$

    /**
     * Properties key of the base URL used to download user's data files.
     */
    private static final String DOWNLOAD_SERVICE_URL = DATA_MGMT_PROPERTY_PREFIX + "file-download"; //$NON-NLS-1$

    // The prefix for all of the properties.
    public static final String PREFIX = "org.iplantc.discoveryenvironment"; //$NON-NLS-1$

    public static final String DE_DEFAULT_BUILD_NUMBER = PREFIX + ".about.defaultBuildNumber"; //$NON-NLS-1$
    public static final String DE_RELEASE_VERSION = PREFIX + ".about.releaseVersion"; //$NON-NLS-1$

    public static final String MULE_SERVICE_BASE_URL = PREFIX + ".muleServiceBaseUrl"; //$NON-NLS-1$
    public static final String NOTIFICATION_BASE_URL = PREFIX + ".notificationAgentBaseUrl"; //$NON-NLS-1$

    /**
     * The list of required properties.
     */
    private static final String[] REQUIRED_PROPERTIES = {MULE_SERVICE_BASE_URL, NOTIFICATION_BASE_URL,
            DATA_MGMT_SERVICE_BASE_URL, UPLOAD_SERVICE_URL, DOWNLOAD_SERVICE_URL};

    /**
     * The properties. Place any default values in the initializer.
     */
    @SuppressWarnings("serial")
    private static Properties properties = new Properties() {
        {
            put(MULE_SERVICE_BASE_URL, "localhost"); //$NON-NLS-1$
        }
    };

    static {
        loadProperties();
        validateProperties(REQUIRED_PROPERTIES);
    }

    /**
     * Validates that we have values for all required properties.
     */
    private static void validateProperties(String[] propertyNames) {
        for (String propertyName : propertyNames) {
            String propertyValue = properties.getProperty(propertyName);
            if (propertyValue == null || propertyValue.equals("")) { //$NON-NLS-1$
                throw new ExceptionInInitializerError("missing required property: " + propertyName); //$NON-NLS-1$
            }
        }
    }

    /**
     * Loads the discovery environment properties. If an error occurs while loading the file, we log the
     * message, but do not throw an exception; the property validation will catch any required properties
     * that are missing.
     */
    private static void loadProperties() {
        try {
            properties.load(DiscoveryEnvironmentProperties.class.getResourceAsStream(PROPERTIES_FILE));
        } catch (IOException e) {
            String msg = "unable to load discovery environment properties"; //$NON-NLS-1$
            LOGGER.error(msg, e);
        }
    }

    /**
     * Gets the default build number.
     * 
     * When a build number is not available, this value will be provided.
     * 
     * @return a string representing the default build number.
     */
    public static String getDefaultBuildNumber() {
        return properties.getProperty(DE_DEFAULT_BUILD_NUMBER);
    }

    /**
     * Gets the release version for the Discovery Environment.
     * 
     * This will be displayed in about text or provided as context.
     * 
     * @return a string representing the release version of the Discovery Environment.
     */
    public static String getReleaseVersion() {
        return properties.getProperty(DE_RELEASE_VERSION);
    }

    /**
     * Gets the base URL to use to access the Mule services.
     * 
     * @return the base URL.
     */
    public static String getMuleServiceBaseUrl() {
        return properties.getProperty(MULE_SERVICE_BASE_URL);
    }

    /**
     * Gets the base URL to use to access the notification agent.
     * 
     * @return the base URL.
     */
    public static String getNotificaitonBaseUrl() {
        return properties.getProperty(NOTIFICATION_BASE_URL);
    }

    /**
     * Gets the base data management URL.
     * 
     * @return the URL as a string.
     */
    public static String getDataMgmtServiceBaseUrl() {
        return properties.getProperty(DATA_MGMT_SERVICE_BASE_URL);
    }

    /**
     * Gets the base URL used to upload user's data files.
     * 
     * @return the URL as a string.
     */
    public static String getUploadFileServiceBaseUrl() {
        return properties.getProperty(UPLOAD_SERVICE_URL);
    }

    /**
     * Gets the base URL used to download user's data files.
     * 
     * @return the URL as a string.
     */
    public static String getDownloadFileServiceBaseUrl() {
        return properties.getProperty(DOWNLOAD_SERVICE_URL);
    }
}
