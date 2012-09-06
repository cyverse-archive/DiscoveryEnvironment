/**
 * 
 */
package org.iplantc.de.client.gxt3.views;

import org.iplantc.core.client.widgets.MenuHyperlink;
import org.iplantc.core.client.widgets.MenuLabel;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.NotificationCountUpdateEvent;
import org.iplantc.de.client.events.NotificationCountUpdateEventHandler;
import org.iplantc.de.client.util.WindowUtil;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import com.sencha.gxt.widget.core.client.menu.Menu;

/**
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

    private final String linkStyle = "de_header_menu_hyperlink"; //$NON-NLS-1$
    private final String hoverStyle = "de_header_menu_hyperlink_hover"; //$NON-NLS-1$

    private final Widget widget;

    @UiTemplate("DEView.ui.xml")
    interface DEViewUiBinder extends UiBinder<Widget, DEViewImpl> {
    }

    public DEViewImpl() {
        widget = uiBinder.createAndBindUi(this);
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
                        // lblNotifications.setCount(ncue.getTotal());

                    }
                });
    }

    @Override
    public void drawHeader() {
        headerPanel.setBorders(true);

        //  headerPanel.setStyleName("iplantc-portal-component"); //$NON-NLS-1$

        headerPanel.add(buildLogoPanel());
        headerPanel.add(buildBufferPanel());
        headerPanel.add(buildHtmlActionsPanel());
    }

    private HorizontalLayoutContainer buildBufferPanel() {
        final HorizontalLayoutContainer buffer = new HorizontalLayoutContainer();
        buffer.setWidth("47%");
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

    private HtmlLayoutContainer buildHtmlActionsPanel() {
        MenuContainerLayoutContainerTemplate menu_template = GWT
                .create(MenuContainerLayoutContainerTemplate.class);
        HtmlLayoutContainer menuContainer = new HtmlLayoutContainer(menu_template.getTemplate());
        menuContainer.setWidth("20%");
        // add user actions menu
        menuContainer
                .add(buildActionsMenu(I18N.DISPLAY.help(), buildHelpMenu()), new HtmlData(".cell1"));
        menuContainer.add(buildActionsMenu(UserInfo.getInstance().getUsername(), buildUserMenu()),
                new HtmlData(".cell3"));
        // // add notification actions menu
        menuContainer.add(buildNotificationMenu(I18N.DISPLAY.notifications()), new HtmlData(".cell2"));
        //
        // lblNotifications = new NotificationIndicator(0);
        // notificationPanel.add(lblNotifications);
        //
        // pnlActions.add(notificationPanel);

        return menuContainer;
    }

    public interface MenuContainerLayoutContainerTemplate extends XTemplates {
        @XTemplate("<div class=\"cell1\" style =\"width:25%; float:right;padding-top:15px\"></div><div class=\"cell2\" style =\"width:45%;float:right;padding-top:15px\"></div><div class=\"cell3\" style =\"width:25%;float:right;padding-top:15px\"></div>")
        SafeHtml getTemplate();
    }

    private HorizontalLayoutContainer buildNotificationMenu(String menuHeaderText) {
        final HorizontalLayoutContainer ret = new HorizontalLayoutContainer();
        ret.setStyleName("de_header_menu_panel"); //$NON-NLS-1$
        // build menu header text and icon
        MenuLabel menuHeader = new MenuLabel(menuHeaderText,
                "de_header_menu_label", "de_header_menu_label_hover"); //$NON-NLS-1$ //$NON-NLS-2$
        menuHeader.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                // showNotificationView(ret);
            }
        });

        IconButton icon = new IconButton("de_header_menu_button", //$NON-NLS-1$
                new SelectionListener<IconButtonEvent>() {
                    @Override
                    public void componentSelected(IconButtonEvent ce) {
                        // showNotificationView(ret);
                    }
                });

        ret.add(menuHeader);
        ret.add(icon);

        return ret;
    }

    private HorizontalLayoutContainer buildActionsMenu(String menuHeaderText, final Menu menu) {
        final HorizontalLayoutContainer ret = new HorizontalLayoutContainer();
        ret.setBorders(true);
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

        menu.addShowHandler(new ShowHandler() {
            @Override
            public void onShow(ShowEvent event) {
                ret.addStyleName("de_header_menu_selected"); //$NON-NLS-1$

            }
        });

        menu.addHideHandler(new HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                ret.removeStyleName("de_header_menu_selected"); //$NON-NLS-1$

            }
        });
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
                        // buildAndShowPreferencesDialog();
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
        actionsMenu.showAt(point.getX() + anchor.getElement().getWidth(true), point.getY()
                + anchor.getElement().getHeight(true) + 20);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}
