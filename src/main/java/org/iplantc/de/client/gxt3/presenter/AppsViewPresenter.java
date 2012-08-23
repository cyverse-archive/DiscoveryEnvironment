package org.iplantc.de.client.gxt3.presenter;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.DEDisplayStrings;
import org.iplantc.de.client.gxt3.model.autoBean.Analysis;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisGroup;
import org.iplantc.de.client.gxt3.views.AppsView;
import org.iplantc.de.client.models.CatalogWindowConfig;
import org.iplantc.de.client.services.TemplateServiceFacade;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadHandler;
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
    private final String tag;
    private CatalogWindowConfig config;


    public AppsViewPresenter(final AppsView view, final TemplateServiceFacade templateService,
            final DEDisplayStrings displayStrings, final UserInfo userInfo, final String tag,
            final CatalogWindowConfig config) {
        /*
         * When the view comes in, it will already have: -- all of its stores
         */
        this.view = view;
        this.templateService = templateService;
        this.displayStrings = displayStrings;
        this.tag = tag;
        this.config = config;

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

        // Fetch AnalysisGroups
        treeLoader.load();
        final List<HandlerRegistration> tmpHndlerRegList = new ArrayList<HandlerRegistration>();
        tmpHndlerRegList.add(treeLoader
                .addLoadHandler(new LoadHandler<AnalysisGroup, List<AnalysisGroup>>() {
                    @Override
                    public void onLoad(LoadEvent<AnalysisGroup, List<AnalysisGroup>> event) {
                        // Get and apply user persisted selections
                        selectCategory(config.getCategoryId());
                        selectApp(config.getAppId());
                        // Remove this handler as it is only needed on first load.
                        if((tmpHndlerRegList != null) && (!tmpHndlerRegList.isEmpty())){
                            tmpHndlerRegList.get(0).removeHandler();
                        }
                    }
                }));
    }

    @Override
    public void selectCategory(String categoryId) {
        // TODO Auto-generated method stub
        // Set heading

    }

    @Override
    public void selectApp(String appId) {
        // TODO Auto-generated method stub
        // Set Heading

    }

    @Override
    public void deSelectCurrentCategory() {
        // TODO Auto-generated method stub

    }

    @Override
    public Analysis getSelectedApp() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AnalysisGroup getSelectedCategory() {
        // TODO Auto-generated method stub
        return null;
    }

}
