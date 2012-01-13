package org.iplantc.de.client.utils;

import java.util.List;

import org.iplantc.de.client.factories.EventJSONFactory;

/**
 * Class used to execute data viewing contexts.
 * 
 * @author amuir
 * 
 */
public class DataViewContextExecutor {
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
        return "]}"; //$NON-NLS-1$
    }

    private void doExecute(final String body) {
        // header
        String payload = buildHeader();

        // body
        payload += body;

        // footer
        payload += buildFooter();

        String json = EventJSONFactory
                .build(EventJSONFactory.ActionType.DISPLAY_VIEWER_WINDOWS, payload);

        MessageDispatcher dispatcher = MessageDispatcher.getInstance();
        dispatcher.processMessage(json);
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
}
