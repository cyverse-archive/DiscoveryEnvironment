package org.iplantc.de.client.views.windows;

import org.iplantc.core.tito.client.ApplicationLayout;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.factories.EventJSONFactory;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.TitoWindowConfig;
import org.iplantc.de.client.models.WindowConfig;
import org.iplantc.de.client.utils.MessageDispatcher;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Element;

public class TitoWindow extends IPlantWindow {
    private TitoWindowConfig config;
    private ApplicationLayout tito;

    /**
     * Dispatches a DISPLAY_WINDOW event so that the Window Manager will open the TitoWindow.
     */
    public static void launch() {
        launch(null, null);
    }

    public static void launch(String view, String id) {
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
        JSONObject windowPayload = configFactory.buildConfigPayload(Constants.CLIENT.titoTag(),
                Constants.CLIENT.titoTag(), windowConfigData);

        // Launch display window event with this payload
        String json = EventJSONFactory.build(ActionType.DISPLAY_WINDOW, windowPayload.toString());

        MessageDispatcher dispatcher = MessageDispatcher.getInstance();
        dispatcher.processMessage(json);
    }

    public TitoWindow(String tag, TitoWindowConfig config) {
        super(tag, false, true, false, true);

        this.config = config;

        init();
    }

    private void init() {
        setHeading(I18N.DISPLAY.createApps());
        setSize(800, 600);
        setResizable(false);
    }

    private void compose() {
        tito = new ApplicationLayout();

        add(tito);
    }

    @Override
    public void cleanup() {
        if (tito != null) {
            tito.cleanup();
        }

        super.cleanup();
    }

    @Override
    public void configure(WindowConfig config) {
        if (config instanceof TitoWindowConfig) {
            this.config = (TitoWindowConfig)config;

            if (isRendered()) {
                updateViewFromConfig();
            }
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
        } else if (TitoWindowConfig.VIEW_APP_COPY.equals(viewMode)) {
            tito.copy(config.getAppId());
        } else if (TitoWindowConfig.VIEW_NEW_TOOL.equals(viewMode)) {
            tito.newTool();
        } else if (TitoWindowConfig.VIEW_NEW_INTERFACE.equals(viewMode)) {
            tito.newInterface();
        } else if (TitoWindowConfig.VIEW_NEW_WORKFLOW.equals(viewMode)) {
            tito.newWorkflow();
        }
    }
}
