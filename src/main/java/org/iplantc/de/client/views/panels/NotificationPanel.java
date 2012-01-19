package org.iplantc.de.client.views.panels;

import static org.iplantc.de.client.models.Notification.PROP_CATEGORY;
import static org.iplantc.de.client.models.Notification.PROP_MESSAGE;
import static org.iplantc.de.client.models.Notification.PROP_TIMESTAMP;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.iplantc.core.client.widgets.Hyperlink;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.images.Resources;
import org.iplantc.de.client.models.Notification;
import org.iplantc.de.client.utils.AnalysisViewContextExecutor;
import org.iplantc.de.client.utils.NotificationManager;
import org.iplantc.de.client.utils.NotificationManager.Category;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreFilter;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Displays user notifications.
 * 
 * @author sriram, hariolf
 */
public class NotificationPanel extends ContentPanel {
    // column ids
    private static final String MENU = "menu"; //$NON-NLS-1$

    private Grid<Notification> grdNotifications;
    private CategoryFilter categoryFilter;
    private ColumnModel columnModel; // an instance of a column model configured for this
    // window
    private SimpleComboBox<Category> dropdown;
    private CheckBoxSelectionModel<Notification> checkBoxModel;

    private Button moreActionsButton;
    private Menu rowMenu; // the drop-down menu in the last grid column

    // the menus on the "more actions" button and the row menus.
    // they are separate so opening the row menu doesn't highlight the "more actions"
    // button.
    private Menu menuMoreActionsNoSelection;
    private Menu menuMoreActionsWithContext;
    private Menu menuMoreActionsNoContext;
    private Menu menuRowWithContext;
    private Menu menuRowNoContext;

    private AnalysisViewContextExecutor analysisContextExecutor;

    /**
     * Creates a new NotificationPanel.
     */
    public NotificationPanel() {
        init();
    }

    /**
     * Initialize all components used by the window.
     */
    private void init() {
        buildMenus();
        buildNotificationGrid();
        buildMoreActionsButton();
        compose();

        analysisContextExecutor = new AnalysisViewContextExecutor();

        setMenu();
    }

    private void buildNotificationGrid() {
        NotificationManager notiMgr = NotificationManager.getInstance();

        buildColumnModel();

        grdNotifications = new Grid<Notification>(notiMgr.getNotifications(), columnModel);
        grdNotifications.addStyleName("menu-row-over"); //$NON-NLS-1$
        grdNotifications.setAutoExpandColumn(PROP_MESSAGE);
        grdNotifications.setAutoExpandMax(2048);

        grdNotifications.setStripeRows(true);

        // enable multi select of checkboxes and select all / unselect all
        grdNotifications.addPlugin(checkBoxModel);
        grdNotifications.setSelectionModel(checkBoxModel);

        GridView view = new GridView();
        view.setForceFit(true);
        view.setEmptyText(I18N.DISPLAY.noNotifications());
        grdNotifications.setView(view);

        addGridEventListeners();
    }

    private void buildMoreActionsButton() {
        moreActionsButton = new Button(I18N.DISPLAY.moreActions());
        moreActionsButton.setId("idMoreActionsBtn"); //$NON-NLS-1$
        moreActionsButton.setMenu(menuMoreActionsNoSelection);
    }

    private void compose() {
        setHeaderVisible(false);
        setLayout(new FitLayout());
        add(grdNotifications);

        setTopComponent(buildButtonBar());
        setBottomComponent(buildStatusPanel());
    }

    /**
     * Configure and return a toolbar include all necessary buttons.
     * 
     * @return a configure instance of a toolbar ready to be rendered.
     */
    private ToolBar buildButtonBar() {
        ToolBar ret = new ToolBar();

        // TODO temporarily disable filtering until more categories are added.
        // ret.add(new Label(I18N.CONSTANT.filterBy()));
        // ret.add(buildFilterDropdown());
        ret.add(new FillToolItem());
        ret.add(moreActionsButton);

        return ret;
    }

    /**
     * Instantiate, configure, and return the appropriate column model.
     * 
     * A column model describes the columns and includes things like the name of the column, the width,
     * and the column header.
     * 
     * This method sets the instance variables columnModel and checkBoxModel.
     */
    private void buildColumnModel() {
        List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

        checkBoxModel = new CheckBoxSelectionModel<Notification>() {
            @Override
            protected void handleMouseClick(GridEvent<Notification> event) {
                super.handleMouseClick(event);

                Grid<Notification> grid = event.getGrid();
                // Show the actions menu if the menu grid column was clicked.
                String colId = grid.getColumnModel().getColumnId(event.getColIndex());
                if (colId != null && colId.equals("menu")) { //$NON-NLS-1$
                    // if the row clicked is not selected, then select it
                    Notification notifi = listStore.getAt(event.getRowIndex());
                    if (notifi != null && !isSelected(notifi)) {
                        select(notifi, true);
                    }

                    // show the menu just under the row and column clicked.
                    Element target = event.getTarget();
                    showMenu(target.getAbsoluteRight() - 10, target.getAbsoluteBottom());
                }

            }

            private void showMenu(int x, int y) {
                rowMenu.showAt(x, y);
            }
        };
        checkBoxModel.setSelectionMode(SelectionMode.SIMPLE);
        ColumnConfig colCheckBox = checkBoxModel.getColumn();
        colCheckBox.setAlignment(HorizontalAlignment.CENTER);
        configs.add(colCheckBox);

        ColumnConfig colCategory = new ColumnConfig(PROP_CATEGORY, PROP_CATEGORY, 100);
        colCategory.setHeader(I18N.CONSTANT.category());
        configs.add(colCategory);

        ColumnConfig colMessage = new ColumnConfig(PROP_MESSAGE, PROP_MESSAGE, 420);
        colMessage.setHeader(I18N.DISPLAY.messagesGridHeader());
        colMessage.setRenderer(new NameCellRenderer());
        configs.add(colMessage);

        ColumnConfig colTimestamp = new ColumnConfig(PROP_TIMESTAMP, PROP_TIMESTAMP, 170);
        colTimestamp.setHeader(I18N.DISPLAY.createdDateGridHeader());
        colTimestamp.setDateTimeFormat(DateTimeFormat
                .getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM));
        configs.add(colTimestamp);

        ColumnConfig colMenu = new ColumnConfig(MENU, "", 25); //$NON-NLS-1$
        // colMenu.setRenderer(new MenuCellRenderer());
        configs.add(colMenu);

        columnModel = new ColumnModel(configs);
    }

    /**
     * Creates the different versions of the "more actions" and the row menu, and assigns them to
     * instance variables.
     */
    private void buildMenus() {
        MenuItem noSelectionItem = new MenuItem(I18N.DISPLAY.selectItems());
        noSelectionItem.setEnabled(false);
        menuMoreActionsNoSelection = new Menu();
        menuMoreActionsNoSelection.add(noSelectionItem);

        menuMoreActionsWithContext = new Menu();
        menuMoreActionsWithContext.add(createViewItem());
        menuMoreActionsWithContext.add(createDeleteItem());

        menuMoreActionsNoContext = new Menu();
        menuMoreActionsNoContext.add(createDeleteItem());

        menuRowWithContext = new Menu();
        menuRowWithContext.add(createViewItem());
        menuRowWithContext.add(createDeleteItem());

        menuRowNoContext = new Menu();
        menuRowNoContext.add(createDeleteItem());
    }

    private MenuItem createViewItem() {
        return new MenuItem(I18N.DISPLAY.view(), AbstractImagePrototype.create(Resources.ICONS
                .fileView()), new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                viewSelected();
            }
        });
    }

    private MenuItem createDeleteItem() {
        return new MenuItem(I18N.DISPLAY.delete(), AbstractImagePrototype.create(Resources.ICONS
                .cancel()), new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(MenuEvent ce) {
                deleteSelected();
            }
        });
    }

    /**
     * View all selected notifications.
     */
    private void viewSelected() {
        String contextAnalysis = null;

        for (Notification notification : checkBoxModel.getSelectedItems()) {
            NotificationManager.Category category = notification.getCategory();

            // did we get a category?
            if (category != null) {
                String context = notification.getContext();

                // did we get a context to execute?
                if (context != null) {
                    if (category == NotificationManager.Category.ANALYSIS) {
                        // we only add the first analysis context
                        if (contextAnalysis == null) {
                            contextAnalysis = context;
                        }
                    }
                }
            }
        }

        // do we have an analysis context?
        if (contextAnalysis != null) {
            analysisContextExecutor.execute(contextAnalysis);
        }
    }

    /** View a notification */
    private void view(Notification notification) {
        if (notification != null) {
            NotificationManager.Category category = notification.getCategory();

            // did we get a category?
            if (category != null) {
                String context = notification.getContext();

                // did we get a context to execute?
                if (context != null) {
                    if (category == NotificationManager.Category.ANALYSIS) {
                        analysisContextExecutor.execute(context);
                    }
                }
            }
        }
    }

    /**
     * Remove selected notifications.
     */
    private void deleteSelected() {
        NotificationManager notiMgr = NotificationManager.getInstance();
        List<Notification> notifications = new ArrayList<Notification>();

        for (Notification notification : checkBoxModel.getSelectedItems()) {
            notifications.add(notification);
        }

        notiMgr.delete(notifications);
    }

    private Component buildStatusPanel() {
        final Label countLabel = new Label();
        countLabel.setText(getNotificationCountLabelText());

        grdNotifications.getStore().addStoreListener(new StoreListener<Notification>() {
            @Override
            public void storeAdd(StoreEvent<Notification> se) {
                updateLabel();
            }

            @Override
            public void storeRemove(StoreEvent<Notification> se) {
                updateLabel();
            }

            @Override
            public void storeClear(StoreEvent<Notification> se) {
                updateLabel();
            }

            @Override
            public void storeFilter(StoreEvent<Notification> se) {
                updateLabel();
            }

            void updateLabel() {
                countLabel.setText(getNotificationCountLabelText());
            }
        });

        // Use ToolBar rather than LayoutContainer so fonts and spacing are consistent
        // with the rest of the UI
        ToolBar statusPanel = new ToolBar();
        statusPanel.add(new FillToolItem());
        statusPanel.add(countLabel);
        return statusPanel;
    }

    private String getNotificationCountLabelText() {
        int count = grdNotifications.getStore().getCount();
        String unit = count == 1 ? I18N.CONSTANT.notificationCountOne() : I18N.CONSTANT
                .notificationCountMultiple();
        return count + " " + unit; //$NON-NLS-1$
    }

    private SimpleComboBox<Category> buildFilterDropdown() {
        dropdown = new SimpleComboBox<Category>();
        dropdown.add(Category.ALL);
        dropdown.add(Category.ANALYSIS);
        dropdown.setValue(dropdown.getStore().getModels().get(0)); // select first item
        dropdown.setTriggerAction(TriggerAction.ALL); // Always show all categories in the
        // drop-down
        dropdown.setEditable(false);

        // remove existing filter so it doesn't interfere with the new filter
        List<StoreFilter<Notification>> filters = grdNotifications.getStore().getFilters();
        if (filters != null) {
            for (Iterator<StoreFilter<Notification>> iter = filters.iterator(); iter.hasNext();) {
                iter.next();
                iter.remove();
            }
        }
        categoryFilter = new CategoryFilter();
        grdNotifications.getStore().addFilter(categoryFilter);

        dropdown.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<Category>>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<SimpleComboValue<Category>> se) {
                SimpleComboValue<Category> selectedItem = se.getSelectedItem();
                Category category = selectedItem == null ? Category.ALL : selectedItem.getValue();
                categoryFilter.setCategory(category);
                grdNotifications.getStore().applyFilters(null);

                // The following is a workaround for the header checkbox not being
                // checked/unchecked correctly when filtering changes
                List<Notification> selectedItems = grdNotifications.getSelectionModel()
                        .getSelectedItems();
                ListStore<Notification> origStore = grdNotifications.getStore();
                grdNotifications.reconfigure(origStore, columnModel);
                grdNotifications.getSelectionModel().setSelection(selectedItems);
            }
        });

        return dropdown;
    }

    /**
     * Filters the list of notifications by a given Category.
     * 
     * @param category
     */
    public void filterBy(Category category) {
        // TODO temporarily disable filtering until more categories are added.
        // dropdown.setValue(dropdown.findModel(category));
    }

    private void addGridEventListeners() {
        grdNotifications.getSelectionModel().addListener(Events.SelectionChange,
                new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        setMenu();
                    }
                });

    }

    private void setMenu() {
        int numSelected = grdNotifications.getSelectionModel().getSelectedItems().size();
        if (numSelected == 0) {
            moreActionsButton.setMenu(menuMoreActionsNoSelection);
            // don't set rowMenu because there are no rows
        } else if (hasContext()) {
            moreActionsButton.setMenu(menuMoreActionsWithContext);
            rowMenu = menuRowWithContext;
        } else {
            moreActionsButton.setMenu(menuMoreActionsNoContext);
            rowMenu = menuRowNoContext;
        }
    }

    /**
     * Returns true if at least one selected notification has a context.
     * 
     * @return
     */
    private boolean hasContext() {
        List<Notification> selectedItems = grdNotifications.getSelectionModel().getSelectedItems();
        for (Notification notification : selectedItems) {
            String context = notification.getContext();
            if (context != null && !context.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private class CategoryFilter implements StoreFilter<Notification> {
        private Category selectedCategory = Category.ALL;

        private void setCategory(Category category) {
            selectedCategory = category;
        }

        @Override
        public boolean select(Store<Notification> store, Notification parent, Notification item,
                String property) {
            return Category.ALL.equals(selectedCategory) || selectedCategory == null
                    || selectedCategory.equals(item.getCategory());
        }
    };

    /**
     * A custom renderer that renders notification messages as hyperlinks
     * 
     * @author sriram, hariolf
     * 
     */
    private class NameCellRenderer implements GridCellRenderer<Notification> {

        @Override
        public Object render(final Notification notification, String property, ColumnData config,
                int rowIndex, int colIndex, ListStore<Notification> store, Grid<Notification> grid) {
            if (notification == null) {
                return null;
            }
            String message = notification.getMessage();

            String context = notification.getContext();
            Component renderer;
            // show a hyperlink if there is a context; if no context, show a label
            if (context == null || context.isEmpty()) {
                renderer = new Label(message);
            } else {
                Hyperlink link = new Hyperlink(message, "de_notification_hyperlink"); //$NON-NLS-1$

                link.addListener(Events.OnClick, new Listener<BaseEvent>() {

                    @Override
                    public void handleEvent(BaseEvent be) {
                        view(notification);
                    }
                });
                renderer = link;
            }

            renderer.setToolTip(message);
            return renderer;
        }
    }
}
