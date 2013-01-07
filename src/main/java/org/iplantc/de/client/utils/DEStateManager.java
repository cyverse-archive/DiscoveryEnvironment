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
import org.iplantc.de.client.Services;
import org.iplantc.de.client.events.SettingsUpdatedEvent;
import org.iplantc.de.client.events.SettingsUpdatedEventHandler;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.MessageBox.MessageBoxType;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
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
        initListeners();
        checkSettings();
    }

    private void enableSessionSave() {
        start();
        saveSession = true;
    }

    private void disableSessionSave() {
        stop();
        saveSession = false;
        clearSession();
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
        Services.USER_SESSION_SERVICE.clearUserSession(new AsyncCallback<String>() {

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

            final byte[] tempHash = JsonUtil.generateHash(obj.toString());
            if (!ByteArrayComparer.arraysEqual(hash, tempHash)) {
                Services.USER_SESSION_SERVICE.saveUserSession(obj, new SaveSessionCallback(savingMask,
                        tempHash, callback));
            }
        } else {
            if (callback != null) {
                callback.execute();
            }
        }
    }

    /**
     * Save user session
     * 
     */
    public void saveUserSession() {
        Services.USER_SESSION_SERVICE.saveUserPreferences(UserSettings.getInstance().toJson(),
                new AsyncCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        // intentionally do nothing
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        // intentionally do nothing, since the user is logging out or reloading the page
                    }
                });
    }

    /**
     * Restore users work session
     * 
     */
    public void restoreUserSession() {
        MessageBox loadingMask = new MessageBox();
        final LoadSessionCallback loadCallback = new LoadSessionCallback(loadingMask);

        loadingMask.setType(MessageBoxType.WAIT);
        loadingMask.setTitle(I18N.DISPLAY.loadingSession());
        loadingMask.setMessage(I18N.DISPLAY.loadingSessionWaitNotice());
        loadingMask.setProgressText(I18N.DISPLAY.loadingMask());
        loadingMask.setButtons(Dialog.CANCEL);
        loadingMask.setClosable(false);

        loadingMask.addCallback(new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent mbe) {
                Button btn = mbe.getButtonClicked();

                // did the user click cancel?
                if (btn != null && Dialog.CANCEL.equals(btn.getItemId())) {
                    loadCallback.cancelLoad();
                }
            }
        });
        loadingMask.show();

        Services.USER_SESSION_SERVICE.getUserSession(loadCallback);
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
            enableSessionSave();
        } else {
            disableSessionSave();
        }
    }

    private final class LoadSessionCallback implements AsyncCallback<String> {
        private final MessageBox loadingMask;
        private boolean loadState = true;

        private LoadSessionCallback(MessageBox loadingMask) {
            this.loadingMask = loadingMask;
        }

        public void cancelLoad() {
            this.loadState = false;
        }

        @Override
        public void onFailure(Throwable caught) {
            GWT.log(I18N.ERROR.loadSessionFailed(), caught);
            loadingMask.close();

            if (loadState) {
                MessageBox.info(I18N.ERROR.loadSessionFailed(), I18N.ERROR.loadSessionFailureNotice(),
                        null);
            }
        }

        @Override
        public void onSuccess(String result) {
            if (loadState && result != null && !result.isEmpty()) {
                JSONObject obj = JsonUtil.getObject(result);
                JSONObject win_states = JsonUtil.getObject(obj, ACTIVE_WINDOWS);
                restoreWindows(win_states);
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
