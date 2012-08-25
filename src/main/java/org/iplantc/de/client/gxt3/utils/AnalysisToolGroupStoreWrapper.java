package org.iplantc.de.client.gxt3.utils;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.models.AnalysisGroup;
import org.iplantc.core.uiapplications.client.models.AnalysisGroupTreeModel;

import com.extjs.gxt.ui.client.store.TreeStore;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * Provides a wrapper around a Analysis TreeStore.
 * 
 * @see com.extjs.gxt.ui.client.store.TreeStore
 */
public class AnalysisToolGroupStoreWrapper {
    private TreeStore<AnalysisGroupTreeModel> store;

    /**
     * Creates a new AnalysisToolGroupStoreWrapper.
     */
    public AnalysisToolGroupStoreWrapper() {
        store = new TreeStore<AnalysisGroupTreeModel>();
    }

    /**
     * @return the store
     */
    public TreeStore<AnalysisGroupTreeModel> getStore() {
        return store;
    }

    /**
     * Updates the wrapper with JSON data.
     * 
     * @param json
     */
    public void updateWrapper(String json) {
        updateWrapper(json, false);
    }

    /**
     * Updates the wrapper with JSON data, with the option to only update with public groups.
     * 
     * @param json
     * @param publicOnly
     */
    public void updateWrapper(String json, boolean publicOnly) {
        store.removeAll();

        if (json != null) {
            JSONArray groups = JsonUtil.getArray(JsonUtil.getObject(json), "groups"); //$NON-NLS-1$

            if (groups != null) {
                for (int i = 0; i < groups.size(); i++) {
                    JSONObject objGrp = JsonUtil.getObjectAt(groups, i);
                    addToolGroup(null, objGrp, publicOnly);
                }
            }
        }
    }
    
    private void addToolGroup(AnalysisGroup parent, JSONObject group, boolean publicOnly) {
        if (group != null) {
            AnalysisGroup analysisGroup = new AnalysisGroup(group);

            if (analysisGroup.isPublic() || !publicOnly) {
                if (parent == null) {
                    store.add(analysisGroup, true);
                } else {
                    analysisGroup.setParent(parent);
                    parent.add(analysisGroup);
                    store.add(parent, analysisGroup, true);
                }

                JSONArray subgroups = JsonUtil.getArray(group, "groups");
                if (subgroups != null) {
                    // loop through our sub-folders and recursively add them
                    for (int i = 0,len = subgroups.size(); i < len; i++) {
                        JSONObject subgroup = JsonUtil.getObjectAt(subgroups, i);
                        addToolGroup(analysisGroup, subgroup, publicOnly);
                    }
                }
            }
        }
    }

}
