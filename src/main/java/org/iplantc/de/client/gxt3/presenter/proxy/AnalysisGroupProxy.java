package org.iplantc.de.client.gxt3.presenter.proxy;

import java.util.List;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisAutoBeanFactory;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisGroup;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisGroupList;
import org.iplantc.de.client.services.TemplateServiceFacade;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.data.client.loader.RpcProxy;

/**
 * XXX JDS Should these proxies be moved to a subpackage?
 * 
 * @author jstroot
 * 
 */
public class AnalysisGroupProxy extends RpcProxy<AnalysisGroup, List<AnalysisGroup>> {

    private final TemplateServiceFacade templateServiceFacade;
    private final UserInfo userInfo;
    private final AnalysisAutoBeanFactory factory = GWT.create(AnalysisAutoBeanFactory.class);

    public AnalysisGroupProxy(final TemplateServiceFacade templateServiceFacade, final UserInfo userInfo) {
        this.templateServiceFacade = templateServiceFacade;
        this.userInfo = userInfo;
    }

    @Override
    public void load(AnalysisGroup loadConfig, final AsyncCallback<List<AnalysisGroup>> callback) {
        templateServiceFacade.getAnalysisCategories(userInfo.getWorkspaceId(),
                new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        AutoBean<AnalysisGroupList> bean = AutoBeanCodex.decode(factory,
                                AnalysisGroupList.class, result);
                        AnalysisGroupList as = bean.as();
                        List<AnalysisGroup> groups = as.getGroups();
                        callback.onSuccess(groups);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(I18N.ERROR.analysisGroupsLoadFailure(), caught);
                    }
                });

    }

}
