/**
 * 
 */
package org.iplantc.de.client.views.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.DiskResourceMetadata;
import org.iplantc.core.uidiskresource.client.models.JsDiskResourceMetaData;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.images.Resources;
import org.iplantc.de.client.services.FolderServiceFacade;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * @author sriram
 * 
 *         A panel to display and edit DiskResource Metadata
 * 
 */
public class MetadataEditorPanel extends ContentPanel {

    private static final String ID_DELETE = "idDelete";
    private static final String ID_ADD = "idAdd";
    private DiskResource resource;
    private EditorGrid<DiskResourceMetadata> grid;
    private ToolBar toolbar;
    private CheckBoxSelectionModel<DiskResourceMetadata> sm;

    public MetadataEditorPanel(DiskResource resource) {
        this.resource = resource;
        setSize(500, 300);
        setHeaderVisible(false);
        setLayout(new FitLayout());
        initToolbar();
        retrieveMetaData();
    }

    public List<DiskResourceMetadata> getModifiedRecords() {
        return grid.getStore().getModels();
    }

    private void retrieveMetaData() {
        FolderServiceFacade facade = new FolderServiceFacade();
        facade.getMetaData(resource, new RetrieveMetadataCallback());
    }

    private void initToolbar() {
        toolbar = new ToolBar();
        setTopComponent(toolbar);
        Button add = buildButton(ID_ADD, I18N.DISPLAY.add(),
                AbstractImagePrototype.create(Resources.ICONS.add()),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        DiskResourceMetadata metadata = new DiskResourceMetadata("attr", "value", "unit");
                        if (grid != null) {
                            grid.stopEditing();
                            grid.getStore().insert(metadata, 0);
                            grid.startEditing(grid.getStore().indexOf(metadata), 0);
                        }
                    }
                });
        toolbar.add(add);

        Button delete = buildButton(ID_DELETE, I18N.DISPLAY.delete(),
                AbstractImagePrototype.create(Resources.ICONS.cancel()),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        for (DiskResourceMetadata md : sm.getSelectedItems()) {
                            grid.getStore().remove(md);
                        }
                    }
                });
        delete.setEnabled(false);
        toolbar.add(delete);
    }

    private Button buildButton(String id, String text,
            com.google.gwt.user.client.ui.AbstractImagePrototype icon,
            SelectionListener<ButtonEvent> listener) {

        Button b = new Button(text, icon, listener);
        b.setId(id);
        return b;

    }

    private void initGrid(List<DiskResourceMetadata> metadata) {
        ListStore<DiskResourceMetadata> store = new ListStore<DiskResourceMetadata>();
        store.add(metadata);
        grid = new EditorGrid<DiskResourceMetadata>(store, buildColumnModel());
        grid.setStripeRows(true);
        grid.setColumnLines(true);
        grid.setAutoExpandColumn(DiskResourceMetadata.ATTRIBUTE);
        grid.setSelectionModel(sm);
        grid.addPlugin(sm);
        grid.getSelectionModel().addSelectionChangedListener(
                new SelectionChangedListener<DiskResourceMetadata>() {

                    @Override
                    public void selectionChanged(SelectionChangedEvent<DiskResourceMetadata> se) {
                        if (se.getSelection().size() > 0) {
                            toolbar.getItemByItemId(ID_DELETE).enable();
                        } else {
                            toolbar.getItemByItemId(ID_DELETE).disable();
                        }

                    }
                });
        add(grid);
        layout();
    }

    private ColumnModel buildColumnModel() {
        // build column configs and add them to a list for the ColumnModel.
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        sm = new CheckBoxSelectionModel<DiskResourceMetadata>();

        ColumnConfig attr = new ColumnConfig(DiskResourceMetadata.ATTRIBUTE, "Attribute", 150);
        attr.setEditor(buildCellEditor(false));
        ColumnConfig val = new ColumnConfig(DiskResourceMetadata.VALUE, "Value", 150);
        val.setEditor(buildCellEditor(false));
        ColumnConfig unit = new ColumnConfig(DiskResourceMetadata.UNIT, "Unit", 75);
        unit.setEditor(buildCellEditor(true));
        columns.addAll(Arrays.asList(sm.getColumn(), attr, val, unit));
        return new ColumnModel(columns);
    }

    private CellEditor buildCellEditor(boolean allowBlank) {
        TextField<String> text = new TextField<String>();
        text.setAllowBlank(allowBlank);
        return new CellEditor(text);
    }

    private class RetrieveMetadataCallback implements AsyncCallback<String> {

        @Override
        public void onFailure(Throwable caught) {
            ErrorHandler.post(caught);
        }

        @Override
        public void onSuccess(String result) {
            JSONObject obj = JSONParser.parseStrict(result).isObject();
            JSONArray arr = obj.get("metadata").isArray();
            List<DiskResourceMetadata> metadata_list = new ArrayList<DiskResourceMetadata>();
            if (arr != null) {
                JsArray<JsDiskResourceMetaData> jsmetadata = JsonUtil.asArrayOf(arr.toString());
                for (int i = 0; i < jsmetadata.length(); i++) {
                    DiskResourceMetadata metadata = new DiskResourceMetadata(
                            jsmetadata.get(i).getAttr(), jsmetadata.get(i).getVal(), jsmetadata.get(i)
                                    .getUnit());
                    metadata_list.add(metadata);
                }
            }

            initGrid(metadata_list);

        }
    }

}
