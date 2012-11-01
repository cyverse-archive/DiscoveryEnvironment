/**
 * Sencha GXT 3.0.1 - Sencha for GWT Copyright(c) 2007-2012, Sencha, Inc. licensing@sencha.com
 * 
 * http://www.sencha.com/products/gxt/license/
 */
package org.iplantc.de.client.desktop.widget;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.events.UserEvent;
import org.iplantc.core.uicommons.client.events.UserEventHandler;
import org.iplantc.core.uicommons.client.models.WindowConfig;
import org.iplantc.core.uidiskresource.client.events.RequestBulkDownloadEvent;
import org.iplantc.core.uidiskresource.client.events.RequestBulkUploadEvent;
import org.iplantc.core.uidiskresource.client.events.RequestImportFromUrlEvent;
import org.iplantc.core.uidiskresource.client.events.RequestSimpleDownloadEvent;
import org.iplantc.core.uidiskresource.client.events.RequestSimpleUploadEvent;
import org.iplantc.core.uidiskresource.client.events.ShowFilePreviewEvent;
import org.iplantc.core.uidiskresource.client.events.ShowFilePreviewEvent.ShowFilePreviewEventHandler;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.desktop.layout.CascadeDesktopLayout;
import org.iplantc.de.client.desktop.layout.CenterDesktopLayout;
import org.iplantc.de.client.desktop.layout.DesktopLayout;
import org.iplantc.de.client.desktop.layout.DesktopLayout.RequestType;
import org.iplantc.de.client.desktop.layout.DesktopLayoutType;
import org.iplantc.de.client.desktop.layout.TileDesktopLayout;
import org.iplantc.de.client.dispatchers.ViewerWindowDispatcher;
import org.iplantc.de.client.events.WindowPayloadEvent;
import org.iplantc.de.client.events.WindowPayloadEventHandler;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.utils.DEWindowManager;
import org.iplantc.de.client.utils.ShortcutManager;
import org.iplantc.de.client.utils.builders.DefaultDesktopBuilder;
import org.iplantc.de.client.views.windows.IPlantWindowInterface;

import com.extjs.gxt.desktop.client.StartMenu;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.container.Viewport;
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
 * A desktop represents a desktop like application which contains a task bar, start menu, and shortcuts.
 * <p/>
 * Rather than adding content directly to the root panel, content should be wrapped in windows. Windows
 * can be opened via shortcuts and the start menu.
 * 
 * @see TaskBar
 * @see StartMenu
 * @see Shortcut
 */
public class Desktop implements IsWidget {

    private class WindowHandler implements ActivateHandler<Window>, DeactivateHandler<Window>,
            MinimizeHandler, HideHandler, ShowHandler {

        @Override
        public void onActivate(ActivateEvent<Window> event) {
            markActive((IPlantWindowInterface)event.getSource());
        }

        @Override
        public void onDeactivate(DeactivateEvent<Window> event) {
            markInactive((IPlantWindowInterface)event.getSource());
        }

        @Override
        public void onHide(HideEvent event) {
            hideWindow((IPlantWindowInterface)event.getSource());
        }

        @Override
        public void onMinimize(MinimizeEvent event) {
            minimizeWindow((IPlantWindowInterface)event.getSource());
        }

        @Override
        public void onShow(ShowEvent event) {
            showWindow((IPlantWindowInterface)event.getSource());
        }

    }

    /**
     * The default desktop layout type.
     */
    public static final DesktopLayoutType DEFAULT_DESKTOP_LAYOUT_TYPE = DesktopLayoutType.CENTER;

    private VBoxLayoutContainer desktop;
    private TaskBar taskBar;
    private List<Shortcut> shortcuts;
    private WindowHandler handler;
    private IPlantWindowInterface activeWindow;
    private VerticalLayoutContainer desktopContainer;
    private Viewport desktopViewport;
    private DesktopLayout desktopLayout;
    private FastMap<DesktopLayout> desktopLayouts;
    private DEWindowManager windowManager;
    private final WindowConfigFactory factoryWindowConfig;

    /**
     * Creates a new Desktop window.
     */
    public Desktop() {
        factoryWindowConfig = new WindowConfigFactory();
        initShortcuts();
        initEventHandlers();
    }

    /**
     * Adds a shortcut to the desktop.
     * 
     * @param shortcut the shortcut to add
     */
    public void addShortcut(Shortcut shortcut) {
        getShortcuts().add(shortcut);
        getDesktop().add(shortcut, new BoxLayoutData(new Margins(5)));
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

        eventbus.addHandler(ShowFilePreviewEvent.TYPE, new ShowFilePreviewEventHandler() {

            @Override
            public void showFilePreview(ShowFilePreviewEvent event) {
                ViewerWindowDispatcher dispatcher = new ViewerWindowDispatcher();
                dispatcher.launchViewerWindow(Lists.newArrayList(event.getFile().getId()), false);
            }
        });
        eventbus.addHandler(RequestBulkDownloadEvent.TYPE, new DesktopFileTransferEventHandler());
        eventbus.addHandler(RequestBulkUploadEvent.TYPE, new DesktopFileTransferEventHandler());
        eventbus.addHandler(RequestImportFromUrlEvent.TYPE, new DesktopFileTransferEventHandler());
        eventbus.addHandler(RequestSimpleDownloadEvent.TYPE, new DesktopFileTransferEventHandler());
        eventbus.addHandler(RequestSimpleUploadEvent.TYPE, new DesktopFileTransferEventHandler());

    }

    private void showWindow(final String type, final WindowConfig config) {
        String tag = type;
        // if we have params, the unique window identifier will be type + params

        if (config != null) {
            tag = tag + config.getTagSuffix();
        }

        IPlantWindowInterface window = getWindowManager().getWindow(tag);

        // do we already have this window?
        if (window == null) {
            window = getWindowManager().add(type, config);
        }

        // show the window and bring it to the front
        if (window != null) {
            window.setWindowConfig(config);
            getWindowManager().show(window.getTag());
        }
    }

    private void dispatchUserEvent(String action, String tag, WindowConfig params) {
        if (action.equals(Constants.CLIENT.windowTag())) {
            showWindow(tag, params);
        }
    }

    @Override
    public Widget asWidget() {
        return getDesktopViewport();
    }

    /**
     * Returns the container of the "desktop", which is the area that contains the shortcuts (i.e. minus
     * the task bar).
     * 
     * @return the desktop layout container
     */
    public VBoxLayoutContainer getDesktop() {
        if (desktop == null) {
            desktop = new VBoxLayoutContainer();
            desktop.addStyleName("x-desktop");
            desktop.setPadding(new Padding(5));
            desktop.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCHMAX);
        }
        return desktop;
    }

    /**
     * Returns a list of the desktop's shortcuts.
     * 
     * @return the shortcuts
     */
    public List<Shortcut> getShortcuts() {
        if (shortcuts == null) {
            shortcuts = new ArrayList<Shortcut>();
        }
        return shortcuts;

    }

    private HorizontalLayoutContainer buildFooterPanel() {
        HorizontalLayoutContainer pnlFooter = new HorizontalLayoutContainer();
        pnlFooter.setWidth("100%"); //$NON-NLS-1$

        CopyRightLayoutContainerTemplate copy_template = GWT
                .create(CopyRightLayoutContainerTemplate.class);
        HtmlLayoutContainer copyright = new HtmlLayoutContainer(copy_template.getTemplate());
        copyright.add(new LabelField(I18N.DISPLAY.projectCopyrightStatement()), new HtmlData(".cell1"));
        copyright.setStyleName("copyright"); //$NON-NLS-1$
        pnlFooter.add(copyright);

        NsfLayoutContainerTemplate nsf_template = GWT.create(NsfLayoutContainerTemplate.class);
        HtmlLayoutContainer nsftext = new HtmlLayoutContainer(nsf_template.getTemplate());
        nsftext.add(new LabelField(I18N.DISPLAY.nsfProjectText()), new HtmlData(".cell1"));
        nsftext.setStyleName("nsf-text"); //$NON-NLS-1$
        pnlFooter.add(nsftext);

        return pnlFooter;
    }

    public interface CopyRightLayoutContainerTemplate extends XTemplates {
        @XTemplate("<div style='float:left;'><div class='cell1'></div></div>")
        SafeHtml getTemplate();
    }

    public interface NsfLayoutContainerTemplate extends XTemplates {
        @XTemplate("<div style='float:right;'><div class='cell1'></div></div>")
        SafeHtml getTemplate();
    }

    /**
     * Returns the desktop's task bar.
     * 
     * @return the task bar
     */
    public TaskBar getTaskBar() {
        if (taskBar == null) {
            taskBar = new TaskBar();
        }
        return taskBar;
    }

    /**
     * Returns a list of the desktop's windows.
     * 
     * @return the windows
     */
    public DEWindowManager getWindowManager() {
        if (windowManager == null) {
            windowManager = new DEWindowManager(null, getHandler(), getHandler(), getHandler(),
                    getHandler(), getHandler());
        }
        return windowManager;
    }

    /**
     * Arranges the windows on the desktop using the requested layout manager.
     * 
     * @param desktopLayoutType the type of layout manager to use
     */
    public void layout(DesktopLayoutType desktopLayoutType) {
        layout(getDesktopLayout(desktopLayoutType), null, RequestType.LAYOUT);
    }

    /**
     * Minimizes the window.
     * 
     * @param window the window to minimize
     */
    public void minimizeWindow(IPlantWindowInterface window) {
        window.setMinimized(true);
        window.hide();
    }

    /**
     * Removes a shortcut from the desktop.
     * 
     * @param shortcut the shortcut to remove
     */
    public void removeShortcut(Shortcut shortcut) {
        getShortcuts().remove(shortcut);
        getDesktop().remove(shortcut);
    }

    /**
     * Sets the type of layout manager to use when new windows are added to the desktop, or the desktop
     * size changes.
     * 
     * @param desktopLayoutType the type of layout manager
     */
    public void setDesktopLayoutType(DesktopLayoutType desktopLayoutType) {
        desktopLayout = getDesktopLayout(desktopLayoutType);
    }

    private void initShortcuts() {
        ShortcutManager mgr = new ShortcutManager(new DefaultDesktopBuilder());

        List<Shortcut> shortcuts = mgr.getShortcuts();

        for (Shortcut shortcut : shortcuts) {
            addShortcut(shortcut);
        }
    }

    private DesktopLayout createDesktopLayout(DesktopLayoutType desktopLayoutType) {
        DesktopLayout desktopLayout;
        switch (desktopLayoutType) {
            case CASCADE:
                desktopLayout = new CascadeDesktopLayout();
                break;
            case CENTER:
                desktopLayout = new CenterDesktopLayout();
                break;
            case TILE:
                desktopLayout = new TileDesktopLayout();
                break;
            default:
                throw new IllegalArgumentException("Unsupported desktopLayoutType" + desktopLayoutType);
        }
        return desktopLayout;
    }

    private VerticalLayoutContainer getDesktopContainer() {
        if (desktopContainer == null) {
            desktopContainer = new VerticalLayoutContainer() {
                @Override
                public void onResize() {
                    super.onResize();
                    layout(null, RequestType.RESIZE);
                }

                @Override
                protected void doLayout() {
                    super.doLayout();
                    layout(null, RequestType.LAYOUT);
                }
            };
            desktopContainer.add(getDesktop(), new VerticalLayoutData(-1, 1));
            desktopContainer.add(buildFooterPanel(), new VerticalLayoutData(1, 20));
            desktopContainer.add(getTaskBar(), new VerticalLayoutData(1, -1));
        }
        return desktopContainer;
    }

    private DesktopLayout getDesktopLayout() {
        if (desktopLayout == null) {
            desktopLayout = getDesktopLayout(DEFAULT_DESKTOP_LAYOUT_TYPE);
        }
        return desktopLayout;
    }

    private DesktopLayout getDesktopLayout(DesktopLayoutType desktopLayoutType) {
        DesktopLayout desktopLayout = getDesktopLayouts().get(desktopLayoutType.name());
        if (desktopLayout == null) {
            desktopLayout = createDesktopLayout(desktopLayoutType);
            getDesktopLayouts().put(desktopLayoutType.name(), desktopLayout);
        }
        return desktopLayout;
    }

    private FastMap<DesktopLayout> getDesktopLayouts() {
        if (desktopLayouts == null) {
            desktopLayouts = new FastMap<DesktopLayout>();
        }
        return desktopLayouts;
    }

    private Viewport getDesktopViewport() {
        if (desktopViewport == null) {
            desktopViewport = new Viewport();
            desktopViewport.add(getDesktopContainer());
        }
        return desktopViewport;
    }

    private WindowHandler getHandler() {
        if (handler == null) {
            handler = new WindowHandler();
        }
        return handler;
    }

    // private IPlantWindowInterface getWidgetWindow(Widget widget) {
    // IPlantWindowInterface window;
    // if (widget instanceof IPlantWindowInterface) {
    // window = (IPlantWindowInterface)widget;
    // } else {
    // window = getWindowManager().find(widget);
    // if (widget == null) {
    // window = new Window();
    // window.add(widget);
    // }
    // }
    // return window;
    // }

    private void hideWindow(IPlantWindowInterface window) {
        if (window.isMinimized()) {
            markInactive(window);
            return;
        }
        if (activeWindow == window) {
            activeWindow = null;
        }
        taskBar.removeTaskButton(getWindowManager().getTaskButton(window.getTag()));
        windowManager.remove(window.getTag());
        layout(window, RequestType.HIDE);
    }

    private boolean isMaximized() {
        com.extjs.gxt.ui.client.core.FastMap<IPlantWindowInterface> deWindows = getWindowManager()
                .getDEWindows();
        for (String windowKey : deWindows.keySet()) {
            IPlantWindowInterface window = deWindows.get(windowKey);
            if (window.isVisible() && window.isMaximized()) {
                return true;
            }
        }
        return false;
    }

    private void layout(DesktopLayout desktopLayout, IPlantWindowInterface window,
            RequestType requestType) {
        if (!isMaximized()) {
            // desktopLayout.layoutDesktop(
            // window,
            // requestType,
            // getDesktop().getElement(),
            // getWindowManager().getWindows().subList(0,
            // getWindowManager().getWindows().size() - 1), getDesktop().getOffsetWidth(),
            // getDesktop().getOffsetHeight());
        }
    }

    private void layout(IPlantWindowInterface window, RequestType requestType) {
        layout(getDesktopLayout(), window, requestType);
    }

    private void markActive(IPlantWindowInterface window) {
        if (activeWindow != null && activeWindow != window) {
            markInactive(activeWindow);
        }
        TaskButton taskButton = getWindowManager().getTaskButton(window.getTag());
        taskBar.setActiveButton(taskButton);
        activeWindow = window;
        taskButton.setValue(true);
        window.setMinimized(false);
    }

    private void markInactive(IPlantWindowInterface window) {
        if (window == activeWindow) {
            activeWindow = null;
            TaskButton taskButton = getWindowManager().getTaskButton(window.getTag());
            taskButton.setValue(false);
        }
    }

    private void showWindow(IPlantWindowInterface window) {
        TaskButton taskButton = getWindowManager().getTaskButton(window.getTag());
        window.setMinimized(false);
        if (taskButton != null && taskBar.getButtons().contains(taskButton)) {
            layout(window, RequestType.SHOW);
            return;
        }
        taskButton = taskBar.addTaskButton(window);
        getWindowManager().setTaskButton(window.getTag(), taskButton);
        layout(window, RequestType.OPEN);
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

    }

}
