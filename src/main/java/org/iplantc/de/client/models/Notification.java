package org.iplantc.de.client.models;

import java.util.Date;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.utils.NotificationManager.Category;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * Models a notification presented by the system to the user.
 */
public class Notification extends DEBaseModelData {
    /** The format used internally, not to be confused with the display format. */
    public static final DateTimeFormat TIMESTAMP_FORMAT = DateTimeFormat
            .getFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z"); //$NON-NLS-1$
    /** Name of the time stamp property */
    public static final String PROP_TIMESTAMP = "timestamp"; //$NON-NLS-1$
    /** Name of the notification category property */
    public static final String PROP_CATEGORY = "category"; //$NON-NLS-1$
    /** Name of the notification message property */
    public static final String PROP_MESSAGE = "message"; //$NON-NLS-1$
    private static final long serialVersionUID = -3739609640220095801L;

    /**
     * Constructs a new instance of a notification given a message argument.
     * 
     * A timestamp is generated at the time of construction and made available.
     * 
     * @param message a string representing a message for the user.
     */
    public Notification(final String message) {
        if (message == null) {
            clear();
        } else {
            set("id", new Date().getTime()); //$NON-NLS-1$ //$NON-NLS-2$
            set(PROP_CATEGORY, ""); //$NON-NLS-1$
            set(PROP_MESSAGE, message);
            set("context", null); //$NON-NLS-1$
            set(PROP_TIMESTAMP, new Date());
        }
    }

    /**
     * Instantiate from JSON object and context. Required fields: id, category, text, stamp.
     * 
     * @param message message JSON object containing message data.
     * @param context client command to execute when clicked.
     */
    public Notification(final JSONObject message, final String context) {
        if (message == null) {
            clear();
        } else {
            set("id", getField(message, "id")); //$NON-NLS-1$ //$NON-NLS-2$
            set(PROP_CATEGORY, getField(message, PROP_CATEGORY));
            set(PROP_MESSAGE, getField(message, "text")); //$NON-NLS-1$
            if (message.get(PROP_TIMESTAMP) != null) {
                String timestamp = message.get(PROP_TIMESTAMP).toString();
                timestamp = JsonUtil.trim(timestamp);
                setTimeStamp(timestamp);
            }
        }

        set("context", context); //$NON-NLS-1$
    }

    private void setTimeStamp(String timestamp) {
        set(PROP_TIMESTAMP, parseDate(timestamp));
    }

    private Date parseDate(String date) {
        long timestamp;

        try {
            timestamp = Long.parseLong(date);
        } catch (Exception e) {
            timestamp = 0;
        }

        return new Date(timestamp);
    }

    /**
     * Instantiate from JSON object. Required fields: id, category, text, stamp. stamp must use the
     * DATE_FORMAT format.
     * 
     * @param message JSON object containing message data.
     * 
     */
    public Notification(final JSONObject message) {
        this(message, null);
    }

    private void clear() {
        set("id", ""); //$NON-NLS-1$ //$NON-NLS-2$
        set(PROP_CATEGORY, ""); //$NON-NLS-1$
        set(PROP_MESSAGE, ""); //$NON-NLS-1$
        set(PROP_TIMESTAMP, ""); //$NON-NLS-1$
    }

    private String getField(final JSONObject jsonObj, final String key) {
        String ret = ""; // assume failure //$NON-NLS-1$

        if (jsonObj != null && key != null) {
            JSONValue val = jsonObj.get(key);

            if (val != null) {
                JSONString str = val.isString();

                if (str != null) {
                    ret = str.stringValue();
                }
            }
        }

        return ret;
    }

    /**
     * Retrieve the id associated with the notification.
     * 
     * @return a string representing the notification id.
     */
    public String getId() {
        return get("id").toString(); //$NON-NLS-1$
    }

    /**
     * Retrieve the category associated with the notification.
     * 
     * @return the notification category, or null if no category is set.
     */
    public Category getCategory() {
        String categoryName = get(PROP_CATEGORY).toString();
        if (categoryName == null || categoryName.isEmpty()) {
            return null;
        } else {
            return Category.fromTypeString(categoryName);
        }
    }

    /**
     * Set the category associated with this notificiation.
     * 
     * @param category new category to set
     */
    public void setCategory(final Category category) {
        set(PROP_CATEGORY, (category == null) ? "" : category); //$NON-NLS-1$
    }

    /**
     * Retrieve the message associated with the notification.
     * 
     * @return a string representing the notification message.
     */
    public String getMessage() {
        return get(PROP_MESSAGE).toString();
    }

    /**
     * Returns the client command to execute when clicked.
     * 
     * @return the client command
     */
    public String getContext() {
        return get("context"); //$NON-NLS-1$
    }

    /**
     * Returns the timestamp associated with the notification
     * 
     * @return the timestamp
     */
    @SuppressWarnings("deprecation")
    public Date getTimestamp() {
        if (get(PROP_TIMESTAMP) != null && !get(PROP_TIMESTAMP).toString().isEmpty()) {
            return new Date(get(PROP_TIMESTAMP).toString());
        } else {
            return new Date();
        }

    }
}
