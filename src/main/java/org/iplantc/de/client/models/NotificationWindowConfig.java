package org.iplantc.de.client.models;

import org.iplantc.de.client.utils.NotificationManager.Category;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * WindowConfig for notification windows.
 */
public class NotificationWindowConfig extends WindowConfig {
    public static final String CATEGORY = "category";
    public static final String SELECTED_IDS = "selectedIds";

    private static final long serialVersionUID = 6533350718698752311L;

    /**
     * Creates a NotificationWindowConfig from the JSON parameter "category". This JSON parameter and any
     * others in the JSON object are additionally added as BaseModelData parameters.
     * 
     * @param json a JSON string containing at least a key named "category"
     */
    public NotificationWindowConfig(JSONObject json) {
        super(json);
        String categoryString = get(CATEGORY); //$NON-NLS-1$
        set(CATEGORY, Category.fromTypeString(categoryString));
    }

    /**
     * Returns the notification category.
     * 
     * @return the category
     */
    public Category getCategory() {
        return get(CATEGORY);
    }

    public JSONArray getSelectedIds() {
        if (get(SELECTED_IDS) != null && !get(SELECTED_IDS).toString().isEmpty()) {
            return JSONParser.parseStrict(get(SELECTED_IDS).toString()).isArray();
        } else {
            return null;
        }
    }
}
