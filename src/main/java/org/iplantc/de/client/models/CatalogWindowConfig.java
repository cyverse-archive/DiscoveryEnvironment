package org.iplantc.de.client.models;

import org.iplantc.de.client.views.windows.DECatalogWindow;

import com.google.gwt.json.client.JSONObject;

/**
 * A {@link WindowConfig} used by {@link DECatalogWindow}.
 * @author hariolf
 *
 */
public class CatalogWindowConfig extends BasicWindowConfig {
    private static final long serialVersionUID = 5958689143280824320L;

    /**
     * Creates a new CatalogWindowConfig.
     * @param json
     */
    public CatalogWindowConfig(JSONObject json) {
        super(json);
    }
    
    /**
     * Returns the tito ID of the application that should be selected in the catalog.
     * @return
     */
    public String getAppID() {
        return get("appID"); //$NON-NLS-1$
    }
}
