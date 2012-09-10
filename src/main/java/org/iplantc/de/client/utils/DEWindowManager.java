package org.iplantc.de.client.utils;

import java.util.HashMap;
import java.util.Map;

import org.iplantc.core.uicommons.client.models.WindowConfig;
import org.iplantc.de.client.factories.WindowFactory;
import org.iplantc.de.client.gxt3.desktop.widget.TaskButton;
import org.iplantc.de.client.gxt3.utils.IplantWindowManager;
import org.iplantc.de.client.views.windows.IPlantWindowInterface;

import com.extjs.gxt.ui.client.core.FastMap;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.widget.core.client.event.ActivateEvent.ActivateHandler;
import com.sencha.gxt.widget.core.client.event.DeactivateEvent.DeactivateHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.MinimizeEvent.MinimizeHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;

/**
 * Manages window widgets in the web "desktop" environment.
 */
public class DEWindowManager extends IplantWindowManager {
    private final WindowListener listener;
    private IPlantWindowInterface activeWindow;
    private final FastMap<IPlantWindowInterface> windows = new FastMap<IPlantWindowInterface>();
    private Point first_window_postion;
    private final ActivateHandler<com.sencha.gxt.widget.core.client.Window> activateHandler;
    private final DeactivateHandler<com.sencha.gxt.widget.core.client.Window> deactivateHandler;
    private final HideHandler hideHandler;
    private final MinimizeHandler minimizeHandler;
    private final ShowHandler showHandler;
    private final Map<String, TaskButton> taskButtons;

    /**
     * Instantiate from a window listener.
     * 
     * @param listener window listener.
     */
    public DEWindowManager(WindowListener listener,
            ActivateHandler<com.sencha.gxt.widget.core.client.Window> activateHandler,
            DeactivateHandler<com.sencha.gxt.widget.core.client.Window> deactivateHandler,
            HideHandler hideHandler, MinimizeHandler minimizeHandler, ShowHandler showHandler) {
        this.listener = listener;
        this.activateHandler = activateHandler;
        this.deactivateHandler = deactivateHandler;
        this.hideHandler = hideHandler;
        this.minimizeHandler = minimizeHandler;
        this.showHandler = showHandler;
        taskButtons = new HashMap<String, TaskButton>();
    }

    /**
     * Bring a window to the foreground.
     * 
     * @param window window to set as active.
     */
    public void setActiveWindow(IPlantWindowInterface window) {
        activeWindow = window;
        if (window != null) {
            bringToFront(window.asWidget());
        }
    }

    /**
     * Retrieve the active window.
     * 
     * @return the active window.
     */
    public IPlantWindowInterface getActiveWindow() {
        return activeWindow;
    }

    /**
     * Add a window to be managed.
     * 
     * @param tag tag of window to pass into WindowFactory for allocation.
     * @param config a WindowConfiguration to use for the new window
     * @return newly added window.
     */
    public IPlantWindowInterface add(String tag, WindowConfig config) {
        IPlantWindowInterface ret = WindowFactory.build(tag, config);
        add(ret);
        return ret;
    }

    /**
     * Add a window to be managed.
     * 
     * @param window window to be added.
     */
    public void add(IPlantWindowInterface window) {
        if (window != null) {
            window.setId(window.getTag());
            getDEWindows().put(window.getTag(), window);
            if (window instanceof com.extjs.gxt.ui.client.widget.Window) {
                window.addWindowListener(listener);
            } else if (window instanceof com.sencha.gxt.widget.core.client.Window) {
                window.addActivateHandler(activateHandler);
                window.addDeactivateHandler(deactivateHandler);
                window.addHideHandler(hideHandler);
                window.addMinimizeHandler(minimizeHandler);
                window.addShowHandler(showHandler);
            }
            register(window.asWidget());
        }
    }

    /**
     * Retrieve a window by tag.
     * 
     * @param tag unique tag for window to retrieve.
     * @return null on failure. Requested window on success.
     */
    public IPlantWindowInterface getWindow(String tag) {
        return getDEWindows().get(tag);
    }

    /**
     * Remove a managed window.
     * 
     * @param tag tag of the window to remove.
     */
    public void remove(String tag) {
        IPlantWindowInterface win = getDEWindows().remove(tag);
        unregister(win.asWidget());
        if (getDEWindows().size() == 0) {
            first_window_postion = null;
        }
    }

    /**
     * Check if a window already exists
     * 
     * @param tag
     * @return boolean
     */
    public boolean contains(String tag) {
        IPlantWindowInterface win = getDEWindows().remove(tag);
        return !(win == null);
    }

    /**
     * @param first_window_postion the first_window_postion to set
     */
    public void setFirst_window_postion(Point first_window_postion) {
        this.first_window_postion = first_window_postion;
    }

    /**
     * @return the first_window_postion
     */
    public Point getFirst_window_postion() {
        return first_window_postion;
    }

    /**
     * get the no.of open windows in the app
     * 
     * @return
     */
    public int getCount() {
        if (getDEWindows() != null) {
            return getDEWindows().size();
        } else {
            return 0;
        }
    }

    /**
     * 
     * Show the window
     * 
     * @param tag
     */
    public void show(String tag) {
        if (tag != null) {
            IPlantWindowInterface window = getDEWindows().get(tag);
            if (window != null) {
                if (getFirst_window_postion() != null) {
                    int new_x = getFirst_window_postion().getX() + ((getCount() - 1) * 10);
                    int new_y = getFirst_window_postion().getY() + ((getCount() - 1) * 20);
                    window.setPagePosition(new_x, new_y);
                }
                window.show();
                window.toFront();
                window.refresh();
                if (getCount() == 1) {
                    setFirst_window_postion(window.getPosition3(true));
                }
            }

        }
    }

    /**
     * Set the task button associated with the window
     * 
     * @param tag window tag
     * @param btn taskbutton
     */
    public void setTaskButton(String tag, TaskButton btn) {
        taskButtons.put(tag, btn);
    }

    /**
     * get the task button associated with a window tag
     * 
     * @param tag window tag
     * @return the task button
     */
    public TaskButton getTaskButton(String tag) {
        return taskButtons.get(tag);
    }

    /**
     * @return the windows
     */
    public FastMap<IPlantWindowInterface> getDEWindows() {
        return windows;
    }

    public JSONObject getActiveWindowStates() {
        JSONObject obj = new JSONObject();
        int index = 0;
        for (Widget win : getStack()) {
            JSONObject state = ((IPlantWindowInterface)win).getWindowState();
            String tag = ((IPlantWindowInterface)win).getTag();
            state.put("order", new JSONString(index++ + ""));
            state.put("tag", new JSONString(tag));
            obj.put(tag, state);
        }
        return obj;
    }
}
