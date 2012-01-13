package org.iplantc.de.client.utils;

import org.iplantc.de.client.models.Notification;

import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.util.Params;

/**
 * Provides a uniform way of presenting notification information to the user.
 * 
 * Optionally, this notification information may be included in a user's My Notification.
 * 
 * Implementation essentially wraps functionality provided in GXT by Info & InfoConfig.
 * 
 * @see com.extjs.gxt.ui.client.widget.Info
 * @see com.extjs.gxt.ui.client.widget.InfoConfig
 */
public class NotifyInfo {
    /**
     * Provide an informative message to the user and include as a notification.
     * 
     * Allows for the text argument to be a parameterized message.
     * 
     * @param category notification category
     * @param title represents a title for the message.
     * @param text represents the message text to display.
     * @param parameters parameters to be merged into the text argument.
     */
    public static void notify(NotificationManager.Category category, final String title,
            final String text, Params parameters) {
        doDisplay(category, title, text, parameters);
    }

    /**
     * Provide an informative message to the user and optionally include as a notification.
     * 
     * @param title represents a title for the message.
     * @param text represents the message text to display.
     */
    public static void display(final String title, final String text) {
        doDisplay(title, text, null);
    }



    private static void doDisplay(final String title, final String text, Params parameters) {
        makeInfoCall(title, text, parameters);
    }

    private static void doDisplay(NotificationManager.Category category, final String title,
            final String text, Params parameters) {
        makeInfoCall(title, text, parameters);

        includeAsNotification(category, text, parameters);
    }

    private static void includeAsNotification(NotificationManager.Category category, final String text,
            Params parameters) {
        NotificationManager mgr = NotificationManager.getInstance();

        // only add to the notification manager when we want inclusion
        if (parameters == null) {
            mgr.add(category, new Notification(text));
        } else {
            mgr.add(category, new Notification(Format.substitute(text, parameters)));
        }
    }

    private static void makeInfoCall(final String title, final String text, Params parameters) {
        if (parameters == null) {
            DEInfo.display(title, text);
        } else {
            DEInfo.display(title, text, parameters);
        }
    }
}
