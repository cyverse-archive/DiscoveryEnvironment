package org.iplantc.de.client.views;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.events.UserEvent;
import org.iplantc.core.uicommons.client.events.UserEventHandler;
import org.iplantc.core.uicommons.client.models.WindowConfig;
import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.controllers.TitoController;
import org.iplantc.de.client.events.LogoutEvent;
import org.iplantc.de.client.events.LogoutEventHandler;
import org.iplantc.de.client.events.WindowPayloadEvent;
import org.iplantc.de.client.events.WindowPayloadEventHandler;
import org.iplantc.de.client.factories.WindowConfigFactory;

import org.iplantc.de.client.services.callbacks.DiskResourceServiceCallback;
import org.iplantc.de.client.services.callbacks.FileEditorServiceFacade;
import org.iplantc.de.client.utils.DEStateManager;
import org.iplantc.de.client.utils.DEWindowManager;
import org.iplantc.de.client.utils.LogoutUtil;
import org.iplantc.de.client.views.taskbar.IPlantTaskButton;
import org.iplantc.de.client.views.taskbar.IPlantTaskbar;
import org.iplantc.de.client.views.windows.FileViewerWindow;
import org.iplantc.de.client.views.windows.FileWindow;
import org.iplantc.de.client.views.windows.Gxt3IplantWindow;
import org.iplantc.de.client.views.windows.IPlantWindow;
import org.iplantc.de.client.views.windows.IPlantWindowInterface;

import com.extjs.gxt.ui.client.core.DomQuery;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.ActivateEvent;
import com.sencha.gxt.widget.core.client.event.ActivateEvent.ActivateHandler;
import com.sencha.gxt.widget.core.client.event.DeactivateEvent;
import com.sencha.gxt.widget.core.client.event.DeactivateEvent.DeactivateHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.MinimizeEvent;
import com.sencha.gxt.widget.core.client.event.MinimizeEvent.MinimizeHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;

/**
 * Provides user interface for desktop workspace area.
 */
public class DesktopView extends ContentPanel implements ActivateHandler<Window>,
        DeactivateHandler<Window>, HideHandler, MinimizeHandler, ShowHandler {
    @SuppressWarnings("unused")
    private TitoController controllerTito;
    private DEWindowManager mgrWindow;
    private El shortcutEl;
    private LayoutContainer desktop;
    private IPlantTaskbar taskBar;
    private WindowConfigFactory factoryWindowConfig;
    // following variables are required for checking
    // save session state rpc is completed and then redirect
    // to logout
    private boolean session_save_completed = false;
    private final int SESSION_SAVE_TIMEOUT = 12000;
    private int expired_time = 0;
    private final int INTERVEL = 1000;

    private DEStateManager mgrState;

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
        mgrState.restoreUserSession();
    }

    private void init() {
        factoryWindowConfig = new WindowConfigFactory();

        initEventHandlers();
        initTaskbar();
        initWindowManager();
        initStateManager();
        controllerTito = TitoController.getInstance();
        initDesktop();
        initShortcuts();
    }

    private void initStateManager() {
        mgrState = DEStateManager.getInstance(mgrWindow);
    }

    private void showWindow(final String type, final WindowConfig config) {
        String tag = type;
        // if we have params, the unique window identifier will be type + params
        if (config != null) {
            tag += config.getTagSuffix();
        }

        IPlantWindowInterface window = mgrWindow.getWindow(tag);

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
        // ShortcutManager mgr = new ShortcutManager(new DefaultDesktopBuilder());
        //
        // List<Shortcut> shortcuts = mgr.getShortcuts();
        //
        // for (Shortcut shortcut : shortcuts) {
        // addShortcut(shortcut);
        // }
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

        // save session to database on logout
        eventbus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {
            @Override
            public void onLogout(LogoutEvent event) {
                MonitorSessionPersistance();
                mgrState.saveUserSession();
                mgrState.persistUserSession(false, new Command() {
                    @Override
                    public void execute() {
                        session_save_completed = true;
                    }
                });
            }
        });
    }

    private void MonitorSessionPersistance() {
        Timer timer = new Timer() {
            @Override
            public void run() {
                if (session_save_completed || (expired_time == SESSION_SAVE_TIMEOUT)) {
                    cancel();
                    mgrState.stop();
                    redirectToLogoutPage();
                } else {
                    expired_time = expired_time + INTERVEL;
                }

            }
        };
        timer.scheduleRepeating(INTERVEL);
    }

    /**
     * Perform logout redirection.
     */
    private void redirectToLogoutPage() {
        com.google.gwt.user.client.Window.Location.assign(LogoutUtil.buildLogoutUrl());
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
        IPlantWindowInterface activeWindow = mgrWindow.getActiveWindow();

        if (activeWindow != null && activeWindow != window) {
            markInactive(activeWindow);
        }

        taskBar.setActiveButton((IPlantTaskButton)window.getData("taskButton")); //$NON-NLS-1$
        mgrWindow.setActiveWindow(window);

        IPlantTaskButton btn = window.getData("taskButton"); //$NON-NLS-1$
        btn.addStyleName("active-win"); //$NON-NLS-1$
        window.setData("minimize", null); //$NON-NLS-1$
    }

    private void markInactive(IPlantWindowInterface window) {
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

    private void minimizeWindow(Gxt3IplantWindow window) {
        window.setData("minimize", true); //$NON-NLS-1$
        window.hide();
    }

    @Override
    public void onShow(ShowEvent event) {
        Window w = (Window)event.getSource();
        IPlantTaskButton btn = w.getData("taskButton"); //$NON-NLS-1$
        w.setData("minimize", null); //$NON-NLS-1$

        if (btn != null && taskBar.getButtons().contains(btn)) {
            return;
        }

        // taskBar.addTaskButton(w);

    }

    @Override
    public void onMinimize(MinimizeEvent event) {
        minimizeWindow((Gxt3IplantWindow)event.getSource());

    }

    @Override
    public void onHide(HideEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeactivate(DeactivateEvent<Window> event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActivate(ActivateEvent<Window> event) {
        // TODO Auto-generated method stub

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
        }, this, this, this, this, this);
    }

    private final class DEReloadListener implements Listener<ComponentEvent> {
        @Override
        public void handleEvent(ComponentEvent be) {
            mgrState.saveUserSession();
            mgrState.persistUserSession(false, null);
        }
    }

    /**
     * 
     * A event handler for WindowPayLoadEvent
     * 
     * @author sriram
     * 
     */
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
            IPlantWindowInterface window = mgrWindow.getWindow(tag);

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
