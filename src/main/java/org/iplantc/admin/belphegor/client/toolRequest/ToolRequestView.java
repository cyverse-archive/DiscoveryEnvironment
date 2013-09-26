package org.iplantc.admin.belphegor.client.toolRequest;

import java.util.List;

import org.iplantc.admin.belphegor.client.toolRequest.service.ToolRequestServiceFacade;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequest;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestDetails;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestUpdate;
import org.iplantc.core.uicommons.client.views.IsMaskable;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface ToolRequestView extends IsWidget, IsMaskable {

    public interface Presenter {

        /**
         * Submits the given update to the {@link ToolRequestServiceFacade#updateToolRequest} endpoint.
         * Upon success, the presenter will refresh the view.
         * 
         * @param update
         */
        void updateToolRequest(ToolRequestUpdate update);

        /**
         * Retrieves and assembles a {@link ToolRequestDetails} object via the
         * {@link ToolRequestServiceFacade#getToolRequestDetails} endpoint.
         * 
         * Upon success, the presenter will refresh the view.
         * 
         */
        void fetchToolRequestDetails(ToolRequest toolRequest);

        void go(HasOneWidget container);

    }

    void setPresenter(Presenter presenter);

    void setToolRequests(List<ToolRequest> toolRequests);

    void maskDetailsPanel(String loadingMask);

    void unmaskDetailsPanel();

    void setDetailsPanel(ToolRequestDetails result);

    void update(ToolRequestUpdate toolRequestUpdate, ToolRequestDetails toolRequestDetails);

}
