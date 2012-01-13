package org.iplantc.de.client.views.windows;

import org.iplantc.de.client.I18N;
import org.iplantc.de.client.images.Resources;
import org.iplantc.de.client.models.WindowConfig;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Header;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Provides a base class for windows in the application desktop.
 */
public abstract class IPlantWindow extends Window {
    private final String BUTTON_STYLE_MAXIMIZE = "x-tool-maximizewindow"; //$NON-NLS-1$
    private final String BUTTON_STYLE_RESTORE = "x-tool-restorewindow"; //$NON-NLS-1$
    private final String WINDOW_STYLE_MAXIMIZED = "x-window-maximized"; //$NON-NLS-1$
    private final String WINDOW_STYLE_DRAGGABLE = "x-window-draggable"; //$NON-NLS-1$
    protected String tag;
    protected Status status;
    private Point restorePos;
    private Size restoreSize;
    private boolean maximized;
    private ToolButton btnMinimize;
    private ToolButton btnMaximize;
    private ToolButton btnRestore;
    private ToolButton btnClose;

    /**
     * Constructs an instance of the window.
     * 
     * @param tag a unique identifier for the window.
     */
    protected IPlantWindow(String tag) {
        this(tag, false, true, false, true);
    }

    /**
     * Constructs an instance of the window.
     * 
     * The parameters passed (isMinimizable, isMaximizable, isClosable) control the appearance of the
     * titlebar and potential functionality.
     * 
     * @param tag a unique identifier for the window.
     * @param haveStatus true indicates the window has a status area.
     * @param isMinimizable true indicates that a window is minimizable.
     * @param isMaximizable true indicates that a window is maximizable.
     * @param isClosable true indicates that a window can be closed.
     */
    protected IPlantWindow(String tag, boolean haveStatus, boolean isMinimizable, boolean isMaximizable,
            boolean isClosable) {
        this.tag = tag;

        if (haveStatus) {
            initStatus();
        }

        Header header = getHeader();

        if (isMinimizable) {
            buildMinimizeButton();
            header.addTool(btnMinimize);
        }

        if (isMaximizable) {
            buildMaximizeButton();
            header.addTool(btnMaximize);
            addDoubleClickMaximize();
        }

        if (isClosable) {
            buildCloseButton();
            header.addTool(btnClose);
        }

        setMaximizable(false);
        setMinimizable(false);
        setClosable(false);
        setFrame(false);

        header.addStyleName("windowLayoutTitle"); //$NON-NLS-1$
        header.setIcon(AbstractImagePrototype.create(Resources.ICONS.whitelogoSmall()));
        setBodyStyleName("accordianbody"); //$NON-NLS-1$
        setStyleAttribute("outline", "none"); //$NON-NLS-1$ //$NON-NLS-2$
     }

     /**
     * Initiate the status components.
     */
    protected void initStatus() {
        status = new Status();
        getHeader().addTool(status);
        status.hide();
    }
    
    /**
     * Show the status widgets.
     */
    protected void showStatus() {
        status.show();
        status.setBusy(""); //$NON-NLS-1$
    }



    /**
     * Retrieves the tag for the window.
     * 
     * @return a string representing a window handle, or a unique identifier.
     */
    public String getTag() {
        return tag;
    }

    private void buildCloseButton() {
        btnClose = new ToolButton("x-tool-closewindow"); //$NON-NLS-1$
        btnClose.setId("idclose-" + tag); //$NON-NLS-1$
        btnClose.sinkEvents(Events.OnMouseOut.getEventCode());
        btnClose.setToolTip(I18N.DISPLAY.close());

        btnClose.addSelectionListener(new SelectionListener<IconButtonEvent>() {
            @Override
            public void componentSelected(IconButtonEvent ce) {
                doHide();
            }
        });
        
        btnClose.addListener(Events.OnMouseOut, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                btnClose.removeStyleName("x-tool-closewindow-hover");
            }
        });
        
        btnClose.addListener(Events.OnMouseOver, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                btnClose.addStyleName("x-tool-closewindow-hover");
            }
        });
    }

    private void buildMaximizeButton() {
        btnMaximize = new ToolButton(BUTTON_STYLE_MAXIMIZE);
        btnMaximize.setId("idmaximize-" + tag); //$NON-NLS-1$
        btnMaximize.sinkEvents(Events.OnMouseOut.getEventCode());
        btnMaximize.setToolTip(I18N.DISPLAY.maximize());

        btnMaximize.addSelectionListener(new SelectionListener<IconButtonEvent>() {
            @Override
            public void componentSelected(IconButtonEvent ce) {
                if (maximized) {
                    restoreWindow();
                } else {
                    maximizeWindow();
                }
            }
        });
        btnMaximize.addListener(Events.OnMouseOver, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                btnMaximize.addStyleName("x-tool-maximizewindow-hover");
            }
        });
        
        btnMaximize.addListener(Events.OnMouseOut, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                btnMaximize.removeStyleName("x-tool-maximizewindow-hover");
            }
        });
    }

    private void buildMinimizeButton() {
        btnMinimize = new ToolButton("x-tool-minimizewindow"); //$NON-NLS-1$
        btnMinimize.setId("idminimize-" + tag); //$NON-NLS-1$
        btnMinimize.sinkEvents(Events.OnMouseOut.getEventCode());
        btnMinimize.setToolTip(I18N.DISPLAY.minimize());

        btnMinimize.addSelectionListener(new SelectionListener<IconButtonEvent>() {
            @Override
            public void componentSelected(IconButtonEvent ce) {
                minimize();
                btnMinimize.removeStyleName("x-tool-minimizewindow-hover");
            }
        });
        
        btnMinimize.addListener(Events.OnMouseOver, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                btnMinimize.addStyleName("x-tool-minimizewindow-hover");
            }
        });
        
        btnMinimize.addListener(Events.OnMouseOut, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                btnMinimize.removeStyleName("x-tool-minimizewindow-hover");
            }
        });
    }

    private ToolButton buildRestoreButton() {
        btnRestore = new ToolButton(BUTTON_STYLE_RESTORE);
        btnRestore.setId("idrestore-" + tag); //$NON-NLS-1$
        btnRestore.sinkEvents(Events.OnMouseOut.getEventCode());
        btnRestore.setToolTip(I18N.DISPLAY.restore());

        btnRestore.addSelectionListener(new SelectionListener<IconButtonEvent>() {
            @Override
            public void componentSelected(IconButtonEvent ce) {
                restoreWindow();
            }
        });
        
        btnRestore.addListener(Events.OnMouseOver, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                btnRestore.addStyleName("x-tool-restorewindow-hover");
            }
        });
        
        btnRestore.addListener(Events.OnMouseOut, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                btnRestore.removeStyleName("x-tool-restorewindow-hover");
            }
        });

        return btnRestore;
    }

    private void addDoubleClickMaximize() {
        getHeader().sinkEvents(Events.OnDoubleClick.getEventCode());
        getHeader().addListener(Events.OnDoubleClick, new Listener<ComponentEvent>() {
            public void handleEvent(ComponentEvent be) {
                if (!maximized) {
                    maximizeWindow();
                } else {
                    restoreWindow();
                }
            }
        });
    }

    private void restoreWindow() {
        el().removeStyleName(WINDOW_STYLE_MAXIMIZED);

        if (isDraggable()) {
            dragger.setEnabled(true);
        }

        replaceRestoreIcon();

        head.addStyleName(WINDOW_STYLE_DRAGGABLE);
        if (restorePos != null) {
            setPosition(restorePos.x, restorePos.y);
            setSize(restoreSize.width, restoreSize.height);
        }
        maximized = false;
        fireEvent(Events.Restore, new WindowEvent(this));
    }

    private void maximizeWindow() {
        if (!maximized) {
            restoreSize = getSize();
            restorePos = getPosition(true);
            maximized = true;
            addStyleName(WINDOW_STYLE_MAXIMIZED);
            head.removeStyleName(WINDOW_STYLE_DRAGGABLE);

            fitContainer();

            replaceMaximizeIcon();

            if (isDraggable()) {
                dragger.setEnabled(false);
            }

            fireEvent(Events.Maximize, new WindowEvent(this));

        } else {
            fitContainer();
        }
    }

    /**
     * Replaces the maximize icon with the restore icon.
     * 
     * The restore icon is only visible to the user when a window is in maximized state.
     */
    private void replaceMaximizeIcon() {
        int index = findMaximizeButtonIndex();
        if (index > -1) {
            getHeader().removeTool(getHeader().getTool(index));
            getHeader().insertTool(buildRestoreButton(), index);
        }
    }

    /**
     * Replaces the restore icon with the maximize icon.
     */
    private void replaceRestoreIcon() {
        int index = findRestoreButtonIndex();
        if (index > -1) {
            getHeader().removeTool(getHeader().getTool(index));
            // remove listener for maximize button
            removeButtonListeners(btnMaximize);
            // re-build maximize button
            buildMaximizeButton();
            // re-insert new maximize button
            getHeader().insertTool(btnMaximize, index);
        }
    }

    private int findToolButtonIndex(String btnToolName) {
        int toolCount = getHeader().getToolCount();
        int index = -1;

        for (int i = 0; i < toolCount; i++) {
            Component tool = getHeader().getTool(i);
            String fullStyle = tool.getStyleName();

            if (fullStyle.contains(btnToolName)) {
                index = i;
                break;
            }
        }

        return index;
    }

    private int findRestoreButtonIndex() {
        return findToolButtonIndex(BUTTON_STYLE_RESTORE);
    }

    private int findMaximizeButtonIndex() {
        return findToolButtonIndex(BUTTON_STYLE_MAXIMIZE);
    }

    /**
     * Forces window to hide.
     */
    protected void doHide() {
        hide();
    }

    private void removeButtonListeners(ToolButton btn) {
        if (btn != null) {
            btn.removeAllListeners();
        }
    }

    /**
     * Release resources allocated by this window.
     */
    public void cleanup() {
        removeButtonListeners(btnMinimize);
        removeButtonListeners(btnMaximize);
        removeButtonListeners(btnClose);
    }

    /**
     * Executes define operations for refreshing the window display.
     */
    public void refresh() {

    }

    /**
     * Applies a window configuration to the window. The default implementation does nothing.
     * 
     * @param config
     */
    public void configure(WindowConfig config) {
    }

}
