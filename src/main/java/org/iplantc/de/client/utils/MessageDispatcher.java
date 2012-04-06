package org.iplantc.de.client.utils;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.commands.EventDispatchCommand;
import org.iplantc.de.client.events.MessagePayloadEvent;
import org.iplantc.de.client.factories.EventFactory;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

/**
 * This class is responsible for taking in JSON messages, converting them to events and firing them off.
 * 
 * @author amuir
 * 
 */
public class MessageDispatcher {
    private static MessageDispatcher instance;
    private EventDispatchCommand cmdDispatch;

    /**
     * Retrieve singleton instance.
     * 
     * @return the singleton instance.
     */
    public static MessageDispatcher getInstance() {
        if (instance == null) {
            instance = new MessageDispatcher();
        }

        return instance;
    }

    /**
     * Set our dispatch command. This is used by unit tests as an alternative to firing events on the
     * event bus.
     * 
     * @param cmdDispatch command that's executed when a valid event has been instantiated and requires
     *            dispatching. If none is specified, the event is fired on the event bus.
     */
    public void setDispatchCommand(EventDispatchCommand cmdDispatch) {
        this.cmdDispatch = cmdDispatch;
    }

    private JSONArray getMessages(JSONObject jsonObj) {
        JSONArray ret = null;

        if (jsonObj != null) {
            JSONValue val = jsonObj.get("messages"); //$NON-NLS-1$

            if (val != null) {
                ret = val.isArray();
            }
        }

        return ret;
    }

    private void dispatch(MessagePayloadEvent<?> event) {
        if (cmdDispatch != null) {
            cmdDispatch.execute(event);
        } else {
            EventBus eventbus = EventBus.getInstance();

            // if we don't have a dispatch command, our default is to
            // fire an event.
            eventbus.fireEvent(event);
        }
    }

    /**
     * Process method takes in a JSON String, breaks out the individual messages, transforms them into
     * events, finally the event is fired.
     * 
     * @param json string to be processed.
     */
    public void processMessages(final String json) {
        // did we get a JSON string?
        if (json != null) {
            JSONObject jsonObj = JsonUtil.getObject(json);

            // did our string parse?
            if (jsonObj != null) {
                // get our notifications
                JSONArray messages = getMessages(jsonObj);

                // do we have any notifications?
                if (messages != null) {
                    // loop through our notifications
                    for (int i = 0,len = messages.size(); i < len; i++) {
                        // build an event
                        EventFactory factory = EventFactory.getInstance();
                        MessagePayloadEvent<?> event = factory.build(JsonUtil.getObjectAt(messages, i));

                        dispatch(event);
                    }
                }
            }
        }
    }

    /**
     * Process a single message.
     * 
     * @param json message JSON.
     */
    public void processMessage(final JSONObject json) {
        dispatch(EventFactory.getInstance().build(json));
    }
}
