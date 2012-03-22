package org.iplantc.de.client.utils;

import org.iplantc.de.client.factories.WindowFactory;
import org.iplantc.de.client.models.WindowConfig;
import org.iplantc.de.client.views.windows.IPlantWindow;

import com.extjs.gxt.ui.client.core.FastMap;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.util.Point;

/**
 * Manages window widgets in the web "desktop" environment.
 */
public class WindowManager {
    private final WindowListener listener;
    private IPlantWindow activeWindow;
    private final FastMap<IPlantWindow> windows = new FastMap<IPlantWindow>();
    private Point first_window_postion;

    /**
     * Instantiate from a window listener.
     * 
     * @param listener window listener.
     */
    public WindowManager(WindowListener listener) {
        this.listener = listener;
    }

    /**
     * Bring a window to the foreground.
     * 
     * @param window window to set as active.
     */
    public void setActiveWindow(IPlantWindow window) {
        activeWindow = window;
    }

    /**
     * Retrieve the active window.
     * 
     * @return the active window.
     */
    public IPlantWindow getActiveWindow() {
        return activeWindow;
    }

    /**
     * Add a window to be managed.
     * 
     * @param tag tag of window to pass into WindowFactory for allocation.
     * @param config a WindowConfiguration to use for the new window
     * @return newly added window.
     */
    public IPlantWindow add(String tag, WindowConfig config) {
        IPlantWindow ret = WindowFactory.build(tag, config);

        add(ret);

        return ret;
    }

    /**
     * Add a window to be managed.
     * 
     * @param window window to be added.
     */
    public void add(IPlantWindow window) {
        if (window != null) {
            getWindows().put(window.getTag(), window);
            window.addWindowListener(listener);
        }
    }

    /**
     * Retrieve a window by tag.
     * 
     * @param tag unique tag for window to retrieve.
     * @return null on failure. Requested window on success.
     */
    public IPlantWindow getWindow(String tag) {
        return getWindows().get(tag);
    }

    /**
     * Remove a managed window.
     * 
     * @param tag tag of the window to remove.
     */
    public void remove(String tag) {
        getWindows().remove(tag);
        if (getWindows().size() == 0) {
            first_window_postion = null;
        }
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
        if (getWindows() != null) {
            return getWindows().size();
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
            IPlantWindow window = getWindows().get(tag);
            if (window != null) {
                if (getFirst_window_postion() != null) {
                    int new_x = getFirst_window_postion().x + ((getCount() - 1) * 10);
                    int new_y = getFirst_window_postion().y + ((getCount() - 1) * 20);
                    window.setPagePosition(new_x, new_y);
                }
                window.show();
                window.toFront();
                window.refresh();
                if (getCount() == 1) {
                    setFirst_window_postion(window.getPosition(true));
                }
            }

        }
    }

    /**
     * @return the windows
     */
    public FastMap<IPlantWindow> getWindows() {
        return windows;
    }
}
