package org.iplantc.de.client.gxt3.presenter;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.de.client.DEDisplayStrings;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.gxt3.model.autoBean.Analysis;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisAutoBeanFactory;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisGroup;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisList;
import org.iplantc.de.client.gxt3.views.AppsView;
import org.iplantc.de.client.services.TemplateServiceFacade;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

class AnalysisPagedProxy extends RpcProxy<PagingLoadConfig, PagingLoadResult<Analysis>> {
    private final TemplateServiceFacade service;
    private final AppsView view;
    private final DEDisplayStrings displayStrings;
    private AnalysisGroup currentAg;

    public AnalysisPagedProxy(final TemplateServiceFacade service, final AppsView view,
            final DEDisplayStrings displayStrings) {
        this.service = service;
        this.view = view;
        this.displayStrings = displayStrings;
    }

    @Override
    public void load(PagingLoadConfig loadConfig,
            final AsyncCallback<PagingLoadResult<Analysis>> callback) {
        view.maskMainPanel(displayStrings.loadingMask());

        int limit = loadConfig.getLimit();
        final int offset = loadConfig.getOffset();
        List<? extends SortInfo> sortInfoList = loadConfig.getSortInfo();
        SortInfo sortInfo = null;
        if ((sortInfoList != null) && !sortInfoList.isEmpty()) {
            sortInfo = sortInfoList.get(0);
        }
        // TODO JDS - This type conversion won't be necessary if we can change the type of SortDir in
        // TemplateServiceFacade.getPagedAnalysis(...)
        com.extjs.gxt.ui.client.Style.SortDir sortDir = ((sortInfo != null) && sortInfo.getSortDir()
                .equals(SortDir.ASC)) ? com.extjs.gxt.ui.client.Style.SortDir.ASC
                : com.extjs.gxt.ui.client.Style.SortDir.DESC;

        service.getPagedAnalysis(currentAg.getId(), limit, sortInfo.getSortField(), offset, sortDir,
                new AsyncCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        AnalysisAutoBeanFactory factory = GWT.create(AnalysisAutoBeanFactory.class);
                        AutoBean<AnalysisList> bean = AutoBeanCodex.decode(factory, AnalysisList.class,
                                result);

                        // Get Total count of paged call
                        int total = bean.as().getAnalyses().size();
                        Number jsonTotal = JsonUtil.getNumber(JsonUtil.getObject(result), "total");
                        if (jsonTotal != null) {
                            total = jsonTotal.intValue();
                        }

                        PagingLoadResult<Analysis> callbackResult = new PagingLoadResultBean<Analysis>(
                                bean.as().getAnalyses(), total, offset);
                        callback.onSuccess(callbackResult);
                        view.unMaskMainPanel();
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);
                        callback.onFailure(caught);
                        view.unMaskMainPanel();
                    }
                });
    }

    public void setCurrentAnalysisGroup(AnalysisGroup ag) {
        this.currentAg = ag;
    }

}