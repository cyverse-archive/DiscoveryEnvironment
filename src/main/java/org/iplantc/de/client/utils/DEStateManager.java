package org.iplantc.de.client.utils;

import com.extjs.gxt.ui.client.state.StateManager;

/**
 * A singleton wrapper to GXT's StateManager. This wrapper set the LocalStorageProvider as the default
 * provider
 * 
 * @author sriram
 * 
 */
public class DEStateManager {

    private static StateManager instance;

    public static StateManager getStateManager() {
        if (instance == null) {
            instance = StateManager.get();
            instance.setProvider(LocalStorageProvider.getInstance());
        }
        return instance;
    }
}
