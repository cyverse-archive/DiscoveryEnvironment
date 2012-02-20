package org.iplantc.de.server;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Contains methods for accessing the confluence.properties file.
 * 
 * @author hariolf
 * 
 */
public class ConfluenceProperties {
    private static final Logger LOGGER = Logger.getLogger(ConfluenceProperties.class);

    // The prefix for all of the properties.
    private static final String PREFIX = "org.iplantc.discoveryenvironment"; //$NON-NLS-1$

    // The name of the properties file.
    private static final String PROPERTIES_FILE = "/confluence.properties"; //$NON-NLS-1$
    private static final String MULE_SERVICE_BASE_URL = PREFIX + ".muleServiceBaseUrl"; //$NON-NLS-1$

    private static final String CONFLUENCE_BASE_URL = PREFIX + ".confluence.baseUrl"; //$NON-NLS-1$
    private static final String CONFLUENCE_PARENT_PAGE = PREFIX + ".confluence.parentPageName"; //$NON-NLS-1$
    private static final String CONFLUENCE_USER = PREFIX + ".confluence.user"; //$NON-NLS-1$
    private static final String CONFLUENCE_PASSWORD = PREFIX + ".confluence.password"; //$NON-NLS-1$
    private static final String CONFLUENCE_SPACE_NAME = PREFIX + ".confluence.spaceName"; //$NON-NLS-1$
    private static final String CONFLUENCE_SPACE_URL = PREFIX + ".confluence.spaceUrl"; //$NON-NLS-1$

    /**
     * The list of required properties.
     */
    private static final String[] REQUIRED_PROPERTIES = {MULE_SERVICE_BASE_URL, CONFLUENCE_BASE_URL,
            CONFLUENCE_PARENT_PAGE, CONFLUENCE_USER, CONFLUENCE_PASSWORD, CONFLUENCE_SPACE_NAME,
            CONFLUENCE_SPACE_URL};

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
            properties.load(ConfluenceProperties.class.getResourceAsStream(PROPERTIES_FILE));
        } catch (IOException e) {
            String msg = "unable to load discovery environment properties"; //$NON-NLS-1$
            LOGGER.error(msg, e);
        }
    }

    /**
     * Gets the base URL used to access the Confluence wiki.
     * 
     * @return the URL as a string.
     */
    public static String getConfluenceBaseUrl() {
        return properties.getProperty(CONFLUENCE_BASE_URL);
    }

    /**
     * Gets the name of the 'List of Applications' page.
     * 
     * @return the name as a string.
     */
    public static String getConfluenceParentPage() {
        return properties.getProperty(CONFLUENCE_PARENT_PAGE);
    }

    /**
     * Gets the Confluence user for adding documentation pages.
     * 
     * @return the user name
     */
    public static String getConfluenceUser() {
        return properties.getProperty(CONFLUENCE_USER);
    }

    /**
     * Gets the Confluence password for adding documentation pages.
     * 
     * @return the password
     */
    public static String getConfluencePassword() {
        return properties.getProperty(CONFLUENCE_PASSWORD);
    }

    /**
     * Gets the name of the 'DE Applications' space in Confluence.
     * 
     * @return the name as a string.
     */
    public static String getConfluenceSpaceName() {
        return properties.getProperty(CONFLUENCE_SPACE_NAME);
    }

    /**
     * Gets the URL of the 'DE Applications' space in Confluence.
     * 
     * @return the URL as a string.
     */
    public static String getConfluenceSpaceUrl() {
        return properties.getProperty(CONFLUENCE_SPACE_URL);
    }
}
