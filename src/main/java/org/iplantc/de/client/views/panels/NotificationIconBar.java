package org.iplantc.de.client.views.panels;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.factories.EventJSONFactory;
import org.iplantc.de.client.utils.MessageDispatcher;
import org.iplantc.de.client.utils.NotificationManager.Category;

import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

/**
 * Contains a button for each notification category. When a button is clicked, the notification window is
 * shown and notifications are filtered by the category that was clicked on.
 * 
 * @author hariolf
 * 
 */
public class NotificationIconBar extends ToolBar {

    private static String buildPayload(final Category category) {
        StringBuffer ret = new StringBuffer();

        ret.append("{"); //$NON-NLS-1$

        ret.append("\"tag\": " + JsonUtil.quoteString(Constants.CLIENT.myNotifyTag())); //$NON-NLS-1$

        ret.append(", \"config\": {\"type\": \"notification_window\", \"data\": {\"category\": \"" //$NON-NLS-1$
                + category + "\"}}"); //$NON-NLS-1$

        ret.append("}"); //$NON-NLS-1$

        return ret.toString();
    }

    /** Makes the notification window visible and filters by a category */
    public static void showNotificationWindow(final Category category) {
        String json = EventJSONFactory.build(EventJSONFactory.ActionType.DISPLAY_WINDOW,
                buildPayload(category));

        MessageDispatcher dispatcher = MessageDispatcher.getInstance();
        dispatcher.processMessage(json);
    }
}
