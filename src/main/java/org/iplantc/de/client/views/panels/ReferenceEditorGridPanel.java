package org.iplantc.de.client.views.panels;

import java.util.Arrays;
import java.util.List;

import org.iplantc.core.client.widgets.BoundedTextArea;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.utils.PanelHelper;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;

/**
 * A Panel with an EditorGrid for adding and removing bounded, multi-line text fields, and a toJson
 * method that builds a JSON array from the rows added to the grid.
 * 
 * @author psarando
 * 
 */
public class ReferenceEditorGridPanel extends ContentPanel {
    private static final String REFERENCE_PROPERTY = "reference"; //$NON-NLS-1$

    private EditorGrid<BaseModelData> grid;
    private Button btnDelete;
    private Button btnAdd;

    public ReferenceEditorGridPanel(int width, int height) {
        ListStore<BaseModelData> store = new ListStore<BaseModelData>();

        CheckBoxSelectionModel<BaseModelData> checkboxModel = buildCheckboxModel();

        grid = new EditorGrid<BaseModelData>(store, buildColumnModel(checkboxModel.getColumn()));
        grid.addPlugin(checkboxModel);
        grid.setSelectionModel(checkboxModel);

        grid.getView().setEmptyText(I18N.DISPLAY.noItemsToDisplay());
        grid.getView().setShowDirtyCells(false);
        grid.getView().setForceFit(true);

        grid.setHideHeaders(true);
        grid.setSize(width, height);

        setBorders(true);
        setHeaderVisible(false);

        setTopComponent(buildToolbar());
        add(grid);
    }

    /**
     * Builds a JSON array containing the strings added to the editor grid.
     * 
     * @return A JSONArray containing the text fields as JSONStrings added by the user.
     */
    public JSONArray toJson() {
        JSONArray ret = new JSONArray();

        int index = 0;
        for (BaseModelData model : grid.getStore().getModels()) {
            ret.set(index++, new JSONString(model.get(REFERENCE_PROPERTY).toString()));
        }

        return ret;
    }

    /**
     * Determines if the references in this grid are all valid. Since references are not required, then
     * an empty grid is also considered valid.
     * 
     * @return False if any reference added to the grid is invalid, true otherwise.
     */
    public boolean isValid() {
        for (BaseModelData model : grid.getStore().getModels()) {
            String value = model.get(REFERENCE_PROPERTY);
            if (value == null || value.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private ColumnModel buildColumnModel(ColumnConfig configCheckBox) {
        configCheckBox.setAlignment(HorizontalAlignment.CENTER);

        ColumnConfig config = new ColumnConfig(REFERENCE_PROPERTY, 500);
        config.setEditor(new CellEditor(buildTextArea()));
        config.setMenuDisabled(true);
        config.setSortable(false);
        config.setAlignment(HorizontalAlignment.LEFT);

        return new ColumnModel(Arrays.asList(configCheckBox, config));
    }

    private TextArea buildTextArea() {
        TextArea ret = new BoundedTextArea();

        ret.setMaxLength(255);
        ret.setAllowBlank(false);
        ret.setValidateOnBlur(true);

        return ret;
    }

    private ToolBar buildToolbar() {
        ToolBar toolBar = new ToolBar();

        buildAddButton();
        buildDeleteButton();

        toolBar.add(btnAdd);

        toolBar.add(new FillToolItem());
        toolBar.add(btnDelete);

        return toolBar;
    }

    private void buildDeleteButton() {
        btnDelete = PanelHelper.buildButton("idReferenceDeleteBtn", I18N.DISPLAY.delete(), //$NON-NLS-1$
                new DeleteSelectionListener());

        btnDelete.disable();
    }

    private void buildAddButton() {
        btnAdd = PanelHelper.buildButton("idReferenceAddBtn", I18N.DISPLAY.add(), null); //$NON-NLS-1$
        btnAdd.addSelectionListener(new AddSelectionListener());
    }

    private class DeleteSelectionListener extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            List<BaseModelData> selected = grid.getSelectionModel().getSelectedItems();
            ListStore<BaseModelData> store = grid.getStore();
            if (selected != null) {
                for (BaseModelData model : selected) {
                    store.remove(model);
                }
            }
        }

    }

    private class AddSelectionListener extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            BaseModelData row = new BaseModelData();

            ListStore<BaseModelData> store = grid.getStore();
            grid.stopEditing();

            store.insert(row, store.getCount());

            grid.startEditing(store.indexOf(row), 1);
        }
    }

    private CheckBoxSelectionModel<BaseModelData> buildCheckboxModel() {
        CheckBoxSelectionModel<BaseModelData> ret = new CheckBoxSelectionModel<BaseModelData>();
        ret.setSelectionMode(SelectionMode.SIMPLE);

        ret.addSelectionChangedListener(new SelectionChangedListener<BaseModelData>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<BaseModelData> se) {
                btnDelete.setEnabled(!se.getSelection().isEmpty());
            }
        });

        return ret;
    }
}
