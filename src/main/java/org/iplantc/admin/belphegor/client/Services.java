package org.iplantc.admin.belphegor.client;

import org.iplantc.admin.belphegor.client.services.impl.AppTemplateAdminServiceFacade;
import org.iplantc.core.uiapplications.client.services.AppServiceFacade;

import com.google.gwt.core.client.GWT;

public class Services {
    public static final AppTemplateAdminServiceFacade ADMIN_TEMPLATE_SERVICE = new AppTemplateAdminServiceFacade();
    public static final AppServiceFacade TEMPLATE_SERVICE = GWT
            .create(AppServiceFacade.class);

}
