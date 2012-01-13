package org.iplantc.de.client.utils;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.factories.EventJSONFactory;

/**
 * Class used to execute analysis viewing contexts.
 * 
 * @author amuir
 * 
 */
public class AnalysisViewContextExecutor {
    private String buildPayload(final String context) {
        StringBuffer ret = new StringBuffer();

        ret.append("{"); //$NON-NLS-1$

        ret.append("\"tag\": " + JsonUtil.quoteString(Constants.CLIENT.myAnalysisTag())); //$NON-NLS-1$

        ret.append(", \"config\": {\"type\": \"analysis_window\", \"data\": " + context + "}"); //$NON-NLS-1$ //$NON-NLS-2$

        ret.append("}"); //$NON-NLS-1$

        return ret.toString();
    }

    /**
     * Execute a context.
     * 
     * @param context user intent as context.
     */
    public void execute(final String context) {
        String json = EventJSONFactory.build(EventJSONFactory.ActionType.DISPLAY_WINDOW,
                buildPayload(context));

        MessageDispatcher dispatcher = MessageDispatcher.getInstance();
        dispatcher.processMessage(json);
    }
}
