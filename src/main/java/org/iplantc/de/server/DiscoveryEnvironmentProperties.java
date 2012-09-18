package org.iplantc.de.server;

import java.util.Properties;


/**
 * Provides access to configuration properties for the Discovery Environment.
 *
 * @author Donald A. Barre
 */
@SuppressWarnings("nls")
public class DiscoveryEnvironmentProperties {

    // Constants used to obtain property values.
    private static final String DATA_MGMT_PROPERTY_PREFIX  = "org.iplantc.services.de-data-mgmt.";
    private static final String DATA_MGMT_SERVICE_BASE_URL = DATA_MGMT_PROPERTY_PREFIX + "base";
    private static final String UPLOAD_SERVICE_URL         = DATA_MGMT_PROPERTY_PREFIX + "file-upload";
    private static final String DOWNLOAD_SERVICE_URL       = DATA_MGMT_PROPERTY_PREFIX + "file-download";
    private static final String URL_IMPORT_SERVICE_URL     = DATA_MGMT_PROPERTY_PREFIX + "file-urlupload";
    private static final String PREFIX                     = "org.iplantc.discoveryenvironment";
    private static final String DE_DEFAULT_BUILD_NUMBER    = PREFIX + ".about.defaultBuildNumber";
    private static final String DE_RELEASE_VERSION         = PREFIX + ".about.releaseVersion";
    private static final String MULE_SERVICE_BASE_URL      = PREFIX + ".muleServiceBaseUrl";

    /**
     * The list of required properties.
     */
    private static final String[] REQUIRED_PROPERTIES = {MULE_SERVICE_BASE_URL, DATA_MGMT_SERVICE_BASE_URL,
        UPLOAD_SERVICE_URL, DOWNLOAD_SERVICE_URL, URL_IMPORT_SERVICE_URL};

    /**
     * The configuration properties.
     */
    private Properties props;

    /**
     * @param props the configuration properties.
     */
    public DiscoveryEnvironmentProperties(Properties props) {
        if (props == null) {
            throw new ExceptionInInitializerError("the properties may not be null");
        }
        this.props = props;
        validateProperties();
    }

    /**
     * Validates that we have values for all required properties.
     */
    private void validateProperties() {
        for (String propertyName : REQUIRED_PROPERTIES) {
            String propertyValue = props.getProperty(propertyName);
            if (propertyValue == null || propertyValue.equals("")) {
                throw new ExceptionInInitializerError("missing required property: " + propertyName);
            }
        }
    }

    /**
     * Gets the default build number.
     *
     * When a build number is not available, this value will be provided.
     *
     * @return a string representing the default build number.
     */
    public String getDefaultBuildNumber() {
        return props.getProperty(DE_DEFAULT_BUILD_NUMBER);
    }

    /**
     * Gets the release version for the Discovery Environment.
     *
     * This will be displayed in about text or provided as context.
     *
     * @return a string representing the release version of the Discovery Environment.
     */
    public String getReleaseVersion() {
        return props.getProperty(DE_RELEASE_VERSION);
    }

    /**
     * Gets the base data management URL.
     *
     * @return the URL as a string.
     */
    public String getDataMgmtServiceBaseUrl() {
        return props.getProperty(DATA_MGMT_SERVICE_BASE_URL);
    }

    /**
     * Gets the base URL used to upload user's data files.
     *
     * @return the URL as a string.
     */
    public String getUploadFileServiceBaseUrl() {
        return props.getProperty(UPLOAD_SERVICE_URL);
    }

    /**
     * Gets the base URL used to download user's data files.
     *
     * @return the URL as a string.
     */
    public String getDownloadFileServiceBaseUrl() {
        return props.getProperty(DOWNLOAD_SERVICE_URL);
    }

    /**
     * Gets the base URL used to import URLs.
     *
     * @return the URL as a string.
     */
    public String getUrlImportServiceBaseUrl() {
        return props.getProperty(URL_IMPORT_SERVICE_URL);
    }
}
