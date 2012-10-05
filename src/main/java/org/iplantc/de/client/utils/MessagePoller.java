package org.iplantc.de.client.utils;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.events.NotificationCountUpdateEvent;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.core.uicommons.client.events.EventBus;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Periodically retrieves messages for the current user from the server.
 */
public class MessagePoller {
    private PollingTimer timer;
    private final int interval;
    private static MessagePoller instance;

    private final int DEFAULT_INTERVAL = 60;

    // total # of unseen messages
    private int total_unseen;

    private MessagePoller() {
        // get interval in seconds and convert to milliseconds
        int millsec = DEProperties.getInstance().getNotificationPollInterval() * 1000;
        interval = (millsec == 0) ? (DEFAULT_INTERVAL * 1000) : millsec;
    }

    /**
     * Retrieve singleton instance.
     * 
     * @return the singleton instance.
     */
    public static MessagePoller getInstance() {
        if (instance == null) {
            instance = new MessagePoller();
        }

        return instance;
    }

    private void initTimer() {
        if (timer == null) {
            UserInfo info = UserInfo.getInstance();

            timer = new PollingTimer(info.getEmail());
            timer.scheduleRepeating(interval);
        }
    }

    /**
     * Starts polling.
     */
    public void start() {
        initTimer();
    }

    /**
     * Stops polling.
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    class PollingTimer extends Timer {
        final String username;

        PollingTimer(final String username) {
            this.username = username;
        }

        @Override
        public void run() {
            MessageServiceFacade facade = new MessageServiceFacade();

            facade.getUnSeenMessageCount(new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    // currently we do nothing on failure
                }

                @Override
                public void onSuccess(String result) {
                    JSONObject obj = JSONParser.parseStrict(result).isObject();
                    total_unseen = Integer.parseInt(JsonUtil.getString(obj, "total"));
                    NotificationCountUpdateEvent event = new NotificationCountUpdateEvent(total_unseen);
                    EventBus.getInstance().fireEvent(event);
                }
            });
        }
    }

}
