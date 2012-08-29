package org.iplantc.de.client.gxt3.presenter;

import java.util.List;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.DEDisplayStrings;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.gxt3.model.autoBean.Analysis;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisAutoBeanFactory;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisGroup;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisList;
import org.iplantc.de.client.gxt3.presenter.proxy.AnalysisGroupProxy;
import org.iplantc.de.client.gxt3.views.AppsView;
import org.iplantc.de.client.models.CatalogWindowConfig;
import org.iplantc.de.client.services.TemplateServiceFacade;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class AppsViewPresenter implements  AppsView.Presenter {

    private final AppsView view;
    private final TemplateServiceFacade templateService;
    // private final AnalysisPagedProxy analysisPagedProxy;

    // private final AnalysisRpcProxy analysisRpcProxy;
    // private final ListLoader<ListLoadConfig, ListLoadResult<Analysis>> analysisListLoader;

    private final AnalysisGroupProxy analysisGroupProxy;
    // private final PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>> pagingListLoader;
    private final DEDisplayStrings displayStrings;
    // TODO JDS Determine if tag is necessary in presenter, or if only needed for parent window.
    // private final String tag;
    private final CatalogWindowConfig config;


    public AppsViewPresenter(final AppsView view, final TemplateServiceFacade templateService,
            final DEDisplayStrings displayStrings, final UserInfo userInfo, final String tag,
            final CatalogWindowConfig config) {
        /*
         * When the view comes in, it will already have: -- all of its stores
         */
        this.view = view;
        this.templateService = templateService;
        this.displayStrings = displayStrings;
        // this.tag = tag;
        this.config = config;

        // Initialize AnalysisGroup TreeStore proxy and loader
        analysisGroupProxy = new AnalysisGroupProxy(this.templateService, userInfo);

        // analysisRpcProxy = new AnalysisRpcProxy(this.templateService, this.view, this.displayStrings);
        // analysisListLoader = new ListLoader<ListLoadConfig,
        // ListLoadResult<Analysis>>(analysisRpcProxy);

        // Initialize Analysis ListStore proxy and loader
        // analysisPagedProxy = new AnalysisPagedProxy(this.templateService, this.view,
        // this.displayStrings);
        // pagingListLoader = new PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>>(
        // this.analysisPagedProxy);

        this.view.setPresenter(this);
        // this.view.setListLoader(analysisListLoader);
        // this.view.setListLoader(pagingListLoader);

        // When/How do I hide the different panels.

    }

    @Override
    public void onAnalysisGroupSelected(final AnalysisGroup ag) {
        view.setMainPanelHeading(ag.getName());
        fetchApps(ag);
    }

    /**
     * Retrieves the apps for the given group by updating and executing the list loader
     * 
     * @param ag
     */
    private void fetchApps(final AnalysisGroup ag) {
        view.maskMainPanel(displayStrings.loadingMask());
        templateService.getAnalysis(ag.getId(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                AnalysisAutoBeanFactory factory = GWT.create(AnalysisAutoBeanFactory.class);
                AutoBean<AnalysisList> bean = AutoBeanCodex.decode(factory, AnalysisList.class, result);

                view.setAnalyses(bean.as().getAnalyses());
                view.unMaskMainPanel();
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);
                view.unMaskMainPanel();
            }
        });
    }

    @Override
    public void onAnalysisSelected(final Analysis analysis) {
        // TODO Auto-generated method stub

    }

    @Override
    public void go(final HasOneWidget container) {
        // What do I need to know when the view comes up?
        // When do I fetch initial data?
        container.setWidget(view.asWidget());

        // Fetch AnalysisGroups
        analysisGroupProxy.load(null, new AsyncCallback<List<AnalysisGroup>>() {
            @Override
            public void onSuccess(List<AnalysisGroup> result) {
                addAnalysisGroup(null, result);

                for (AnalysisGroup ag : view.getTreeStore().getAll()) {
                    if (!ag.isPublic()) {
                        view.getTreeStore().remove(ag);
                    }
                }

                // Select previous user selections
                if (config != null) {
                    view.selectAnalysisGroup(config.getCategoryId());
                    view.selectAnalysis(config.getAppId());
                }
            }

            private void addAnalysisGroup(AnalysisGroup parent, List<AnalysisGroup> children) {
                if ((children == null) || children.isEmpty()) {
                    return;
                }
                if (parent == null) {
                    view.getTreeStore().add(children);
                } else {
                    view.getTreeStore().replaceChildren(parent, children);
                }

                for (AnalysisGroup ag : children) {
                    addAnalysisGroup(ag, ag.getGroups());
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });
    }

    @Override
    public Analysis getSelectedAnalysis() {
        return view.getSelectedAnalysis();
    }

    @Override
    public AnalysisGroup getSelectedAnalysisGroup() {
        return view.getSelectedAnalysisGroup();
    }


}
