package org.iplantc.de.client.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iplantc.core.client.widgets.Hyperlink;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.util.CommonComparator;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.dispatchers.ViewerWindowDispatcher;
import org.iplantc.de.client.events.disk.mgmt.DiskResourceSelectedEvent;
import org.iplantc.de.client.events.disk.mgmt.DiskResourceSelectedEventHandler;
import org.iplantc.de.client.models.ClientDataModel;
import org.iplantc.de.client.utils.DataUtils;
import org.iplantc.de.client.views.panels.DataPreviewPanel;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.filters.GridFilters;
import com.extjs.gxt.ui.client.widget.grid.filters.StringFilter;
import com.extjs.gxt.ui.client.widget.tips.ToolTip;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;

/**
 * A grid that displays users files and folders. Provides floating menus with support for delete, rename,
 * view
 * 
 * @author sriram
 * 
 */
public class MyDataGrid extends Grid<DiskResource> {
    public static final String COLUMN_ID_NAME = DiskResource.NAME;
    public static final String COLUMN_ID_DATE_MODIFIED = DiskResource.DATE_MODIFIED;
    public static final String COLUMN_ID_DATE_CREATED = DiskResource.DATE_CREATED;
    public static final String COLUMN_ID_SIZE = File.SIZE;
    public static final String COLUMN_ID_MENU = "menu"; //$NON-NLS-1$

    protected ClientDataModel controller;
    protected ArrayList<HandlerRegistration> handlers;
    protected final static CheckBoxSelectionModel<DiskResource> sm = new DataGridSelectionModel();
    protected String callertag;
    protected String currentFolderId;

    private final DataActionsMenu menuActions;

    /**
     * Create a new MyDataGrid
     * 
     * 
     * @param store store to be used by the grid
     * @param colModel column model describing the columns in the grid
     * @param currentFolderId id of the current folder to be displayed
     * @param callertag the caller tag
     */
    private MyDataGrid(ListStore<DiskResource> store, ColumnModel colModel, String currentFolderId,
            final String callertag) {
        super(store, colModel);

        menuActions = new DataActionsMenu(callertag);
        this.callertag = callertag;
        this.currentFolderId = currentFolderId;

        init();
        registerHandlers();
    }

    /**
     * Initialize the grid's properties
     */
    protected void init() {
        setBorders(true);
        setHeight(260);
        setAutoExpandColumn(COLUMN_ID_NAME);
        setAutoExpandMin(100);

        getView().setEmptyText(I18N.DISPLAY.selectFolderToViewContents());
        getView().setShowDirtyCells(false);
        getView().setForceFit(true);
    }

    /**
     * Handle click event of grid row
     * 
     * @param dr disk resource on which the click was made
     * @param tag caller tag
     */
    protected void handleRowClick(final DiskResource dr, final String tag) {

        if (dr instanceof File && this.callertag.equals(tag)) {
            List<DiskResource> resources = new ArrayList<DiskResource>();
            resources.add(dr);
            if (DataUtils.isViewable(resources)) {
                List<String> contexts = new ArrayList<String>();
                contexts.add(dr.getId());

                ViewerWindowDispatcher dispatcher = new ViewerWindowDispatcher();
                dispatcher.launchViewerWindow(contexts, false);
            } else {
                MessageBox.alert(I18N.DISPLAY.permissionErrorTitle(),
                        I18N.DISPLAY.permissionErrorMessage(), null);
            }
        }
    }

    /**
     * Show the Actions menu at the given absolute x, y position. The items displayed in the menu will
     * depend on the resources selected in the grid, according to DataUtils.getSupportedActions.
     * 
     * @param x
     * @param y
     * 
     * @see org.iplantc.de.client.utils.DataUtils
     */
    public void showMenu(int x, int y) {
        menuActions.showAt(x, y);
    }

    /**
     * Create the column model for the tree grid.
     * 
     * @return an instance of ColumnModel representing the columns visible in a grid
     */
    protected static ColumnModel buildColumnModel(String tag) {
        // build column configs and add them to a list for the ColumnModel.
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        ColumnConfig name = new ColumnConfig(COLUMN_ID_NAME, I18N.DISPLAY.name(), 235);
        name.setRenderer(new NameCellRenderer(tag));

        ColumnConfig date = new ColumnConfig(COLUMN_ID_DATE_MODIFIED, I18N.DISPLAY.lastModified(), 150);
        date.setDateTimeFormat(DateTimeFormat
                .getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM));

        if (tag.equals(Constants.CLIENT.myDataTag())) {
            // add the checkbox as the first column of the row
            columns.add(sm.getColumn());
        }

        ColumnConfig created = new ColumnConfig(COLUMN_ID_DATE_CREATED, I18N.DISPLAY.createdDate(), 150);
        created.setDateTimeFormat(DateTimeFormat
                .getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM));
        created.setHidden(true);

        ColumnConfig size = new ColumnConfig(COLUMN_ID_SIZE, I18N.DISPLAY.size(), 100);
        size.setRenderer(new SizeCellRenderer());

        ColumnConfig menu = new ColumnConfig(COLUMN_ID_MENU, "", 25); //$NON-NLS-1$
        menu.setSortable(false);
        menu.setMenuDisabled(true);
        menu.setFixed(true);

        columns.addAll(Arrays.asList(name, date, created, size, menu));

        return new ColumnModel(columns);
    }

    private static MyDataGrid createInstanceImpl(String currentFolderId, String tag,
            ClientDataModel controller) {
        final ListStore<DiskResource> store = new ListStore<DiskResource>();
        final ColumnModel colModel = buildColumnModel(tag);

        boolean isMyDataWindow = tag.equals(Constants.CLIENT.myDataTag());

        MyDataGrid ret;

        store.setStoreSorter(new DataGridStoreSorter());
        ret = new MyDataGrid(store, colModel, currentFolderId, tag);

        if (isMyDataWindow) {
            ret.setSelectionModel(sm);
            ret.addPlugin(sm);
            ret.addStyleName("menu-row-over"); //$NON-NLS-1$
        }

        ret.getView().setForceFit(false);
        ret.setAutoExpandMax(2048);

        ret.controller = controller;

        GridFilters filters = new GridFilters();
        filters.setLocal(true);
        StringFilter nameFilter = new StringFilter("name"); //$NON-NLS-1$
        filters.addFilter(nameFilter);
        ret.addPlugin(filters);

        return ret;
    }

    /**
     * Allocate default instance.
     * 
     * @return newly allocated my data grid.
     */
    public static MyDataGrid createInstance(String currentFolderId, String tag,
            ClientDataModel controller) {
        return createInstanceImpl(currentFolderId, tag, controller);
    }

    /**
     * get root folder id
     * 
     * @return id of the root folder t
     */
    public String getRootFolderId() {
        return controller.getRootFolderId();
    }

    /**
     * Free any unneeded resources.
     */
    public void cleanup() {
        menuActions.cleanup();
        removeEventHandlers();
        removeAllListeners();
    }

    private void removeEventHandlers() {
        EventBus eventbus = EventBus.getInstance();

        // unregister
        for (HandlerRegistration reg : handlers) {
            eventbus.removeHandler(reg);
        }

        // clear our list
        handlers.clear();
    }

    /**
     * register event handlers
     * 
     */
    protected void registerHandlers() {
        handlers = new ArrayList<HandlerRegistration>();
        EventBus eventbus = EventBus.getInstance();

        // disk resource selected
        handlers.add(eventbus.addHandler(DiskResourceSelectedEvent.TYPE,
                new DiskResourceSelectedEventHandler() {
                    @Override
                    public void onSelected(DiskResourceSelectedEvent event) {
                        handleRowClick(event.getResource(), event.getTag());
                    }
                }));
    }
}

/**
 * A CheckBoxSelectionModel that will preserve the current selection when the user clicks the
 * menu-column, but also adds the row where the menu was clicked to the selection.
 * 
 * @author psarando
 * 
 */
class DataGridSelectionModel extends CheckBoxSelectionModel<DiskResource> {
    public DataGridSelectionModel() {
        super();

        getColumn().setAlignment(HorizontalAlignment.CENTER);
        getColumn().setFixed(true);
    }

    @Override
    protected void handleMouseDown(GridEvent<DiskResource> event) {
        // Only handle row selection if the menu-column was not selected. Otherwise the MULTI
        // selection mode will cause the current selection to be lost when the user clicks the
        // menu-column.
        String colId = grid.getColumnModel().getColumnId(event.getColIndex());
        if (!MyDataGrid.COLUMN_ID_MENU.equals(colId)) {
            super.handleMouseDown(event);
        }
    }

    @Override
    protected void handleMouseClick(GridEvent<DiskResource> event) {
        String colId = grid.getColumnModel().getColumnId(event.getColIndex());
        if (MyDataGrid.COLUMN_ID_MENU.equals(colId) && event.getRowIndex() != -1 && !isLocked()) {
            // Show the actions menu if the menu-column was clicked.
            DiskResource resource = listStore.getAt(event.getRowIndex());
            if (resource != null && !isSelected(resource)) {
                // If the row clicked is not selected, then select it.
                select(resource, true);
            }

            // Show the menu just under the row and column clicked.
            Element target = event.getTarget();
            MyDataGrid grid = (MyDataGrid)event.getGrid();

            grid.showMenu(target.getAbsoluteRight() - 10, target.getAbsoluteBottom());
        } else {
            // Handle the row selection normally.
            super.handleMouseClick(event);
        }
    }
}

/**
 * A custom store sorter for the data grid that sorts folders ahead of files.
 * 
 * @author psarando
 * 
 */
class DataGridStoreSorter extends StoreSorter<DiskResource> {
    public DataGridStoreSorter() {
        super(new CommonComparator());
    }

    @Override
    public int compare(Store<DiskResource> store, DiskResource m1, DiskResource m2, String property) {
        if (DiskResource.NAME.equals(property)) {
            if (m1 instanceof Folder && m2 instanceof File) {
                return -1;
            }
            if (m1 instanceof File && m2 instanceof Folder) {
                return 1;
            }
        }

        return super.compare(store, m1, m2, property);
    }
}

/**
 * A custom renderer that renders folder / file names as hyperlink
 * 
 * @author sriram
 * 
 */
class NameCellRenderer implements GridCellRenderer<DiskResource> {
    private final String callertag;

    NameCellRenderer(String caller) {
        callertag = caller;
    }

    @Override
    public Object render(final DiskResource model, String property, ColumnData config, int rowIndex,
            int colIndex, ListStore<DiskResource> store, final Grid<DiskResource> grid) {
        Hyperlink link = null;

        if (model instanceof Folder) {
            link = new Hyperlink("<img src='./gxt/images/default/tree/folder.gif'/>&nbsp;" //$NON-NLS-1$
                    + model.getName(), "mydata_name"); //$NON-NLS-1$
            link.setToolTip(model.getName());
        } else {
            link = new Hyperlink("<img src='./images/file.gif'/>&nbsp;" + model.getName(), "mydata_name"); //$NON-NLS-1$ //$NON-NLS-2$
            addPreviewToolTip(link, model);
        }

        link.addListener(Events.OnClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                DiskResourceSelectedEvent e = new DiskResourceSelectedEvent(model, callertag);
                EventBus.getInstance().fireEvent(e);
            }
        });

        link.setWidth(model.getName().length());

        return link;
    }

    private void addPreviewToolTip(Component target, final DiskResource resource) {
        ToolTipConfig ttConfig = new ToolTipConfig();
        ttConfig.setShowDelay(1000);
        ttConfig.setDismissDelay(0); // never hide tool tip while mouse is still over it
        ttConfig.setAnchorToTarget(true);
        ttConfig.setTitle(I18N.DISPLAY.preview() + ": " + resource.getName()); //$NON-NLS-1$

        final LayoutContainer pnl = new LayoutContainer();
        final DataPreviewPanel previewPanel = new DataPreviewPanel();
        pnl.add(previewPanel);
        ToolTip tip = new ToolTip(target, ttConfig) {
            // overridden to populate the preview
            @Override
            protected void updateContent() {
                getHeader().setText(title);
                if (resource != null) {
                    previewPanel.update(resource);
                }
            }
        };
        tip.setWidth(312);
        tip.add(pnl);
    }
}

class SizeCellRenderer implements GridCellRenderer<DiskResource> {

    @Override
    public Object render(DiskResource model, String property, ColumnData config, int rowIndex,
            int colIndex, ListStore<DiskResource> store, Grid<DiskResource> grid) {
        if (model instanceof Folder) {
            return null;
        } else {
            File f = (File)model;
            return DataUtils.getSizeForDisplay(f.getSize());
        }
    }

}
