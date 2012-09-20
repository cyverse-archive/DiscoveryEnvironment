/**
 * 
 */
package org.iplantc.de.client.views.windows;

import java.util.LinkedList;
import java.util.List;

import org.iplantc.core.uicommons.client.models.WindowConfig;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.gxt3.model.Analysis;
import org.iplantc.de.client.gxt3.model.AnalysisProperties;
import org.iplantc.de.client.gxt3.presenter.AnalysesPresenter;
import org.iplantc.de.client.gxt3.views.AnalysesView;
import org.iplantc.de.client.gxt3.views.AnalysesView.Presenter;
import org.iplantc.de.client.gxt3.views.AnalysesViewImpl;
import org.iplantc.de.client.gxt3.views.cells.AnalysesTimeStampCell;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONObject;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

/**
 * @author sriram
 * 
 */
public class MyAnalysesWindow3 extends Gxt3IplantWindow {

    public MyAnalysesWindow3(String tag, WindowConfig config) {
        super(tag, config);
        setSize("800", "410");
        AnalysisKeyProvider provider = new AnalysisKeyProvider();
        ListStore<Analysis> listStore = new ListStore<Analysis>(provider);
        AnalysesView view = new AnalysesViewImpl(listStore, buildColumnModel());
        Presenter p = new AnalysesPresenter(view);
        p.go(this);
    }

    @SuppressWarnings("unchecked")
    private ColumnModel<Analysis> buildColumnModel() {
        AnalysisProperties props = GWT.create(AnalysisProperties.class);

        List<ColumnConfig<Analysis, ?>> configs = new LinkedList<ColumnConfig<Analysis, ?>>();

        CheckBoxSelectionModel<Analysis> checkBoxModel = new CheckBoxSelectionModel<Analysis>(
                new IdentityValueProvider<Analysis>());
        @SuppressWarnings("rawtypes")
        ColumnConfig colCheckBox = checkBoxModel.getColumn();
        configs.add(colCheckBox);

        ColumnConfig<Analysis, String> name = new ColumnConfig<Analysis, String>(props.name(), 100);
        name.setHeader(I18N.DISPLAY.name());
        configs.add(name);
        name.setMenuDisabled(true);

        ColumnConfig<Analysis, String> app = new ColumnConfig<Analysis, String>(props.analysisName(),
                100);
        name.setHeader(I18N.DISPLAY.appName());
        configs.add(app);
        app.setMenuDisabled(true);

        ColumnConfig<Analysis, Analysis> startdate = new ColumnConfig<Analysis, Analysis>(
                new IdentityValueProvider<Analysis>(), 150);
        startdate.setCell(new AnalysesTimeStampCell());
        startdate.setHeader(I18N.DISPLAY.startDate());
        configs.add(startdate);

        ColumnConfig<Analysis, Analysis> enddate = new ColumnConfig<Analysis, Analysis>(
                new IdentityValueProvider<Analysis>(), 150);
        enddate.setCell(new AnalysesTimeStampCell());
        enddate.setHeader(I18N.DISPLAY.endDate());
        configs.add(enddate);

        ColumnConfig<Analysis, String> status = new ColumnConfig<Analysis, String>(props.status(), 100);
        status.setHeader(I18N.DISPLAY.status());
        configs.add(status);
        status.setMenuDisabled(true);

        return new ColumnModel<Analysis>(configs);

    }

    @Override
    public JSONObject getWindowState() {
        // TODO Auto-generated method stub
        return null;
    }

    private class AnalysisKeyProvider implements ModelKeyProvider<Analysis> {

        @Override
        public String getKey(Analysis item) {
            return item.getId();
        }

    }

}
