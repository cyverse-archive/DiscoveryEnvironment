package org.iplantc.de.client.gxt3.views;

import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Folder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class AppsViewImpl extends Composite implements AppsView {
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiTemplate("AppsView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, AppsViewImpl> {
    }

    class KeyProvider implements ModelKeyProvider<Folder> {
        @Override
        public String getKey(Folder item) {
            return (item instanceof Folder ? "f-" : "m-") + item.getId().toString();
        }
    }

    private Presenter presenter;

    @UiField
    Tree<Folder, String> tree;

    @UiField(provided = true)
    final TreeStore<Folder> treeStore;

    @UiField
    Grid<File> grid;

    @UiField
    GridView<File> gridView;

    @UiField(provided = true)
    final ListStore<File> listStore;

    @UiField(provided = true)
    final ColumnModel<File> cm;

    @UiField
    ToolBar toolbar;

    @UiField
    BorderLayoutContainer con;

    @UiField
    ContentPanel navPanel;
    @UiField
    ContentPanel mainPanel;
    @UiField
    ContentPanel detailPanel;

    public AppsViewImpl(TreeStore<Folder> treeStore, ListStore<File> listStore, ColumnModel<File> cm) {
        // XXX Using Dependency injection, you can get global references to stores
        // treeStore = new TreeStore<Folder>(new KeyProvider());
        // listStore = new ListStore<File>(new ModelKeyProvider<File>() {
        // @Override
        // public String getKey(File item) {
        // return item.getId();
        // }
        // });
        // cm = new ColumnModel<File>(null);
        this.treeStore = treeStore;
        this.listStore = listStore;
        this.cm = cm;
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return uiBinder.createAndBindUi(this);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ListStore<File> getListStore() {
        return listStore;
    }

    @Override
    public TreeStore<Folder> getTreeStore() {
        return treeStore;
    }

    @Override
    public void setTreeLoader(final TreeLoader<Folder> treeLoader) {
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
    public void setListLoader(PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>> listLoader) {
        this.grid.setLoader(listLoader);
    }

}