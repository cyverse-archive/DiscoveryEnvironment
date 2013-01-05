/**
 * 
 */
package org.iplantc.de.client.analysis.views;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.de.client.I18N;
import org.iplantc.de.client.analysis.models.Analysis;
import org.iplantc.de.client.desktop.widget.DEPagingToolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.RowExpander;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * @author sriram
 * 
 */
public class AnalysesViewImpl implements AnalysesView {

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiTemplate("AnalysesView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, AnalysesViewImpl> {
    }

    @UiField(provided = true)
    final ListStore<Analysis> listStore;

    @UiField(provided = true)
    final ColumnModel<Analysis> cm;

    @UiField
    GridView<Analysis> gridView;

    @UiField
    Grid<Analysis> grid;

    @UiField
    FramedPanel mainPanel;

    @UiField
    BorderLayoutContainer con;

    @UiField
    BorderLayoutData northData;

    @UiField
    DEPagingToolbar toolBar;

    private final Widget widget;

    private Presenter presenter;

    private ArrayList<HandlerRegistration> handlers;

    public AnalysesViewImpl(ListStore<Analysis> listStore, ColumnModel<Analysis> cm,
            GridSelectionModel<Analysis> checkBoxModel, RowExpander<Analysis> expander) {
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
        gridView.setEmptyText(I18N.DISPLAY.noAnalyses());
        expander.initPlugin(grid);
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

    @Override
    public ListStore<Analysis> getListStore() {
        return listStore;
    }

    @Override
    public void setLoader(PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>> loader) {
        grid.setLoader(loader);
        toolBar.bind(loader);
        grid.getLoader().load();

    }

    @Override
    public TextButton getRefreshButton() {
        return toolBar.getRefreshButton();
    }

}
