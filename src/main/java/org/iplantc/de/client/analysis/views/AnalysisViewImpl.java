/**
 * 
 */
package org.iplantc.de.client.analysis.views;

import java.util.List;

import org.iplantc.de.client.analysis.models.Analysis;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * @author sriram
 * 
 */
public class AnalysisViewImpl implements AnalysesView {

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiTemplate("AnalysesView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, AnalysisViewImpl> {
    }

    @UiField(provided = true)
    final ListStore<Analysis> listStore;

    @UiField(provided = true)
    final ColumnModel<Analysis> cm;

    @UiField
    GridView gridView;

    @UiField
    Grid<Analysis> grid;

    @UiField
    FramedPanel mainPanel;

    @UiField
    BorderLayoutContainer con;

    @UiField
    BorderLayoutData northData;

    private final Widget widget;

    private Presenter presenter;

    public AnalysisViewImpl(ListStore<Analysis> listStore, ColumnModel<Analysis> cm,
            GridSelectionModel<Analysis> checkBoxModel) {
        this.listStore = listStore;
        this.cm = cm;
        widget = uiBinder.createAndBindUi(this);
        grid.setSelectionModel(checkBoxModel);
        grid.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
        grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<Analysis>() {

            @Override
            public void onSelectionChanged(SelectionChangedEvent<Analysis> event) {
                presenter.onAnalysesSelection(event.getSelection());
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
     */
    @Override
    public Widget asWidget() {
        return widget;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.gxt3.views.AnalysesView#setPresenter(org.iplantc.de.client.gxt3.views.
     * AnalysesView.Presenter)
     */
    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.iplantc.de.client.gxt3.views.AnalysesView#setNorthWidget(com.google.gwt.user.client.ui.IsWidget
     * )
     */
    @Override
    public void setNorthWidget(IsWidget widget) {
        con.setNorthWidget(widget, northData);
    }

    @Override
    public void loadAnalyses(List<Analysis> items) {
        listStore.clear();
        listStore.addAll(items);
    }

    @Override
    public List<Analysis> getSelectedAnalyses() {
        return grid.getSelectionModel().getSelectedItems();
    }

    @Override
    public void removeFromStore(List<Analysis> items) {
        if (items != null & items.size() > 0) {
            for (Analysis a : items) {
                grid.getStore().remove(a);
            }
        }

    }
}
