package org.iplantc.de.client.models;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.utils.NotificationManager.Category;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * WindowConfig for notification windows.
 */
public class NotificationWindowConfig extends WindowConfig {
    public static final String CATEGORY = "category"; //$NON-NLS-1$
    public static final String SELECTED_IDS = "selectedIds"; //$NON-NLS-1$

    private static final long serialVersionUID = 6533350718698752311L;

    /**
     * Creates a NotificationWindowConfig from the JSON parameter "category". This JSON parameter and any
     * others in the JSON object are additionally added as BaseModelData parameters.
     * 
     * @param json a JSON string containing at least a key named "category"
     */
    public NotificationWindowConfig(JSONObject json) {
        super(json);
    }

    /**
     *  
     */
    public NotificationWindowConfig() {
        super();
    }

    /**
     * Returns the notification category.
     * 
     * @return the category
     */
    public Category getCategory() {
        return Category.fromTypeString(JsonUtil.getRawValueAsString(get(CATEGORY)));
    }

    public void setCategory(Category category) {
        setString(CATEGORY, category.toString());
    }

    public JSONArray getSelectedIds() {
        return JsonUtil.getArray(this, SELECTED_IDS);
    }

    public void setSelectedIds(List<Notification> notifications) {
        List<String> selectedIds = null;

        if (notifications != null) {
            selectedIds = new ArrayList<String>();
            for (Notification n : notifications) {
                selectedIds.add(n.getId());
            }
        }

        put(SELECTED_IDS, JsonUtil.buildArrayFromStrings(selectedIds));
    }
}
