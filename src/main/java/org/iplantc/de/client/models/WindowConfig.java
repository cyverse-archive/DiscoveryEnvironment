package org.iplantc.de.client.models;

import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Base class for window configurations. A window configuration is specific to a type of window
 * (Notifications, Analysis, etc.) and describes how a window should be presented. For example, data
 * could be filtered, or certain UI elements could be disabled.
 * 
 * @author hariolf, Paul
 * 
 */
public class WindowConfig extends JSONObject {
    private static final long serialVersionUID = 3602295075858973528L;
    public static final String IS_MAXIMIZED = "isMaximized"; //$NON-NLS-1$
    public static final String IS_MINIMIZED = "isMinimized"; //$NON-NLS-1$
    public static final String WIN_LEFT = "win_left"; //$NON-NLS-1$
    public static final String WIN_TOP = "win_top"; //$NON-NLS-1$
    public static final String WIN_WIDTH = "width"; //$NON-NLS-1$
    public static final String WIN_HEIGHT = "height"; //$NON-NLS-1$

    /**
     * Constructs a WindowConfig and adds all JSON key/value pairs as BaseModelData parameters.
     * 
     * @param json
     */
    public WindowConfig(JSONObject json) {
        if (json != null) {
            for (String key : json.keySet()) {
                put(key, json.get(key));
            }
        }
    }

    /**
     * Construct a empty WindowConfig
     * 
     */
    public WindowConfig() {

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

    public boolean isWindowMinimized() {
        return JsonUtil.getBoolean(this, IS_MINIMIZED, false);
    }

    public boolean isWindowMaximized() {
        return JsonUtil.getBoolean(this, IS_MAXIMIZED, false);
    }

    protected void setString(String key, String value) {
        if (key != null) {
            put(key, value == null ? null : new JSONString(value));
        }
    }
}
