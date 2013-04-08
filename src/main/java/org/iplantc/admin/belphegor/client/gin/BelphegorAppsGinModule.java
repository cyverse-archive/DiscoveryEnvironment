package org.iplantc.admin.belphegor.client.gin;

import org.iplantc.admin.belphegor.client.apps.views.widgets.BelphegorAppsToolbar;
import org.iplantc.admin.belphegor.client.apps.views.widgets.BelphegorAppsToolbarImpl;
import org.iplantc.core.uiapps.client.gin.AppGroupTreeProvider;
import org.iplantc.core.uiapps.client.gin.AppGroupTreeStoreProvider;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;
import org.iplantc.core.uiapps.client.views.AppsView;
import org.iplantc.core.uiapps.client.views.AppsViewImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class BelphegorAppsGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<TreeStore<AppGroup>>() {
        }).toProvider(AppGroupTreeStoreProvider.class).in(Singleton.class);

        bind(new TypeLiteral<Tree<AppGroup, String>>() {
        }).toProvider(AppGroupTreeProvider.class).in(Singleton.class);

        bind(AppsView.class).to(AppsViewImpl.class);
        bind(BelphegorAppsToolbar.class).to(BelphegorAppsToolbarImpl.class);
    }
}
