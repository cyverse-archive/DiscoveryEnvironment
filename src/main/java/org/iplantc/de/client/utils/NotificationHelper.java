package org.iplantc.de.client.utils;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.DeleteNotificationsUpdateEvent;
import org.iplantc.de.client.events.NotificationCountUpdateEvent;
import org.iplantc.de.client.models.Notification;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.client.utils.builders.context.AnalysisContextBuilder;
import org.iplantc.de.client.utils.builders.context.DataContextBuilder;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * helps with notifications for the user.
 * 
 * 
 * @author lenards, sriram
 * 
 */
public class NotificationHelper {
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
        ANALYSIS(I18N.CONSTANT.notificationCategoryAnalysis()),

        /** unseen notifications */
        NEW(I18N.CONSTANT.notificationCategoryUnseen());

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

    private static NotificationHelper instance = null;

    private DataContextBuilder dataContextBuilder;
    private AnalysisContextBuilder analysisContextBuilder;

    private DataViewContextExecutor dataContextExecutor;
    private AnalysisViewContextExecutor analysisContextExecutor;

    private final MessageServiceFacade facadeMessageService;

    private NotificationHelper() {
        facadeMessageService = new MessageServiceFacade();

        initContextBuilders();
        initContextExecuters();
        initMessagePoller();

    }

    private void initContextBuilders() {
        dataContextBuilder = new DataContextBuilder();
        analysisContextBuilder = new AnalysisContextBuilder();
    }

    private void initContextExecuters() {
        dataContextExecutor = new DataViewContextExecutor();
        analysisContextExecutor = new AnalysisViewContextExecutor();
    }

    public String buildAnalysisContext(JSONObject objPayload) {
        return analysisContextBuilder.build(objPayload);
    }

    public String buildDataContext(JSONObject objPayload) {
        return dataContextBuilder.build(objPayload);
    }

    /** View a notification */
    public void view(Notification notification) {
        if (notification != null) {
            NotificationHelper.Category category = notification.getCategory();

            // did we get a category?
            if (category != null) {
                String context = notification.getContext();

                // did we get a context to execute?
                if (context != null) {
                    if (category == NotificationHelper.Category.DATA) {
                        // execute data context
                        dataContextExecutor.execute(context);
                    } else if (category == NotificationHelper.Category.ANALYSIS) {
                        analysisContextExecutor.execute(context);
                    }
                }
            }
        }
    }

    /**
     * Return the shared, singleton instance of the manager.
     * 
     * @return a singleton reference to the notification manager.
     */
    public static NotificationHelper getInstance() {
        if (instance == null) {
            instance = new NotificationHelper();
        }

        return instance;
    }

    /**
     * Mark notifications as seen
     * 
     */
    public void markAsSeen(List<Notification> list) {
        if (list != null && list.size() > 0) {
            JSONArray arr = buildSeenServiceRequestBody(list);

            if (arr.size() > 0) {
                JSONObject obj = new JSONObject();
                obj.put("uuids", arr);

                MessageServiceFacade facade = new MessageServiceFacade();
                facade.markAsSeen(obj, new AsyncCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        JSONObject obj = JSONParser.parseStrict(result).isObject();
                        int new_count = Integer.parseInt(JsonUtil.getString(obj, "count"));
                        // fire update of the new unseen count;
                        NotificationCountUpdateEvent event = new NotificationCountUpdateEvent(new_count);
                        EventBus.getInstance().fireEvent(event);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(caught);
                    }
                });
            }
        }
    }

    private JSONArray buildSeenServiceRequestBody(List<Notification> list) {
        JSONArray arr = new JSONArray();
        int i = 0;

        for (Notification n : list) {
            if (!n.isSeen()) {
                arr.set(i++, new JSONString(n.get("id").toString()));
                n.set(Notification.SEEN, true);
            }
        }
        return arr;
    }

    /**
     * Initialize the notification manager.
     */
    public void init() {
        // initialization code goes here.
    }

    private void doDelete(final List<Notification> notifications, final JSONObject json,
            final Command callback) {
        if (notifications != null && json != null) {
            facadeMessageService.deleteMessages(json, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(I18N.ERROR.notificationDeletFail(), caught);

                }

                @Override
                public void onSuccess(String result) {
                    if (callback != null) {
                        callback.execute();
                    }
                    DeleteNotificationsUpdateEvent event = new DeleteNotificationsUpdateEvent(
                            notifications);
                    EventBus.getInstance().fireEvent(event);
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
            JSONObject obj = new JSONObject();
            JSONArray arr = new JSONArray();
            int i = 0;
            for (Notification n : notifications) {
                arr.set(i++, new JSONString(n.getId()));
            }
            obj.put("uuids", arr);

            doDelete(notifications, obj, callback);
        }
    }

    private void initMessagePoller() {
        MessagePoller poller = MessagePoller.getInstance();
        poller.start();
    }

    public Notification buildNotification(final String type, final boolean seen, final JSONObject objItem) {
        if (type != null && objItem != null) {
            JSONObject objMessage = objItem.get("message").isObject(); //$NON-NLS-1$
            JSONObject objPayload = objItem.get("payload").isObject(); //$NON-NLS-1$

            if (type.equals("data")) { //$NON-NLS-1$
                return addItemToStore(Category.DATA, objMessage, seen, NotificationHelper.getInstance()
                        .buildDataContext(objPayload));
            } else if (type.equals("analysis")) { //$NON-NLS-1$
                return addItemToStore(Category.ANALYSIS, objMessage, seen, NotificationHelper
                        .getInstance().buildAnalysisContext(objPayload));
            }
        }

        return null;
    }

    private Notification addItemToStore(final Category category, final JSONObject objMessage,
            final boolean seen, final String context) {
        Notification ret = null; // assume failure

        if (objMessage != null) {
            ret = new Notification(objMessage, seen, context);
            ret.setCategory(category);
        }

        return ret;
    }

}
