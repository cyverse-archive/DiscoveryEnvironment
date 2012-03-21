package org.iplantc.de.client.views.windows;

import org.iplantc.de.client.I18N;
import org.iplantc.de.client.models.NotificationWindowConfig;
import org.iplantc.de.client.models.WindowConfig;
import org.iplantc.de.client.utils.NotificationManager.Category;
import org.iplantc.de.client.views.panels.NotificationPanel;

import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.json.client.JSONObject;

/**
 * Window for displaying event notifications.
 * 
 * @author lenards, sriram, hariolf
 * 
 */
public class NotificationWindow extends IPlantWindow {
    private NotificationPanel panel;

    /**
     * Constructs an instance given a unique identifier.
     * 
     * @param tag a unique identifier that serves as a "window handle."
     * @param config configuration parameters
     */
    public NotificationWindow(String tag, NotificationWindowConfig config) {
        super(tag, false, true, true, true);

        init();
        configure(config);
    }

    private void init() {
        setId(tag);
        setHeading(I18N.DISPLAY.myNotifications());
        setResizable(true);
        setWidth(740);
        setHeight(400);
        setLayout(new FitLayout());

        panel = new NotificationPanel();
        add(panel);
    }

    @Override
    public void configure(WindowConfig config) {
        if (config instanceof NotificationWindowConfig) {
            Category category = ((NotificationWindowConfig)config).getCategory();
            panel.filterBy(category);
        }
    }

    @Override
    public JSONObject getWindowState() {
        // TODO Auto-generated method stub
        return null;
    }
}
