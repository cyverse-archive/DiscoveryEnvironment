package org.iplantc.admin.belphegor.client.toolRequest;

import org.iplantc.admin.belphegor.client.toolRequest.service.ToolRequestServiceFacade;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestDetails;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestUpdate;

import com.google.gwt.user.client.ui.HasOneWidget;

public interface ToolRequestView {

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
        void fetchToolRequestDetails();

        void go(HasOneWidget container);

    }

    void setPresenter(Presenter presenter);

}
