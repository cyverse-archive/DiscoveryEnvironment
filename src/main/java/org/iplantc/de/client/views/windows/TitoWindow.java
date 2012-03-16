package org.iplantc.de.client.views.windows;

import org.iplantc.core.tito.client.ApplicationLayout;
import org.iplantc.core.tito.client.controllers.ApplicationController;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.factories.EventJSONFactory;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;
import org.iplantc.de.client.utils.MessageDispatcher;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Element;

public class TitoWindow extends IPlantWindow {

    /**
     * Dispatches a DISPLAY_WINDOW event so that the Window Manager will open the TitoWindow.
     */
    public static void launch() {
        // Build window payload
        JSONObject windowPayload = new JSONObject();
        windowPayload.put("tag", new JSONString(Constants.CLIENT.titoTag())); //$NON-NLS-1$

        // Launch display window event with this payload
        String json = EventJSONFactory.build(ActionType.DISPLAY_WINDOW, windowPayload.toString());

        MessageDispatcher dispatcher = MessageDispatcher.getInstance();
        dispatcher.processMessage(json);
    }

    public TitoWindow(String tag) {
        super(tag, false, true, false, true);

        init();
    }

    private void init() {
        setHeading(I18N.DISPLAY.createApps());
        setSize(800, 600);
        setResizable(false);
    }

    private void compose() {
        ApplicationController controller = ApplicationController.getInstance();
        ApplicationLayout tito = new ApplicationLayout();
        controller.init(tito);

        add(tito);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        compose();
    }

}
