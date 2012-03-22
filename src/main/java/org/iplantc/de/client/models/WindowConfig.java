package org.iplantc.de.client.models;

import org.iplantc.core.jsonutil.JsonUtil;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Base class for window configurations. A window configuration is specific to a type of window
 * (Notifications, Analysis, etc.) and describes how a window should be presented. For example, data
 * could be filtered, or certain UI elements could be disabled.
 * 
 * @author hariolf
 * 
 */
public abstract class WindowConfig extends BaseModelData {
    private static final long serialVersionUID = 3602295075858973528L;

    /**
     * Constructs a WindowConfig and adds all JSON key/value pairs as BaseModelData parameters.
     * 
     * @param json
     */
    protected WindowConfig(JSONObject json) {
        for (String key : json.keySet()) {
            String value = JsonUtil.getRawValueAsString(json.get(key));

            set(key, value);
        }
    }

    /**
     * Returns a string that identifies the window instance, if appropriate. This implementation returns
     * an empty string.
     * 
     * @return
     */
    public String getTagSuffix() {
        return ""; //$NON-NLS-1$
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        for (String key : getPropertyNames()) {
            String value = get(key);
            if (value != null) {
                json.put(key, new JSONString(value));
            }
        }

        return json;
    }
}
