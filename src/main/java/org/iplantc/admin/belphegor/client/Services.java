package org.iplantc.admin.belphegor.client;

import org.iplantc.admin.belphegor.client.services.impl.AppAdminServiceFacade;
import org.iplantc.core.uiapps.client.services.AppServiceFacade;

import com.google.gwt.core.client.GWT;

public class Services {
    public static final AppAdminServiceFacade ADMIN_APP_SERVICE = new AppAdminServiceFacade();
    public static final AppServiceFacade TEMPLATE_SERVICE = GWT
            .create(AppServiceFacade.class);

}
