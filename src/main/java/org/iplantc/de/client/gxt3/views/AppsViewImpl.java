package org.iplantc.de.client.gxt3.views;

import java.util.List;

import org.iplantc.de.client.gxt3.model.autoBean.Analysis;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.CellClickEvent.CellClickHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;

/**
 * @author jstroot
 * 
 */
public class AppsViewImpl implements AppsView {
    /**
     * FIXME CORE-2992: Add an ID to the Categories panel collapse tool to assist QA.
     */
    private static String WEST_COLLAPSE_BTN_ID = "idCategoryCollapseBtn"; //$NON-NLS-1$
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiTemplate("AppsView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, AppsViewImpl> {
    }

    private Presenter presenter;

    @UiField
    Tree<AnalysisGroup, String> tree;

    @UiField(provided = true)
    final TreeStore<AnalysisGroup> treeStore;

    @UiField
    Grid<Analysis> grid;

    @UiField
    GridView<Analysis> gridView;

    @UiField(provided = true)
    final ListStore<Analysis> listStore;

    @UiField(provided = true)
    final ColumnModel<Analysis> cm;

    @UiField
    BorderLayoutContainer con;

    @UiField
    ContentPanel navPanel;
    @UiField
    ContentPanel mainPanel;
    @UiField
    ContentPanel detailPanel;

    private final Widget widget;

    public AppsViewImpl(TreeStore<AnalysisGroup> treeStore, ListStore<Analysis> listStore,
            ColumnModel<Analysis> cm) {
        // XXX JDS Using Dependency injection, you can get global references to stores
        this.treeStore = treeStore;
        this.listStore = listStore;
        this.cm = cm;
        this.widget = uiBinder.createAndBindUi(this);
        // toolbar.add(new LiveToolItem(grid));
        grid.addCellClickHandler(new CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent arg0) {
                // TODO Auto-generated method stub

            }
        });

        grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<Analysis>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<Analysis> event) {
                if ((event.getSelection() != null) && !event.getSelection().isEmpty()) {
                    presenter.onAnalysisSelected(event.getSelection().get(0));
                }
            }
        });

        tree.getSelectionModel().addSelectionChangedHandler(
                new SelectionChangedHandler<AnalysisGroup>() {
                    @Override
                    public void onSelectionChanged(SelectionChangedEvent<AnalysisGroup> event) {
                        if ((event.getSelection() != null) && !event.getSelection().isEmpty()) {
                            presenter.onAnalysisGroupSelected(event.getSelection().get(0));
                        }
                    }
                });

    }

    @UiFactory
    public ValueProvider<AnalysisGroup, String> createValueProvider() {
        return new ValueProvider<AnalysisGroup, String>() {

            @Override
            public String getValue(AnalysisGroup object) {
                return object.getName();
            }

            @Override
            public void setValue(AnalysisGroup object, String value) {
            }

            @Override
            public String getPath() {
                return "name";
            }
        };
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ListStore<Analysis> getListStore() {
        return listStore;
    }

    @Override
    public TreeStore<AnalysisGroup> getTreeStore() {
        return treeStore;
    }

    @Override
    public void setTreeLoader(final TreeLoader<AnalysisGroup> treeLoader) {
        this.tree.setLoader(treeLoader);
    }

    @Override
    public void setMainPanelHeading(final String name) {
        mainPanel.setHeadingText(name);
    }

    @Override
    public void maskMainPanel(final String loadingMask) {
        mainPanel.mask(loadingMask);
    }

    @Override
    public void unMaskMainPanel() {
        mainPanel.unmask();
    }

    @Override
    public void setListLoader(ListLoader<ListLoadConfig, ListLoadResult<Analysis>> listLoader) {
        grid.setLoader(listLoader);
    }

    @Override
    public void selectAnalysis(String analysisId) {
        Analysis app = listStore.findModelWithKey(analysisId);
        // TODO JDS Set Heading
        if (app != null) {
            grid.getSelectionModel().select(app, true);
        }
    }

    @Override
    public void selectAnalysisGroup(String analysisGroupId) {
        AnalysisGroup ag = treeStore.findModelWithKey(analysisGroupId);
        if (ag != null) {
            tree.getSelectionModel().select(ag, true);
            // Set heading
            setMainPanelHeading(ag.getName());
        }
    }

    @Override
    public Analysis getSelectedAnalysis() {
        return grid.getSelectionModel().getSelectedItem();
    }

    @Override
    public AnalysisGroup getSelectedAnalysisGroup() {
        return tree.getSelectionModel().getSelectedItem();
    }

    @Override
    public void setAnalyses(final List<Analysis> analyses) {
        listStore.clear();
        listStore.addAll(analyses);
    }
}
