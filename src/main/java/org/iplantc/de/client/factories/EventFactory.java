package org.iplantc.de.client.factories;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.events.AnalysisPayloadEvent;
import org.iplantc.de.client.events.DataPayloadEvent;
import org.iplantc.de.client.events.MessagePayloadEvent;
import org.iplantc.de.client.events.SystemPayloadEvent;
import org.iplantc.de.client.events.WindowPayloadEvent;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * Factory class to build an event from a JSON object.
 * 
 * @author amuir
 * 
 */
public class EventFactory {

    private static EventFactory instance;

    private EventFactory() {

    }

    public static EventFactory getInstance() {
        if (instance == null) {
            instance = new EventFactory();
        }

        return instance;
    }

    private String getType(final JSONObject jsonEvent) {
        String ret = null;

        if (jsonEvent != null) {
            JSONValue jsonVal = jsonEvent.get("type"); //$NON-NLS-1$

            if (jsonVal != null) {
                JSONString json = jsonVal.isString();

                if (json != null) {
                    ret = json.stringValue();
                }
            }
        }

        return ret;
    }

    /**
     * Build a payload event of the correct type from a JSONObject
     * 
     * @param jsonEvent the full event JSON object
     * @return A new event that descends from MessagePayloadEvent. null on failure.
     */
    public MessagePayloadEvent<?> build(final JSONObject jsonEvent) {
        MessagePayloadEvent<?> ret = null; // assume failure

        // did we get a notification?
        if (jsonEvent != null) {
            String type = getType(jsonEvent);

            // if we don't have a type, we don't know what to build.
            if (type != null) {
                JSONObject jsonMsg = JsonUtil.getObject(jsonEvent, "message"); //$NON-NLS-1$
                JSONObject jsonPayload = JsonUtil.getObject(jsonEvent, "payload"); //$NON-NLS-1$

                if (type.equals("data")) { //$NON-NLS-1$
                    ret = new DataPayloadEvent(jsonMsg, jsonPayload);
                } else if (type.equals("analysis")) { //$NON-NLS-1$
                    ret = new AnalysisPayloadEvent(jsonMsg, jsonPayload);
                } else if (type.equals("window")) { //$NON-NLS-1$
                    ret = new WindowPayloadEvent(jsonMsg, jsonPayload);
                } else if (type.equals("system")) { //$NON-NLS-1$
                    ret = new SystemPayloadEvent(jsonMsg, jsonPayload);
                }
            }
        }

        return ret;
    }
}
