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
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
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
        setTopComponent(toolBar);
    }

    private Button buildAddButton() {
        Button b = new Button(I18N.DISPLAY.add());
        b.setId(ID_BTN_ADD);
        b.setIcon(AbstractImagePrototype.create(Resources.ICONS.category()));
        b.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                showRefEditDialog(null, RefGenomeFormPanel.MODE.ADD);
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
            showRefEditDialog(model, RefGenomeFormPanel.MODE.EDIT);
        }
    }

    private class EditCompleteCallback extends AdminServiceCallback {
        Dialog dialog;

        public EditCompleteCallback(Dialog d) {
            dialog = d;
        }

        @Override
        protected void onSuccess(JSONObject jsonResult) {
            dialog.hide();
            //updateApp(JsonUtil.getObject(jsonResult, "application")); //$NON-NLS-1$
        }

        @Override
        protected String getErrorMessage() {
            return org.iplantc.admin.belphegor.client.I18N.ERROR.updateApplicationError();
        }

    }

    private void getGenomes() {
        ReferenceGenomesServiceFacade facade = new ReferenceGenomesServiceFacade(this);
        facade.getReferenceGenomes(new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                JSONObject obj = JSONParser.parseStrict(result).isObject();
                JSONArray arr = JsonUtil.getArray(obj, "genomes");
                JsArray<JsReferenceGenome> js_genomes = JsonUtil.asArrayOf(arr.toString());
                List<ReferenceGenome> genomes = new ArrayList<ReferenceGenome>();
                for (int i = 0; i < js_genomes.length(); i++) {
                    genomes.add(new ReferenceGenome(js_genomes.get(i)));
                }

                grid.getStore().add(genomes);
                unmask();
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                unmask();
            }
        });

    }

    private void showRefEditDialog(ReferenceGenome model, RefGenomeFormPanel.MODE mode) {
        final Dialog d = new Dialog();
        RefGenomeFormPanel editPanel = new RefGenomeFormPanel(model, mode, new EditCompleteCallback(d));
        editPanel.addCancelButtonSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                d.hide();
            }
        });

        if (model != null) {
            d.setHeading((String)model.get(ReferenceGenome.NAME));
        } else {
            d.setHeading("New Untitled");
        }
        d.getButtonBar().removeAll();
        d.setSize(595, 400);
        d.add(editPanel);
        d.show();
    }
}
