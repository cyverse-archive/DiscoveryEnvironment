package org.iplantc.admin.belphegor.client;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BorderLayoutEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;

/**
 * Defines the overall layout for the root panel of the web application.
 * 
 * @author sriram
 */
public class ApplicationLayout extends Viewport {
    private ContentPanel north;
    private Component center;

    private final BorderLayout layout;

    /**
     * Default constructor.
     */
    public ApplicationLayout() {
        // build top level layout
        layout = new BorderLayout();

        // make sure we re-draw when a panel expands
        layout.addListener(Events.Expand, new Listener<BorderLayoutEvent>() {
            @Override
            public void handleEvent(BorderLayoutEvent be) {
                layout();
            }
        });

        setLayout(layout);
        setStyleName("iplantc_background"); //$NON-NLS-1$

        addListeners();
    }

    private void addListeners() {
        EventBus instance = EventBus.getInstance();
//        instance.addHandler(NavigateToHomeEvent.TYPE, new NavigateToHomeEventHandler() {
//            @Override
//            public void onHome() {
//                addListOfTools();
//            }
//
//        });
    }

    private void drawNorth() {
        north = new ContentPanel();
        north.setHeaderVisible(false);
        north.setBodyBorder(false);
        north.setBorders(false);
        north.setBodyStyleName("iplantc_portal_component"); //$NON-NLS-1$

        north.add(new HeaderPanel());

        BorderLayoutData data = new BorderLayoutData(LayoutRegion.NORTH, 145);
        data.setCollapsible(false);
        data.setFloatable(false);
        data.setHideCollapseTool(true);
        data.setSplit(false);
        data.setMargins(new Margins(0, 0, 0, 0));

        add(north, data);
    }

    private void drawSouth() {
        HorizontalPanel south = new HorizontalPanel();

        Html copyright = new Html(I18N.DISPLAY.projectCopyrightStatement());
        copyright.setStyleName("copyright"); //$NON-NLS-1$

        Html nsftext = new Html(I18N.DISPLAY.nsfProjectText());
        nsftext.setStyleName("nsf_text"); //$NON-NLS-1$

        south.add(copyright);
        south.add(nsftext);

        BorderLayoutData data = new BorderLayoutData(LayoutRegion.SOUTH, 20);
        data.setCollapsible(false);
        data.setFloatable(false);
        data.setHideCollapseTool(true);
        data.setSplit(false);
        data.setMargins(new Margins(0, 0, 0, 0));

        add(south, data);
    }

    /**
     * Replace the contents of the center panel.
     * 
     * @param view a new component to set in the center of the BorderLayout.
     */
    public void replaceCenterPanel(Component view) {
        if (center != null) {
            remove(center);
        }

        center = view;

        BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
        data.setMargins(new Margins(0));

        if (center != null) {
            add(center, data);
        }

        if (isRendered()) {
            layout();
        }
    }

    public void reset() {
        // clear our center
        if (center != null) {
            remove(center);
        }

        center = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        drawNorth();
        drawSouth();

    }

    private class HeaderPanel extends HorizontalPanel {
        public HeaderPanel() {
            setBorders(false);

            add(buildLogoPanel());
            add(buildActionsPanel());
            setStyleName("iplantc_portal_component"); //$NON-NLS-1$
            setStyleAttribute("background-image", "url('" + Constants.CLIENT.iplantLogoFill() + "')"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        private VerticalPanel buildLogoPanel() {
            VerticalPanel panel = new VerticalPanel();

            Image logo = new Image(Constants.CLIENT.iplantLogo());
            logo.addStyleName("iplantc_logo"); //$NON-NLS-1$
            logo.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent arg0) {
                    com.google.gwt.user.client.Window.Location.assign(Constants.CLIENT.iplantHome());
                }
            });
            panel.add(logo);

            return panel;
        }

        private HorizontalPanel buildActionsPanel() {
            HorizontalPanel pnlActions = new HorizontalPanel();
            pnlActions.setStyleName("iplantc_header_actions"); //$NON-NLS-1$
            pnlActions.setSpacing(5);

            pnlActions.add(buildActionsMenu(UserInfo.getInstance().getUsername(), buildUserMenu()));
            pnlActions.add(buildActionsMenu(I18N.DISPLAY.help(), buildHelpMenu()));

            return pnlActions;
        }

        private Menu buildUserMenu() {
            Menu userMenu = buildMenu();

//            userMenu.add(new CustomHyperlink(I18N.DISPLAY.logout(), new LogoutSelectionListener(), I18N.DISPLAY.logoutToolTipText()));

            return userMenu;
        }

        private Menu buildHelpMenu() {
            Menu helpMenu = buildMenu();

//            helpMenu.add(new CustomHyperlink(I18N.DISPLAY.documentation(),
//                    new DocumentationSelectionListener(),""));
//            helpMenu.add(new CustomHyperlink(I18N.DISPLAY.forums(), new Listener<BaseEvent>() {
//                @Override
//                public void handleEvent(BaseEvent be) {
//                    WindowUtil.open(Constants.CLIENT.forumsUrl());
//                }
//            }, ""));
//
//            helpMenu.add(new CustomHyperlink(I18N.DISPLAY.contactSupport(), new Listener<BaseEvent>() {
//                @Override
//                public void handleEvent(BaseEvent be) {
//                    WindowUtil.open(Constants.CLIENT.supportUrl());
//                }
//            }, ""));
//            helpMenu.add(new CustomHyperlink(I18N.DISPLAY.about(), new AboutSelectionListener(),""));

            return helpMenu;
        }

        private HorizontalPanel buildActionsMenu(String menuHeaderText, final Menu menu) {
            final HorizontalPanel ret = new HorizontalPanel();
            ret.setStyleName("iplantc_header_menu_panel"); //$NON-NLS-1$

//            // build menu header text and icon
//            CustomLabel menuHeader = new CustomLabel(menuHeaderText);
//            menuHeader.addListener(Events.OnClick, new Listener<BaseEvent>() {
//                @Override
//                public void handleEvent(BaseEvent be) {
//                    showHeaderActionsMenu(ret, menu);
//                }
//            });
//
//            IconButton icon = new IconButton("iplantc_header_menu_button",
//                    new SelectionListener<IconButtonEvent>() {
//                        @Override
//                        public void componentSelected(IconButtonEvent ce) {
//                            showHeaderActionsMenu(ret, menu);
//                        }
//                    });
//
//            ret.add(menuHeader);
//            ret.add(icon);
//
//            // update header style when menu is shown
//            menu.addListener(Events.Show, new Listener<MenuEvent>() {
//                @Override
//                public void handleEvent(MenuEvent be) {
//                    ret.addStyleName("iplantc_header_menu_selected");
//                }
//            });
//
//            menu.addListener(Events.Hide, new Listener<MenuEvent>() {
//                @Override
//                public void handleEvent(MenuEvent be) {
//                    ret.removeStyleName("iplantc_header_menu_selected");
//                }
//            });

            return ret;
        }

        private Menu buildMenu() {
            Menu menu = new Menu();

            menu.setSize(110, 90);
            menu.setBorders(true);
            menu.setStyleName("iplantc_header_menu_body"); //$NON-NLS-1$

            return menu;
        }

        private void showHeaderActionsMenu(HorizontalPanel anchor, Menu actionsMenu) {
            // show the menu so that its right edge is aligned with with the anchor's right edge,
            // and its top is aligned with the anchor's bottom.
            actionsMenu.showAt(anchor.getAbsoluteLeft() + anchor.getWidth() - 110,
                    anchor.getAbsoluteTop() + anchor.getHeight());
        }
    }
}
