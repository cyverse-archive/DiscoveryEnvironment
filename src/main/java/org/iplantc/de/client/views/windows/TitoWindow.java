package org.iplantc.de.client.views.windows;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.tito.client.TitoPanel;
import org.iplantc.core.uiapplications.client.events.AnalysisDeleteEvent;
import org.iplantc.core.uiapplications.client.events.AnalysisDeleteEventHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.TitoWindowConfig;
import org.iplantc.de.client.models.WindowConfig;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Element;

/**
 * 
 * A window for Tito editor
 * 
 * @author sriram
 * 
 */
public class TitoWindow extends IPlantWindow {
    private TitoWindowConfig config;
    private TitoPanel tito;

    protected List<HandlerRegistration> handlers;

    public TitoWindow(String tag, TitoWindowConfig config) {
        super(tag, false, true, false, true);

        this.config = config;

        init();
        initHandlers();
    }

    private void initHandlers() {
        EventBus eventbus = EventBus.getInstance();

        handlers = new ArrayList<HandlerRegistration>();

        handlers.add(eventbus.addHandler(AnalysisDeleteEvent.TYPE, new AnalysisDeleteEventHandler() {
            @Override
            public void onDelete(AnalysisDeleteEvent ade) {
                if (tito != null && ade.getId() != null && ade.getId().equals(tito.getId())) {
                    hide();
                }
            }
        }));

    }

    private void init() {
        setHeading(I18N.DISPLAY.createApps());
        setSize(800, 600);
        setResizable(false);
    }

    private void compose() {
        tito = new TitoPanel(tag);

        add(tito);
    }

    @Override
    public void cleanup() {
        removeEventHandlers();

        if (tito != null) {
            tito.cleanup();
        }

        super.cleanup();
    }

    public void removeEventHandlers() {
        EventBus eventbus = EventBus.getInstance();

        // unregister
        for (HandlerRegistration reg : handlers) {
            eventbus.removeHandler(reg);
        }

        // clear our list
        handlers.clear();
    }

    @Override
    protected void doHide() {
        if (tito.isDirty()) {
            confirmNavigation(new CloseWarningMsgBoxListener());
        } else {
            super.doHide();
        }
    }

    private void confirmNavigation(final Listener<MessageBoxEvent> listener) {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                MessageBox.confirm(I18N.DISPLAY.confirmAction(), I18N.DISPLAY.navigateWarning(),
                        listener);
            }
        });
    }

    @Override
    public void setWindowConfig(WindowConfig config) {
        if (config instanceof TitoWindowConfig) {
            this.config = (TitoWindowConfig)config;

            if (isRendered()) {
                if (tito.isDirty()) {
                    confirmNavigation(new EditWarningMsgBoxListener());
                } else {
                    updateViewFromConfig();
                }
            }
        }
    }

    private class EditWarningMsgBoxListener implements Listener<MessageBoxEvent> {
        @Override
        public void handleEvent(MessageBoxEvent be) {
            if (be.getButtonClicked().getText().equals("Yes")) { //$NON-NLS-1$
                updateViewFromConfig();
            }

            MessageBox box = be.getMessageBox();
            box.close();

        }

    }

    private class CloseWarningMsgBoxListener implements Listener<MessageBoxEvent> {
        @Override
        public void handleEvent(MessageBoxEvent be) {
            if (be.getButtonClicked().getText().equals("Yes")) { //$NON-NLS-1$
                hide();
            }

            MessageBox box = be.getMessageBox();
            box.close();

        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        compose();
        updateViewFromConfig();
    }

    private void updateViewFromConfig() {
        if (config == null || config.getView() == null) {
            return;
        }

        String viewMode = config.getView();

        if (TitoWindowConfig.VIEW_APP_EDIT.equals(viewMode)) {
            tito.load(config.getAppId());
        } else if (TitoWindowConfig.VIEW_NEW_TOOL.equals(viewMode)) {
            tito.newTool();
        } else if (TitoWindowConfig.VIEW_APP_EDIT_FROM_JSON.equals(viewMode)) {
            tito.loadFromJson(config.getAppJson());
        }
    }

    @Override
    public JSONObject getWindowState() {
        TitoWindowConfig configData = new TitoWindowConfig(getWindowViewState());

        configData.setView(TitoWindowConfig.VIEW_APP_EDIT_FROM_JSON);
        configData.setAppJson(tito.getTitoConfig());

        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory
                .buildWindowConfig(Constants.CLIENT.titoTag(), configData);
        WindowDispatcher dispatcher = new WindowDispatcher(windowConfig);
        return dispatcher.getDispatchJson(Constants.CLIENT.titoTag(), ActionType.DISPLAY_WINDOW);
    }
}
