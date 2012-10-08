/**
 * 
 */
package org.iplantc.de.client.models;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.models.WindowConfig;
import org.iplantc.core.uidiskresource.client.models.FileIdentifier;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;

/**
 * @author sriram
 * 
 */
public class ViewerWindowConfig extends WindowConfig {

    public static String FILE_ID = "fileId";
    public static String TREE_TAB = "treeTab";
    public static String FILE_NAME = "fileName";
    public static String FILE_PARENT = "fileParent";

    public ViewerWindowConfig() {
        super();
    }

    public ViewerWindowConfig(JSONObject config) {
        super(config);
    }

    public void setFileIdentifier(FileIdentifier fd) {
        setString(FILE_ID, fd.getFileId());
        setString(FILE_NAME, fd.getFilename());
        setString(FILE_PARENT, fd.getParentId());
    }

    public FileIdentifier getFileIdentifier() {
        FileIdentifier fd = new FileIdentifier(JsonUtil.getRawValueAsString(get(FILE_NAME)),
                JsonUtil.getRawValueAsString(get(FILE_PARENT)),
                JsonUtil.getRawValueAsString(get(FILE_ID)));
        return fd;
    }

    public void setShowTreeTab(boolean treetab) {
        put(TREE_TAB, JSONBoolean.getInstance(treetab));
    }

    public boolean isShowTreeTab() {
        return JsonUtil.getBoolean(this, TREE_TAB, false);
    }

    @Override
    public String getTagSuffix() {
        return "#" + JsonUtil.getRawValueAsString(get(FILE_ID));
    }

}
