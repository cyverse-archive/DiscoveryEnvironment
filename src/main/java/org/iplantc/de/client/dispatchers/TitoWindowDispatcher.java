package org.iplantc.de.client.dispatchers;

import org.iplantc.de.client.Constants;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.TitoWindowConfig;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * 
 * A dispatcher to launch Tito Window given a selected app and a view. A view can be edit or create.
 * 
 * @author sriram
 * 
 */
public class TitoWindowDispatcher extends WindowDispatcher {

    /**
     * Launch the tito window with given view and selected app id
     * 
     * @param view create or edit view
     * @param id id of the selected app
     */
    public void launchTitoWindow(String view, String id) {
        // Build window config data
        JSONObject windowConfigData = new JSONObject();

        if (view != null) {
            windowConfigData.put(TitoWindowConfig.VIEW, new JSONString(view));
        }
        if (id != null) {
            windowConfigData.put(TitoWindowConfig.APP_ID, new JSONString(id));
        }

        // Build window payload with config
        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowPayload = configFactory.buildWindowConfig(Constants.CLIENT.titoTag(),
                windowConfigData);

        // Dispatch window display action with this config
        setConfig(windowPayload);
        dispatchAction(Constants.CLIENT.titoTag());
    }

}
