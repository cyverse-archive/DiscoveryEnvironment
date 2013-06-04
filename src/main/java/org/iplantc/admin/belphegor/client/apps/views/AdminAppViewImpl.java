package org.iplantc.admin.belphegor.client.apps.views;

import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;
import org.iplantc.core.uiapps.client.views.AppsViewImpl;

import com.google.gwt.uibinder.client.UiFactory;
import com.google.inject.Inject;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class AdminAppViewImpl extends AppsViewImpl {

    @Inject
    public AdminAppViewImpl(Tree<AppGroup, String> tree, TreeStore<AppGroup> treeStore) {
        super(tree, treeStore);

    }

    @Override
    @UiFactory
    public ColumnModel<App> createColumnModel() {
        return new BelphegorAnalysisColumnModel(this);
    }

}
