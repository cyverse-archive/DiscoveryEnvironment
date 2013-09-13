package org.iplantc.admin.belphegor.client.toolRequest.presenter;

import java.util.List;

import org.iplantc.admin.belphegor.client.toolRequest.ToolRequestView;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequest;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestUpdate;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.sencha.gxt.data.shared.ListStore;

public class ToolRequestPresenterImpl implements ToolRequestView.Presenter {

    private ListStore<ToolRequest> store;

    @Inject
    public ToolRequestPresenterImpl(ToolRequestView view) {
        view.setPresenter(this);
    }

    private List<ToolRequest> fetchToolRequests() {
        // TODO Implement
        return null;
    }

    @Override
    public void updateToolRequest(ToolRequestUpdate update) {
        /*
         * TODO Call server with the update tool request
         * Upon success, refresh view/store.
         */

    }

    @Override
    public void fetchToolRequestDetails() {
        // TODO Auto-generated method stub

    }

    @Override
    public void go(HasOneWidget container) {
        // TODO Auto-generated method stub

    }
}
