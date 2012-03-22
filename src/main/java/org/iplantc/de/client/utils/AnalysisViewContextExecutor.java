package org.iplantc.de.client.utils;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.factories.WindowConfigFactory;

import com.google.gwt.json.client.JSONObject;

/**
 * Class used to execute analysis viewing contexts.
 * 
 * @author amuir
 * 
 */
public class AnalysisViewContextExecutor {
    /**
     * Execute a context.
     * 
     * @param context user intent as context.
     */
    public void execute(final String context) {
        // Build window config
        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig("analysis_window", //$NON-NLS-1$
                JsonUtil.getObject(context));

        // Dispatch window display action with this config
        WindowDispatcher dispatcher = new WindowDispatcher(windowConfig);
        dispatcher.dispatchAction(Constants.CLIENT.myAnalysisTag());
    }
}
