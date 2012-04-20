package org.iplantc.de.client.models;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.json.client.JSONObject;

/**
 * A Window config for the simple download window.
 * 
 * @author psarando
 * 
 */
public class SimpleDownloadWindowConfig extends WindowConfig {
    public static String DOWNLOAD_PATHS = "paths"; //$NON-NLS-1$

    public SimpleDownloadWindowConfig() {
        super(null);
    }

    public SimpleDownloadWindowConfig(JSONObject json) {
        super(json);
    }

    /**
     * Stores the list of file paths for download.
     * 
     * @param paths
     */
    public void setDownloadPaths(List<String> paths) {
        put(DOWNLOAD_PATHS, JsonUtil.buildArrayFromStrings(paths));
    }

    /**
     * Gets the list of file paths for download.
     * 
     * @return
     */
    public List<String> getDownloadPaths() {
        return JsonUtil.buildStringList(JsonUtil.getArray(this, DOWNLOAD_PATHS));
    }
}
