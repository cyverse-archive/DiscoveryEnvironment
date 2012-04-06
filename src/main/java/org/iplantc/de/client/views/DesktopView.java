package org.iplantc.de.client.views;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.UserEvent;
import org.iplantc.de.client.events.UserEventHandler;
import org.iplantc.de.client.events.WindowPayloadEvent;
import org.iplantc.de.client.events.WindowPayloadEventHandler;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.WindowConfig;
import org.iplantc.de.client.services.DiskResourceServiceCallback;
import org.iplantc.de.client.services.FileEditorServiceFacade;
import org.iplantc.de.client.utils.DEStateManager;
import org.iplantc.de.client.utils.DEWindowManager;
import org.iplantc.de.client.utils.MessageDispatcher;
import org.iplantc.de.client.utils.ShortcutManager;
import org.iplantc.de.client.utils.builders.DefaultDesktopBuilder;
import org.iplantc.de.client.views.taskbar.IPlantTaskButton;
import org.iplantc.de.client.views.taskbar.IPlantTaskbar;
import org.iplantc.de.client.views.windows.FileViewerWindow;
import org.iplantc.de.client.views.windows.FileWindow;
import org.iplantc.de.client.views.windows.IPlantWindow;

import com.extjs.gxt.ui.client.core.DomQuery;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.state.StateManager;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Provides user interface for desktop workspace area.
 */
public class DesktopView extends ContentPanel {

    private DEWindowManager mgrWindow;
    private El shortcutEl;
    private LayoutContainer desktop;
    private IPlantTaskbar taskBar;
    private WindowConfigFactory factoryWindowConfig;

    public static final String ACTIVE_WINDOWS = "active_windows";

    /**
     * Default constructor.
     */
    public DesktopView() {
        setHeaderVisible(false);
        setFooter(false);
        setBodyBorder(false);
        setBorders(false);
        setBodyStyleName("iplantc-portal-component"); //$NON-NLS-1$

        init();

        setLayout(new RowLayout());
        add(desktop, new RowData(1, 1));
    }

    private void init() {
        factoryWindowConfig = new WindowConfigFactory();

        initEventHandlers();
        initTaskbar();
        initWindowManager();
        initDesktop();
        initShortcuts();
    }

    private void showWindow(final String type, final WindowConfig config) {
        String tag = type;
        // if we have params, the unique window identifier will be type + params
        if (config != null) {
            tag += config.getTagSuffix();
        }

        IPlantWindow window = mgrWindow.getWindow(tag);

        // do we already have this window?
        if (window == null) {
            window = mgrWindow.add(tag, config);
        }

        // show the window and bring it to the front
        if (window != null) {
            window.setWindowConfig(config);
            mgrWindow.show(window.getTag());
        }
    }

    private void dispatchUserEvent(String action, String tag, WindowConfig params) {
        if (action.equals(Constants.CLIENT.windowTag())) {
            showWindow(tag, params);
        }
    }

    private void initTaskbar() {
        taskBar = new IPlantTaskbar();
        setBottomComponent(taskBar);
    }

    private void initShortcuts() {
        ShortcutManager mgr = new ShortcutManager(new DefaultDesktopBuilder());

        List<Shortcut> shortcuts = mgr.getShortcuts();

        for (Shortcut shortcut : shortcuts) {
            addShortcut(shortcut);
        }
    }

    private void initDesktop() {
        desktop = new LayoutContainer() {
            @Override
            protected void onRender(Element parent, int index) {
                super.onRender(parent, index);
                Element el = DomQuery.selectNode("#x-desktop"); //$NON-NLS-1$
                if (el == null) {
                    el = DOM.createDiv();
                    el.setClassName("x-desktop"); //$NON-NLS-1$
                }
                getElement().appendChild(el);
            }
        };

        Element el = DomQuery.selectNode("#x-shortcuts"); //$NON-NLS-1$

        if (el == null) {
            el = DOM.createDiv();
            el.setClassName("x-shortcuts"); //$NON-NLS-1$
            XDOM.getBody().appendChild(el);
        }

        desktop.addListener(Events.Detach, new DEReloadListener());
        desktop.addListener(Events.Render, new DEAfterRenderListener());

        shortcutEl = new El(el);
    }

    private void initEventHandlers() {
        EventBus eventbus = EventBus.getInstance();

        // user action
        eventbus.addHandler(UserEvent.TYPE, new UserEventHandler() {
            @Override
            public void onEvent(UserEvent event) {
                dispatchUserEvent(event.getAction(), event.getType(), event.getParams());
            }
        });

        // window payload events
        eventbus.addHandler(WindowPayloadEvent.TYPE, new WindowPayloadEventHandlerImpl());
    }

    /**
     * Sets the background color of the Desktop to the given CSS color value.
     * 
     * @param cssColor
     */
    public void setBackgroundColor(String cssColor) {
        // setBodyStyle("background-color: " + cssColor);
    }

    /**
     * Adds a shortcut to the desktop.
     * 
     * @param shortcut the shortcut to add
     */
    public void addShortcut(Shortcut shortcut) {
        if (shortcutEl != null) {
            shortcut.render(shortcutEl.dom);
            ComponentHelper.doAttach(shortcut);
        }
    }

    private void onHide(IPlantWindow window) {
        if (window.getData("minimize") != null) { //$NON-NLS-1$
            markInactive(window);

        } else {
            if (mgrWindow.getActiveWindow() == window) {
                mgrWindow.setActiveWindow(null);
            }

            window.cleanup();
            mgrWindow.remove(window.getTag());

            taskBar.removeTaskButton((IPlantTaskButton)window.getData("taskButton")); //$NON-NLS-1$
        }
    }

    private void markActive(IPlantWindow window) {
        IPlantWindow activeWindow = mgrWindow.getActiveWindow();

        if (activeWindow != null && activeWindow != window) {
            markInactive(activeWindow);
        }

        taskBar.setActiveButton((IPlantTaskButton)window.getData("taskButton")); //$NON-NLS-1$
        mgrWindow.setActiveWindow(window);

        IPlantTaskButton btn = window.getData("taskButton"); //$NON-NLS-1$
        btn.addStyleName("active-win"); //$NON-NLS-1$
        window.setData("minimize", null); //$NON-NLS-1$
    }

    private void markInactive(IPlantWindow window) {
        if (window == mgrWindow.getActiveWindow()) {
            mgrWindow.setActiveWindow(null);

            IPlantTaskButton btn = window.getData("taskButton"); //$NON-NLS-1$
            btn.removeStyleName("active-win"); //$NON-NLS-1$
        }
    }

    private void onShow(IPlantWindow window) {
        IPlantTaskButton btn = window.getData("taskButton"); //$NON-NLS-1$
        window.setData("minimize", null); //$NON-NLS-1$

        if (btn != null && taskBar.getButtons().contains(btn)) {
            return;
        }

        taskBar.addTaskButton(window);
    }

    private void minimizeWindow(IPlantWindow window) {
        window.setData("minimize", true); //$NON-NLS-1$
        window.hide();
    }

    private void initWindowManager() {
        mgrWindow = new DEWindowManager(new WindowListener() {
            @Override
            public void windowActivate(WindowEvent we) {
                markActive((IPlantWindow)we.getWindow());
            }

            @Override
            public void windowDeactivate(WindowEvent we) {
                markInactive((IPlantWindow)we.getWindow());
            }

            @Override
            public void windowHide(WindowEvent we) {
                onHide((IPlantWindow)we.getWindow());
            }

            @Override
            public void windowMinimize(WindowEvent we) {
                minimizeWindow((IPlantWindow)we.getWindow());
            }

            @Override
            public void windowShow(WindowEvent we) {
                onShow((IPlantWindow)we.getWindow());
            }
        });
    }

    private final class DEReloadListener implements Listener<ComponentEvent> {
        @Override
        public void handleEvent(ComponentEvent be) {
            saveWindowState(ACTIVE_WINDOWS, mgrWindow.getActiveWindowStates());
        }

        private void saveWindowState(String tag, Map<String, String> state) {
            if (state != null) {
                StateManager mgr = DEStateManager.getStateManager();
                List<Window> windows = mgrWindow.getStack();
                for (Window w : windows) {
                    System.out.println(((IPlantWindow)w).getTag());
                }
                System.out.println("win-->" + state.toString());
                mgr.set(tag, state);
            }
        }
    }

    private final class DEAfterRenderListener implements Listener<ComponentEvent> {
        @Override
        public void handleEvent(ComponentEvent be) {
            StateManager mgr = DEStateManager.getStateManager();
            Map<String, Object> win_state = mgr.getMap(ACTIVE_WINDOWS);
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

        private List<JSONObject> getOrderedState(Set<String> tags, Map<String, Object> win_state) {
            List<JSONObject> temp = new ArrayList<JSONObject>();
            for (String tag : tags) {
                JSONObject obj = JsonUtil.getObject(win_state.get(tag).toString());
                temp.add(obj);
            }
            java.util.Collections.sort(temp, new WindowOrderComparator());
            return temp;

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

    private class WindowPayloadEventHandlerImpl implements WindowPayloadEventHandler {
        private boolean isWindowDisplayPayload(final JSONObject objPayload) {
            boolean ret = false; // assume failure

            // do we have a payload?
            if (objPayload != null) {
                // get our action
                String action = JsonUtil.getString(objPayload, "action"); //$NON-NLS-1$

                if (action.equals("display_window")) { //$NON-NLS-1$
                    ret = true;
                }
            }

            return ret;
        }

        private boolean payloadContainsAction(String action, final JSONObject payload) {
            return JsonUtil.getString(payload, "action").equals(action); //$NON-NLS-1$
        }

        private boolean isViewerPayload(final JSONObject objPayload) {
            return payloadContainsAction("display_viewer", objPayload); //$NON-NLS-1$
        }

        private boolean isTreeViewerPayload(final JSONObject objPayload) {
            return payloadContainsAction("display_viewer_add_treetab", objPayload); //$NON-NLS-1$
        }

        @Override
        public void onFire(WindowPayloadEvent event) {
            JSONObject objPayload = event.getPayload();

            if (objPayload != null) {
                if (isWindowDisplayPayload(objPayload)) {
                    JSONObject objData = JsonUtil.getObject(objPayload, "data"); //$NON-NLS-1$

                    if (objData != null) {
                        String tag = JsonUtil.getString(objData, "tag"); //$NON-NLS-1$

                        WindowConfig config = factoryWindowConfig.build(JsonUtil.getObject(objData,
                                "config")); //$NON-NLS-1$

                        showWindow(tag, config);
                    }
                } else {
                    if (isViewerPayload(objPayload)) {
                        addFileWindows(objPayload, false);
                    } else if (isTreeViewerPayload(objPayload)) {
                        addFileWindows(objPayload, true);
                    }
                }
            }
        }

        private void addFileWindows(final JSONObject objPayload, boolean addTreeTab) {
            if (objPayload != null) {
                JSONObject objData = JsonUtil.getObject(objPayload, "data"); //$NON-NLS-1$

                WindowConfig config = factoryWindowConfig.build(JsonUtil.getObject(objData, "config")); //$NON-NLS-1$

                if (config != null)
                    System.out.println("config==>" + config.toString());

                if (objData != null) {
                    JSONValue valFiles = objData.get("files"); //$NON-NLS-1$

                    if (!JsonUtil.isEmpty(valFiles)) {
                        String tag;
                        JSONArray files = valFiles.isArray();

                        // loop through our sub-folders and recursively add them
                        for (int i = 0,len = files.size(); i < len; i++) {
                            JSONObject file = JsonUtil.getObjectAt(files, i);

                            FileIdentifier identifier = buildFileIdentifier(file);

                            if (identifier != null) {
                                tag = Constants.CLIENT.fileEditorTag() + identifier.getFileId();

                                addFileWindow(tag, identifier, addTreeTab, config);
                            }
                        }
                    }
                }
            }
        }

        private FileIdentifier buildFileIdentifier(final JSONObject objData) {
            FileIdentifier ret = null; // assume failure

            if (objData != null) {
                String id = JsonUtil.getString(objData, "id"); //$NON-NLS-1$
                String name = JsonUtil.getString(objData, "name"); //$NON-NLS-1$
                String idParent = JsonUtil.getString(objData, "idParent"); //$NON-NLS-1$

                ret = new FileIdentifier(name, idParent, id);
            }

            return ret;
        }

        private void buildNewWindow(final String tag, final FileIdentifier file, final String json,
                boolean addTreeTab, WindowConfig config) {
            if (json != null) {
                FileWindow window;
                FileViewerWindow editorWindow = new FileViewerWindow(tag, file, json);

                if (addTreeTab) {
                    editorWindow.loadTreeTab();
                }
                editorWindow.setWindowConfig(config);
                window = editorWindow;
                mgrWindow.add(window);
                mgrWindow.show(tag);

            }
        }

        private void retrieveFileManifest(final String tag, final FileIdentifier file,
                final boolean addTreeTab, final WindowConfig config) {
            FileEditorServiceFacade facade = new FileEditorServiceFacade();

            facade.getManifest(file.getFileId(), new DiskResourceServiceCallback() {
                @Override
                public void onSuccess(String result) {
                    if (result != null) {
                        buildNewWindow(tag, file, result, addTreeTab, config);
                    } else {
                        onFailure(null);
                    }
                }

                @Override
                protected String getErrorMessageDefault() {
                    return I18N.ERROR.unableToRetrieveFileManifest(file.getFilename());
                }

                @Override
                protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
                    return getErrorMessageForFiles(code, file.getFilename());
                }
            });
        }

        private void addFileWindow(final String tag, final FileIdentifier file, boolean addTreeTab,
                WindowConfig config) {
            IPlantWindow window = mgrWindow.getWindow(tag);

            // do we already have a window for this file... let's bring it to the front
            if (window != null) {
                window.setWindowConfig(config);
                window.show();
                window.toFront();
                window.refresh();
            } else {
                retrieveFileManifest(tag, file, addTreeTab, config);
            }
        }

    }
}
