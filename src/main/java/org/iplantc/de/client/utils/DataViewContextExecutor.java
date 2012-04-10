package org.iplantc.de.client.utils;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.factories.EventJSONFactory;

import com.google.gwt.json.client.JSONObject;

/**
 * Class used to execute data viewing contexts.
 * 
 * @author amuir
 * 
 */
public class DataViewContextExecutor {
    private JSONObject config;

    private String buildHeader() {
        return "{\"files\": ["; //$NON-NLS-1$
    }

    private String buildBody(final String jsonPayload) {
        return jsonPayload;
    }

    private String buildBody(final List<String> jsonPayloads) {
        StringBuffer ret = new StringBuffer();

        if (jsonPayloads != null) {
            boolean first = true;

            for (String item : jsonPayloads) {
                if (first) {
                    first = false;
                } else {
                    ret.append(", "); //$NON-NLS-1$
                }

                ret.append(item);
            }
        }

        return ret.toString();
    }

    private String buildFooter() {
        return "}"; //$NON-NLS-1$
    }

    private void doExecute(final String body) {

        JSONObject obj = getDispatchJson(body);

        MessageDispatcher dispatcher = MessageDispatcher.getInstance();
        dispatcher.processMessage(obj);
    }

    public JSONObject getDispatchJson(final String jsonPayload) {
        // header
        String payload = buildHeader();

        // body
        payload += jsonPayload;
        payload += "]";
        if (config != null) {
            payload = payload + ",\"config\":" + config.toString();
        }

        // footer
        payload += buildFooter();

        JSONObject json = EventJSONFactory.build(EventJSONFactory.ActionType.DISPLAY_VIEWER_WINDOWS,
                JsonUtil.getObject(payload));
        return json;
    }

    /**
     * Execute from a single payload string.
     * 
     * @param jsonPayload single JSON string used to comprise the body of our execute.
     */
    public void execute(final String jsonPayload) {
        // execute
        doExecute(buildBody(jsonPayload));
    }

    /**
     * Execute from a list of payload strings.
     * 
     * @param jsonPayloads list of JSON strings used to comprise the body of our execute.
     */
    public void execute(final List<String> jsonPayloads) {
        // execute
        doExecute(buildBody(jsonPayloads));
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
