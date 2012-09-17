package org.iplantc.admin.belphegor.client;

import org.iplantc.admin.belphegor.client.services.impl.AppTemplateAdminServiceFacade;
import org.iplantc.core.uiapplications.client.services.AppTemplateServiceFacade;

import com.google.gwt.core.client.GWT;

public class Services {
    public static final AppTemplateAdminServiceFacade ADMIN_TEMPLATE_SERVICE = new AppTemplateAdminServiceFacade();
    public static final AppTemplateServiceFacade TEMPLATE_SERVICE = GWT
            .create(AppTemplateServiceFacade.class);

}
