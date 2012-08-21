package org.iplantc.de.client.gxt3.presenter;

import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.DEDisplayStrings;
import org.iplantc.de.client.gxt3.model.autoBean.Analysis;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisGroup;
import org.iplantc.de.client.gxt3.views.AppsView;
import org.iplantc.de.client.services.TemplateServiceFacade;

import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

public class AppsViewPresenter implements Presenter, AppsView.Presenter {

    private final AppsView view;
    private final TemplateServiceFacade templateService;
    private final AnalysisPagedProxy analysisProxy;
    private final TreeLoader<AnalysisGroup> treeLoader;
    private final AnalysisGroupProxy analysisGroupProxy;
    private final PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>> listLoader;
    private final DEDisplayStrings displayStrings;


    public AppsViewPresenter(AppsView view, TemplateServiceFacade templateService,
            DEDisplayStrings displayStrings, UserInfo userInfo) {
        /*
         * When the view comes in, it will already have: -- all of its stores
         */
        this.view = view;
        this.templateService = templateService;
        this.displayStrings = displayStrings;

        // Initialize AnalysisGroup TreeStore proxy and loader
        analysisGroupProxy = new AnalysisGroupProxy(this.templateService, userInfo);
        treeLoader = new TreeLoader<AnalysisGroup>(analysisGroupProxy) {
            @Override
            public boolean hasChildren(AnalysisGroup parent) {
                return (parent.getGroups() != null) && !parent.getGroups().isEmpty();
            }
        };

        // Initialize Analysis ListStore proxy and loader
        analysisProxy = new AnalysisPagedProxy(this.templateService, this.view, this.displayStrings);
        listLoader = new PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>>(
                this.analysisProxy);
        treeLoader.addLoadHandler(new ChildTreeStoreBinding<AnalysisGroup>(view.getTreeStore()));

        this.view.setPresenter(this);
        this.view.setListLoader(listLoader);
        this.view.setTreeLoader(treeLoader);
        this.view.getGridSelectionModel().addSelectionChangedHandler(
                new SelectionChangedHandler<Analysis>() {

                    @Override
                    public void onSelectionChanged(SelectionChangedEvent<Analysis> event) {
                        // TODO Auto-generated method stub

                    }
                });

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
        this.analysisProxy.setCurrentAnalysisGroup(ag);
        this.listLoader.load();
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

}
