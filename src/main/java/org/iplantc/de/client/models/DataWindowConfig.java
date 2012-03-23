/**
 * 
 */
package org.iplantc.de.client.models;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * @author sriram
 * 
 */
public class DataWindowConfig extends BasicWindowConfig {

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
        return get(FOLDER_ID);
    }

    /**
     * Sets the ID of the folder that should be selected in the Navigation panel.
     * 
     * @param folderId
     */
    public void setFolderId(String folderId) {
        set(FOLDER_ID, folderId);
    }

    /**
     * Returns the ID's of the disk resources that should be selected in the Data Main panel.
     * 
     * @return
     */
    public JSONArray getDiskResourceId() {
        return JSONParser.parseStrict(get(DISK_RESOURCE_IDS).toString()).isArray();
    }

    /**
     * Sets the ID of the disk resource that should be selected in the Data Main panel.
     * 
     * @param appId
     */
    public void setDiskResourceIds(String diskresourceId) {
        set(DISK_RESOURCE_IDS, diskresourceId);
    }

}
