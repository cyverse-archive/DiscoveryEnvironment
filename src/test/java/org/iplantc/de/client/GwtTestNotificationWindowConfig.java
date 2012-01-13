package org.iplantc.de.client;

import org.iplantc.de.client.models.NotificationWindowConfig;
import org.iplantc.de.client.utils.NotificationManager.Category;
import org.junit.Test;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestNotificationWindowConfig extends GWTTestCase {

    @Test
    public void testNotificationWindowConfigJSONObject() {
        Category category = Category.ANALYSIS;
        String fooParameter = "blah 12345"; // a parameter not used by NotificationWindowConfig that the //$NON-NLS-1$
                                            // constructor should still add as a BaseModelObject
                                            // parameter

        String jsonString = "{ \"category\" : \"" + category + "\", \"fooParameter\" : \"" //$NON-NLS-1$ //$NON-NLS-2$
                + fooParameter + "\" }"; //$NON-NLS-1$
        JSONObject json = JSONParser.parseStrict(jsonString).isObject();
        assertNotNull(json);
        NotificationWindowConfig config = new NotificationWindowConfig(json);
        assertEquals(category, config.getCategory());
        assertEquals(fooParameter, config.get("fooParameter")); //$NON-NLS-1$
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }

}
