package org.iplantc.admin.belphegor.client.models;

import java.util.Arrays;
import java.util.Map;

import com.extjs.gxt.ui.client.core.FastMap;

public class ToolIntegrationAdminProperties {

    private static ToolIntegrationAdminProperties instance;
    private FastMap<String> serviceUrlMap;

    /**
     * The prefix used in each of the property names.
     */
    private static final String PROPERTY_NAME_PREFIX = "org.iplantc.belphegor."; //$NON-NLS-1$

    /**
     * The URL used to access the App Groups service.
     */
    private static final String SERVICE_URL_CATEGORY_LIST = PROPERTY_NAME_PREFIX + "get-app-groups"; //$NON-NLS-1$

    /**
     * The URL used to access the Apps by Group service.
     */
    private static final String SERVICE_URL_CATEGORY_APPS = PROPERTY_NAME_PREFIX + "get-apps-in-group"; //$NON-NLS-1$

    /**
     * The URL used to access the Add Category service.
     */
    private static final String SERVICE_URL_CATEGORY_ADD = PROPERTY_NAME_PREFIX + "add-category"; //$NON-NLS-1$

    /**
     * The URL used to access the Rename Category service.
     */
    private static final String SERVICE_URL_CATEGORY_RENAME = PROPERTY_NAME_PREFIX + "rename-category"; //$NON-NLS-1$

    /**
     * The URL used to access the Move Category service.
     */
    private static final String SERVICE_URL_CATEGORY_MOVE = PROPERTY_NAME_PREFIX + "move-category"; //$NON-NLS-1$

    /**
     * The URL used to access the Delete Category service.
     */
    private static final String SERVICE_URL_CATEGORY_DELETE = PROPERTY_NAME_PREFIX + "delete-category"; //$NON-NLS-1$

    /**
     * The URL used to access the Update App service.
     */
    private static final String SERVICE_URL_APP_UPDATE = PROPERTY_NAME_PREFIX + "update-app"; //$NON-NLS-1$

    /**
     * The URL used to access the Move App service.
     */
    private static final String SERVICE_URL_APP_MOVE = PROPERTY_NAME_PREFIX + "move-app"; //$NON-NLS-1$

    /**
     * The URL used to access the Delete App service.
     */
    private static final String SERVICE_URL_APP_DELETE = PROPERTY_NAME_PREFIX + "delete-app"; //$NON-NLS-1$

    /**
     * Properties key of the default Beta Category ID.
     */
    private static final String CATEGORY_DEFAULT_BETA_GROUP_ID = PROPERTY_NAME_PREFIX
            + "category.defaultBetaAnalysisGroupId";//$NON-NLS-1$

    /**
     * Properties key of the context click enabled option
     */
    private static final String CONTEXT_CLICK_ENABLED = PROPERTY_NAME_PREFIX + "contextMenu.enabled";//$NON-NLS-1$

    private String defaultBetaAnalysisGroupId;

    private boolean contextClickEnabled;

    public static ToolIntegrationAdminProperties getInstance() {

        if (instance == null) {
            instance = new ToolIntegrationAdminProperties();
        }
        return instance;
    }

    /**
     * Initializes this class from the given set of properties.
     * 
     * @param properties the properties that were fetched from the server.
     */
    public void initialize(Map<String, String> properties) {
        serviceUrlMap = new FastMap<String>();

        for (String key : Arrays.asList(SERVICE_URL_CATEGORY_ADD, SERVICE_URL_CATEGORY_RENAME,
                SERVICE_URL_CATEGORY_MOVE, SERVICE_URL_CATEGORY_DELETE, SERVICE_URL_CATEGORY_LIST,
                SERVICE_URL_CATEGORY_APPS, SERVICE_URL_APP_UPDATE, SERVICE_URL_APP_MOVE,
                SERVICE_URL_APP_DELETE)) {
            serviceUrlMap.put(key, properties.get(key));
        }

        defaultBetaAnalysisGroupId = properties.get(CATEGORY_DEFAULT_BETA_GROUP_ID);

        try {
            contextClickEnabled = Boolean.parseBoolean(properties.get(CONTEXT_CLICK_ENABLED));
        } catch (Exception e) {
            contextClickEnabled = false;
        }
    }

    /**
     * Gets the Add Category service URL.
     * 
     * @return the URL as a string.
     */
    public String getAddCategoryServiceUrl() {
        return serviceUrlMap.get(SERVICE_URL_CATEGORY_ADD);
    }

    /**
     * Gets the Rename Category service URL.
     * 
     * @return the URL as a string.
     */
    public String getRenameCategoryServiceUrl() {
        return serviceUrlMap.get(SERVICE_URL_CATEGORY_RENAME);
    }

    /**
     * Gets the Move Category service URL.
     * 
     * @return the URL as a string.
     */
    public String getMoveCategoryServiceUrl() {
        return serviceUrlMap.get(SERVICE_URL_CATEGORY_MOVE);
    }

    /**
     * Gets the Delete Category service URL.
     * 
     * @return the URL as a string.
     */
    public String getDeleteCategoryServiceUrl() {
        return serviceUrlMap.get(SERVICE_URL_CATEGORY_DELETE);
    }

    /**
     * Gets the Category Listing service URL.
     * 
     * @return the URL as a string.
     */
    public String getCategoryListServiceUrl() {
        return serviceUrlMap.get(SERVICE_URL_CATEGORY_LIST);
    }

    /**
     * Gets the Apps-by-Category service URL.
     * 
     * @return the URL as a string.
     */
    public String getAppsInCategoryServiceUrl() {
        return serviceUrlMap.get(SERVICE_URL_CATEGORY_APPS);
    }

    /**
     * Gets the Update App service URL.
     * 
     * @return the URL as a string.
     */
    public String getUpdateAppServiceUrl() {
        return serviceUrlMap.get(SERVICE_URL_APP_UPDATE);
    }

    /**
     * Gets the Move App service URL.
     * 
     * @return the URL as a string.
     */
    public String getMoveAppServiceUrl() {
        return serviceUrlMap.get(SERVICE_URL_APP_MOVE);
    }

    /**
     * Gets the Delete App service URL.
     * 
     * @return the URL as a string.
     */
    public String getDeleteAppServiceUrl() {
        return serviceUrlMap.get(SERVICE_URL_APP_DELETE);
    }

    /**
     * Gets the default Beta Category ID.
     * 
     * @return the Beta Category ID as a string.
     */
    public String getDefaultBetaAnalysisGroupId() {
        return defaultBetaAnalysisGroupId;
    }

    /**
     * @return the contextClickEnabled
     */
    public boolean isContextClickEnabled() {
        return contextClickEnabled;
    }
}
