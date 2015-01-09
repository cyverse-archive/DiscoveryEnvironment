package org.iplantc.de.admin.desktop.client.gin;

import org.iplantc.de.admin.desktop.client.BelphegorResources;
import org.iplantc.de.admin.desktop.client.apps.views.AdminAppViewImpl;
import org.iplantc.de.admin.desktop.client.apps.views.AdminAppsView;
import org.iplantc.de.admin.desktop.client.apps.views.widgets.BelphegorAppsToolbarImpl;
import org.iplantc.de.admin.desktop.client.models.BelphegorAdminProperties;
import org.iplantc.de.admin.desktop.client.presenter.BelphegorPresenterImpl;
import org.iplantc.de.admin.desktop.client.refGenome.RefGenomeView;
import org.iplantc.de.admin.desktop.client.refGenome.presenter.RefGenomePresenterImpl;
import org.iplantc.de.admin.desktop.client.refGenome.service.ReferenceGenomeServiceFacade;
import org.iplantc.de.admin.desktop.client.refGenome.service.impl.ReferenceGenomeServiceFacadeImpl;
import org.iplantc.de.admin.desktop.client.refGenome.view.RefGenomeViewImpl;
import org.iplantc.de.admin.desktop.client.services.AppAdminServiceFacade;
import org.iplantc.de.admin.desktop.client.services.impl.AppAdminServiceFacadeImpl;
import org.iplantc.de.admin.desktop.client.services.impl.AppAdminUserServiceFacade;
import org.iplantc.de.admin.desktop.client.systemMessage.SystemMessageView;
import org.iplantc.de.admin.desktop.client.systemMessage.presenter.SystemMessagePresenterImpl;
import org.iplantc.de.admin.desktop.client.systemMessage.service.SystemMessageServiceFacade;
import org.iplantc.de.admin.desktop.client.systemMessage.service.impl.SystemMessageServiceFacadeImpl;
import org.iplantc.de.admin.desktop.client.systemMessage.view.SystemMessageViewImpl;
import org.iplantc.de.admin.desktop.client.toolRequest.ToolRequestView;
import org.iplantc.de.admin.desktop.client.toolRequest.presenter.ToolRequestPresenterImpl;
import org.iplantc.de.admin.desktop.client.toolRequest.service.ToolRequestServiceFacade;
import org.iplantc.de.admin.desktop.client.toolRequest.service.impl.ToolRequestServiceFacadeImpl;
import org.iplantc.de.admin.desktop.client.toolRequest.view.ToolRequestViewImpl;
import org.iplantc.de.admin.desktop.client.views.BelphegorView;
import org.iplantc.de.admin.desktop.client.views.BelphegorViewImpl;
import org.iplantc.de.apps.client.views.AppsView;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.gin.ServicesInjector;
import org.iplantc.de.client.models.DEProperties;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.services.MetadataServiceFacade;
import org.iplantc.de.client.services.SearchServiceFacade;
import org.iplantc.de.client.services.TagsServiceFacade;
import org.iplantc.de.shared.services.DiscEnvApiService;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class BelphegorAppsGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(BelphegorView.class).to(BelphegorViewImpl.class);
        bind(BelphegorView.Presenter.class).to(BelphegorPresenterImpl.class).in(Singleton.class);

        bind(AppsView.class).to(AdminAppViewImpl.class);
        bind(AdminAppsView.Toolbar.class).to(BelphegorAppsToolbarImpl.class);

        bind(RefGenomeView.class).to(RefGenomeViewImpl.class);
        bind(RefGenomeView.Presenter.class).to(RefGenomePresenterImpl.class);
        bind(ReferenceGenomeServiceFacade.class).to(ReferenceGenomeServiceFacadeImpl.class);

        bind(ToolRequestView.class).to(ToolRequestViewImpl.class);
        bind(ToolRequestView.Presenter.class).to(ToolRequestPresenterImpl.class);
        bind(ToolRequestServiceFacade.class).to(ToolRequestServiceFacadeImpl.class);

        bind(SystemMessageView.class).to(SystemMessageViewImpl.class);
        bind(SystemMessageView.Presenter.class).to(SystemMessagePresenterImpl.class);
        bind(SystemMessageServiceFacade.class).to(SystemMessageServiceFacadeImpl.class);

        bind(BelphegorResources.class).in(Singleton.class);

        bind(AppUserServiceFacade.class).to(AppAdminUserServiceFacade.class);
        bind(AppAdminServiceFacade.class).to(AppAdminServiceFacadeImpl.class);
        bind(AppServiceFacade.class).to(AppAdminServiceFacadeImpl.class);

        bind(DiscEnvApiService.class).in(Singleton.class);
    }

    @Provides @Singleton public DiskResourceServiceFacade createDiskResourceService() {
        return ServicesInjector.INSTANCE.getDiskResourceServiceFacade();
    }
    @Provides @Singleton public SearchServiceFacade createSearchServiceFacade() {
        return ServicesInjector.INSTANCE.getSearchServiceFacade();
    }

    @Provides public TagsServiceFacade createMetadataServiceFacade() {
        return ServicesInjector.INSTANCE.getMetadataService();
    }

    @Provides public MetadataServiceFacade createFileSystemMetadataServiceFacade() {
        return ServicesInjector.INSTANCE.getFileSysteMetadataServiceFacade();
    }


    @Provides
    public EventBus createEventBus(){
        return EventBus.getInstance();
    }

    @Provides
    public UserInfo createUserInfo() {
        return UserInfo.getInstance();
    }

    @Provides public UserSettings createUserSettings(){
        return UserSettings.getInstance();
    }

    @Provides
    public DEProperties createDeProps() {
        return DEProperties.getInstance();
    }

    @Provides public BelphegorAdminProperties getAdminProperties() {
        return BelphegorAdminProperties.getInstance();
    }
}
