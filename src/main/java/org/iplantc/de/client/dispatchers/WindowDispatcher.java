package org.iplantc.de.client.dispatchers;

import org.iplantc.de.client.factories.EventJSONFactory;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;
import org.iplantc.de.client.utils.MessageDispatcher;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class WindowDispatcher implements ActionDispatcher {

    private JSONObject windowConfig;

    public WindowDispatcher() {
        this(null);
    }

    public WindowDispatcher(JSONObject windowConfig) {
        setConfig(windowConfig);
    }

    public void setConfig(JSONObject windowConfig) {
        this.windowConfig = windowConfig;
    }

    @Override
    public void dispatchAction(String tag) {
        JSONObject windowPayload = new JSONObject();
        windowPayload.put("tag", new JSONString(tag == null ? "" : tag)); //$NON-NLS-1$ //$NON-NLS-2$

        if (windowConfig != null) {
            windowPayload.put("config", windowConfig); //$NON-NLS-1$
        }

        String json = EventJSONFactory.build(ActionType.DISPLAY_WINDOW, windowPayload.toString());

        MessageDispatcher dispatcher = MessageDispatcher.getInstance();
        dispatcher.processMessage(json);
    }

}
