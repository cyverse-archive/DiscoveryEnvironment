package org.iplantc.de.server;

import java.util.Properties;

import javax.servlet.ServletContext;

import org.iplantc.clavin.spring.ClavinPropertyPlaceholderConfigurer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


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
     * @param configurer the configurer that was used to load the properties.
     */
    public DiscoveryEnvironmentProperties(ClavinPropertyPlaceholderConfigurer configurer) {
        if (configurer == null) {
            throw new IllegalArgumentException("the configurer may not be null");
        }
        props = configurer.getConfig("discoveryenvironment");
        if (props == null) {
            throw new IllegalArgumentException("discovery environment configuration not found");
        }
        validateProperties();
    }

    /**
     * @param props the configuration properties.
     */
    public DiscoveryEnvironmentProperties(Properties props) {
        if (props == null) {
            throw new IllegalArgumentException("the properties may not be null");
        }
        this.props = props;
        validateProperties();
    }

    /**
     * Gets the discovery environment properties to use for the given servlet context.
     *
     * @param context the servlet context.
     * @return the discovery environment properties.
     * @throws IllegalStateException if the discovery environment properties aren't defined.
     */
    public static DiscoveryEnvironmentProperties getDiscoveryEnvironmentProperties(ServletContext context) {
        WebApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        DiscoveryEnvironmentProperties result = appContext.getBean(DiscoveryEnvironmentProperties.class);
        if (result == null) {
            throw new IllegalStateException("discovery environment properties bean not defined");
        }
        return result;
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
