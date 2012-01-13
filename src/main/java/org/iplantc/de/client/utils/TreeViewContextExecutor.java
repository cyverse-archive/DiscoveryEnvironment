package org.iplantc.de.client.utils;

import org.iplantc.de.client.factories.EventJSONFactory;

/**
 * Class used to execute tree viewing contexts.
 * 
 * @author psarando
 * 
 */
public class TreeViewContextExecutor {
    /**
     * Execute from a single payload string.
     * 
     * @param jsonPayload single JSON string used to comprise the body of our execute.
     */
    public void execute(final String jsonPayload) {
        String payload = "{\"files\": [" + jsonPayload + "]}"; //$NON-NLS-1$ //$NON-NLS-2$

        String json = EventJSONFactory.build(EventJSONFactory.ActionType.DISPLAY_TREE_VIEWER_WINDOWS,
                payload);

        // execute
        MessageDispatcher dispatcher = MessageDispatcher.getInstance();
        dispatcher.processMessage(json);
    }
}
