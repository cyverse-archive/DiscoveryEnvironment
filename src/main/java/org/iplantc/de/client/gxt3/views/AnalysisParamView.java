package org.iplantc.de.client.gxt3.views;

import org.iplantc.de.client.gxt3.model.AnalysisParameter;
import org.iplantc.de.client.gxt3.views.AnalysesView.Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class AnalysisParamView implements IsWidget {

    private static AnalysisParamViewUiBinder uiBinder = GWT.create(AnalysisParamViewUiBinder.class);

    @UiTemplate("AnalysisParamView.ui.xml")
    interface AnalysisParamViewUiBinder extends UiBinder<Widget, AnalysisParamView> {
    }

    @UiField(provided = true)
    final ListStore<AnalysisParameter> listStore;
    @UiField(provided = true)
    final ColumnModel<AnalysisParameter> cm;

    @UiField
    Grid<AnalysisParameter> grid;

    @UiField
    FramedPanel mainPanel;

    @UiField
    BorderLayoutContainer con;

    @UiField
    ToolBar menuToolBar;

    @UiField
    BorderLayoutData northData;

    @UiField
    Dialog dialog;

    private final Widget widget;
    private Presenter presenter;

    public AnalysisParamView(ListStore<AnalysisParameter> listStore, ColumnModel<AnalysisParameter> cm) {
        this.cm = cm;
        this.listStore = listStore;
        this.widget = uiBinder.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;

    }

    public void show() {
        dialog.show();
    }

}
