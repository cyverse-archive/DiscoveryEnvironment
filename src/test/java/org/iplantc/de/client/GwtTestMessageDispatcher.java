package org.iplantc.de.client;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.commands.EventDispatchCommand;
import org.iplantc.de.client.events.AnalysisPayloadEvent;
import org.iplantc.de.client.utils.MessageDispatcher;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestMessageDispatcher extends GWTTestCase {
    public void testProcessValidMultiple() {
        String json = "{\"messages\": [{\"type\": \"analysis\", " //$NON-NLS-1$
                + "\"message\": {\"id\": \"someId\", \"text\": \"here's a message\"}, " //$NON-NLS-1$
                + "\"payload\": {\"id\": \"payloadId\"}}, " + "{\"type\": \"analysis\", " //$NON-NLS-1$ //$NON-NLS-2$
                + "\"message\": {\"id\": \"anotherId\", \"text\": \"here's another message\"}, " //$NON-NLS-1$
                + "\"payload\": {\"id\": \"anotherPayloadId\"}}" + "]" + "}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        MessageDispatcher dispatcher = MessageDispatcher.getInstance();
        dispatcher.setDispatchCommand(new BasicValidatingDispatchCommand());
        dispatcher.processMessages(json);
    }

    public void testProcessValidDeep() {
        String jsonMessage = "{\"id\": \"someId\", \"text\": \"here's a message\"}"; //$NON-NLS-1$
        String jsonPayload = "{\"id\": \"payloadId\"}"; //$NON-NLS-1$
        String json = "{\"messages\": [{\"type\": \"analysis\", " + "\"message\": " + jsonMessage + ", " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "\"payload\": " + jsonPayload + "}]" + "}"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        MessageDispatcher dispatcher = MessageDispatcher.getInstance();
        dispatcher.setDispatchCommand(new DeepValidatingDispatchCommand(jsonMessage, jsonPayload));

        dispatcher.processMessages(json);
    }

    @Override
    public String getModuleName() {
        return "org.iplantc.de.discoveryenvironment"; //$NON-NLS-1$
    }

    class BasicValidatingDispatchCommand implements EventDispatchCommand {
        @Override
        public void execute(GwtEvent<?> event) {
            assertNotNull(event);
            assertTrue(event instanceof AnalysisPayloadEvent);

            AnalysisPayloadEvent jpe = (AnalysisPayloadEvent)event;

            JSONObject payload = jpe.getPayload();
            assertNotNull(payload);

            JSONObject message = jpe.getMessage();
            assertNotNull(message);
        }
    }

    class DeepValidatingDispatchCommand implements EventDispatchCommand {
        private final JSONObject messageIn;
        private final JSONObject payloadIn;

        public DeepValidatingDispatchCommand(final String jsonMessageIn, final String jsonPayloadIn) {
            messageIn = JsonUtil.getObject(jsonMessageIn);
            payloadIn = JsonUtil.getObject(jsonPayloadIn);
        }

        private void validateMessage(final JSONObject message) {
            assertNotNull(message);

            String lval = messageIn.get("id").isString().toString(); //$NON-NLS-1$
            String rval = message.get("id").isString().toString(); //$NON-NLS-1$
            assertTrue(lval.equals(rval));

            lval = messageIn.get("text").isString().toString(); //$NON-NLS-1$
            rval = message.get("text").isString().toString(); //$NON-NLS-1$
            assertTrue(lval.equals(rval));
        }

        private void validatePayload(final JSONObject payload) {
            assertNotNull(payload);

            String lval = payloadIn.get("id").isString().toString(); //$NON-NLS-1$
            String rval = payload.get("id").isString().toString(); //$NON-NLS-1$
            assertTrue(lval.equals(rval));
        }

        @Override
        public void execute(GwtEvent<?> event) {
            assertNotNull(event);
            assertTrue(event instanceof AnalysisPayloadEvent);

            AnalysisPayloadEvent jpe = (AnalysisPayloadEvent)event;

            validateMessage(jpe.getMessage());
            validatePayload(jpe.getPayload());
        }
    }
}
