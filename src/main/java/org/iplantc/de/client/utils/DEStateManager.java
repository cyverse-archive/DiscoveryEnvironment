package org.iplantc.de.client.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.core.uicommons.client.util.ByteArrayComparer;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.SettingsUpdatedEvent;
import org.iplantc.de.client.events.SettingsUpdatedEventHandler;
import org.iplantc.de.client.services.UserSessionServiceFacade;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A singleton wrapper to DE State manager. Used to persist and restore user sessions
 * 
 * @author sriram
 * 
 */
public class DEStateManager {

    private static DEStateManager instance;
    private DEWindowManager mgrWindow;
    // session hash
    private byte[] hash;

    private final int SAVE_INTERVAL = 60000;

    private Timer t;

    public static final String ACTIVE_WINDOWS = "active_windows";
    public static final String NOTIFI_COUNT = "notification_count";

    public static final String PREFERENCES = "preferences";

    private boolean saveSession;

    private DEStateManager() {

        UserSessionServiceFacade facade = new UserSessionServiceFacade();
        facade.getUserPreferences(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                // by default enable save session
                start();
                saveSession = true;
            }

            @Override
            public void onSuccess(String result) {
                loadPrerences(JSONParser.parseStrict(result).isObject());
                checkSettings();

            }
        });

        initListeners();
    }

    private void loadPrerences(JSONObject obj) {
        UserSettings.getInstance().setValues(obj);
    }

    /**
     * Stop periodic saving
     * 
     */
    public void stop() {
        instance = null;
        if (t != null) {
            t.cancel();
        }
    }

    /**
     * Start periodic saving
     * 
     */
    public void start() {
        // kick-off a timer that saves users session at regular intervals
        t = new Timer() {

            @Override
            public void run() {
                persistUserSession(true, null);

            }
        };

        t.scheduleRepeating(SAVE_INTERVAL);
    }

    public static DEStateManager getInstance(DEWindowManager mgrWindow) {
        if (instance == null) {
            instance = new DEStateManager();
            instance.mgrWindow = mgrWindow;

        }

        return instance;
    }

    private void initListeners() {
        EventBus.getInstance().addHandler(SettingsUpdatedEvent.TYPE, new SettingsUpdatedEventHandler() {

            @Override
            public void onUpdate(SettingsUpdatedEvent event) {
                checkSettings();
            }
        });
    }

    private void clearSession() {
        UserSessionServiceFacade facade = new UserSessionServiceFacade();
        facade.clearUserSession(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(String result) {
                // do nothing

            }
        });

    }

    public void persistUserSession(final boolean runInBackground, final Command callback) {

        if (saveSession) {
            MessageBox savingMask = null;

            if (!runInBackground) {
                savingMask = MessageBox.wait(I18N.DISPLAY.savingSession(),
                        I18N.DISPLAY.savingSessionWaitNotice(), I18N.DISPLAY.savingMask());
                savingMask.show();
            }

            JSONObject obj = new JSONObject();
            obj.put(ACTIVE_WINDOWS, mgrWindow.getActiveWindowStates());
            obj.put(NOTIFI_COUNT, NotificationManager.getInstance().getNotificationCountStatus());

            final byte[] tempHash = JsonUtil.generateHash(obj.toString());
            if (!ByteArrayComparer.arraysEqual(hash, tempHash)) {
                UserSessionServiceFacade session = new UserSessionServiceFacade();
                session.saveUserSession(obj, new SaveSessionCallback(savingMask, tempHash, callback));
            }
        } else {
            if (callback != null) {
                callback.execute();
            }
        }
    }

    /**
     * Restore users work session
     * 
     */
    public void restoreUserSession() {
        final MessageBox loadingMask = MessageBox.wait(I18N.DISPLAY.loadingSession(),
                I18N.DISPLAY.loadingSessionWaitNotice(), I18N.DISPLAY.loadingMask());

        UserSessionServiceFacade session = new UserSessionServiceFacade();
        session.getUserSession(new LoadSessionCallback(loadingMask));
    }

    private void restoreNotificationCountStatus(JSONObject obj) {
        if (obj != null) {
            int data_count = Integer.parseInt(JsonUtil.getString(obj,
                    NotificationManager.DATA_NOTIFI_COUNT));
            int anal_count = Integer.parseInt(JsonUtil.getString(obj,
                    NotificationManager.ANALYSES_NOTIFI_COUNT));
            NotificationManager.getInstance().initNotificationCount(data_count, anal_count);
        }

    }

    private void restoreWindows(JSONObject win_state) {
        if (win_state != null) {
            Set<String> tags = win_state.keySet();
            if (tags.size() > 0) {
                for (JSONObject state : getOrderedState(tags, win_state)) {
                    if (state != null) {
                        MessageDispatcher dispatcher = new MessageDispatcher();
                        dispatcher.processMessage(state);
                    }
                }
            }
        }
    }

    private List<JSONObject> getOrderedState(Set<String> tags, JSONObject win_state) {
        List<JSONObject> temp = new ArrayList<JSONObject>();
        for (String tag : tags) {
            JSONObject obj = JsonUtil.getObject(win_state.get(tag).toString());
            temp.add(obj);
        }
        java.util.Collections.sort(temp, new WindowOrderComparator());
        return temp;

    }

    private void checkSettings() {
        UserSettings us = UserSettings.getInstance();
        if (us.isSaveSession()) {
            start();
            saveSession = true;
        } else {
            stop();
            saveSession = false;
            clearSession();
        }
    }

    private final class LoadSessionCallback implements AsyncCallback<String> {
        private final MessageBox loadingMask;

        private LoadSessionCallback(MessageBox loadingMask) {
            this.loadingMask = loadingMask;
        }

        @Override
        public void onFailure(Throwable caught) {
            GWT.log(I18N.ERROR.loadSessionFailed(), caught);
            loadingMask.close();
            MessageBox.info(I18N.ERROR.loadSessionFailed(), I18N.ERROR.loadSessionFailureNotice(), null);
        }

        @Override
        public void onSuccess(String result) {
            if (result != null && !result.isEmpty()) {
                JSONObject obj = JsonUtil.getObject(result);
                JSONObject win_states = JsonUtil.getObject(obj, ACTIVE_WINDOWS);
                restoreWindows(win_states);
                restoreNotificationCountStatus(JsonUtil.getObject(obj, NOTIFI_COUNT));
            }
            loadingMask.close();
        }
    }

    private final class SaveSessionCallback implements AsyncCallback<String> {
        private final MessageBox savingMask;
        private final byte[] tempHash;
        private final Command callback;

        private SaveSessionCallback(MessageBox savingMask, byte[] tempHash, Command callback) {
            this.savingMask = savingMask;
            this.tempHash = tempHash;
            this.callback = callback;
        }

        @Override
        public void onSuccess(String result) {
            if (callback != null) {
                callback.execute();
            }

            // update hash
            hash = tempHash;

            if (savingMask != null) {
                savingMask.close();
            }
        }

        @Override
        public void onFailure(Throwable caught) {
            GWT.log(I18N.ERROR.saveSessionFailed(), caught);

            if (callback != null) {
                callback.execute();
            }

            if (savingMask != null) {
                savingMask.close();
            }
        }
    }

    private class WindowOrderComparator implements Comparator<JSONObject> {
        @Override
        public int compare(JSONObject arg0, JSONObject arg1) {
            if (arg0 != null && arg1 != null) {
                try {
                    int temp1 = Integer.parseInt(JsonUtil.getString(arg0, "order"));
                    int temp2 = Integer.parseInt(JsonUtil.getString(arg1, "order"));
                    return temp1 - temp2;
                } catch (Exception e) {
                    // if order is not present, dont care about it
                    return 0;
                }
            } else {
                // if any of object is null, dont care about ordering
                return 0;
            }
        }

    }

}
