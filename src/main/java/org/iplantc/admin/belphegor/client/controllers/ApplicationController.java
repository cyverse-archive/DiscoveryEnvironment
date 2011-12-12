package org.iplantc.admin.belphegor.client.controllers;

import org.iplantc.admin.belphegor.client.ApplicationLayout;
import org.iplantc.core.uicommons.client.events.EventBus;

/**
 * A controller class that initializes the layout and event handlers
 * 
 * @author sriram
 * 
 */
public class ApplicationController {
    private static ApplicationController instance;
    private ApplicationLayout layout;

    private ApplicationController() {
        initListeners();
        disableBrowserContextMenu();
    }

    public static ApplicationController getInstance() {
        if (instance == null) {
            instance = new ApplicationController();
        }

        return instance;
    }

    private void initListeners() {
        EventBus eventbus = EventBus.getInstance();

    }

    public void init(ApplicationLayout layout) {
        this.layout = layout;
    }

    /**
     * Disable the context menu of the browser using native JavaScript.
     * 
     * This disables the user's ability to right-click on this widget and get the browser's context menu
     */
    private native void disableBrowserContextMenu()
    /*-{
		$doc.oncontextmenu = function() {
			return false;
		};
    }-*/;
}
