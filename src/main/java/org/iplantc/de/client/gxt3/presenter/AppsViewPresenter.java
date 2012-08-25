package org.iplantc.de.client.gxt3.presenter;

import java.util.List;

import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.DEDisplayStrings;
import org.iplantc.de.client.gxt3.model.autoBean.Analysis;
import org.iplantc.de.client.gxt3.model.autoBean.AnalysisGroup;
import org.iplantc.de.client.gxt3.views.AppsView;
import org.iplantc.de.client.models.CatalogWindowConfig;
import org.iplantc.de.client.services.TemplateServiceFacade;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

public class AppsViewPresenter implements Presenter, AppsView.Presenter {

    private final AppsView view;
    private final TemplateServiceFacade templateService;
    private final AnalysisPagedProxy analysisProxy;
    // private final TreeLoader<AnalysisGroup> treeLoader;
    private final AnalysisGroupProxy analysisGroupProxy;
    private final PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>> listLoader;
    private final DEDisplayStrings displayStrings;
    private final String tag;
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
        this.tag = tag;
        this.config = config;

        // Initialize AnalysisGroup TreeStore proxy and loader
        analysisGroupProxy = new AnalysisGroupProxy(this.templateService, userInfo);
        // treeLoader = new TreeLoader<AnalysisGroup>(analysisGroupProxy) {
        // @Override
        // public boolean hasChildren(AnalysisGroup parent) {
        // return (parent.getGroups() != null) && !parent.getGroups().isEmpty();
        // }
        // };

        // Initialize Analysis ListStore proxy and loader
        analysisProxy = new AnalysisPagedProxy(this.templateService, this.view, this.displayStrings);
        listLoader = new PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>>(
                this.analysisProxy);
        // treeLoader.addLoadHandler(new ChildTreeStoreBinding<AnalysisGroup>(view.getTreeStore()) {
        // @Override
        // public void onLoad(LoadEvent<AnalysisGroup, List<AnalysisGroup>> event) {
        // AnalysisGroup parent = event.getLoadConfig();
        //
        // if ((parent != null) && !view.getTreeStore().hasChildren(parent)
        // && (view.getTreeStore().findModelWithKey(parent.getId()) == null)) {
        // view.getTreeStore().replaceChildren(parent, event.getLoadResult());
        // }
        // if (view.getTreeStore().getAll().isEmpty()) {
        // view.getTreeStore().replaceChildren(parent, event.getLoadResult());
        // }
        // }
        // });

        this.view.setPresenter(this);
        this.view.setListLoader(listLoader);
        // this.view.setTreeLoader(treeLoader);
        SelectionChangedHandler<Analysis> gridSelChangeHandler = new SelectionChangedHandler<Analysis>() {

            @Override
            public void onSelectionChanged(SelectionChangedEvent<Analysis> event) {
                if ((event.getSelection() != null) && !event.getSelection().isEmpty()) {
                    Analysis analysis = event.getSelection().get(0);
                    onAnalysisSelected(analysis);
                }
            }
        };
        SelectionChangedHandler<AnalysisGroup> treeSelChangeHandler = new SelectionChangedHandler<AnalysisGroup>() {

            @Override
            public void onSelectionChanged(SelectionChangedEvent<AnalysisGroup> event) {
                onAnalysisGroupSelected(event.getSelection().get(0));
            }
        };
        this.view.getGridSelectionModel().addSelectionChangedHandler(gridSelChangeHandler);
        this.view.getTreeSelectionModel().addSelectionChangedHandler(treeSelChangeHandler);

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
    public void go(final HasOneWidget container) {
        // What do I need to know when the view comes up?
        // When do I fetch initial data?
        container.setWidget(view.asWidget());

        // Fetch AnalysisGroups
        // treeLoader.load();
        //
        // final List<HandlerRegistration> tmpHndlerRegList = new ArrayList<HandlerRegistration>();
        // tmpHndlerRegList.add(treeLoader
        // .addLoadHandler(new LoadHandler<AnalysisGroup, List<AnalysisGroup>>() {
        // @Override
        // public void onLoad(LoadEvent<AnalysisGroup, List<AnalysisGroup>> event) {
        // // Get and apply user persisted selections
        // if (config != null) {
        // selectCategory(config.getCategoryId());
        // selectApp(config.getAppId());
        // }
        // // Remove this handler as it is only needed on first load.
        // if ((tmpHndlerRegList != null) && (!tmpHndlerRegList.isEmpty())) {
        // tmpHndlerRegList.get(0).removeHandler();
        // }
        // }
        // }));

        analysisGroupProxy.load(null, new AsyncCallback<List<AnalysisGroup>>() {

            @Override
            public void onSuccess(List<AnalysisGroup> result) {

                addAnalysisGroup(null, result);

                for (AnalysisGroup ag : view.getTreeStore().getAll()) {
                    if (!ag.isPublic()) {
                        view.getTreeStore().remove(ag);
                    }
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
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void selectCategory(String categoryId) {
        AnalysisGroup ag = view.getTreeStore().findModelWithKey(categoryId);

        if (ag != null) {
            view.getTreeSelectionModel().select(ag, true);
            // Set heading
            view.setMainPanelHeading(ag.getName());
        }
    }

    @Override
    public void selectApp(String appId) {
        Analysis app = view.getListStore().findModelWithKey(appId);
        // Set Heading
        if (app != null) {
            view.getGridSelectionModel().select(app, true);
        }
    }

    @Override
    public void deSelectCurrentCategory() {
        view.getTreeSelectionModel().deselect(getSelectedCategory());
    }

    @Override
    public Analysis getSelectedApp() {
        return view.getGridSelectionModel().getSelectedItem();
    }

    @Override
    public AnalysisGroup getSelectedCategory() {
        return view.getTreeSelectionModel().getSelectedItem();
    }

}
