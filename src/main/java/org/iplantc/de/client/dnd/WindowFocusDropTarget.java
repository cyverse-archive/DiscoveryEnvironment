package org.iplantc.de.client.dnd;

import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.dnd.DropTarget;
import com.extjs.gxt.ui.client.dnd.StatusProxy;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.Timer;

/**
 * An abstract DropTarget class that listens to drag events over some Component, in order to focus (bring
 * to front) some window after a small delay, when the user drags then hovers an item over that target
 * Component.
 * 
 * @author psarando
 * 
 */
public abstract class WindowFocusDropTarget extends DropTarget {
    /**
     * How long the user must hover over the target before the window is focused (1 second).
     */
    private final static int WIN_FOCUS_DELAY_MILLIS = 1000;

    private Timer focusDelay;

    public WindowFocusDropTarget(Component target) {
        super(target);
    }

    /**
     * This method is called after a user drags and hovers over the target component, in order to bring
     * some window to the front.
     */
    protected abstract void windowToFront();

    @Override
    protected void onDragCancelled(DNDEvent event) {
        super.onDragCancelled(event);

        if (focusDelay != null) {
            focusDelay.cancel();
        }
    }

    @Override
    protected void onDragDrop(DNDEvent event) {
        if (focusDelay != null) {
            focusDelay.cancel();
        }
    }

    @Override
    protected void onDragEnter(DNDEvent event) {
        final StatusProxy eventStatus = event.getStatus();

        // This target component is not actually a valid drop zone, so update the StatusProxy.
        eventStatus.setStatus(false);

        focusDelay = new Timer() {
            @Override
            public void run() {
                windowToFront();

                // Keep the StatusProxy on top of the focused window.
                eventStatus.setZIndex(XDOM.getTopZIndex(10));
            }
        };

        focusDelay.schedule(WIN_FOCUS_DELAY_MILLIS);
    }

    @Override
    protected void onDragFail(DNDEvent event) {
        if (focusDelay != null) {
            focusDelay.cancel();
        }
    }

    @Override
    protected void onDragLeave(DNDEvent event) {
        if (focusDelay != null) {
            focusDelay.cancel();
        }
    }

    @Override
    protected void onDragMove(DNDEvent event) {
        if (focusDelay != null) {
            // Reset the delay when the user moves the mouse.
            focusDelay.schedule(WIN_FOCUS_DELAY_MILLIS);
        }
    }
}
