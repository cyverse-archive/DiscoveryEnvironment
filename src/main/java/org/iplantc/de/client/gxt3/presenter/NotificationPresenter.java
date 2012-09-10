package org.iplantc.de.client.gxt3.presenter;

import java.util.List;

import org.iplantc.core.uiapplications.client.models.autobeans.Analysis;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisGroup;
import org.iplantc.de.client.gxt3.model.Notification;
import org.iplantc.de.client.gxt3.views.NotificationView.Presenter;
import org.iplantc.de.client.utils.NotificationHelper.Category;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.google.gwt.user.client.ui.HasOneWidget;

public class NotificationPresenter implements Presenter,
        org.iplantc.core.uiapplications.client.views.AppsView.Presenter {

    @Override
    public void go(HasOneWidget arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnalysisSelected(Analysis analysis) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnalysisGroupSelected(AnalysisGroup ag) {
        // TODO Auto-generated method stub

    }

    @Override
    public Analysis getSelectedAnalysis() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AnalysisGroup getSelectedAnalysisGroup() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void filterBy(Category category) {
        // TODO Auto-generated method stub

    }

    @Override
    public Category getCurrentFilter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SortDir getCurrentSortDir() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getCurrentOffset() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Notification> getSelectedItems() {
        // TODO Auto-generated method stub
        return null;
    }

}
