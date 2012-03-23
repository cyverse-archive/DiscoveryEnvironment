package org.iplantc.de.client.utils;

import static org.iplantc.de.client.models.Notification.PROP_TIMESTAMP;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.AnalysisPayloadEvent;
import org.iplantc.de.client.events.AnalysisPayloadEventHandler;
import org.iplantc.de.client.events.DataPayloadEvent;
import org.iplantc.de.client.events.DataPayloadEventHandler;
import org.iplantc.de.client.models.Notification;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.client.utils.builders.context.AnalysisContextBuilder;
import org.iplantc.de.client.utils.builders.context.DataContextBuilder;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Handles the management of a list of system notifications for the user.
 * 
 * The list is a "rolling list" of size MAX.
 * 
 * @author lenards, sriram
 * 
 */
public class NotificationManager {
    /**
     * Represents a notification category.
     */
    public enum Category {
        /** All notification categories */
        ALL(I18N.CONSTANT.notificationCategoryAll()),
        /** System notifications */
        SYSTEM(I18N.CONSTANT.notificationCategorySystem()),
        /** Data notifications */
        DATA(I18N.CONSTANT.notificationCategoryData()),
        /** Analysis notifications */
        ANALYSIS(I18N.CONSTANT.notificationCategoryAnalysis());

        private String displayText;

        private Category(String displayText) {
            this.displayText = displayText;
        }

        /**
         * Null-safe and case insensitive variant of valueOf(String)
         * 
         * @param typeString
         * @return
         */
        public static Category fromTypeString(String typeString) {
            if (typeString == null || typeString.isEmpty()) {
                return null;
            }
            return valueOf(typeString.toUpperCase());
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

    /**
     * Maximum number of notifications that are managed.
     */
    public static int MAX_NOTIFICATIONS = 50;

    private static NotificationManager instance = null;

    private final ListStore<Notification> storeAll;

    private DataContextBuilder dataContextBuilder;
    private AnalysisContextBuilder analysisContextBuilder;
    private final MessageServiceFacade facadeMessageService;
    private Command storeLoadCompleteCallbackCmd;

    private NotificationManager(Command storeLoadCompleteCallbackCmd) {
        facadeMessageService = new MessageServiceFacade();
        storeAll = new ListStore<Notification>();
        this.storeLoadCompleteCallbackCmd = storeLoadCompleteCallbackCmd;

        // keep notifications sorted by time
        storeAll.setDefaultSort(PROP_TIMESTAMP, SortDir.DESC);
        storeAll.setSortField(PROP_TIMESTAMP);

        initContextBuilders();

        registerEventHandlers();

        getExistingNotifications(null);
    }

    private NotificationManager() {
        this(null);
    }

    private void initContextBuilders() {
        dataContextBuilder = new DataContextBuilder();
        analysisContextBuilder = new AnalysisContextBuilder();
    }

    private Notification addItemToStore(final Category category, final JSONObject objMessage,
            final String context) {
        Notification ret = null; // assume failure

        if (objMessage != null) {
            ret = new Notification(objMessage, context);
            add(category, ret);
        }

        return ret;
    }

    private void addFromEventHandler(final Category category, final String header,
            final JSONObject objMessage, final String context) {
        Notification notification = addItemToStore(category, objMessage, context);

        if (notification != null) {
            NotifyInfo.display(header, notification.getMessage());
        }
    }

    private void registerEventHandlers() {
        EventBus eventbus = EventBus.getInstance();

        // handle data events
        eventbus.addHandler(DataPayloadEvent.TYPE, new DataPayloadEventHandler() {
            @Override
            public void onFire(DataPayloadEvent event) {
                addFromEventHandler(Category.DATA, I18N.DISPLAY.fileUpload(), event.getMessage(),
                        dataContextBuilder.build(event.getPayload()));
            }
        });

        // handle analysis events
        eventbus.addHandler(AnalysisPayloadEvent.TYPE, new AnalysisPayloadEventHandler() {
            @Override
            public void onFire(AnalysisPayloadEvent event) {
                addFromEventHandler(Category.ANALYSIS, I18N.CONSTANT.analysis(), event.getMessage(),
                        analysisContextBuilder.build(event.getPayload()));
            }
        });
    }

    /**
     * Return the shared, singleton instance of the manager.
     * 
     * @return a singleton reference to the notification manager.
     */
    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }

        return instance;
    }

    /**
     * Return the shared, singleton instance of the manager.
     * 
     * @return a singleton reference to the notification manager.
     */
    public static NotificationManager getInstance(Command storeLoadCompleteCallback) {
        if (instance == null) {
            instance = new NotificationManager(storeLoadCompleteCallback);
        } else {
            instance.storeLoadCompleteCallbackCmd = storeLoadCompleteCallback;
        }

        return instance;
    }

    private Notification getLastElement() {
        return storeAll.getAt(storeAll.getCount() - 1);
    }

    /**
     * Add a new notification.
     * 
     * @param category category for this notification.
     * @param notification notification to add.
     */
    public void add(final Category category, final Notification notification) {
        // did we get a valid notification?
        if (category != Category.ALL && notification != null) {
            notification.setCategory(category);

            storeAll.add(notification);
            // sort doesn't happen automatically on add()
            storeAll.sort(PROP_TIMESTAMP, SortDir.DESC);

            // we want to enforce a rolling list of MAX_NOTIFICATIONS size.
            if (storeAll.getCount() > MAX_NOTIFICATIONS) {
                // if we're over the MAX, drop the last element
                storeAll.remove(getLastElement());
            }
        }
    }

    private void getExistingNotifications(final Command callback) {
        UserInfo info = UserInfo.getInstance();

        facadeMessageService.getNotifications(info.getUsername(), MAX_NOTIFICATIONS,
                new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(I18N.ERROR.notificationRetrievalFail(), caught);

                        MessagePoller poller = MessagePoller.getInstance();
                        poller.start();
                        if (callback != null) {
                            callback.execute();
                        }
                    }

                    @Override
                    public void onSuccess(String result) {
                        addInitialNotificationsToStore(result);

                        MessagePoller poller = MessagePoller.getInstance();
                        poller.start();
                        if (callback != null) {
                            callback.execute();
                        }
                        storeLoadCompleteCallbackCmd.execute();
                    }
                });
    }

    private void addInitialNotificationsToStore(final String json) {
        JSONObject objMessages = JSONParser.parseStrict(json).isObject();

        if (objMessages != null) {
            JSONArray arr = objMessages.get("messages").isArray(); //$NON-NLS-1$

            if (arr != null) {
                String type;
                JSONObject objItem;

                for (int i = 0,len = arr.size(); i < len; i++) {
                    objItem = arr.get(i).isObject();

                    if (objItem != null) {
                        type = JsonUtil.getString(objItem, "type"); //$NON-NLS-1$

                        addItemToStore(type, objItem);
                    }
                }
            }
        }
    }

    private void addItemToStore(final String type, final JSONObject objItem) {
        if (type != null && objItem != null) {
            JSONObject objMessage = objItem.get("message").isObject(); //$NON-NLS-1$
            JSONObject objPayload = objItem.get("payload").isObject(); //$NON-NLS-1$

            if (type.equals("data")) { //$NON-NLS-1$
                addItemToStore(Category.DATA, objMessage, dataContextBuilder.build(objPayload));
            } else if (type.equals("analysis")) { //$NON-NLS-1$
                addItemToStore(Category.ANALYSIS, objMessage, analysisContextBuilder.build(objPayload));
            }
        }
    }

    /**
     * Initialize the notification manager.
     */
    public void init() {
        // initialization code goes here.
    }

    private void doDelete(final List<Notification> notifications, final String json,
            final Command callback) {
        if (notifications != null && json != null) {
            facadeMessageService.deleteMessages(json, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(I18N.ERROR.notificationDeletFail(), caught);
                    if (callback != null) {
                        callback.execute();
                    }
                }

                @Override
                public void onSuccess(String result) {
                    for (Notification notification : notifications) {
                        storeAll.remove(notification);
                    }

                    // load next set
                    storeAll.removeAll();
                    getExistingNotifications(callback);

                }
            });
        }
    }

    /**
     * Delete a list of notifications.
     * 
     * @param notifications notifications to be deleted.
     */
    public void delete(final List<Notification> notifications, Command callback) {
        // do we have any notifications to delete?
        if (notifications != null && !notifications.isEmpty()) {
            boolean first = true;
            StringBuffer buf = new StringBuffer();

            buf.append("["); //$NON-NLS-1$
            for (Notification notification : notifications) {
                if (first) {
                    first = false;
                } else {
                    buf.append(", "); //$NON-NLS-1$
                }

                buf.append(JsonUtil.quoteString(notification.getId()));
            }

            buf.append("]"); //$NON-NLS-1$

            doDelete(notifications, buf.toString(), callback);
        }
    }

    /**
     * Retrieve all notifications.
     * 
     * @return store containing desired notifications.
     */
    public ListStore<Notification> getNotifications() {
        return storeAll;
    }
}