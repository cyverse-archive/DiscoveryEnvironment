package org.iplantc.de.client;

import org.iplantc.core.client.widgets.MenuHyperlink;
import org.iplantc.core.client.widgets.MenuLabel;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.dispatchers.ActionDispatcher;
import org.iplantc.de.client.dispatchers.DefaultActionDispatcher;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.events.NotificationCountUpdateEvent;
import org.iplantc.de.client.events.NotificationCountUpdateEventHandler;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.NotificationWindowConfig;
import org.iplantc.de.client.util.WindowUtil;
import org.iplantc.de.client.utils.NotificationManager;
import org.iplantc.de.client.utils.NotificationManager.Category;
import org.iplantc.de.client.views.dialogs.UserPreferencesDialog;
import org.iplantc.de.client.views.panels.ViewNotification;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BorderLayoutEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;

/**
 * Defines the overall layout for the root panel of the web application.
 * 
 * @author sriram
 */
public class ApplicationLayout extends Viewport {
    private final ContentPanel north;
    private LayoutContainer center;

    private HorizontalPanel headerPanel;
    private final LayoutContainer mainPanel;

    private NotificationIndicator lblNotifications;

    private NotificationManager notifyMgr;

    private final String linkStyle = "de_header_menu_hyperlink"; //$NON-NLS-1$
    private final String hoverStyle = "de_header_menu_hyperlink_hover"; //$NON-NLS-1$
    private ViewNotification view;

    /**
     * Default constructor.
     */
    public ApplicationLayout() {
        setLayout(new FitLayout());
        setBorders(false);
        setStyleName("iplantc-background"); //$NON-NLS-1$

        // build top level layout
        BorderLayout layout = new BorderLayout();

        // make sure we re-draw when a panel expands
        layout.addListener(Events.Expand, new Listener<BorderLayoutEvent>() {
            @Override
            public void handleEvent(BorderLayoutEvent be) {
                layout();
            }
        });

        // mainPanel exists so we can overlay the gradient background with a centered iPlant logo
        mainPanel = new LayoutContainer();
        mainPanel.setLayout(layout);
        mainPanel.addStyleName("iplantc-centerlogo"); //$NON-NLS-1$
        add(mainPanel);

        north = new ContentPanel();
        north.setBodyStyleName("iplantc-portal-component"); //$NON-NLS-1$

        initEventHandlers();
    }

    private void initEventHandlers() {
        EventBus eventbus = EventBus.getInstance();

        // handle data events
        eventbus.addHandler(NotificationCountUpdateEvent.TYPE,
                new NotificationCountUpdateEventHandler() {

                    @Override
                    public void onCountUpdate(NotificationCountUpdateEvent ncue) {
                        int analysesCount = ncue.getAnalysesCount();
                        int dataCount = ncue.getDataCount();
                        lblNotifications.setCount(analysesCount + dataCount);

                    }
                });
    }

    private void assembleHeader() {
        drawHeader();
        north.add(headerPanel);
    }

    private void drawHeader() {
        headerPanel = new HorizontalPanel();
        headerPanel.setBorders(false);
        headerPanel.setStyleName("iplantc-portal-component"); //$NON-NLS-1$

        headerPanel.add(buildLogoPanel());
        headerPanel.add(buildActionsPanel());
    }

    private VerticalPanel buildLogoPanel() {
        VerticalPanel panel = new VerticalPanel();

        Image logo = new Image(Constants.CLIENT.iplantLogo());
        logo.addStyleName("iplantc-logo"); //$NON-NLS-1$
        logo.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                WindowUtil.open(Constants.CLIENT.iplantHome());
            }
        });

        panel.add(logo);

        return panel;
    }

    private HorizontalPanel buildActionsPanel() {
        final HorizontalPanel pnlActions = new HorizontalPanel();
        pnlActions.setStyleName("iplantc-header-actions"); //$NON-NLS-1$
        pnlActions.setSpacing(5);

        // add user actions menu
        pnlActions.add(buildActionsMenu(UserInfo.getInstance().getUsername(), buildUserMenu()));

        // add help actions menu
        pnlActions.add(buildActionsMenu(I18N.DISPLAY.help(), buildHelpMenu()));

        // add notification actions menu
        HorizontalPanel notificationPanel = buildNotificationMenu(I18N.DISPLAY.notifications());

        lblNotifications = new NotificationIndicator(0);
        notificationPanel.add(lblNotifications);

        pnlActions.add(notificationPanel);

        return pnlActions;
    }

    private HorizontalPanel buildFooterPanel() {
        HorizontalPanel pnlFooter = new HorizontalPanel();
        pnlFooter.setWidth("100%"); //$NON-NLS-1$

        Html copyright = new Html(I18N.DISPLAY.projectCopyrightStatement());
        copyright.setStyleName("copyright"); //$NON-NLS-1$
        pnlFooter.add(copyright);

        Html nsftext = new Html(I18N.DISPLAY.nsfProjectText());
        nsftext.setStyleName("nsf-text"); //$NON-NLS-1$
        pnlFooter.add(nsftext);

        return pnlFooter;
    }

    private void doLogout() {
        ActionDispatcher actionDispatcher = new DefaultActionDispatcher();
        actionDispatcher.dispatchAction(Constants.CLIENT.logoutTag());
    }

    private void drawNorth() {
        north.setHeaderVisible(false);
        north.setBodyBorder(false);
        north.setBorders(false);

        BorderLayoutData data = new BorderLayoutData(LayoutRegion.NORTH, 90);
        data.setCollapsible(false);
        data.setFloatable(false);
        data.setHideCollapseTool(true);
        data.setSplit(false);
        data.setMargins(new Margins(0, 0, 0, 0));

        mainPanel.add(north, data);
    }

    /**
     * Compose widgets and panels, setting the header's background color to the given CSS color value.
     */
    public void assembleLayout() {
        drawNorth();
        assembleHeader();
        if (notifyMgr == null) {
            notifyMgr = NotificationManager.getInstance();
        }
        initViewNotification();
    }

    /**
     * Replace the contents of the center panel.
     * 
     * @param view a new component to set in the center of the BorderLayout.
     */
    public void replaceCenterPanel(LayoutContainer view) {
        if (center != null) {
            remove(center);
        }

        center = view;

        BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
        data.setMargins(new Margins(0));
        data.setSplit(false);

        if (center != null) {
            center.add(buildFooterPanel());

            mainPanel.add(center, data);
        }

        layout();
    }

    private void displayAboutDe() {
        WindowDispatcher dispatcher = new WindowDispatcher();
        dispatcher.dispatchAction(Constants.CLIENT.myAboutTag());
    }

    private Menu buildUserMenu() {
        final Menu userMenu = buildMenu();

        userMenu.add(new MenuHyperlink(I18N.DISPLAY.logout(), linkStyle, hoverStyle, //$NON-NLS-1$ //$NON-NLS-2$
                new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        doLogout();
                        userMenu.hide();
                    }
                }, null));
        userMenu.add(new MenuHyperlink(I18N.DISPLAY.preferences(), linkStyle, hoverStyle, //$NON-NLS-1$ //$NON-NLS-2$
                new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        buildAndShowPreferencesDialog();
                        userMenu.hide();
                    }
                }, null));

        return userMenu;
    }

    private Menu buildHelpMenu() {
        final Menu helpMenu = buildMenu();
        helpMenu.add(new MenuHyperlink(I18N.DISPLAY.documentation(), linkStyle, hoverStyle,
                new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        WindowUtil.open(Constants.CLIENT.deHelpFile());
                        helpMenu.hide();
                    }
                }));

        helpMenu.add(new MenuHyperlink(I18N.DISPLAY.forums(), linkStyle, hoverStyle,
                new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        WindowUtil.open(Constants.CLIENT.forumsUrl());
                        helpMenu.hide();
                    }
                }));

        helpMenu.add(new MenuHyperlink(I18N.DISPLAY.contactSupport(), linkStyle, hoverStyle,
                new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        WindowUtil.open(Constants.CLIENT.supportUrl());
                        helpMenu.hide();
                    }
                }));

        helpMenu.add(new MenuHyperlink(I18N.DISPLAY.about(), linkStyle, hoverStyle,
                new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        displayAboutDe();
                        helpMenu.hide();
                    }
                }));

        return helpMenu;
    }

    private void buildAndShowPreferencesDialog() {
        UserPreferencesDialog usd = new UserPreferencesDialog();
        usd.setSize(450, 430);
        usd.show();
    }

    private HorizontalPanel buildNotificationMenu(String menuHeaderText) {
        final HorizontalPanel ret = new HorizontalPanel();
        ret.setStyleName("de_header_menu_panel"); //$NON-NLS-1$
        // build menu header text and icon
        MenuLabel menuHeader = new MenuLabel(menuHeaderText,
                "de_header_menu_label", "de_header_menu_label_hover"); //$NON-NLS-1$ //$NON-NLS-2$
        menuHeader.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                showNotificationView(ret);
            }
        });

        IconButton icon = new IconButton("de_header_menu_button", //$NON-NLS-1$
                new SelectionListener<IconButtonEvent>() {
                    @Override
                    public void componentSelected(IconButtonEvent ce) {
                        showNotificationView(ret);
                    }
                });

        ret.add(menuHeader);
        ret.add(icon);

        return ret;
    }

    private HorizontalPanel buildActionsMenu(String menuHeaderText, final Menu menu) {
        final HorizontalPanel ret = new HorizontalPanel();
        ret.setStyleName("de_header_menu_panel"); //$NON-NLS-1$

        // build menu header text and icon
        MenuLabel menuHeader = new MenuLabel(menuHeaderText,
                "de_header_menu_label", "de_header_menu_label_hover"); //$NON-NLS-1$ //$NON-NLS-2$
        menuHeader.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                showHeaderActionsMenu(ret, menu);
            }
        });

        IconButton icon = new IconButton("de_header_menu_button", //$NON-NLS-1$
                new SelectionListener<IconButtonEvent>() {
                    @Override
                    public void componentSelected(IconButtonEvent ce) {
                        showHeaderActionsMenu(ret, menu);
                    }
                });

        ret.add(menuHeader);
        ret.add(icon);

        // update header style when menu is shown
        menu.addListener(Events.Show, new Listener<MenuEvent>() {
            @Override
            public void handleEvent(MenuEvent be) {
                ret.addStyleName("de_header_menu_selected"); //$NON-NLS-1$
            }
        });

        menu.addListener(Events.Hide, new Listener<MenuEvent>() {
            @Override
            public void handleEvent(MenuEvent be) {
                ret.removeStyleName("de_header_menu_selected"); //$NON-NLS-1$
            }
        });

        return ret;
    }

    private Menu buildMenu() {
        Menu d = new Menu();

        d.setSize(110, 90);
        d.setBorders(true);
        d.setStyleName("de_header_menu_body"); //$NON-NLS-1$

        return d;
    }

    private void showHeaderActionsMenu(HorizontalPanel anchor, Menu actionsMenu) {
        // show the menu so that its right edge is aligned with with the anchor's right edge,
        // and its top is aligned with the anchor's bottom.
        Point point = anchor.getPosition(false);
        actionsMenu.showAt(point.x + anchor.getWidth() - 110, point.y + anchor.getHeight());
    }

    /** Makes the notification window visible and filters by a category */
    private void showNotificationWindow(final Category category) {
        NotificationWindowConfig config = new NotificationWindowConfig();
        config.setCategory(category);

        // Build window config
        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory
                .buildWindowConfig(Constants.CLIENT.myNotifyTag(), config);
        WindowDispatcher dispatcher = new WindowDispatcher(windowConfig);
        dispatcher.dispatchAction(Constants.CLIENT.myNotifyTag());
    }

    private void showNotificationView(final HorizontalPanel ret) {
        Point point = ret.getPosition(false);
        point.x = point.x - 142;
        point.y = point.y + 22;

        view.removeAllListeners();
        // update header style when menu is shown
        view.addListener(Events.Show, new Listener<MenuEvent>() {
            @Override
            public void handleEvent(MenuEvent be) {
                ret.addStyleName("de_header_menu_selected"); //$NON-NLS-1$
            }
        });

        view.addListener(Events.Hide, new Listener<MenuEvent>() {
            @Override
            public void handleEvent(MenuEvent be) {
                ret.removeStyleName("de_header_menu_selected"); //$NON-NLS-1$
            }
        });
        lblNotifications.setCount(0);
        notifyMgr.resetCount();
        view.showAt(point.x, point.y);
    }

    private void initViewNotification() {
        view = new ViewNotification();
        view.setBorders(true);
        view.setSize(250, 310);

        view.setStyleName("de_header_menu_body");
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new MenuHyperlink(I18N.DISPLAY.allNotifications(), linkStyle, "",
                new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        showNotificationWindow(NotificationManager.Category.ALL);
                    }
                }));
        view.add(hp);
    }

    /**
     * A Label with a setCount method that can set the label's styled text to the count when it's greater
     * than 0, or setting empty text and removing the style for a count of 0 or less.
     * 
     * @author psarando
     * 
     */
    private class NotificationIndicator extends Label {
        public NotificationIndicator(int initialCount) {
            super();

            setStyleName("de_notification_indicator"); //$NON-NLS-1$
            setCount(initialCount);
        }

        public void setCount(int count) {
            if (count > 0) {
                setText(String.valueOf(count));
                addStyleName("de_notification_indicator_highlight"); //$NON-NLS-1$
                Window.setTitle("(" + count + ") " + I18N.DISPLAY.rootApplicationTitle());
            } else {
                setText("&nbsp;&nbsp;"); //$NON-NLS-1$
                removeStyleName("de_notification_indicator_highlight"); //$NON-NLS-1$
                Window.setTitle(I18N.DISPLAY.rootApplicationTitle());
            }
        }
    }

}
