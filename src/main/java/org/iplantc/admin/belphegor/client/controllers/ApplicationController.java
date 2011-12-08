package org.iplantc.admin.belphegor.client.controllers;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.admin.belphegor.client.ApplicationLayout;
import org.iplantc.admin.belphegor.client.I18N;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A controller class that initializes the layout and event handlers
 * 
 * @author sriram
 *
 */
public class ApplicationController {
    private static ApplicationController instance;
    private ApplicationLayout layout;

//    private TemplateTabPanel template;

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

//        eventbus.addHandler(NewProjectEvent.TYPE, new NewProjectEventHandlerImpl());
//        eventbus.addHandler(TemplateLoadEvent.TYPE, new TemplateLoadEventHandlerImpl());
//        eventbus.addHandler(NavigateToHomeEvent.TYPE, new NavigateToHomeEventHandlerImpl());
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
