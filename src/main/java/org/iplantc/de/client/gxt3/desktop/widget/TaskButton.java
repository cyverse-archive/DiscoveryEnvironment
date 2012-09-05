/**
 * Sencha GXT 3.0.1 - Sencha for GWT Copyright(c) 2007-2012, Sencha, Inc. licensing@sencha.com
 * 
 * http://www.sencha.com/products/gxt/license/
 */
package org.iplantc.de.client.gxt3.desktop.widget;

import org.iplantc.de.client.images.Resources;
import org.iplantc.de.client.views.windows.IPlantWindowInterface;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.widget.core.client.WindowManager;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Provides a task button that can be added to a task bar and indicates the current state of a desktop
 * application. Clicking on a task button activates the window associated with a desktop application. The
 * active desktop application is indicated by the "pressed" visual state of the task button.
 * 
 * @see TaskBar
 */
public class TaskButton extends ToggleButton {

    private IPlantWindowInterface win;

    /**
     * Creates a task button for the specified window.
     * 
     * @param win a window containing a desktop application
     */
    public TaskButton(IPlantWindowInterface win) {
        super(new TaskButtonCell());
        ImageResource icon = Resources.ICONS.whitelogoSmall();
        String text = win.getTitle();
        if (text != null) {
            setText(Format.ellipse(text, 26));
        }
        setIcon(icon);
        setHeight(28);
        this.win = win;
        addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                doSelect(event);
            }
        });
    }

    protected void doSelect(SelectEvent event) {
        if (!win.isVisible()) {
            win.show();
        } else if (win == WindowManager.get().getActive()) {
            win.minimize();
        } else {
            win.toFront();
        }
    }
}
