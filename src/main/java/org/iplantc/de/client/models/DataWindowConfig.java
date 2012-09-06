/**
 * 
 */
package org.iplantc.de.client.models;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.models.WindowConfig;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.de.client.utils.DataUtils;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * @author sriram
 * 
 */
public class DataWindowConfig extends WindowConfig {

    /**
     * 
     */
    private static final long serialVersionUID = -676073321285063262L;
    public static final String FOLDER_ID = "folderId"; //$NON-NLS-1$
    public static final String DISK_RESOURCE_IDS = "diskresourceIds"; //$NON-NLS-1$

    public DataWindowConfig(JSONObject json) {
        super(json);
    }

    /**
     * Returns the ID of the folder that should be selected in the Navigation panel.
     * 
     * @return
     */
    public String getFolderId() {
        return JsonUtil.getRawValueAsString(get(FOLDER_ID));
    }

    /**
     * Sets the ID of the folder that should be selected in the Navigation panel.
     * 
     * @param folderId
     */
    public void setFolderId(String folderId) {
        setString(FOLDER_ID, folderId);
    }

    /**
     * Returns the ID's of the disk resources that should be selected in the Data Main panel.
     * 
     * @return
     */
    public JSONArray getDiskResourceIds() {
        return JsonUtil.getArray(this, DISK_RESOURCE_IDS);
    }

    /**
     * Sets the ID of the disk resource that should be selected in the Data Main panel.
     * 
     * @param appId
     */
    public void setDiskResourceIds(List<DiskResource> resources) {
        put(DISK_RESOURCE_IDS,
                JsonUtil.buildArrayFromStrings(DataUtils.getDiskResourceIdList(resources)));
    }

}
