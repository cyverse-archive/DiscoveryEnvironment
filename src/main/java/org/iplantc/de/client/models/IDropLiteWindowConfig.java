package org.iplantc.de.client.models;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uidiskresource.client.models.DiskResource;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

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
    public static String TAG_SUFFIX = "tag_suffix"; //$NON-NLS-1$

    public IDropLiteWindowConfig(JSONObject json) {
        super(json);

        setDownloadPaths(JsonUtil.getArray(json, DOWNLOAD_PATHS));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTagSuffix() {
        String suffix = get(TAG_SUFFIX);
        return suffix == null ? "" : suffix; //$NON-NLS-1$
    }

    public int getDisplayMode() {
        String mode = get(DISPLAY_MODE);

        try {
            return new Integer(mode);
        } catch (Exception ignore) {
            return 0;
        }
    }

    public void setDisplayMode(int mode) {
        set(DISPLAY_MODE, String.valueOf(mode));
    }

    public String getUploadDest() {
        return get(UPLOAD_DEST);
    }

    public void setUploadDest(String uploadDest) {
        set(UPLOAD_DEST, uploadDest);
    }

    public String getCurrentPath() {
        return get(MANAGE_DATA_CURRENT_PATH);
    }

    public void setCurrentPath(String currentPath) {
        set(MANAGE_DATA_CURRENT_PATH, currentPath);
    }

    public JSONArray getDownloadPaths() {
        String paths = get(DOWNLOAD_PATHS);

        try {
            return JSONParser.parseStrict(paths).isArray();
        } catch (Exception e) {
            return null;
        }
    }

    public void setDownloadPaths(JSONArray paths) {
        set(DOWNLOAD_PATHS, paths == null ? null : paths.toString());
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();

        json.put(DISPLAY_MODE, new JSONNumber(getDisplayMode()));

        JSONArray downloadPaths = getDownloadPaths();
        if (downloadPaths != null) {
            json.put(DOWNLOAD_PATHS, downloadPaths);
        }

        return json;
    }
}
