package org.iplantc.admin.belphegor.client.toolRequest.presenter;

import java.util.List;

import org.iplantc.admin.belphegor.client.toolRequest.ToolRequestView;
import org.iplantc.admin.belphegor.client.toolRequest.service.ToolRequestServiceFacade;
import org.iplantc.core.resources.client.messages.IplantDisplayStrings;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequest;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestDetails;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestUpdate;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.info.IplantAnnouncer;
import org.iplantc.core.uicommons.client.info.SuccessAnnouncementConfig;
import org.iplantc.core.uicommons.client.models.UserInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

public class ToolRequestPresenterImpl implements ToolRequestView.Presenter {

    private final ToolRequestView view;
    private final IplantDisplayStrings strings;
    private final ToolRequestServiceFacade toolReqService;

    @Inject
    public ToolRequestPresenterImpl(ToolRequestView view, ToolRequestServiceFacade toolReqService, IplantDisplayStrings strings) {
        this.view = view;
        this.strings = strings;
        this.toolReqService = toolReqService;
        view.setPresenter(this);
    }

    @Override
    public void updateToolRequest(final ToolRequestUpdate update) {
        toolReqService.updateToolRequest(update, new AsyncCallback<ToolRequestDetails>() {

            @Override
            public void onSuccess(ToolRequestDetails result) {
                IplantAnnouncer.getInstance().schedule(new SuccessAnnouncementConfig("Tool Request updated"));
                view.update(update, result);
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });

    }

    @Override
    public void fetchToolRequestDetails(ToolRequest toolRequest) {
        view.maskDetailsPanel(strings.loadingMask());
        toolReqService.getToolRequestDetails(toolRequest, new AsyncCallback<ToolRequestDetails>() {

            @Override
            public void onSuccess(ToolRequestDetails result) {
                view.unmaskDetailsPanel();
                view.setDetailsPanel(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                view.unmaskDetailsPanel();
                ErrorHandler.post(caught);
            }
        });
    }

    @Override
    public void go(HasOneWidget container) {
        view.mask(strings.loadingMask());
        container.setWidget(view);
        toolReqService.getToolRequests(null, UserInfo.getInstance().getUsername(), new AsyncCallback<List<ToolRequest>>() {

            @Override
            public void onSuccess(List<ToolRequest> result) {
                view.unmask();
                view.setToolRequests(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                view.unmask();
                ErrorHandler.post(caught);
            }
        });


    }
}
