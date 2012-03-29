package org.iplantc.de.client.views.windows;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.Notification;
import org.iplantc.de.client.models.NotificationWindowConfig;
import org.iplantc.de.client.models.WindowConfig;
import org.iplantc.de.client.utils.NotificationManager.Category;
import org.iplantc.de.client.views.panels.NotificationPanel;

import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Window for displaying event notifications.
 * 
 * @author lenards, sriram, hariolf
 * 
 */
public class NotificationWindow extends IPlantWindow {
    private NotificationPanel panel;
    private WindowConfig config;

    /**
     * Constructs an instance given a unique identifier.
     * 
     * @param tag a unique identifier that serves as a "window handle."
     * @param config configuration parameters
     */
    public NotificationWindow(String tag, NotificationWindowConfig config) {
        super(tag, false, true, true, true);

        init();
        setWindowConfig(config);
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
    public void setWindowConfig(WindowConfig config) {
        if (config instanceof NotificationWindowConfig) {
            this.config = config;
        }

    }

    private void setWindowDisplayState() {
        if (config == null) {
            return;
        }

        if (config.isWindowMinimized()) {
            minimize();
            return;
        }

        if (config.isWindowMaximized()) {
            maximizeWindow();
            return;
        }
    }

    @Override
    public void show() {
        super.show();
        if (config != null) {
            Category category = ((NotificationWindowConfig)config).getCategory();
            panel.filterBy(category);
            List<String> selectedIds = JsonUtil.buildStringList(((NotificationWindowConfig)config)
                    .getSelectedIds());
            panel.selectNotifications(selectedIds);
            setWindowDisplayState();
        }
    }

    @Override
    public JSONObject getWindowState() {
        JSONObject obj = super.getWindowState();
        JSONArray arr = new JSONArray();
        if (panel.getSelectedItems().size() > 0) {

            int i = 0;
            for (Notification n : panel.getSelectedItems()) {
                arr.set(i++, new JSONString(n.getId()));
            }
        }
        obj.put(NotificationWindowConfig.SELECTED_IDS, arr);

        // Build window config
        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.myNotifyTag(), obj);
        return windowConfig;

    }
}
