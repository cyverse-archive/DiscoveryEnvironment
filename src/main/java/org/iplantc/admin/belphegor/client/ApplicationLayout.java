package org.iplantc.admin.belphegor.client;

import org.iplantc.admin.belphegor.client.gxt3.presenter.BelphegorAppsViewPresenter;
import org.iplantc.admin.belphegor.client.gxt3.views.BelphegorAnalysisColumnModel;
import org.iplantc.admin.belphegor.client.models.CASCredentials;
import org.iplantc.admin.belphegor.client.views.panels.ReferenceGenomeListingPanel;
import org.iplantc.core.client.widgets.Hyperlink;
import org.iplantc.core.uiapplications.client.models.autobeans.Analysis;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisGroup;
import org.iplantc.core.uiapplications.client.views.AppsView;
import org.iplantc.core.uiapplications.client.views.AppsViewImpl;
import org.iplantc.core.uicommons.client.I18N;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BorderLayoutEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.WidgetComponent;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

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
    }

    private void drawNorth() {
        north = new ContentPanel();
        north.setHeaderVisible(false);
        north.setBodyBorder(false);
        north.setBorders(false);
        north.setBodyStyleName("iplantc-portal-component"); //$NON-NLS-1$
        north.add(new HeaderPanel());

        BorderLayoutData data = new BorderLayoutData(LayoutRegion.NORTH, 125);
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
        south.setStyleAttribute("background-color", "#FEFDDD");

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

    /**
     * Used to place GXT3 widgets inside the center panel.
     * 
     * @param view
     */
    public void replaceCenterPanel(com.sencha.gxt.widget.core.client.Component view) {
        replaceCenterPanel(new WidgetComponent(view));
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
        TabPanel tabPanel = new TabPanel();
        TabItem appItem = new TabItem("Apps");
        appItem.setLayout(new FitLayout());

        TabItem refItem = new TabItem(org.iplantc.admin.belphegor.client.I18N.DISPLAY.referenceGenomes());
        refItem.setLayout(new FitLayout());

        ReferenceGenomeListingPanel refPanel = new ReferenceGenomeListingPanel();
        refItem.add(refPanel);

        // CatalogAdminPanel panel = new CatalogAdminPanel();

        // --------->
        TreeStore<AnalysisGroup> treeStore = new TreeStore<AnalysisGroup>(
                new AnalysisGroupModelKeyProvider());
        ListStore<Analysis> listStore = new ListStore<Analysis>(new AnalysisModelKeyProvider());
        BelphegorAnalysisColumnModel cm = new BelphegorAnalysisColumnModel();
        AppsView view = new AppsViewImpl(treeStore, listStore, cm);

        BelphegorAppsViewPresenter presenter = new BelphegorAppsViewPresenter(view);
        // Create view and presenter and add it here.
        SimpleContainer appViewContentPanel = new SimpleContainer();
        appViewContentPanel.setPixelSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
        presenter.go(appViewContentPanel);
        appItem.add(appViewContentPanel);
        // appItem.add(panel);

        tabPanel.add(appItem);
        tabPanel.add(refItem);

        replaceCenterPanel(tabPanel);
    }

    private final class AnalysisGroupModelKeyProvider implements ModelKeyProvider<AnalysisGroup> {
        @Override
        public String getKey(AnalysisGroup item) {
            return item.getId();
        }
    }

    private final class AnalysisModelKeyProvider implements ModelKeyProvider<Analysis> {
        @Override
        public String getKey(Analysis item) {
            return item.getId();
        }
    }

    private class HeaderPanel extends HorizontalPanel {
        public HeaderPanel() {
            setBorders(false);

            add(buildLogoPanel());
            add(buildActionsPanel());
            setStyleName("iplantc-portal-component");
            addStyleName("iplantc_header_right");
        }

        private VerticalPanel buildLogoPanel() {
            VerticalPanel panel = new VerticalPanel();
            panel.addStyleName("iplantc_logo"); //$NON-NLS-1$
            panel.setSize(500, 143);
            panel.addListener(Events.OnClick, new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    com.google.gwt.user.client.Window.Location.assign(Constants.CLIENT.iplantHome());
                }
            });

            return panel;
        }

        private HorizontalPanel buildActionsPanel() {
            HorizontalPanel pnlActions = new HorizontalPanel();
            pnlActions.setStyleName("iplantc_header_actions"); //$NON-NLS-1$
            pnlActions.setSpacing(5);

            String username = CASCredentials.getInstance().getUsername();
            String firstName = CASCredentials.getInstance().getFirstName();
            String lastName = CASCredentials.getInstance().getLastName();
            String menuLabel = (firstName != null && lastName != null) ? firstName + " " + lastName
                    : username;
            pnlActions.add(buildActionsMenu(menuLabel, buildUserMenu()));
            // pnlActions.add(buildActionsMenu(I18N.DISPLAY.help(), buildHelpMenu()));

            return pnlActions;
        }

        private Menu buildUserMenu() {
            Menu userMenu = buildMenu();
            userMenu.add(new CustomHyperlink(I18N.DISPLAY.logout(), new LogoutSelectionListener(),
                    I18N.DISPLAY.logoutToolTipText()));
            return userMenu;
        }

        private HorizontalPanel buildActionsMenu(String menuHeaderText, final Menu menu) {
            final HorizontalPanel ret = new HorizontalPanel();
            ret.setStyleName("iplantc_header_menu_panel"); //$NON-NLS-1$

            // build menu header text and icon
            CustomLabel menuHeader = new CustomLabel(menuHeaderText);
            menuHeader.addListener(Events.OnClick, new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    showHeaderActionsMenu(ret, menu);
                }
            });

            IconButton icon = new IconButton("iplantc_header_menu_button",
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
                    ret.addStyleName("iplantc_header_menu_selected");
                }
            });

            menu.addListener(Events.Hide, new Listener<MenuEvent>() {
                @Override
                public void handleEvent(MenuEvent be) {
                    ret.removeStyleName("iplantc_header_menu_selected");
                }
            });

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

    private class LogoutSelectionListener implements Listener<BaseEvent> {
        @Override
        public void handleEvent(BaseEvent be) {
            com.google.gwt.user.client.Window.Location.assign(GWT.getHostPageBaseURL()
                    + Constants.CLIENT.logoutUrl());

        }
    }

    /**
     * A custom label class used in header to act/style like a menus
     * 
     * @author sriram
     * 
     */
    private class CustomLabel extends Label {

        public CustomLabel(String text) {
            super(text);

            sinkBrowserEvents();
            setStyleName("iplantc_header_menu_label");
            addListeners();
        }

        private void sinkBrowserEvents() {
            sinkEvents(Events.OnClick.getEventCode());
            sinkEvents(Events.OnMouseOver.getEventCode());
            sinkEvents(Events.OnMouseOut.getEventCode());
        }

        private void addListeners() {
            addListener(Events.OnMouseOver, new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    addStyleName("iplantc_header_menu_label_hover");
                }
            });

            addListener(Events.OnMouseOut, new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    removeStyleName("iplantc_header_menu_label_hover");
                }
            });

        }
    }

    /**
     * A Hyperlink class that can be initialized with a click listener and that adds or removes it's own
     * style on mouse over or out events.
     * 
     * @author psarando
     * 
     */
    private class CustomHyperlink extends Hyperlink {

        public CustomHyperlink(String text, Listener<BaseEvent> clickListener, String toolTipText) {
            super(text, "iplantc_hyperlink");
            setToolTipText(toolTipText);
            initListeners(clickListener);
        }

        private void setToolTipText(String toolTipText) {
            if (toolTipText != null && !toolTipText.isEmpty()) {
                setToolTip(toolTipText);
            }
        }

        protected void initListeners(Listener<BaseEvent> clickListener) {
            if (clickListener != null) {
                addListener(Events.OnClick, clickListener);
            }

            addListener(Events.OnMouseOver, new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    addStyleName("iplantc_header_hyperlink_hover");
                }
            });

            addListener(Events.OnMouseOut, new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    removeStyleName("iplantc_header_hyperlink_hover");
                }
            });
        }
    }
}
