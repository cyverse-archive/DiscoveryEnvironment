package org.iplantc.de.client.controllers;

import org.iplantc.core.tito.client.events.NewProjectEvent;
import org.iplantc.core.tito.client.events.NewProjectEventHandler;
import org.iplantc.core.uiapplications.client.events.TemplateLoadEvent;
import org.iplantc.core.uiapplications.client.events.handlers.TemplateLoadEventHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.dispatchers.TitoWindowDispatcher;
import org.iplantc.de.client.models.TitoWindowConfig;

/**
 * A controller class that initializes the Tito window via event handlers.
 * 
 * @author sriram
 * 
 */
public class TitoController {
    private static TitoController instance;
    private TitoWindowDispatcher dispatcher;

    private TitoController() {
        dispatcher = new TitoWindowDispatcher();
        initListeners();
    }

    public static TitoController getInstance() {
        if (instance == null) {
            instance = new TitoController();
        }

        return instance;
    }

    private void initListeners() {
        EventBus eventbus = EventBus.getInstance();

        eventbus.addHandler(NewProjectEvent.TYPE, new NewProjectEventHandlerImpl());
        eventbus.addHandler(TemplateLoadEvent.TYPE, new TemplateLoadEventHandlerImpl());
    }

    private class NewProjectEventHandlerImpl implements NewProjectEventHandler {
        @Override
        public void newTool() {
            dispatcher.launchTitoWindow(TitoWindowConfig.VIEW_NEW_TOOL, null);
        }

    }

    private class TemplateLoadEventHandlerImpl implements TemplateLoadEventHandler {
        @Override
        public void onLoad(TemplateLoadEvent event) {
            String viewMode = null;
            if (event.getMode() == TemplateLoadEvent.MODE.EDIT) {
                viewMode = TitoWindowConfig.VIEW_APP_EDIT;
            }
            dispatcher.launchTitoWindow(viewMode, event.getIdTemplate());
        }
    }
}
