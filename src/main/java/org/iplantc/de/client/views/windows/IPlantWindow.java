package org.iplantc.de.client.views.windows;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.dnd.WindowFocusDropTarget;
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
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.sencha.gxt.widget.core.client.event.ActivateEvent.ActivateHandler;
import com.sencha.gxt.widget.core.client.event.DeactivateEvent.DeactivateHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.MinimizeEvent.MinimizeHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;

/**
 * Provides a base class for windows in the application desktop.
 */
public abstract class IPlantWindow extends Window implements IPlantWindowInterface {
    private final String BUTTON_STYLE_MAXIMIZE = "x-tool-maximizewindow"; //$NON-NLS-1$
    private final String BUTTON_STYLE_RESTORE = "x-tool-restorewindow"; //$NON-NLS-1$
    private final String WINDOW_STYLE_MAXIMIZED = "x-window-maximized"; //$NON-NLS-1$
    private final String WINDOW_STYLE_DRAGGABLE = "x-window-draggable"; //$NON-NLS-1$

    protected String tag;
    protected WindowConfig config;
    protected Status status;
    protected IPlantWindowDropTarget dropTarget;

    private Point restorePos;
    private Size restoreSize;
    protected boolean maximized;
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
        setBodyStyleName("windowBody"); //$NON-NLS-1$
        setShadow(false);
        setStyleAttribute("outline", "none"); //$NON-NLS-1$ //$NON-NLS-2$

        dropTarget = new IPlantWindowDropTarget(this);
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
            boolean isClosable, WindowConfig config) {
        this(tag, haveStatus, isMinimizable, isMaximizable, isClosable);
        this.config = config;
    }

    /**
     * Returns the window state information.
     * 
     * @return
     */
    public abstract JSONObject getWindowState();

    /**
     * Returns the window's view state and config data.
     * 
     * @return
     */
    protected JSONObject getWindowViewState() {
        JSONObject obj = new JSONObject();
        storeWindowViewState(obj);
        return obj;
    }

    protected void storeWindowViewState(JSONObject obj) {
        if (obj == null) {
            return;
        }

        obj.put(WindowConfig.IS_MAXIMIZED, JSONBoolean.getInstance(maximized));
        obj.put(WindowConfig.IS_MINIMIZED, JSONBoolean.getInstance(!isVisible()));
        obj.put(WindowConfig.WIN_LEFT, new JSONNumber(getAbsoluteLeft()));
        obj.put(WindowConfig.WIN_TOP, new JSONNumber(getAbsoluteTop()));
        obj.put(WindowConfig.WIN_WIDTH, new JSONNumber(getWidth()));
        obj.put(WindowConfig.WIN_HEIGHT, new JSONNumber(getHeight()));
    }

    /**
     * Sets windows view state
     * 
     */
    protected void setWindowViewState() {
        if (config == null) {
            return;
        }

        if (config.isWindowMinimized()) {
            minimize();
        } else if (config.isWindowMaximized()) {
            maximizeWindow();
        } else {
            setWindowPosition();
            setWinSize();
        }

    }

    private void setWinSize() {
        Number width = JsonUtil.getNumber(config, WindowConfig.WIN_WIDTH);
        Number height = JsonUtil.getNumber(config, WindowConfig.WIN_HEIGHT);

        if (width != null && height != null && width.intValue() > 0 && height.intValue() > 0) {
            setSize(width.intValue(), height.intValue());
        }
    }

    private void setWindowPosition() {
        Number left = JsonUtil.getNumber(config, WindowConfig.WIN_LEFT);
        Number top = JsonUtil.getNumber(config, WindowConfig.WIN_TOP);

        if (left != null && top != null && left.intValue() > 0 && top.intValue() > 0) {
            setPosition(left.intValue(), top.intValue());
        }
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
    @Override
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
                btnClose.removeStyleName("x-tool-closewindow-hover"); //$NON-NLS-1$
            }
        });

        btnClose.addListener(Events.OnMouseOver, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                btnClose.addStyleName("x-tool-closewindow-hover"); //$NON-NLS-1$
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
                btnMaximize.addStyleName("x-tool-maximizewindow-hover"); //$NON-NLS-1$
            }
        });

        btnMaximize.addListener(Events.OnMouseOut, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                btnMaximize.removeStyleName("x-tool-maximizewindow-hover"); //$NON-NLS-1$
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
                btnMinimize.removeStyleName("x-tool-minimizewindow-hover"); //$NON-NLS-1$
            }
        });

        btnMinimize.addListener(Events.OnMouseOver, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                btnMinimize.addStyleName("x-tool-minimizewindow-hover"); //$NON-NLS-1$
            }
        });

        btnMinimize.addListener(Events.OnMouseOut, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                btnMinimize.removeStyleName("x-tool-minimizewindow-hover"); //$NON-NLS-1$
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
                btnRestore.addStyleName("x-tool-restorewindow-hover"); //$NON-NLS-1$
            }
        });

        btnRestore.addListener(Events.OnMouseOut, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                btnRestore.removeStyleName("x-tool-restorewindow-hover"); //$NON-NLS-1$
            }
        });

        return btnRestore;
    }

    private void addDoubleClickMaximize() {
        getHeader().sinkEvents(Events.OnDoubleClick.getEventCode());
        getHeader().addListener(Events.OnDoubleClick, new Listener<ComponentEvent>() {
            @Override
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

    protected void maximizeWindow() {
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
    @Override
    public void refresh() {

    }

    /**
     * Applies a window configuration to the window. The default implementation does nothing.
     * 
     * @param config
     */
    @Override
    public void setWindowConfig(WindowConfig config) {
        // do nothing intentionally
    }

    /**
     * A WindowFocusDropTarget class that listens to drag events over an IPlantWindow in order to focus
     * that same window after a small delay.
     * 
     * @author psarando
     * 
     */
    protected class IPlantWindowDropTarget extends WindowFocusDropTarget {
        private final IPlantWindow window;

        public IPlantWindowDropTarget(IPlantWindow target) {
            super(target);

            window = target;
        }

        @Override
        protected void windowToFront() {
            window.toFront();
        }
    }

    @Override
    public HandlerRegistration addActivateHandler(
            ActivateHandler<com.sencha.gxt.widget.core.client.Window> handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HandlerRegistration addDeactivateHandler(
            DeactivateHandler<com.sencha.gxt.widget.core.client.Window> handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HandlerRegistration addMinimizeHandler(MinimizeHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HandlerRegistration addHideHandler(HideHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HandlerRegistration addShowHandler(ShowHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

}
