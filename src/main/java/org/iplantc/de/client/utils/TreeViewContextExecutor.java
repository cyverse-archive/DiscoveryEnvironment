package org.iplantc.de.client.utils;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.factories.EventJSONFactory;

import com.google.gwt.json.client.JSONObject;

/**
 * Class used to execute tree viewing contexts.
 * 
 * @author psarando
 * 
 */
public class TreeViewContextExecutor {

    private JSONObject config;

    /**
     * Execute from a single payload string.
     * 
     * @param jsonPayload single JSON string used to comprise the body of our execute.
     * 
     */
    public void execute(final String jsonPayload) {
        // execute
        MessageDispatcher dispatcher = MessageDispatcher.getInstance();
        dispatcher.processMessage(getDispatchJson(jsonPayload));
    }

    public JSONObject getDispatchJson(final String jsonPayload) {
        String payload = null;
        if (getConfig() != null) {
            payload = "{\"files\": [" + jsonPayload + "]" + ",\"config\":" + getConfig().toString() + "}"; //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            payload = "{\"files\": [" + jsonPayload + "]}"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        JSONObject json = EventJSONFactory.build(
                EventJSONFactory.ActionType.DISPLAY_TREE_VIEWER_WINDOWS, JsonUtil.getObject(payload));
        return json;
    }

    /**
     * @param config the config to set
     */
    public void setConfig(JSONObject config) {
        this.config = config;
    }

    /**
     * @return the config
     */
    public JSONObject getConfig() {
        return config;
    }
}
