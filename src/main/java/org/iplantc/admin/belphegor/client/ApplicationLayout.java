package org.iplantc.admin.belphegor.client;

import org.iplantc.admin.belphegor.client.views.panels.CatalogAdminPanel;
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
        setStyleName(""); //$NON-NLS-1$

        addListeners();
    }

    private void addListeners() {
        EventBus instance = EventBus.getInstance();

    }

    private void drawNorth() {
        north = new ContentPanel();
        north.setHeaderVisible(false);
        north.setBodyBorder(false);
        north.setBorders(false);

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
        initAdminPanel();

    }

    private void initAdminPanel() {
        CatalogAdminPanel panel = new CatalogAdminPanel();
        replaceCenterPanel(panel);
    }

    private class HeaderPanel extends HorizontalPanel {
        public HeaderPanel() {
            setBorders(false);

            add(buildLogoPanel());
            add(buildActionsPanel());
            setStyleName("iplantc_header_right"); //$NON-NLS-1$
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
            return userMenu;
        }

        private Menu buildHelpMenu() {
            Menu helpMenu = buildMenu();
            return helpMenu;
        }

        private HorizontalPanel buildActionsMenu(String menuHeaderText, final Menu menu) {
            final HorizontalPanel ret = new HorizontalPanel();
            ret.setStyleName("iplantc_header_menu_panel"); //$NON-NLS-1$

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
