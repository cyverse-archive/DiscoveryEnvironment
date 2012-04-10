package org.iplantc.de.client.views.panels;

import org.iplantc.de.client.Constants;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.utils.NotificationManager.Category;

import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Contains a button for each notification category. When a button is clicked, the notification window is
 * shown and notifications are filtered by the category that was clicked on.
 * 
 * @author hariolf
 * 
 */
public class NotificationIconBar extends ToolBar {

    /** Makes the notification window visible and filters by a category */
    public static void showNotificationWindow(final Category category) {
        JSONObject windowConfigData = new JSONObject();
        windowConfigData.put("category", new JSONString(category.toString())); //$NON-NLS-1$

        // Build window config
        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig("notification_window", //$NON-NLS-1$
                windowConfigData);

        // Dispatch window display action with this config
        WindowDispatcher dispatcher = new WindowDispatcher(windowConfig);
        dispatcher.dispatchAction(Constants.CLIENT.myNotifyTag());
    }
}
