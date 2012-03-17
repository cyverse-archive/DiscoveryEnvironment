package org.iplantc.de.client.controllers;

import org.iplantc.core.tito.client.ApplicationLayout;
import org.iplantc.core.tito.client.events.NewProjectEvent;
import org.iplantc.core.tito.client.events.NewProjectEventHandler;
import org.iplantc.core.tito.client.events.TemplateLoadEvent;
import org.iplantc.core.tito.client.events.TemplateLoadEventHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.models.TitoWindowConfig;
import org.iplantc.de.client.views.windows.TitoWindow;

/**
 * A controller class that initializes the Tito window via event handlers.
 * 
 * @author sriram
 * 
 */
public class TitoController {
    private static TitoController instance;
    private ApplicationLayout tito;

    private TitoController() {
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
        public void newInterface() {
            TitoWindow.launch(TitoWindowConfig.VIEW_NEW_INTERFACE, null);
        }

        @Override
        public void newTool() {
            TitoWindow.launch(TitoWindowConfig.VIEW_NEW_TOOL, null);
        }

        @Override
        public void newWorkflow() {
            TitoWindow.launch(TitoWindowConfig.VIEW_NEW_WORKFLOW, null);
        }
    }

    private class TemplateLoadEventHandlerImpl implements TemplateLoadEventHandler {
        @Override
        public void onLoad(TemplateLoadEvent event) {
            String viewMode = null;
            if (event.getMode() == TemplateLoadEvent.MODE.EDIT) {
                viewMode = TitoWindowConfig.VIEW_APP_EDIT;
            } else if (event.getMode() == TemplateLoadEvent.MODE.COPY) {
                viewMode = TitoWindowConfig.VIEW_APP_COPY;
            }

            TitoWindow.launch(viewMode, event.getIdTemplate());
        }
    }
}
