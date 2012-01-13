package org.iplantc.de.client.views.panels;

import java.util.Arrays;
import java.util.List;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.ManageDataRefreshEvent;
import org.iplantc.de.client.images.Resources;
import org.iplantc.de.client.utils.PanelHelper;
import org.iplantc.de.client.views.MyDataGrid;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class DataMainToolBar extends ToolBar {
    private final String tag;
    private final DataContainer container;
    private TextField<String> filterField;
    private ColumnModel columnModel;
    private Button btnRefresh;
    // private ToggleButton btnToggleDisplay;
    private SimpleComboBox<String> cbxToggleDisplay;
    private GridView gridView;

    /**
     * Type of views supported by Manage Data Grid
     * 
     * @author sriram
     * 
     */
    public enum VIEWS {
        BASIC_VIEW(I18N.DISPLAY.basicView(), 0), DETAILED_VIEW(I18N.DISPLAY.detailView(), 1);

        private String displayText;
        private int index;

        private VIEWS(String displaytext, int index) {
            this.displayText = displaytext;
            this.index = index;
        }

        /**
         * Returns a string that identifies the EXECUTION_STATUS.
         * 
         * @return
         */
        public String getTypeString() {
            return toString().toLowerCase();
        }



        @Override
        public String toString() {
            return displayText;
        }

        public int getIndex() {
            return index;
        }
    }

    public DataMainToolBar(final String tag, final DataContainer container) {
        this.tag = tag;
        this.container = container;
        setSpacing(4);
        addButtons();
    }

    private void addButtons() {
        btnRefresh = buildRefreshButton();
        add(btnRefresh);
        filterField = buildFilterField();
        add(filterField);
        add(new FillToolItem());
    }

    private Button buildRefreshButton() {
        Button refresh = PanelHelper.buildButton("idRefresh", I18N.DISPLAY.refresh(), //$NON-NLS-1$
                new RereshButtonListener());
        refresh.setIcon(AbstractImagePrototype.create(Resources.ICONS.refresh()));
        return refresh;
    }

    /**
     * Builds a text field for filtering items displayed in the data container.
     */
    private TextField<String> buildFilterField() {
        TextField<String> filter = new TextField<String>() {
            @Override
            public void onKeyUp(FieldEvent fe) {
                String filter = getValue();
                if (filter != null && !filter.isEmpty()) {
                    container.getDataStore().filter("name", filter); //$NON-NLS-1$
                } else {
                    container.getDataStore().clearFilters();
                }

            }
        };

        filter.setEmptyText(I18N.DISPLAY.filterDataList());

        return filter;
    }

    private void buildViewSelectionField() {
        cbxToggleDisplay = new SimpleComboBox<String>();
        cbxToggleDisplay.setAllowBlank(false);
        cbxToggleDisplay.setTriggerAction(TriggerAction.ALL);
        cbxToggleDisplay.setFireChangeEventOnSetValue(true);
        cbxToggleDisplay.add(VIEWS.BASIC_VIEW.toString());
        cbxToggleDisplay.add(VIEWS.DETAILED_VIEW.toString());
        cbxToggleDisplay.select(VIEWS.BASIC_VIEW.index);
        cbxToggleDisplay.setSimpleValue(VIEWS.BASIC_VIEW.toString());
        cbxToggleDisplay.setEditable(false);
    }

    // set force fit to true for basic view else force fit to false
    private void setForceFit() {
        gridView.setForceFit(cbxToggleDisplay.getSimpleValue().toString()
                .equals(VIEWS.DETAILED_VIEW.toString()) ? false : true);
        gridView.refresh(true);
    }

    // true - detailed view false - basic view
    private boolean getViewToDisplay() {
        return cbxToggleDisplay.getSimpleValue().toString().equals(VIEWS.DETAILED_VIEW.toString()) ? true
                : false;
    }

    /**
     * Clears the toolbar's filtering text field.
     */
    public void clearFilters() {
        filterField.clear();
        container.getDataStore().clearFilters();
    }

    /**
     * Adds a toggle button to the toolbar that will show or hide columns in the given ColumnModel based
     * on the toggle state.
     * 
     * @param columnModel The ColumnModel containing the columns to show or hide.
     * @param gridView The GridView that needs to be refreshed after columns are hidden or un-hidden.
     */
    public void addColumnViewToggle(final ColumnModel columnModel, GridView gridView) {
        this.columnModel = columnModel;
        this.gridView = gridView;

        if (cbxToggleDisplay == null) {
            buildViewSelectionField();
            add(cbxToggleDisplay);
            addViewSelectionChangeListener(gridView);
        } else {
            setView();
        }
    }

    private void addViewSelectionChangeListener(final GridView gridView) {
        cbxToggleDisplay
                .addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<String>>() {

                    @Override
                    public void selectionChanged(SelectionChangedEvent<SimpleComboValue<String>> se) {
                        if (gridView != null) {
                            setView();
                        }

                    }
                });
    }

    private void toggleColumnDisplay(boolean showAll) {
        if (columnModel == null) {
            return;
        }

        // Always show these columns when column display is toggled.
        List<String> colsAlwaysShow = Arrays.asList(MyDataGrid.COLUMN_ID_NAME,
                MyDataGrid.COLUMN_ID_DATE_MODIFIED);

        for (String configId : colsAlwaysShow) {
            ColumnConfig config = columnModel.getColumnById(configId);

            if (config != null) {
                config.setHidden(false);
            }
        }

        // Show these columns according to showAll value.
        List<String> colsInShowAll = Arrays.asList(MyDataGrid.COLUMN_ID_DATE_CREATED,
                MyDataGrid.COLUMN_ID_SIZE);

        for (String configId : colsInShowAll) {
            ColumnConfig config = columnModel.getColumnById(configId);

            if (config != null) {
                config.setHidden(!showAll);
            }
        }
    }

    private void setView() {
        toggleColumnDisplay(getViewToDisplay());
        setForceFit();
    }

    private class RereshButtonListener extends SelectionListener<ButtonEvent> {

        @Override
        public void componentSelected(ButtonEvent ce) {
            ManageDataRefreshEvent event = new ManageDataRefreshEvent(tag, container.getCurrentPath());
            EventBus.getInstance().fireEvent(event);
        }

    }
}
