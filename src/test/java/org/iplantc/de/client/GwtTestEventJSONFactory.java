package org.iplantc.de.client;

import org.iplantc.de.client.factories.EventJSONFactory;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestEventJSONFactory extends GWTTestCase {
    private void validateJSON(final String json, final String typeExpected, boolean hasPayload) {
        assertNotNull(json);

        JSONValue valEvent = JSONParser.parseStrict(json);
        assertEquals(valEvent.isNull(), null);

        JSONObject objEvent = valEvent.isObject();
        assertEquals(objEvent.isNull(), null);

        JSONValue valType = objEvent.get("type"); //$NON-NLS-1$
        assertTrue(valType.isString() != null);

        String type = valType.isString().stringValue();
        assertTrue(type.equals(typeExpected));

        if (hasPayload) {
            JSONValue valPayload = objEvent.get("payload"); //$NON-NLS-1$
            assertEquals(valPayload.isNull(), null);

            JSONObject objPayload = valPayload.isObject();
            assertEquals(objPayload.isNull(), null);
        }
    }

    private void validateJSON(final String json, final String typeExpected) {
        validateJSON(json, typeExpected, true);
    }

    public void testBuildNullJSON() {
        String test = EventJSONFactory.build(EventJSONFactory.ActionType.DISPLAY_VIEWER_WINDOWS, null);

        assertNull(test);
    }

    public void testBuildValidViewerWindowJSON() {
        String json = "{\"files\": [{\"info\": \"file_info\"}]}"; //$NON-NLS-1$

        String test = EventJSONFactory.build(EventJSONFactory.ActionType.DISPLAY_VIEWER_WINDOWS, json);

        validateJSON(test, "window"); //$NON-NLS-1$
    }

    public void testBuildValidWindowJSON() {
        String json = "{\"tag\": \"some_tag\"}"; //$NON-NLS-1$

        String test = EventJSONFactory.build(EventJSONFactory.ActionType.DISPLAY_WINDOW, json);

        validateJSON(test, "window"); //$NON-NLS-1$
    }

    public void testBuildUploadCompleteJSON() {
        String json = "{\"created\":[{\"description\":\"\",\"id\":\"1793\",\"name\":\"dondon.nex\",\"status\":\"PENDING\",\"type\":\"Unknown\",\"uploaded\":\"2010-12-14 10:01:41\",\"url\":\"\"}]}"; //$NON-NLS-1$

        String test = EventJSONFactory.build(EventJSONFactory.ActionType.UPLOAD_COMPLETE, json);

        validateJSON(test, "data"); //$NON-NLS-1$
    }

    public void testBuildFolderCreatedJSON() {
        String json = "{\"id\": \"3328\", \"name\": \"foo\"}"; //$NON-NLS-1$

        String test = EventJSONFactory.build(EventJSONFactory.ActionType.FOLDER_CREATED, json);

        validateJSON(test, "data"); //$NON-NLS-1$
    }

    public void testBuildSaveAsJSON() {
        String json = "{\"created\": [{\"description\":\"\", \"id\":\"3843\", \"name\":\"Gene Tree- pg00892\", \"status\":\"PENDING\", \"type\":\"Unknown\", \"uploaded\":\"2010-12-14 10:11:04\", \"url\":\"\"}], \"idParent\": \"3328\", \"idOrig\": \"null\"}"; //$NON-NLS-1$

        String test = EventJSONFactory.build(EventJSONFactory.ActionType.SAVE_AS, json);

        validateJSON(test, "data"); //$NON-NLS-1$
    }

    public void testBuildFileRenamedJSON() {
        String json = "{\"id\": \"3842\", \"name\": \"Foodles\"}"; //$NON-NLS-1$

        String test = EventJSONFactory.build(EventJSONFactory.ActionType.FILE_RENAMED, json);

        validateJSON(test, "data"); //$NON-NLS-1$
    }

    public void testBuildFolderRenamedJSON() {
        String json = "{\"id\": \"3843\", \"name\": \"foo\"}"; //$NON-NLS-1$

        String test = EventJSONFactory.build(EventJSONFactory.ActionType.FOLDER_RENAMED, json);

        validateJSON(test, "data"); //$NON-NLS-1$
    }

    public void testBuildDeleteJSON() {
        String json = "{\"files\": [], \"folders\": [\"3328\"]}"; //$NON-NLS-1$

        String test = EventJSONFactory.build(EventJSONFactory.ActionType.DELETE, json);

        validateJSON(test, "data"); //$NON-NLS-1$
    }

    public void testBuildJobLaunchedJSON() {
        String json = "{\"name\": \"foo\", \"id\": \"id_analysis\"}"; //$NON-NLS-1$

        String test = EventJSONFactory.build(EventJSONFactory.ActionType.JOB_LAUNCHED, json);

        validateJSON(test, "analysis", false); //$NON-NLS-1$
    }

    public void testBuildLogoutJSON() {
        String test = EventJSONFactory.build(EventJSONFactory.ActionType.LOGOUT, "{}"); //$NON-NLS-1$

        validateJSON(test, "system"); //$NON-NLS-1$
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }
}
