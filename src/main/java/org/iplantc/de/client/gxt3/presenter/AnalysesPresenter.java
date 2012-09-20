package org.iplantc.de.client.gxt3.presenter;

import java.util.List;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.Services;
import org.iplantc.de.client.gxt3.model.AnalysesAutoBeanFactory;
import org.iplantc.de.client.gxt3.model.AnalysesList;
import org.iplantc.de.client.gxt3.model.Analysis;
import org.iplantc.de.client.gxt3.model.NotificationList;
import org.iplantc.de.client.gxt3.views.AnalysesToolbarView;
import org.iplantc.de.client.gxt3.views.AnalysesToolbarViewImpl;
import org.iplantc.de.client.gxt3.views.AnalysesView;
import org.iplantc.de.client.gxt3.views.AnalysesView.Presenter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

/**
 * 
 * A presenter for analyses view
 * 
 * @author sriram
 * 
 */
public class AnalysesPresenter implements Presenter,
        org.iplantc.de.client.gxt3.views.AnalysesToolbarView.Presenter {

    private final AnalysesView view;
    private final AnalysesToolbarView toolbar;
    private AnalysesAutoBeanFactory factory = GWT.create(AnalysesAutoBeanFactory.class);

    public AnalysesPresenter(AnalysesView view) {
        this.view = view;
        toolbar = new AnalysesToolbarViewImpl();
        toolbar.setPresenter(this);
        view.setNorthWidget(toolbar);
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
        retrieveData();
    }

    @Override
    public void onDeleteClicked() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onViewParamClicked() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCancelClicked() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnalysesSelection(List<Analysis> selectedItems) {
        // TODO Auto-generated method stub

    }

    private void retrieveData() {

        Services.ANALYSIS_SERVICE.getAnalyses(UserInfo.getInstance().getWorkspaceId(),
                new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        AutoBean<AnalysesList> bean = AutoBeanCodex.decode(factory, AnalysesList.class,
                                result);
                        view.loadAnalyses(bean.as().getAnalysisList());
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(I18N.DISPLAY.analysesRetrievalFailure(), caught);
                    }
                });
    }

}
