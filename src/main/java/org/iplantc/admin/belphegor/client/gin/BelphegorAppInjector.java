package org.iplantc.admin.belphegor.client.gin;

import org.iplantc.admin.belphegor.client.apps.presenter.BelphegorAppsViewPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(BelphegorAppsGinModule.class)
public interface BelphegorAppInjector extends Ginjector {

    public static final BelphegorAppInjector INSTANCE = GWT.create(BelphegorAppInjector.class);

    public BelphegorAppsViewPresenter getAppsViewPresenter();
}
