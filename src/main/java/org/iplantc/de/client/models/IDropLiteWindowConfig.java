package org.iplantc.de.client.models;

import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * A Window config for the idrop-lite applet window.
 * 
 * @author psarando
 * 
 */
public class IDropLiteWindowConfig extends WindowConfig {
    private static final long serialVersionUID = 2774828059117261221L;

    public static String DISPLAY_MODE = "displayMode"; //$NON-NLS-1$
    public static String UPLOAD_DEST = "uploadDest"; //$NON-NLS-1$
    public static String DOWNLOAD_PATHS = "paths"; //$NON-NLS-1$
    public static String MANAGE_DATA_CURRENT_PATH = "currentPath"; //$NON-NLS-1$

    public IDropLiteWindowConfig(JSONObject json) {
        super(json);

        setDisplayMode(JsonUtil.getNumber(json, DISPLAY_MODE));
        setDownloadPaths(JsonUtil.getArray(json, DOWNLOAD_PATHS));
    }

    public Number getDisplayMode() {
        return get(DISPLAY_MODE);
    }

    public void setDisplayMode(Number mode) {
        set(DISPLAY_MODE, mode);
    }

    public String getUploadDest() {
        return get(UPLOAD_DEST);
    }

    public String getCurrentPath() {
        return get(MANAGE_DATA_CURRENT_PATH);
    }

    public JSONArray getDownloadPaths() {
        return get(DOWNLOAD_PATHS);
    }

    public void setDownloadPaths(JSONArray paths) {
        set(DOWNLOAD_PATHS, paths);
    }
}
