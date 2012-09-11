package org.iplantc.admin.belphegor.client.gxt3.presenter;

import java.util.List;

import org.iplantc.admin.belphegor.client.gxt3.presenter.proxy.AnalysisGroupProxy;
import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.models.autobeans.Analysis;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisAutoBeanFactory;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisGroup;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisList;
import org.iplantc.core.uiapplications.client.presenter.AppsViewPresenter;
import org.iplantc.core.uiapplications.client.services.AppTemplateServiceFacade;
import org.iplantc.core.uiapplications.client.views.AppsView;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.presenter.Presenter;
import org.iplantc.de.client.CommonDisplayStrings;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

/**
 * Presenter class for the Belphegor <code>AppsView</code>.
 * 
 * This class is intentionally not a subclass of {@link AppsViewPresenter} in order to keep a clear
 * difference between the service facades utilized.
 * 
 * @author jstroot
 * 
 */
public class BelphegorAppsViewPresenter implements Presenter, AppsView.Presenter {

    private final AppsView view;
    private final AppTemplateServiceFacade templateService;
    private final CommonDisplayStrings displayStrings;
    private final AnalysisGroupProxy analysisGroupProxy;

    public BelphegorAppsViewPresenter(final AppsView view,
            final AppTemplateServiceFacade templateService, final CommonDisplayStrings displayStrings,
            final UserInfo userInfo) {
        this.view = view;
        this.templateService = templateService;
        this.displayStrings = displayStrings;

        analysisGroupProxy = new AnalysisGroupProxy(templateService, userInfo);
        this.view.setPresenter(this);
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
    public Analysis getSelectedAnalysis() {
        return view.getSelectedAnalysis();
    }

    @Override
    public AnalysisGroup getSelectedAnalysisGroup() {
        return view.getSelectedAnalysisGroup();
    }

    @Override
    public void go(final HasOneWidget container) {
        container.setWidget(view.asWidget());

        // Fetch AnalysisGroups
        analysisGroupProxy.load(null, new AsyncCallback<List<AnalysisGroup>>() {
            @Override
            public void onSuccess(List<AnalysisGroup> result) {
                addAnalysisGroup(null, result);
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
    public void onAnalysisSelected(final Analysis analysis) {
        // TODO Auto-generated method stub
    }

}
