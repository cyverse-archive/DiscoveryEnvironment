package org.iplantc.de.client.dispatchers;

import java.util.List;

import org.iplantc.de.client.Constants;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.SimpleDownloadWindowConfig;

import com.google.gwt.json.client.JSONObject;

/**
 * A WindowDispatcher that can launch a simple download window with links for a given list of paths.
 * 
 * @author psarando
 * 
 */
public class SimpleDownloadWindowDispatcher extends WindowDispatcher {
    /**
     * Launches a simple download window with links for the given list of paths.
     * 
     * @param paths
     */
    public void launchDownloadWindow(List<String> paths) {
        // Build window config
        SimpleDownloadWindowConfig configData = new SimpleDownloadWindowConfig();
        configData.setDownloadPaths(paths);

        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.simpleDownloadTag(),
                configData);

        // Dispatch window display action with this config
        setConfig(windowConfig);
        dispatchAction(Constants.CLIENT.simpleDownloadTag());
    }

}
