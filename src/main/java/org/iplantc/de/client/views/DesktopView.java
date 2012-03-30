package org.iplantc.de.client.views;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.iplantc.de.client.models.WindowConfig;
import org.iplantc.de.client.utils.DEStateManager;
import org.iplantc.de.client.utils.DEWindowManager;
import org.iplantc.de.client.utils.ShortcutManager;
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
import com.extjs.gxt.ui.client.widget.Window;
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
    private DEWindowManager mgrWindow;
    private El shortcutEl;
    private LayoutContainer desktop;
    private IPlantTaskbar taskBar;
    private WindowConfigFactory factoryWindowConfig;

    private static final String ACTIVE_WINDOWS = "active_windows";

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

                            WindowConfig config = factoryWindowConfig.build(JsonUtil.getObject(objData,
                                    "config")); //$NON-NLS-1$

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
                            WindowDispatcher dispatcher = new WindowDispatcher(JsonUtil.getObject(state
                                    .toString()));
                            dispatcher.dispatchAction(JsonUtil.getString(state, "tag"));
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

    private void initEditorController() {
        controllerEditor = new EditorController(mgrWindow);
    }

}
