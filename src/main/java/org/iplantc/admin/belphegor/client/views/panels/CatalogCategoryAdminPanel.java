package org.iplantc.admin.belphegor.client.views.panels;

import org.iplantc.admin.belphegor.client.services.AppTemplateAdminServiceFacade;
import org.iplantc.core.uiapplications.client.store.AnalysisToolGroupStoreWrapper;
import org.iplantc.core.uiapplications.client.views.panels.AbstractCatalogCategoryPanel;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class CatalogCategoryAdminPanel extends AbstractCatalogCategoryPanel {

    public CatalogCategoryAdminPanel() {
        getData();
    }

    private void getData() {
        AppTemplateAdminServiceFacade facade = new AppTemplateAdminServiceFacade();

        facade.getAnalysisCategories(UserInfo.getInstance().getWorkspaceId(),
                new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        AnalysisToolGroupStoreWrapper wrapper = new AnalysisToolGroupStoreWrapper();
                        wrapper.updateWrapper(result);
                        seed(wrapper.getStore());
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(caught);
                    }
                });
    }

}
