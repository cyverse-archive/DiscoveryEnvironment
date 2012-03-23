package org.iplantc.de.client;

import org.iplantc.core.client.widgets.MenuHyperlink;
import org.iplantc.core.client.widgets.MenuLabel;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.dispatchers.ActionDispatcher;
import org.iplantc.de.client.dispatchers.DefaultActionDispatcher;
import org.iplantc.de.client.events.AnalysisPayloadEvent;
import org.iplantc.de.client.events.AnalysisPayloadEventHandler;
import org.iplantc.de.client.events.DataPayloadEvent;
import org.iplantc.de.client.events.DataPayloadEventHandler;
import org.iplantc.de.client.factories.EventJSONFactory;
import org.iplantc.de.client.images.Resources;
import org.iplantc.de.client.util.WindowUtil;
import org.iplantc.de.client.utils.LogoutUtil;
import org.iplantc.de.client.utils.MessageDispatcher;
import org.iplantc.de.client.utils.NotificationManager.Category;
import org.iplantc.de.client.views.panels.NotificationIconBar;
import org.iplantc.de.client.views.windows.IPlantWindow;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BorderLayoutEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.widget.Component;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
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
    private NotificationLabel lblNotificationsAll;
    private NotificationLabel lblNotificationsAnalyses;
    private NotificationLabel lblNotificationsData;

    private int countNotificationsAll = 0;
    private int countNotificationsAnalyses = 0;
    private int countNotificationsData = 0;

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
        eventbus.addHandler(DataPayloadEvent.TYPE, new DataPayloadEventHandler() {
            @Override
            public void onFire(DataPayloadEvent event) {
                if (event.getMessage() != null) {
                    countNotificationsAll++;
                    countNotificationsData++;

                    lblNotifications.setCount(countNotificationsAll);
                    lblNotificationsAll.setCount(countNotificationsAll);
                    lblNotificationsData.setCount(countNotificationsData);
                }
            }
        });

        // handle analysis events
        eventbus.addHandler(AnalysisPayloadEvent.TYPE, new AnalysisPayloadEventHandler() {
            @Override
            public void onFire(AnalysisPayloadEvent event) {
                if (event.getMessage() != null) {
                    countNotificationsAll++;
                    countNotificationsAnalyses++;

                    lblNotifications.setCount(countNotificationsAll);
                    lblNotificationsAll.setCount(countNotificationsAll);
                    lblNotificationsAnalyses.setCount(countNotificationsAnalyses);
                }
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
        HorizontalPanel notificationPanel = buildActionsMenu(I18N.DISPLAY.notifications(),
                buildNotificationsMenu());

        lblNotifications = new NotificationIndicator(countNotificationsAll);
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
        if (GXT.isIE) {
            showLogoutMessage();
            return;
        } else {
            actionDispatcher.dispatchAction(Constants.CLIENT.logoutTag());
        }
    }

    private void showLogoutMessage() {
        ContentPanel panel = new ContentPanel();
        Html html = panel.addText(LogoutUtil.buildLogoutMessageText());
        html.setStyleAttribute("padding", "5px"); //$NON-NLS-1$ //$NON-NLS-2$
        html.setStyleAttribute("height", "115px"); //$NON-NLS-1$ //$NON-NLS-2$
        panel.setHeaderVisible(false);
        panel.setWidth(350);
        panel.setHeight(115);
        panel.setAutoHeight(true);
        IPlantWindow win = new IPlantWindow("", false, false, false, true) { //$NON-NLS-1$
        };

        win.getHeader().setIcon(AbstractImagePrototype.create(Resources.ICONS.whitelogo()));
        win.setHeading(I18N.DISPLAY.logoutMessageTitle());
        win.setWidth(350);
        win.setHeight(115);
        win.setBodyBorder(false);
        win.add(panel);
        win.setModal(true);
        win.show();
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

    private class NotificationAllListener implements Listener<BaseEvent> {
        Component source;

        NotificationAllListener(Menu m) {
            super();
            source = m;
        }

        @Override
        public void handleEvent(BaseEvent be) {
            source.hide();

            countNotificationsAll = 0;
            countNotificationsData = 0;
            countNotificationsAnalyses = 0;

            lblNotifications.setCount(countNotificationsAll);
            lblNotificationsAll.setCount(countNotificationsAll);
            lblNotificationsData.setCount(countNotificationsData);
            lblNotificationsAnalyses.setCount(countNotificationsAnalyses);

            NotificationIconBar.showNotificationWindow(Category.ALL);
        }
    }

    private class NotificationDataListener implements Listener<BaseEvent> {
        Component source;

        public NotificationDataListener(Menu m) {
            super();
            source = m;
        }

        @Override
        public void handleEvent(BaseEvent be) {
            source.hide();

            countNotificationsData = 0;
            countNotificationsAll = countNotificationsAnalyses;

            lblNotifications.setCount(countNotificationsAll);
            lblNotificationsAll.setCount(countNotificationsAll);
            lblNotificationsData.setCount(countNotificationsData);

            NotificationIconBar.showNotificationWindow(Category.DATA);
        }
    }

    private class NotificationAnalysisListener implements Listener<BaseEvent> {
        Component source;

        NotificationAnalysisListener(Menu m) {
            super();
            source = m;
        }

        @Override
        public void handleEvent(BaseEvent be) {
            source.hide();

            countNotificationsAnalyses = 0;
            countNotificationsAll = countNotificationsData;

            lblNotifications.setCount(countNotificationsAll);
            lblNotificationsAll.setCount(countNotificationsAll);
            lblNotificationsAnalyses.setCount(countNotificationsAnalyses);

            NotificationIconBar.showNotificationWindow(Category.ANALYSIS);
        }
    }

    private String buildPayload(final String tag) {
        StringBuffer ret = new StringBuffer();

        ret.append("{"); //$NON-NLS-1$

        ret.append("\"tag\": " + JsonUtil.quoteString(tag)); //$NON-NLS-1$

        ret.append("}"); //$NON-NLS-1$

        return ret.toString();
    }

    private void displayAboutDe() {
        String json = EventJSONFactory.build(EventJSONFactory.ActionType.DISPLAY_WINDOW,
                buildPayload(Constants.CLIENT.myAboutTag()));

        MessageDispatcher dispatcher = MessageDispatcher.getInstance();
        dispatcher.processMessage(json);
    }

    private Menu buildUserMenu() {
        final Menu userMenu = buildMenu();

        userMenu.add(new MenuHyperlink(I18N.DISPLAY.logout(),
                "de_header_menu_hyperlink_hover", "de_header_menu_hyperlink", //$NON-NLS-1$ //$NON-NLS-2$
                new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        doLogout();
                        userMenu.hide();
                    }
                }, I18N.DISPLAY.logoutToolTipText()));

        return userMenu;
    }

    private Menu buildHelpMenu() {
        final Menu helpMenu = buildMenu();
        String linkStyle = "de_header_menu_hyperlink"; //$NON-NLS-1$
        String hoverStyle = "de_header_menu_hyperlink_hover"; //$NON-NLS-1$

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

    private Menu buildNotificationsMenu() {
        Menu notificationMenu = buildMenu();
        String linkStyle = "de_header_menu_hyperlink"; //$NON-NLS-1$

        lblNotificationsAll = new NotificationLabel(I18N.DISPLAY.all(), linkStyle,
                countNotificationsAll, new NotificationAllListener(notificationMenu));
        lblNotificationsData = new NotificationLabel(I18N.DISPLAY.data(), linkStyle,
                countNotificationsData, new NotificationDataListener(notificationMenu));
        lblNotificationsAnalyses = new NotificationLabel(I18N.DISPLAY.apps(), linkStyle,
                countNotificationsAnalyses, new NotificationAnalysisListener(notificationMenu));

        notificationMenu.add(lblNotificationsAll);
        notificationMenu.add(lblNotificationsData);
        notificationMenu.add(lblNotificationsAnalyses);

        return notificationMenu;
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
            } else {
                setText("&nbsp;&nbsp;"); //$NON-NLS-1$
                removeStyleName("de_notification_indicator_highlight"); //$NON-NLS-1$
            }
        }
    }

    private class NotificationLabel extends MenuHyperlink {
        private final String text;

        public NotificationLabel(String text, String baseStyle, int initialCount,
                Listener<BaseEvent> clickListener) {
            super(text, baseStyle, "de_header_menu_hyperlink_hover", clickListener); //$NON-NLS-1$

            this.text = text;
            setCount(initialCount);
        }

        public void setCount(int count) {
            if (count > 0) {
                setHtml(Format.substitute("{0} <span class='iplantc-orange'>({1})</span>", text, count)); //$NON-NLS-1$
                Window.setTitle("(" + count + ") " + I18N.DISPLAY.rootApplicationTitle());
            } else {
                Window.setTitle(I18N.DISPLAY.rootApplicationTitle());
                setHtml(text);
            }

        }
    }
}
