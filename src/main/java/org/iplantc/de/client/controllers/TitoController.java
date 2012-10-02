package org.iplantc.de.client.controllers;

import org.iplantc.core.uiapplications.client.events.AppLoadEvent;
import org.iplantc.core.uiapplications.client.events.AppLoadEvent.AppLoadEventHandler;
import org.iplantc.core.uiapplications.client.events.CreateNewAppEvent;
import org.iplantc.core.uiapplications.client.events.handlers.CreateNewAppEventHandler;
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
    private final TitoWindowDispatcher dispatcher;

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

        eventbus.addHandler(AppLoadEvent.TYPE, new AppLoadEventHandlerImpl());

        eventbus.addHandler(CreateNewAppEvent.TYPE, new CreateNewAppEventHandlerImpl());
    }

    private final class CreateNewAppEventHandlerImpl implements CreateNewAppEventHandler {
        @Override
        public void createNewApp() {
            dispatcher.launchTitoWindow(TitoWindowConfig.VIEW_NEW_TOOL, null);
        }
    }

    private class AppLoadEventHandlerImpl implements AppLoadEventHandler {
        @Override
        public void onLoad(AppLoadEvent event) {
            String viewMode = null;
            if (event.getMode() == AppLoadEvent.MODE.EDIT) {
                viewMode = TitoWindowConfig.VIEW_APP_EDIT;
            }
            dispatcher.launchTitoWindow(viewMode, event.getIdTemplate());
        }
    }
}
