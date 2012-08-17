package org.iplantc.de.client.gxt3.presenter;

import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uiapplications.client.models.AnalysisGroup;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.de.client.DEDisplayStrings;
import org.iplantc.de.client.gxt3.views.AppsView;
import org.iplantc.de.client.services.TemplateServiceFacade;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.data.shared.loader.TreeLoader;

public class AppsViewPresenter implements Presenter, AppsView.Presenter {

    private final AppsView view;
    private final TemplateServiceFacade templateService;
    private final AnalysisPagedProxy analysisProxy;
    private final DEDisplayStrings displayStrings;

    public AppsViewPresenter(AppsView view, TemplateServiceFacade templateService,
            DEDisplayStrings displayStrings) {
        /*
         * When the view comes in, it will already have: -- all of its stores
         */
        this.view = view;
        this.templateService = templateService;
        this.displayStrings = displayStrings;
        this.analysisProxy = new AnalysisPagedProxy(this.templateService, this.view, this.displayStrings);
        this.view.setPresenter(this);

        final PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>> listLoader = new PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>>(
                this.analysisProxy);
        this.view.setListLoader(listLoader);
        this.view.setTreeLoader(getTreeLoader());


        // When/How do I hide the different panels.

    }

    /**
     * Create proxy and tree loader for loading files
     * 
     * @return
     */
    private TreeLoader<Folder> getTreeLoader() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onAnalysisGroupSelected(final AnalysisGroup ag) {
        view.setMainPanelHeading(ag.getName());
        fetchApps(ag);
        // TODO Auto-generated method stub

    }

    /**
     * Retrieves the apps for the given group by updating and executing the list loader
     * 
     * @param ag
     */
    private void fetchApps(final AnalysisGroup ag) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnalysisSelected(final Analysis analysis) {
        // TODO Auto-generated method stub

    }

    @Override
    public void go(final HasWidgets container) {
        // What do I need to know when the view comes up?
        // When do I fetch initial data?
        container.clear();
        container.add(view.asWidget());
    }

    private class AnalysisPagedProxy extends RpcProxy<PagingLoadConfig, PagingLoadResult<Analysis>> {

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
        public void load(PagingLoadConfig loadConfig, AsyncCallback<PagingLoadResult<Analysis>> callback) {
            view.maskMainPanel(displayStrings.loadingMask());
            // FIXME JDS Change to paged analysis method
            service.getAnalysis(currentAg.getId(), new AsyncCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onFailure(Throwable caught) {
                    // TODO Auto-generated method stub

                }
            });
        }

        public void setCurrentAnalysisGroup(AnalysisGroup ag) {
            this.currentAg = ag;
        }

    }

    private class AnalysisListLoader extends ListLoader<PagingLoadConfig, PagingLoadResult<Analysis>> {

        private AnalysisGroup ag;

        public AnalysisListLoader(RpcProxy<PagingLoadConfig, PagingLoadResult<Analysis>> proxy) {
            super(proxy);
        }

        public void setAnalysisGroup(final AnalysisGroup ag) {
            this.ag = ag;
        }

    }

}
