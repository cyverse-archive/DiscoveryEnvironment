package org.iplantc.de.client.utils;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.factories.EventJSONFactory;
import org.iplantc.de.client.models.ShortcutDesc;
import org.iplantc.de.client.utils.builders.DesktopBuilder;
import org.iplantc.de.client.views.Shortcut;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;

/**
 * Contains all of the application's desktop shortcuts.
 * 
 * @author amuir
 * 
 */
public class ShortcutManager {
    private List<Shortcut> shortcuts = new ArrayList<Shortcut>();

    private SelectionListener<ComponentEvent> listener = new SelectionListener<ComponentEvent>() {
        private String buildPayload(final String tag) {
            StringBuffer ret = new StringBuffer();

            ret.append("{"); //$NON-NLS-1$

            ret.append("\"tag\": " + JsonUtil.quoteString(tag)); //$NON-NLS-1$

            ret.append("}"); //$NON-NLS-1$

            return ret.toString();
        }

        @Override
        public void componentSelected(ComponentEvent ce) {
            Shortcut shortcut = (Shortcut)ce.getComponent();

            String json = EventJSONFactory.build(EventJSONFactory.ActionType.DISPLAY_WINDOW,
                    buildPayload(shortcut.getTag()));

            MessageDispatcher dispatcher = MessageDispatcher.getInstance();
            dispatcher.processMessage(json);
        }
    };

    /**
     * Instantiate from desktop builder.
     * 
     * @param builder builder which contains shortcut templates.
     */
    public ShortcutManager(DesktopBuilder builder) {
        addShortcuts(builder);
    }

    /**
     * Helper method to extract shortcut templates from a desktop builder and allocate shortcuts.
     * 
     * @param builder builder which contains shortcut templates.
     */
    protected void addShortcuts(DesktopBuilder builder) {
        if (builder != null) {
            List<ShortcutDesc> descs = builder.getShortcuts();

            for (ShortcutDesc desc : descs) {
                shortcuts.add(new Shortcut(desc, listener));
            }
        }
    }

    /**
     * Retrieve our list of shortcuts.
     * 
     * @return shortcut list.
     */
    public List<Shortcut> getShortcuts() {
        return shortcuts;
    }
}
