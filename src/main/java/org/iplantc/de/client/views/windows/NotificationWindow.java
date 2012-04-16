package org.iplantc.de.client.views.windows;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.Notification;
import org.iplantc.de.client.models.NotificationWindowConfig;
import org.iplantc.de.client.models.WindowConfig;
import org.iplantc.de.client.utils.NotificationManager;
import org.iplantc.de.client.utils.NotificationManager.Category;
import org.iplantc.de.client.views.panels.NotificationPanel;

import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Command;

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

    @Override
    public void show() {
        super.show();
    }

    private void applyWindowConfig() {
        Category category = ((NotificationWindowConfig)config).getCategory();
        panel.filterBy(category);
        List<String> selectedIds = JsonUtil.buildStringList(((NotificationWindowConfig)config)
                .getSelectedIds());
        panel.selectNotifications(selectedIds);
        setWindowViewState();
        config = null;
    }

    private void retrieveData() {
        mask(I18N.DISPLAY.loadingMask());
        Command cmd = new Command() {
            @Override
            public void execute() {
                unmask();
                if (config != null) {
                    applyWindowConfig();
                }

            }

        };
        NotificationManager.getInstance().getExistingNotifications(cmd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanup() {
        super.cleanup();
        NotificationManager.getInstance().cleanup();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterRender() {
        super.afterRender();

        retrieveData();
    }

    @Override
    public JSONObject getWindowState() {
        JSONObject obj = super.getWindowViewState();
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
        WindowDispatcher dispatcher = new WindowDispatcher(windowConfig);
        return dispatcher.getDispatchJson(Constants.CLIENT.myNotifyTag(), ActionType.DISPLAY_WINDOW);

    }
}
