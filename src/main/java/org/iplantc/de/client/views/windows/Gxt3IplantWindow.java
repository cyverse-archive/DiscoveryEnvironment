package org.iplantc.de.client.views.windows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplantc.core.uicommons.client.I18N;
import org.iplantc.core.uicommons.client.images.Resources;
import org.iplantc.core.uicommons.client.models.WindowConfig;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.theme.gray.client.window.GrayWindowAppearance;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.MaximizeEvent;
import com.sencha.gxt.widget.core.client.event.MaximizeEvent.MaximizeHandler;
import com.sencha.gxt.widget.core.client.event.RestoreEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * This class is intended to be a GXT 3 replacement of the {@link DECatalogWindow}.
 * 
 * @author jstroot
 * 
 */
public abstract class Gxt3IplantWindow extends Window implements IPlantWindowInterface {
    private static final String BUTTON_STYLE_RESTORE_HOVER = "x-tool-restorewindow-hover";
    private static final String HEADER_STYLE = "windowLayoutTitle3"; //$NON-NLS-1$
    private static final String BUTTON_STYLE_CLOSE = "x-tool-closewindow"; //$NON-NLS-1$
    private static final String BUTTON_STYLE_CLOSE_HOVER = "x-tool-closewindow-hover"; //$NON-NLS-1$
    private static final String BUTTON_STYLE_MAXIMIZE = "x-tool-maximizewindow"; //$NON-NLS-1$
    private static final String BUTTON_STYLE_MAXIMIZED_HOVER = "x-tool-maximizewindow-hover"; //$NON-NLS-1$
    private static final String BUTTON_STYLE_MINIMIZED = "x-tool-minimizewindow"; //$NON-NLS-1$
    private static final String BUTTON_STYLE_MINIMIZED_HOVER = "x-tool-minimizewindow-hover"; //$NON-NLS-1$
    private static final String BUTTON_STYLE_RESTORE = "x-tool-restorewindow"; //$NON-NLS-1$
    private static final String WINDOW_STYLE_BODY = "windowBody3"; //$NON-NLS-1$

    protected String tag;
    protected WindowConfig config;
    protected Status status;

    protected boolean maximized;
    protected boolean minimized;
    // FIXME Add drag and drop functionality

    private ToolButton btnMinimize;
    private ToolButton btnMaximize;
    private ToolButton btnRestore;
    private ToolButton btnClose;

    /**
     * Used to store the <code>HandlerRegistration</code>s of widgets when needed.
     */
    private final Map<Widget, List<HandlerRegistration>> handlerRegMap = new HashMap<Widget, List<HandlerRegistration>>();

    /**
     * Constructs an instance of the window.
     * 
     * @param tag a unique identifier for the window.
     */
    protected Gxt3IplantWindow(String tag) {
        this(tag, false, true, false, true);
    }

    public Gxt3IplantWindow(WindowAppearance appearance) {
        super(appearance);
        // TODO Auto-generated constructor stub
    }

    protected Gxt3IplantWindow(final String tag, final WindowConfig config) {
        this(tag, false, true, true, true);
    }

    protected Gxt3IplantWindow(String tag, boolean haveStatus, boolean isMinimizable,
            boolean isMaximizable, boolean isClosable, WindowConfig config) {
        this(tag, haveStatus, isMinimizable, isMaximizable, isClosable);
        this.config = config;
    }

    public Gxt3IplantWindow(String tag, boolean haveStatus, boolean isMinimizable,
            boolean isMaximizable, boolean isClosable) {
        super(new GrayWindowAppearance());
        this.tag = tag;

        if (haveStatus) {
            status = new Status();
            getHeader().addTool(status);
            status.hide();
        }

        if (isMinimizable) {
            btnMinimize = createMinimizeButton();
            getHeader().addTool(btnMinimize);
        }
        if (isMaximizable) {
            btnMaximize = createMaximizeButton();
            getHeader().addTool(btnMaximize);
            getHeader().sinkEvents(Event.ONDBLCLICK);
            getHeader().addHandler(createHeaderDblClickHandler(), DoubleClickEvent.getType());
        }
        if (isClosable) {
            btnClose = createCloseButton();
            getHeader().addTool(btnClose);
        }

        // Turn off default window buttons.
        setMaximizable(false);
        setMinimizable(false);
        setClosable(false);

        getHeader().addStyleName(HEADER_STYLE);
        getHeader().setIcon(Resources.ICONS.whitelogoSmall());

        setStyleName(WINDOW_STYLE_BODY);
        setShadow(false);
        setBodyBorder(false);
        setBorders(false);

        addHandler(new RestoreEvent.RestoreHandler() {
            @Override
            public void onRestore(RestoreEvent event) {
                replaceRestoreIcon();
            }
        }, RestoreEvent.getType());

        addMaximizeHandler(new MaximizeHandler() {
            @Override
            public void onMaximize(MaximizeEvent event) {
                replaceMaximizeIcon();
            }
        });
    }

    protected void doHide() {
        hide();
    }

    private ToolButton createMaximizeButton() {
        final ToolButton newMaxBtn = new ToolButton(BUTTON_STYLE_MAXIMIZE);
        newMaxBtn.setId("idmaximize-" + tag); //$NON-NLS-1$
        newMaxBtn.sinkEvents(Event.ONMOUSEOUT);
        newMaxBtn.setToolTip(I18N.DISPLAY.maximize());

        ArrayList<HandlerRegistration> hrList = new ArrayList<HandlerRegistration>();
        HandlerRegistration reg;
        reg = newMaxBtn.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                if (!isMaximized()) {
                    maximize();
                    maximized = true;
                } else {
                    restore();
                    maximized = false;
                }
            }
        });
        hrList.add(reg);

        reg = newMaxBtn.addHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                newMaxBtn.addStyleName(BUTTON_STYLE_MAXIMIZED_HOVER);
            }
        }, MouseOverEvent.getType());
        hrList.add(reg);

        reg = newMaxBtn.addHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                newMaxBtn.removeStyleName(BUTTON_STYLE_MAXIMIZED_HOVER);
            }
        }, MouseOutEvent.getType());
        hrList.add(reg);

        handlerRegMap.put(newMaxBtn, hrList);
        return newMaxBtn;
    }

    private ToolButton createRestoreButton() {
        final ToolButton btnRestore = new ToolButton(BUTTON_STYLE_RESTORE);
        btnRestore.setId("idrestore-" + tag); //$NON-NLS-1$
        btnRestore.sinkEvents(Events.OnMouseOut.getEventCode());
        btnRestore.setToolTip(I18N.DISPLAY.restore());

        ArrayList<HandlerRegistration> hrList = new ArrayList<HandlerRegistration>();
        HandlerRegistration reg;
        reg = btnRestore.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                restore();
            }
        });
        hrList.add(reg);

        reg = btnRestore.addHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                btnRestore.addStyleName(BUTTON_STYLE_RESTORE_HOVER);
            }
        }, MouseOverEvent.getType());
        hrList.add(reg);

        reg = btnRestore.addHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                btnRestore.removeStyleName(BUTTON_STYLE_RESTORE_HOVER);
            }
        }, MouseOutEvent.getType());
        hrList.add(reg);

        handlerRegMap.put(btnRestore, hrList);
        return btnRestore;
    }

    private ToolButton createMinimizeButton() {
        final ToolButton newMinBtn = new ToolButton(BUTTON_STYLE_MINIMIZED);
        newMinBtn.setId("idminimize-" + tag); //$NON-NLS-1$
        newMinBtn.sinkEvents(Events.OnMouseOut.getEventCode());
        newMinBtn.setToolTip(I18N.DISPLAY.minimize());

        newMinBtn.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                minimize();
                minimized = true;
                newMinBtn.removeStyleName(BUTTON_STYLE_MINIMIZED_HOVER);
            }
        });

        newMinBtn.addHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                newMinBtn.addStyleName(BUTTON_STYLE_MINIMIZED_HOVER);
            }
        }, MouseOverEvent.getType());

        newMinBtn.addHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                newMinBtn.removeStyleName(BUTTON_STYLE_MINIMIZED_HOVER);
            }
        }, MouseOutEvent.getType());

        return newMinBtn;
    }

    private ToolButton createCloseButton() {
        final ToolButton newCloseBtn = new ToolButton(BUTTON_STYLE_CLOSE);
        newCloseBtn.setId("idclose-" + tag); //$NON-NLS-1$
        newCloseBtn.sinkEvents(Events.OnMouseOut.getEventCode());
        newCloseBtn.setToolTip(I18N.DISPLAY.close());

        newCloseBtn.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                doHide();
            }
        });

        newCloseBtn.addHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                newCloseBtn.removeStyleName(BUTTON_STYLE_CLOSE_HOVER);
            }
        }, MouseOutEvent.getType());

        newCloseBtn.addHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                newCloseBtn.addStyleName(BUTTON_STYLE_CLOSE_HOVER);
            }
        }, MouseOverEvent.getType());

        return newCloseBtn;
    }

    private DoubleClickHandler createHeaderDblClickHandler() {
        DoubleClickHandler handler = new DoubleClickHandler() {

            @Override
            public void onDoubleClick(DoubleClickEvent event) {
                if (!isMaximized()) {
                    maximize();
                } else {
                    restore();
                }
            }
        };

        return handler;
    }

    /**
     * Replaces the maximize icon with the restore icon.
     * 
     * The restore icon is only visible to the user when a window is in maximized state.
     */
    private void replaceMaximizeIcon() {
        int index = findToolButtonIndex(BUTTON_STYLE_MAXIMIZE);
        if (index > -1) {
            getHeader().removeTool(btnMaximize);
            btnMaximize.removeFromParent();
            removeButtonListeners(btnMaximize);
            btnRestore = createRestoreButton();

            // re-insert restore button at same index of maximize button
            getHeader().insertTool(btnRestore, index);
        }
    }

    /**
     * Replaces the restore icon with the maximize icon.
     */
    private void replaceRestoreIcon() {
        int index = findToolButtonIndex(BUTTON_STYLE_RESTORE);
        if (index > -1) {
            getHeader().removeTool(btnRestore);
            removeButtonListeners(btnRestore);
            btnMaximize = createMaximizeButton();

            // re-insert maximize button at same index as restore button
            getHeader().insertTool(btnMaximize, index);
        }
    }

    private void removeButtonListeners(Widget btnMaximize2) {
        if (handlerRegMap.containsKey(btnMaximize2)) {
            for (HandlerRegistration reg : handlerRegMap.get(btnMaximize2)) {
                reg.removeHandler();
            }
            handlerRegMap.remove(btnMaximize2);
        }

    }

    private int findToolButtonIndex(String btnToolName) {
        int toolCount = getHeader().getToolCount();
        int index = -1;

        for (int i = 0; i < toolCount; i++) {
            Widget tool = getHeader().getTool(i);
            String fullStyle = tool.getStyleName();

            if (fullStyle.contains(btnToolName)) {
                index = i;
                break;
            }
        }

        return index;
    }

    protected void storeWindowViewState(JSONObject obj) {
        if (obj == null) {
            return;
        }

        obj.put(WindowConfig.IS_MAXIMIZED, JSONBoolean.getInstance(isMaximized()));
        obj.put(WindowConfig.IS_MINIMIZED, JSONBoolean.getInstance(!isVisible()));
        obj.put(WindowConfig.WIN_LEFT, new JSONNumber(getAbsoluteLeft()));
        obj.put(WindowConfig.WIN_TOP, new JSONNumber(getAbsoluteTop()));
        obj.put(WindowConfig.WIN_WIDTH, new JSONNumber(getElement().getWidth(true)));
        obj.put(WindowConfig.WIN_HEIGHT, new JSONNumber(getElement().getHeight(true)));
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public void addWindowListener(WindowListener listener) {
    }

    @Override
    public void refresh() {
    }

    @Override
    public Point getPosition3(boolean b) {
        return getElement().getPosition(b);
    }

    @Override
    public void setWindowConfig(WindowConfig config) {
        this.config = config;
    }

    @Override
    public boolean isVisible() {
        return super.isVisible();
    }

    @Override
    public boolean isMaximized() {
        return maximized;
    }

    @Override
    public void minimize() {
        super.minimize();
    }

    @Override
    public boolean isMinimized() {
        return minimized;
    }

    @Override
    public void setMinimized(boolean min) {
        minimized = min;
    }

    @Override
    public void setTitle(String wintitle) {
        setHeadingText(wintitle);
    }

    @Override
    public String getTitle() {
        return getHeader().getText();
    }

    @Override
    public void setPixelSize(int width, int height) {
        super.setPixelSize(width, height);
    }

    @Override
    public int getHeaderOffSetHeight() {
        return getHeader().getOffsetHeight();
    }

    @Override
    public void alignTo(Element e, AnchorAlignment align, int[] offsets) {
        super.alignTo(e, align, offsets);
    }

}
