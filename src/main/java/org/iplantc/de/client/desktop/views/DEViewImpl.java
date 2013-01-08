/**
 * 
 */
package org.iplantc.de.client.desktop.views;

import org.iplantc.core.client.widgets.MenuHyperlink;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.widgets.PushButton;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.collaborators.views.ManageCollaboratorsDailog;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.events.NotificationCountUpdateEvent;
import org.iplantc.de.client.events.NotificationCountUpdateEventHandler;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.images.Resources;
import org.iplantc.de.client.models.NotificationWindowConfig;
import org.iplantc.de.client.notifications.util.NotificationHelper.Category;
import org.iplantc.de.client.preferences.views.PreferencesDialog;
import org.iplantc.de.client.util.WindowUtil;
import org.iplantc.de.client.views.panels.ViewNotificationMenu;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Label;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import com.sencha.gxt.widget.core.client.menu.Menu;

/**
 * Default DE View as Desktop
 * 
 * @author sriram
 * 
 */
public class DEViewImpl implements DEView {

    private static DEViewUiBinder uiBinder = GWT.create(DEViewUiBinder.class);

    @UiField
    HorizontalLayoutContainer headerPanel;
    @UiField
    SimpleContainer mainPanel;

    @UiField
    MarginData centerData;
    @UiField
    BorderLayoutContainer con;

    private Presenter presenter;
    private NotificationIndicator lblNotifications;
    private ViewNotificationMenu notificationsView;

    // TODO JDS Reimplement these styles in CssResource
    private final String linkStyle = "de_header_menu_hyperlink"; //$NON-NLS-1$
    private final String hoverStyle = "de_header_menu_hyperlink_hover"; //$NON-NLS-1$

    private final Widget widget;

    @UiTemplate("DEView.ui.xml")
    interface DEViewUiBinder extends UiBinder<Widget, DEViewImpl> {
    }

    public DEViewImpl() {
        widget = uiBinder.createAndBindUi(this);
        // TODO JDS Reimplement with CssResource
        con.setStyleName("iplantc-background"); //$NON-NLS-1$)
        initEventHandlers();
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    private void initEventHandlers() {
        EventBus eventbus = EventBus.getInstance();

        // handle data events
        eventbus.addHandler(NotificationCountUpdateEvent.TYPE,
                new NotificationCountUpdateEventHandler() {

                    @Override
                    public void onCountUpdate(NotificationCountUpdateEvent ncue) {
                        int new_count = ncue.getTotal();
                        if (new_count > 0 && new_count > lblNotifications.getCount()) {
                            notificationsView.fetchUnseenNotifications();
                        }
                        notificationsView.setUnseenCount(new_count);
                        lblNotifications.setCount(new_count);

                    }
                });
    }

    @Override
    public void drawHeader() {
        headerPanel.add(buildLogoPanel());
        headerPanel.add(buildBufferPanel());
        headerPanel.add(buildHtmlActionsPanel());
    }

    private HorizontalLayoutContainer buildBufferPanel() {
        final HorizontalLayoutContainer buffer = new HorizontalLayoutContainer();
        buffer.setWidth("37%");
        return buffer;
    }

    private VerticalLayoutContainer buildLogoPanel() {
        VerticalLayoutContainer panel = new VerticalLayoutContainer();
        panel.setWidth("33%");

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

    /**
     * Replace the contents of the center panel.
     * 
     * @param view a new component to set in the center of the BorderLayout.
     */
    @Override
    public void replaceCenterPanel(IsWidget view) {
        con.remove(con.getCenterWidget());
        con.setCenterWidget(view, centerData);
    }

    private HorizontalPanel buildHtmlActionsPanel() {
        HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(10);
        panel.setWidth("20%");
        panel.add(buildActionsMenu(UserInfo.getInstance().getUsername(), 60, buildUserMenu()));
        panel.add(buildActionsMenu(I18N.DISPLAY.help(), 60, buildHelpMenu()));
        panel.add(buildNotificationMenu(I18N.DISPLAY.notifications(), 85));

        return panel;
    }

    private HorizontalLayoutContainer buildNotificationMenu(String menuHeaderText, int headerWidth) {
        final HorizontalLayoutContainer ret = new HorizontalLayoutContainer();
        lblNotifications = new NotificationIndicator(0);

        final PushButton button = new PushButton(menuHeaderText, headerWidth);
        notificationsView = new ViewNotificationMenu();
        notificationsView.setBorders(false);
        notificationsView.setStyleName("de_header_menu_body"); //$NON-NLS-1$
        notificationsView.setShadow(false);
        notificationsView.addShowHandler(new ShowHandler() {

            @Override
            public void onShow(ShowEvent event) {
                button.addStyleName("de_header_menu_selected");

            }
        });

        notificationsView.addHideHandler(new HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                button.removeStyleName("de_header_menu_selected");

            }
        });
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                // showNotificationWindow(Category.ALL);
                showHeaderActionsMenu(ret, notificationsView);
                lblNotifications.setCount(0);
            }
        });

        button.setImage(new Image(Resources.ICONS.menuAnchor()));
        ret.add(button);
        ret.add(lblNotifications);

        return ret;
    }

    private HorizontalLayoutContainer buildActionsMenu(String menuHeaderText, int headerWidth,
            final Menu menu) {
        final HorizontalLayoutContainer ret = new HorizontalLayoutContainer();
        ret.setBorders(false);

        final PushButton button = new PushButton(menuHeaderText, headerWidth);
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                showHeaderActionsMenu(ret, menu);

            }
        });

        menu.addShowHandler(new ShowHandler() {

            @Override
            public void onShow(ShowEvent event) {
                button.addStyleName("de_header_menu_selected");

            }
        });

        menu.addHideHandler(new HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                button.removeStyleName("de_header_menu_selected");

            }
        });

        button.setImage(new Image(Resources.ICONS.menuAnchor()));
        ret.add(button);

        return ret;
    }

    private Menu buildUserMenu() {
        final Menu userMenu = buildMenu();

        userMenu.add(new MenuHyperlink(I18N.DISPLAY.logout(), linkStyle, hoverStyle,
                new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        // doLogout();
                        userMenu.hide();
                    }
                }, null));
        userMenu.add(new MenuHyperlink(I18N.DISPLAY.preferences(), linkStyle, hoverStyle,
                new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        buildAndShowPreferencesDialog();
                        userMenu.hide();
                    }
                }, null));
        userMenu.add(new MenuHyperlink(I18N.DISPLAY.collaborators(), linkStyle, hoverStyle,
                new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        ManageCollaboratorsDailog dialog = new ManageCollaboratorsDailog();
                        dialog.show();
                        userMenu.hide();
                    }
                }, null));

        return userMenu;
    }

    private void buildAndShowPreferencesDialog() {
        PreferencesDialog d = new PreferencesDialog();
        d.show();
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
                        // displayAboutDe();
                        helpMenu.hide();
                    }
                }));

        return helpMenu;
    }

    private Menu buildMenu() {
        Menu d = new Menu();

        d.setSize("110px", "90px");
        d.setBorders(true);
        d.setStyleName("de_header_menu_body"); //$NON-NLS-1$
        d.setShadow(false);

        return d;
    }

    private void showHeaderActionsMenu(HorizontalLayoutContainer anchor, Menu actionsMenu) {
        // show the menu so that its right edge is aligned with with the anchor's right edge,
        // and its top is aligned with the anchor's bottom.
        com.sencha.gxt.core.client.util.Point point = new com.sencha.gxt.core.client.util.Point(
                anchor.getAbsoluteLeft(), anchor.getAbsoluteTop());
        //
        actionsMenu.showAt(point.getX() + anchor.getElement().getWidth(true) + 2, point.getY()
                + anchor.getElement().getHeight(true) + 25);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

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

    /**
     * A Label with a setCount method that can set the label's styled text to the count when it's greater
     * than 0, or setting empty text and removing the style for a count of 0 or less.
     * 
     * @author psarando
     * 
     */
    private class NotificationIndicator extends Label {

        int count;

        public NotificationIndicator(int initialCount) {
            super();

            setStyleName("de_notification_indicator"); //$NON-NLS-1$
            setCount(initialCount);
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
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
