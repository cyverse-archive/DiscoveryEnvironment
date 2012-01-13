package org.iplantc.de.client.views.panels;

import java.util.ArrayList;

import org.iplantc.core.uicommons.client.events.EventBus;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;

public abstract class AbstractDataPanel extends ContentPanel {
    protected final String tag;
    protected ArrayList<HandlerRegistration> handlers;

    protected AbstractDataPanel(final String tag) {
        this.tag = tag;

        init();

        setHeading();

        registerHandlers();
    }

    protected void init() {
        // do nothing by default
    }

    protected void setHeading() {
        // do nothing by default
    }

    protected abstract void compose();

    protected void registerHandlers() {
        handlers = new ArrayList<HandlerRegistration>();
    }

    private void removeEventHandlers() {
        EventBus eventbus = EventBus.getInstance();

        // unregister
        for (HandlerRegistration reg : handlers) {
            eventbus.removeHandler(reg);
        }

        // clear our list
        handlers.clear();
    }

    public void cleanup() {
        removeEventHandlers();
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
