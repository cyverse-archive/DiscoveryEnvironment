package org.iplantc.de.client.models;

import org.iplantc.de.client.utils.NotificationManager.Category;

import com.google.gwt.json.client.JSONObject;

/**
 * WindowConfig for notification windows.
 */
public class NotificationWindowConfig extends WindowConfig {
    private static final long serialVersionUID = 6533350718698752311L;

    private Category category;

    /**
     * Creates a NotificationWindowConfig from the JSON parameter "category". This JSON parameter and any
     * others in the JSON object are additionally added as BaseModelData parameters.
     * 
     * @param json a JSON string containing at least a key named "category"
     */
    public NotificationWindowConfig(JSONObject json) {
        super(json);
        String categoryString = get("category"); //$NON-NLS-1$
        category = Category.fromTypeString(categoryString);
    }

    /**
     * Returns the notification category.
     * 
     * @return the category
     */
    public Category getCategory() {
        return category;
    }
}
