package org.iplantc.de.client.models;

import org.iplantc.de.client.views.windows.DECatalogWindow;

import com.google.gwt.json.client.JSONObject;

/**
 * A {@link WindowConfig} used by {@link DECatalogWindow}.
 * 
 * @author hariolf
 * 
 */
public class CatalogWindowConfig extends BasicWindowConfig {
    private static final long serialVersionUID = 5958689143280824320L;

    public static final String CATEGORY_ID = "categoryID"; //$NON-NLS-1$
    public static final String APP_ID = "appID"; //$NON-NLS-1$

    /**
     * Creates a new CatalogWindowConfig.
     * 
     * @param json
     */
    public CatalogWindowConfig(JSONObject json) {
        super(json);
    }

    /**
     * Returns the ID of the category that should be selected in the catalog.
     * 
     * @return
     */
    public String getCategoryId() {
        return get(CATEGORY_ID);
    }

    /**
     * Sets the ID of the category that should be selected in the catalog.
     * 
     * @param categoryId
     */
    public void setCategoryId(String categoryId) {
        set(CATEGORY_ID, categoryId);
    }

    /**
     * Returns the tito ID of the application that should be selected in the catalog.
     * 
     * @return
     */
    public String getAppId() {
        return get(APP_ID);
    }

    /**
     * Sets the tito ID of the application that should be selected in the catalog.
     * 
     * @param appId
     */
    public void setAppId(String appId) {
        set(APP_ID, appId);
    }
}
