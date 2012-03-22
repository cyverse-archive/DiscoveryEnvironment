package org.iplantc.de.client.views;

import java.util.List;
import java.util.Map;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.controllers.EditorController;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.events.UserEvent;
import org.iplantc.de.client.events.UserEventHandler;
import org.iplantc.de.client.events.WindowPayloadEvent;
import org.iplantc.de.client.events.WindowPayloadEventHandler;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.factories.WindowFactory;
import org.iplantc.de.client.models.WindowConfig;
import org.iplantc.de.client.utils.DEStateManager;
import org.iplantc.de.client.utils.ShortcutManager;
import org.iplantc.de.client.utils.WindowManager;
import org.iplantc.de.client.utils.builders.DefaultDesktopBuilder;
import org.iplantc.de.client.views.taskbar.IPlantTaskButton;
import org.iplantc.de.client.views.taskbar.IPlantTaskbar;
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
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Provides user interface for desktop workspace area.
 */
public class DesktopView extends ContentPanel {

    @SuppressWarnings("unused")
    private EditorController controllerEditor;
    private WindowManager mgrWindow;
    private El shortcutEl;
    private LayoutContainer desktop;
    private IPlantTaskbar taskBar;
    private WindowConfigFactory factoryWindowConfig;

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
        initEditorController();
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
            window.configure(config);
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
        eventbus.addHandler(WindowPayloadEvent.TYPE, new WindowPayloadEventHandler() {
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

            @Override
            public void onFire(WindowPayloadEvent event) {
                JSONObject objPayload = event.getPayload();

                if (objPayload != null) {
                    if (isWindowDisplayPayload(objPayload)) {
                        JSONObject objData = JsonUtil.getObject(objPayload, "data"); //$NON-NLS-1$

                        if (objData != null) {
                            String tag = JsonUtil.getString(objData, "tag"); //$NON-NLS-1$

                            WindowConfig config = factoryWindowConfig.build(objPayload);

                            showWindow(tag, config);
                        }
                    }
                }
            }
        });
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
        mgrWindow = new WindowManager(new WindowListener() {
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
            // before saving new state, clear old state
            clearState();
            Map<String, IPlantWindow> windows = mgrWindow.getWindows();
            for (IPlantWindow win : windows.values()) {
                System.out.println("win-->" + win.getTag());
                saveWindowState(win.getTag(), win.getWindowState());
            }
        }

        private void saveWindowState(String tag, JSONObject state) {
            if (state != null) {
                StateManager mgr = DEStateManager.getStateManager();
                mgr.set(tag, state.toString());
            }
        }

        private void clearState() {
            List<String> tags = WindowFactory.getAllWindowTags();
            StateManager mgr = DEStateManager.getStateManager();
            for (String tag : tags) {
                mgr.set(tag, null);
            }
        }
    }

    private final class DEAfterRenderListener implements Listener<ComponentEvent> {

        @Override
        public void handleEvent(ComponentEvent be) {
            List<String> tags = WindowFactory.getAllWindowTags();
            StateManager mgr = DEStateManager.getStateManager();
            for (String tag : tags) {
                String state = mgr.getString(tag);
                if (state != null) {
                    // Dispatch window display action
                    WindowDispatcher dispatcher = new WindowDispatcher();
                    dispatcher.dispatchAction(tag);
                }
            }
        }
    }

    private void initEditorController() {
        controllerEditor = new EditorController(mgrWindow);
    }
}
