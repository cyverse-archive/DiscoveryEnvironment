package org.iplantc.admin.belphegor.client.toolRequest.service;

import java.util.List;

import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequest;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestDetails;
import org.iplantc.core.uiapps.client.models.toolrequest.ToolRequestUpdate;
import org.iplantc.core.uicommons.client.models.HasId;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.SortInfo;

public interface ToolRequestServiceFacade {

    /**
     * <a href=
     * "https://github.com/iPlantCollaborativeOpenSource/Donkey/blob/master/doc/endpoints/app-metadata.md#listing-tool-installation-request-details"
     * >Donkey Doc</a><br/>
     * <a href=
     * "https://github.com/iPlantCollaborativeOpenSource/metadactyl-clj/blob/master/doc/endpoints/app-metadata/tool-requests.md#obtaining-tool-request-details"
     * >Metadactyl-clj Doc</a>
     * 
     * @param toolRequest
     * @param callback
     */
    void getToolRequestDetails(HasId toolRequest, AsyncCallback<ToolRequestDetails> callback);

    /**
     * <a href=
     * "https://github.com/iPlantCollaborativeOpenSource/Donkey/blob/master/doc/endpoints/app-metadata.md#updating-a-tool-installation-request-administrator"
     * >Donkey Doc</a><br/>
     * <a href=
     * "https://github.com/iPlantCollaborativeOpenSource/metadactyl-clj/blob/master/doc/endpoints/app-metadata/tool-requests.md#updating-the-status-of-a-tool-request"
     * >Metadactyl-clj Doc</a>
     * 
     * @param trUpdate
     * @param callback
     */
    void updateToolRequest(ToolRequestUpdate trUpdate, AsyncCallback<ToolRequestDetails> callback);

    /**
     * <a href=
     * "https://github.com/iPlantCollaborativeOpenSource/Donkey/blob/master/doc/endpoints/app-metadata.md#listing-tool-installation-requests"
     * >Donkey
     * Doc</a>
     * <a href=
     * "https://github.com/iPlantCollaborativeOpenSource/metadactyl-clj/blob/master/doc/endpoints/app-metadata/tool-requests.md#listing-tool-requests"
     * >Metadactyl-clj
     * Doc</a>
     * 
     * @param sortInfo
     * @param userName
     * @param callback
     */
    void getToolRequests(SortInfo sortInfo, String userName, AsyncCallback<List<ToolRequest>> callback);

}
