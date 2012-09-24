package org.iplantc.de.client.utils;

import java.util.List;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.Services;
import org.iplantc.de.client.notifications.models.NotificationMessage;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
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

    private static NotificationHelper instance = null;
    private DataViewContextExecutor dataContextExecutor;
    private AnalysisViewContextExecutor analysisContextExecutor;

    private int total;

    private NotificationHelper() {
        initContextExecuters();
    }

    private void initContextExecuters() {
        dataContextExecutor = new DataViewContextExecutor();
        analysisContextExecutor = new AnalysisViewContextExecutor();
    }

    /** View a notification */
    public void view(NotificationMessage msg) {
        if (msg != null) {
            NotificationHelper.Category category = msg.getCategory();

            // did we get a category?
            if (category != null) {
                String context = msg.getContext();

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

    private void doDelete(final JSONObject json, final Command callback) {
        if (json != null) {
            Services.MESSAGE_SERVICE.deleteMessages(json, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(I18N.ERROR.notificationDeletFail(), caught);

                }

                @Override
                public void onSuccess(String result) {
                    if (callback != null) {
                        callback.execute();
                    }
                }
            });
        }
    }

    /**
     * Delete a list of notifications.
     * 
     * @param notifications notifications to be deleted.
     */
    public void delete(final List<NotificationMessage> notifications, Command callback) {
        // do we have any notifications to delete?
        if (notifications != null && !notifications.isEmpty()) {
            JSONObject obj = new JSONObject();
            JSONArray arr = new JSONArray();
            int i = 0;
            for (NotificationMessage n : notifications) {
                arr.set(i++, new JSONString(n.getId()));
            }
            obj.put("uuids", arr);

            doDelete(obj, callback);
        }
    }

    /**
     * @param total the total to set
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * @return the total
     */
    public int getTotal() {
        return total;
    }
}
