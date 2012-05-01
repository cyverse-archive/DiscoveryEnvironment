package org.iplantc.de.client;

import org.iplantc.de.client.models.NotificationWindowConfig;
import org.iplantc.de.client.utils.NotificationManager.Category;
import org.junit.Test;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.junit.client.GWTTestCase;

@SuppressWarnings("nls")
public class GwtTestNotificationWindowConfig extends GWTTestCase {

    @Test
    public void testNotificationWindowConfigJSONObject() {
        Category category = Category.ANALYSIS;
        String fooParameter = "blah 12345"; // a parameter not used by NotificationWindowConfig that the
                                            // constructor should still add as a BaseModelObject
                                            // parameter

        String jsonString = "{ \"category\" : \"" + category + "\", \"fooParameter\" : \""
                + fooParameter + "\" }";
        JSONObject json = JSONParser.parseStrict(jsonString).isObject();
        assertNotNull(json);

        NotificationWindowConfig config = new NotificationWindowConfig(json);
        assertEquals(category, config.getCategory());

        JSONValue jsonFoo = config.get("fooParameter");
        assertNotNull(jsonFoo);
        assertNotNull(jsonFoo.isString());
        assertEquals(fooParameter, jsonFoo.isString().stringValue());
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment";
    }

}
