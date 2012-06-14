package org.iplantc.admin.belphegor.client.views.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iplantc.admin.belphegor.client.images.Resources;
import org.iplantc.admin.belphegor.client.models.JsReferenceGenome;
import org.iplantc.admin.belphegor.client.models.ReferenceGenome;
import org.iplantc.admin.belphegor.client.services.AdminServiceCallback;
import org.iplantc.admin.belphegor.client.services.ReferenceGenomesServiceFacade;
import org.iplantc.core.client.widgets.Hyperlink;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.I18N;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * 
 * A grid panel to display a list of reference genomes
 * 
 * @author sriram
 * 
 */
public class ReferenceGenomeListingPanel extends ContentPanel {

    private static final String ID_BTN_ADD = "idBtnAdd";
    private Grid<ReferenceGenome> grid;
    private ToolBar toolBar;
    private Dialog refGenomeDialog;

    public ReferenceGenomeListingPanel() {
        init();
    }

    private void init() {
        setLayout(new FitLayout());
        setSize(1024, 768);
        buildGrid();
        buildToolBar();
        getGenomes();
    }

    private void buildGrid() {
        grid = new Grid<ReferenceGenome>(new ListStore<ReferenceGenome>(), buildColumnModel());
        add(grid);
    }

    private void buildToolBar() {
        toolBar = new ToolBar();
        toolBar.add(buildAddButton());
        toolBar.add(buildFilterField());
        setTopComponent(toolBar);
    }

    private Button buildAddButton() {
        Button b = new Button(I18N.DISPLAY.add());
        b.setId(ID_BTN_ADD);
        b.setIcon(AbstractImagePrototype.create(Resources.ICONS.category()));
        b.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                showRefEditDialog(null, RefGenomeFormPanel.MODE.ADD, new AddCompleteCallback());
            }
        });
        return b;
    }

    private ColumnModel buildColumnModel() {
        ColumnConfig name = new ColumnConfig(ReferenceGenome.NAME,
                org.iplantc.admin.belphegor.client.I18N.DISPLAY.refGenName(), 250);
        name.setRenderer(new RefNameCellRenderer());
        ColumnConfig path = new ColumnConfig(ReferenceGenome.PATH,
                org.iplantc.admin.belphegor.client.I18N.DISPLAY.refGenPath(), 250);
        ColumnConfig createdon = new ColumnConfig(ReferenceGenome.CREATED_ON,
                org.iplantc.admin.belphegor.client.I18N.DISPLAY.createdOn(), 150);
        createdon.setDateTimeFormat(DateTimeFormat
                .getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM));
        ColumnConfig createdby = new ColumnConfig(ReferenceGenome.CREATED_BY,
                org.iplantc.admin.belphegor.client.I18N.DISPLAY.createdBy(), 250);
        return new ColumnModel(Arrays.asList(name, path, createdon, createdby));
    }

    private void updateRefGenome(List<ReferenceGenome> genomes) {
        ReferenceGenome genome = genomes.get(0);
        ListStore<ReferenceGenome> store = grid.getStore();
        ReferenceGenome found_genome = store.findModel(ReferenceGenome.UUID,
                genome.get(ReferenceGenome.UUID));
        store.remove(found_genome);
        store.add(genome);
        Info.display(org.iplantc.admin.belphegor.client.I18N.DISPLAY.referenceGenomes(),
                org.iplantc.admin.belphegor.client.I18N.DISPLAY.updateRefGenome());
    }

    private void closeDialog() {
        if (refGenomeDialog != null) {
            refGenomeDialog.hide();
        }
    }

    /**
     * Displays ref genome names as hyperlinks; clicking a link will allow users to edit.
     */
    public class RefNameCellRenderer implements GridCellRenderer<ReferenceGenome> {

        @Override
        public Object render(final ReferenceGenome model, String property, ColumnData config,
                int rowIndex, int colIndex, ListStore<ReferenceGenome> store, Grid<ReferenceGenome> grid) {
            String name = model.get(ReferenceGenome.NAME);
            Hyperlink link = new Hyperlink(name, "link_name"); //$NON-NLS-1$
            link.addListener(Events.OnClick, new RefNameClickHandler(model));
            link.setWidth(name.length());
            return link;
        }
    }

    private final class RefNameClickHandler implements Listener<BaseEvent> {
        private final ReferenceGenome model;

        private RefNameClickHandler(ReferenceGenome model) {
            this.model = model;
        }

        @Override
        public void handleEvent(BaseEvent be) {
            showRefEditDialog(model, RefGenomeFormPanel.MODE.EDIT, new EditCompleteCallback());
        }
    }

    private class AddCompleteCallback extends AdminServiceCallback {

        @Override
        protected void onSuccess(JSONObject result) {
            grid.getStore().add(parseResult(result));
            Info.display(org.iplantc.admin.belphegor.client.I18N.DISPLAY.referenceGenomes(),
                    org.iplantc.admin.belphegor.client.I18N.DISPLAY.addRefGenome());
            closeDialog();
        }

        @Override
        protected String getErrorMessage() {
            closeDialog();
            return org.iplantc.admin.belphegor.client.I18N.ERROR.addRefGenomeError();
        }

    }

    private class EditCompleteCallback extends AdminServiceCallback {

        @Override
        protected void onSuccess(JSONObject jsonResult) {
            updateRefGenome(parseResult(jsonResult)); //$NON-NLS-1$
            closeDialog();
        }

        @Override
        protected String getErrorMessage() {
            closeDialog();
            return org.iplantc.admin.belphegor.client.I18N.ERROR.updateRefGenomeError();
        }

    }

    private void getGenomes() {
        ReferenceGenomesServiceFacade facade = new ReferenceGenomesServiceFacade(this);
        facade.getReferenceGenomes(new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                JSONObject obj = JSONParser.parseStrict(result).isObject();
                ListStore<ReferenceGenome> store = grid.getStore();
                store.removeAll();
                store.add(parseResult(obj));
                unmask();
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                unmask();
            }
        });

    }

    private TextField<String> buildFilterField() {
        final TextField<String> filter = new TextField<String>();
        filter.setEmptyText("Filter by name");
        filter.addListener(Events.Change, new Listener<FieldEvent>() {

            @Override
            public void handleEvent(FieldEvent be) {
                filterGrid(filter.getValue());
            }
        });

        filter.addKeyListener(new KeyListener() {

            /**
             * Fires on key press.
             * 
             * @param event the component event
             */
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == 13) {
                    filterGrid(filter.getValue());
                }
            }
        });

        return filter;
    }

    private void filterGrid(String val) {
        if (val == null || val.isEmpty()) {
            grid.getStore().clearFilters();
        } else {
            grid.getStore().filter(ReferenceGenome.NAME, val);
        }
    }

    private void showRefEditDialog(ReferenceGenome model, RefGenomeFormPanel.MODE mode,
            AdminServiceCallback callaback) {
        refGenomeDialog = new Dialog();
        refGenomeDialog.setModal(true);
        RefGenomeFormPanel editPanel = new RefGenomeFormPanel(model, mode, callaback);
        editPanel.addCancelButtonSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                refGenomeDialog.hide();
            }
        });

        if (model != null) {
            refGenomeDialog.setHeading((String)model.get(ReferenceGenome.NAME));
        } else {
            refGenomeDialog.setHeading("New Untitled");
        }
        refGenomeDialog.getButtonBar().removeAll();
        refGenomeDialog.setSize(595, 400);
        refGenomeDialog.add(editPanel);
        refGenomeDialog.show();
    }

    private List<ReferenceGenome> parseResult(JSONObject obj) {
        JSONArray arr = JsonUtil.getArray(obj, "genomes");
        JsArray<JsReferenceGenome> js_genomes = JsonUtil.asArrayOf(arr.toString());
        List<ReferenceGenome> genomes = new ArrayList<ReferenceGenome>();
        for (int i = 0; i < js_genomes.length(); i++) {
            genomes.add(new ReferenceGenome(js_genomes.get(i)));
        }

        return genomes;
    }
}
