/**
 * 
 */
package org.iplantc.de.client.periodic;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.events.NotificationCountUpdateEvent;
import org.iplantc.de.client.notifications.services.MessageServiceFacade;
import org.iplantc.de.client.utils.TaskRunner;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author sriram
 * 
 */
public class MessagePoller implements Runnable {

    private static MessagePoller instance;

    /**
     * Ensures only 1 MessagePoller at a time is added to the TaskRunner.
     */
    private boolean polling = false;

    private MessagePoller() {
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

    /**
     * Starts polling.
     */
    public void start() {
        if (!polling) {
            TaskRunner.getInstance().addTask(this);
            polling = true;
        }
    }

    /**
     * Stops polling.
     */
    public void stop() {
        if (polling) {
            TaskRunner.getInstance().removeTask(this);
            polling = false;
        }
    }

    /**
     * Polls for notification messages if notification polling is enabled.
     */
    @Override
    public void run() {
        new MessageServiceFacade().getUnSeenMessageCount(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                // currently we do nothing on failure
            }

            @Override
            public void onSuccess(String result) {
                JSONObject obj = JsonUtil.getObject(result);
                NotificationCountUpdateEvent event = new NotificationCountUpdateEvent(Integer
                        .parseInt(JsonUtil.getString(obj, "total")));
                EventBus.getInstance().fireEvent(event);
            }
        });
    }

}
