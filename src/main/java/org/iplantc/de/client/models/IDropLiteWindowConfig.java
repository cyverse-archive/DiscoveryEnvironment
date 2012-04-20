package org.iplantc.de.client.models;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.de.client.utils.IDropLite;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
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
    public static String TAG_SUFFIX = "tag_suffix"; //$NON-NLS-1$
    public static String TAG_SUFFIX_UPLOAD = "_upload"; //$NON-NLS-1$
    public static String TAG_SUFFIX_DOWNLOAD = "_download"; //$NON-NLS-1$

    public IDropLiteWindowConfig() {
        super(new JSONObject());
    }

    public IDropLiteWindowConfig(JSONObject json) {
        super(json);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTagSuffix() {
        return JsonUtil.getString(this, TAG_SUFFIX);
    }

    public int getDisplayMode() {
        Number mode = JsonUtil.getNumber(this, DISPLAY_MODE);

        if (mode != null) {
            return mode.intValue();
        }

        return 0;
    }

    /**
     * Sets the display mode, and also sets the appropriate TAG_SUFFIX value based on this mode.
     * 
     * @param mode
     */
    public void setDisplayMode(int mode) {
        put(DISPLAY_MODE, new JSONNumber(mode));

        if (mode == IDropLite.DISPLAY_MODE_UPLOAD) {
            setString(TAG_SUFFIX, TAG_SUFFIX_UPLOAD);
        } else if (mode == IDropLite.DISPLAY_MODE_DOWNLOAD) {
            setString(TAG_SUFFIX, TAG_SUFFIX_DOWNLOAD);
        }
    }

    public String getUploadDest() {
        return JsonUtil.getRawValueAsString(get(UPLOAD_DEST));
    }

    public void setUploadDest(String uploadDest) {
        setString(UPLOAD_DEST, uploadDest);
    }

    public String getCurrentPath() {
        return JsonUtil.getRawValueAsString(get(MANAGE_DATA_CURRENT_PATH));
    }

    public void setCurrentPath(String currentPath) {
        setString(MANAGE_DATA_CURRENT_PATH, currentPath);
    }

    public JSONArray getDownloadPaths() {
        return JsonUtil.getArray(this, DOWNLOAD_PATHS);
    }

    /**
     * Stores the IDs (paths) from the given DiskResource list as a JSON array.
     * 
     * @param resources
     */
    public void setDownloadPaths(List<DiskResource> resources) {
        if (resources == null) {
            put(DOWNLOAD_PATHS, null);
        } else {
            ArrayList<String> resourceIds = new ArrayList<String>();

            for (DiskResource resource : resources) {
                resourceIds.add(resource.getId());
            }

            put(DOWNLOAD_PATHS, JsonUtil.buildArrayFromStrings(resourceIds));
        }

    }
}
