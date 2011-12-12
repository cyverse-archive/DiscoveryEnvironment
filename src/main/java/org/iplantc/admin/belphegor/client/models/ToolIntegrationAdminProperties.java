package org.iplantc.admin.belphegor.client.models;

import java.util.Map;

public class ToolIntegrationAdminProperties {

    private static ToolIntegrationAdminProperties instance;

    /**
     * The prefix used in each of the property names.
     */
    private static final String PROPERTY_NAME_PREFIX = "org.iplantc.belphegor."; //$NON-NLS-1$

    /**
     * The base URL used to access the Mule services.
     */
    private static final String UNPROTECTED_MULE_SERVICE_BASE_URL = PROPERTY_NAME_PREFIX
            + "unprotectedMuleServiceBaseUrl"; //$NON-NLS-1$

    /**
     * The base URL used to access the Mule services.
     */
    private static final String MULE_SERVICE_BASE_URL = PROPERTY_NAME_PREFIX + "muleServiceBaseUrl"; //$NON-NLS-1$

    /**
     * Properties key of the context click enabled option
     */

    private static final String CONTEXT_CLICK_ENABLED = PROPERTY_NAME_PREFIX + "contextMenu.enabled";//$NON-NLS-1$

    /**
     * The base URL used to access the DE Unprotected Mule services.
     */
    private String unproctedMuleServiceBaseUrl;

    private String muleServiceBaseUrl;

    private boolean contextClickEnabled;

    /**
     * Gets the base URL used to access the DE Mule services.
     * 
     * @return the URL as a string.
     */
    public String getMuleServiceBaseUrl() {
        return muleServiceBaseUrl;
    }

    public static ToolIntegrationAdminProperties getInstance() {

        if (instance == null) {
            instance = new ToolIntegrationAdminProperties();
        }
        return instance;
    }

    /**
     * @return the unproctedMuleServiceBaseUrl
     */
    public String getUnproctedMuleServiceBaseUrl() {
        return unproctedMuleServiceBaseUrl;
    }

    /**
     * @return the contextClickEnabled
     */
    public boolean isContextClickEnabled() {
        return contextClickEnabled;
    }

    /**
     * Initializes this class from the given set of properties.
     * 
     * @param properties the properties that were fetched from the server.
     */
    public void initialize(Map<String, String> properties) {
        muleServiceBaseUrl = properties.get(MULE_SERVICE_BASE_URL);
        unproctedMuleServiceBaseUrl = properties.get(UNPROTECTED_MULE_SERVICE_BASE_URL);

        try {
            contextClickEnabled = Boolean.parseBoolean(properties.get(CONTEXT_CLICK_ENABLED));
        } catch (Exception e) {
            contextClickEnabled = false;
        }
    }

}
