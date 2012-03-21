package org.iplantc.de.client.utils;

import com.extjs.gxt.ui.client.state.Provider;
import com.google.gwt.storage.client.Storage;

/**
 * 
 * A Singleton class that provides local storage support (Html 5)
 * 
 * @author sriram
 * 
 */
public class LocalStorageProvider extends Provider {

    private Storage localStorage;
    private static LocalStorageProvider instance;

    /**
     * Init storage provider
     * 
     */
    private LocalStorageProvider() {
        localStorage = Storage.getLocalStorageIfSupported();
    }

    /**
     * Construct a new instance of LocalStorageProvider
     * 
     * @return LocalStorageProvider
     */
    public static LocalStorageProvider getInstance() {
        if (instance == null) {
            instance = new LocalStorageProvider();
        }

        return instance;
    }

    @Override
    protected void clearKey(String key) {
        if (localStorage != null) {
            localStorage.removeItem(key);
        }

    }

    @Override
    protected String getValue(String key) {
        if (localStorage != null) {
            return localStorage.getItem(key);
        }

        return null;
    }

    @Override
    protected void setValue(String name, String value) {
        if (localStorage != null) {
            localStorage.setItem(name, value);
        }
    }

}
