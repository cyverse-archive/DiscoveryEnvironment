package org.iplantc.de.client.models;

import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.json.client.JSONObject;

@SuppressWarnings("nls")
public class TitoWindowConfig extends WindowConfig {
    private static final long serialVersionUID = 2475114222792415666L;

    public static final String APP_ID = "appID";
    public static final String VIEW = "view";
    public static final String VIEW_NEW_TOOL = "new_tool";
    public static final String VIEW_APP_EDIT = "app_edit";
    public static final String VIEW_APP_EDIT_FROM_JSON = "app_edit_json";
    public static final String APP_JSON = "appJson";

    public TitoWindowConfig(JSONObject json) {
        super(json);
    }

    /**
     * Returns the initial view that should be displayed in Tito.
     * 
     * @return
     */
    public String getView() {
        return JsonUtil.getRawValueAsString(get(VIEW));
    }

    public void setView(String view) {
        setString(VIEW, view);
    }

    /**
     * Returns the Tito ID of the application that should be loaded in the window.
     * 
     * @return
     */
    public String getAppId() {
        return JsonUtil.getRawValueAsString(get(APP_ID));
    }

    /**
     * Returns the json associated with an app
     * 
     * @return
     */
    public JSONObject getAppJson() {
        return JsonUtil.getObject(this, APP_JSON);
    }

    public void setAppJson(JSONObject appJson) {
        put(APP_JSON, appJson);
    }
}
