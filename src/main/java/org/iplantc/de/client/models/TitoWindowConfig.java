package org.iplantc.de.client.models;

import com.google.gwt.json.client.JSONObject;

@SuppressWarnings("nls")
public class TitoWindowConfig extends WindowConfig {
    private static final long serialVersionUID = 2475114222792415666L;

    public static final String APP_ID = "appID";
    public static final String VIEW = "view";
    public static final String VIEW_NEW_TOOL = "new_tool";
    public static final String VIEW_NEW_INTERFACE = "new_interface";
    public static final String VIEW_NEW_WORKFLOW = "new_workflow";
    public static final String VIEW_APP_EDIT = "app_edit";
    public static final String VIEW_APP_COPY = "app_copy";

    public TitoWindowConfig(JSONObject json) {
        super(json);
    }

    /**
     * Returns the initial view that should be displayed in Tito.
     * 
     * @return
     */
    public String getView() {
        return get(VIEW);
    }

    /**
     * Returns the Tito ID of the application that should be loaded in the window.
     * 
     * @return
     */
    public String getAppId() {
        return get(APP_ID);
    }
}
