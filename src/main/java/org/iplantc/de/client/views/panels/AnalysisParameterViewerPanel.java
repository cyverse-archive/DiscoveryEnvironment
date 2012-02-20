package org.iplantc.de.client.views.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.models.AnalysisParameter;
import org.iplantc.de.client.models.JsAnalysisParameter;
import org.iplantc.de.client.services.AnalysisServiceFacade;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AnalysisParameterViewerPanel extends ContentPanel {

    private Grid<AnalysisParameter> grid;

    public AnalysisParameterViewerPanel() {
        init();
    }

    private void init() {
        setSize(400, 300);
        setLayout(new FitLayout());
        retrieveData();
        initGrid();
        add(grid);
    }

    private void retrieveData() {
        AnalysisServiceFacade facade = new AnalysisServiceFacade();
        facade.getAnalysisParams(new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                JSONObject obj = JSONParser.parseStrict(result).isObject();
                JSONArray arr = JsonUtil.getArray(obj, "parameters");
                JsArray<JsAnalysisParameter> js_params = JsonUtil.asArrayOf(arr.toString());
                List<AnalysisParameter> parameters = new ArrayList<AnalysisParameter>();
                for (int i = 0; i < js_params.length(); i++) {
                    JsAnalysisParameter jsp = js_params.get(i);
                    AnalysisParameter param = new AnalysisParameter(jsp.getId(), jsp.getName(), jsp
                            .getType(), jsp.getValue());
                    parameters.add(param);
                }

                grid.getStore().removeAll();
                grid.getStore().add(parameters);

            }

            @Override
            public void onFailure(Throwable caught) {
                System.out.println(caught.getMessage());

            }
        });

    }

    private void initGrid() {
        final ColumnModel colModel = buildColumnModel();
        grid = new Grid<AnalysisParameter>(new ListStore<AnalysisParameter>(), colModel);
    }

    private ColumnModel buildColumnModel() {
        ColumnConfig param_name = new ColumnConfig("param_name", I18N.DISPLAY.paramName(), 150); //$NON-NLS-1$
        ColumnConfig param_type = new ColumnConfig("param_type", I18N.DISPLAY.paramType(), 100); //$NON-NLS-1$
        ColumnConfig param_value = new ColumnConfig("param_value", I18N.DISPLAY.paramValue(), 150); //$NON-NLS-1$

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.addAll(Arrays.asList(param_name, param_type, param_value));

        return new ColumnModel(columns);
    }

}
