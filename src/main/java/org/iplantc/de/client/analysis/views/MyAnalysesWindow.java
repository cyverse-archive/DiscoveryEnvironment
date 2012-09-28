/**
 * 
 */
package org.iplantc.de.client.analysis.views;

import java.util.LinkedList;
import java.util.List;

import org.iplantc.core.uicommons.client.models.WindowConfig;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.analysis.models.Analysis;
import org.iplantc.de.client.analysis.models.AnalysisProperties;
import org.iplantc.de.client.analysis.presenter.AnalysesPresenter;
import org.iplantc.de.client.analysis.views.AnalysesView.Presenter;
import org.iplantc.de.client.analysis.views.cells.AnalysisAppNameCell;
import org.iplantc.de.client.analysis.views.cells.AnalysisNameCell;
import org.iplantc.de.client.analysis.views.cells.AnalysisTimeStampCell;
import org.iplantc.de.client.views.windows.Gxt3IplantWindow;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.RowExpander;

/**
 * @author sriram
 * 
 */
public class MyAnalysesWindow extends Gxt3IplantWindow {

    private CheckBoxSelectionModel<Analysis> checkBoxModel;
    private RowExpander<Analysis> expander;

    public MyAnalysesWindow(String tag, WindowConfig config) {
        super(tag, config);
        setTitle(I18N.DISPLAY.analyses());
        setSize("800", "410");
        AnalysisKeyProvider provider = new AnalysisKeyProvider();
        ListStore<Analysis> listStore = new ListStore<Analysis>(provider);
        AnalysesView view = new AnalysesViewImpl(listStore, buildColumnModel(), checkBoxModel, expander);
        Presenter p = new AnalysesPresenter(view);
        p.go(this);
    }

    @SuppressWarnings("unchecked")
    private ColumnModel<Analysis> buildColumnModel() {
        AnalysisProperties props = GWT.create(AnalysisProperties.class);

        List<ColumnConfig<Analysis, ?>> configs = new LinkedList<ColumnConfig<Analysis, ?>>();

        IdentityValueProvider<Analysis> valueProvider = new IdentityValueProvider<Analysis>();
        checkBoxModel = new CheckBoxSelectionModel<Analysis>(valueProvider);
        @SuppressWarnings("rawtypes")
        ColumnConfig colCheckBox = checkBoxModel.getColumn();
        configs.add(colCheckBox);

        expander = new RowExpander<Analysis>(valueProvider, new AbstractCell<Analysis>() {

            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context, Analysis value,
                    SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<p style='margin: 5px 5px 10px'><b>Description:</b>"
                        + value.getDescription() + "</p>");

            }
        });

        configs.add(expander);

        ColumnConfig<Analysis, Analysis> name = new ColumnConfig<Analysis, Analysis>(valueProvider, 100);
        name.setHeader(I18N.DISPLAY.name());
        configs.add(name);
        name.setMenuDisabled(true);
        name.setCell(new AnalysisNameCell());

        ColumnConfig<Analysis, Analysis> app = new ColumnConfig<Analysis, Analysis>(valueProvider, 100);
        app.setHeader(I18N.DISPLAY.appName());
        configs.add(app);
        app.setMenuDisabled(true);
        app.setCell(new AnalysisAppNameCell());

        ColumnConfig<Analysis, Analysis> startdate = new ColumnConfig<Analysis, Analysis>(valueProvider,
                150);
        startdate.setCell(new AnalysisTimeStampCell());
        startdate.setHeader(I18N.DISPLAY.startDate());
        configs.add(startdate);

        ColumnConfig<Analysis, Analysis> enddate = new ColumnConfig<Analysis, Analysis>(valueProvider,
                150);
        enddate.setCell(new AnalysisTimeStampCell());
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
