package org.iplantc.de.client;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.events.AnalysisPayloadEvent;
import org.iplantc.de.client.events.MessagePayloadEvent;
import org.iplantc.de.client.factories.EventFactory;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestEventFactory extends GWTTestCase {
    public void testNullJsonObject() {
        JSONObject obj = null;
        MessagePayloadEvent<?> event = EventFactory.getInstance().build(obj);

        assertNull(event);
    }

    public void testNullJsonString() {
        String json = null;
        MessagePayloadEvent<?> event = EventFactory.getInstance().build(json);

        assertNull(event);
    }

    public void testAnalysisPayloadEventBuild() {
        String json = "{\"type\": \"analysis\", " //$NON-NLS-1$
                + "\"message\": {\"id\": \"someId\", \"text\": \"here's a message\"}, " //$NON-NLS-1$
                + "\"payload\": {\"id\": \"payloadId\"}" + "}"; //$NON-NLS-1$ //$NON-NLS-2$

        JSONObject jsonObj = JsonUtil.getObject(json);

        MessagePayloadEvent<?> event = EventFactory.getInstance().build(jsonObj);

        assertNotNull(event);
        assertTrue(event instanceof AnalysisPayloadEvent);
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }
}
