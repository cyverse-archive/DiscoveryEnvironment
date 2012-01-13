package org.iplantc.de.client.services;

import org.iplantc.de.client.models.DEProperties;
import org.iplantc.de.shared.services.MultiPartServiceWrapper;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Provides access to remote services to acquire raw data.
 * 
 * @author amuir
 * 
 */
public class RawDataServices {


    /**
     * Call service to retrieve the provenance data for a requested file
     * 
     * @param idFile file to retrieve provenance from.
     * @param callback executes when RPC call is complete.
     */
    public static void getFileProvenance(String idFile, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "files/" + idFile //$NON-NLS-1$
                + "/provenance.text.all"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }



    /**
     * Call service to save the raw data
     * 
     * @param idFile identifier of file to save.
     * @param filename name of the file.
     * @param body multi-part body.
     * @param callback executes when RPC call is complete.
     */
    public static void saveRawData(String idFile, String filename, String body,
            AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "files/" + idFile; //$NON-NLS-1$
        MultiPartServiceWrapper wrapper = new MultiPartServiceWrapper(MultiPartServiceWrapper.Type.PUT,
                address);

        String disposition = "name=\"file\"; filename=\"" + filename + "\""; //$NON-NLS-1$ //$NON-NLS-2$
        wrapper.addPart(body, disposition);

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    /**
     * Call service to save the raw data under a different name
     * 
     * @param idWorkspace unique id for user's workspace.
     * @param idFolder parent folder identifier.
     * @param idFile id of original file to be saved.
     * @param filename new file name.
     * @param body multi-part body.
     * @param callback executes when RPC call is complete.
     */
    public static void saveAsRawData(String idWorkspace, String idFolder, String idFile,
            String filename, String body, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "workspaces/" //$NON-NLS-1$
                + idWorkspace + "/folders/" + idFolder + "/files"; //$NON-NLS-1$ //$NON-NLS-2$
        MultiPartServiceWrapper wrapper = new MultiPartServiceWrapper(MultiPartServiceWrapper.Type.POST,
                address);

        String disposition = "name=\"file\"; filename=\"" + filename + "\""; //$NON-NLS-1$ //$NON-NLS-2$
        wrapper.addPart(body, disposition);

        disposition = "name=\"copied-from\""; //$NON-NLS-1$
        wrapper.addPart(idFile, disposition);

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }



    /**
     * Call service to save file description
     * 
     * @param idFile id of file to save.
     * @param description new description.
     * @param callback executes when RPC call is complete.
     */
    public static void saveFileDescription(String idFile, String description,
            AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() + "files/" + idFile //$NON-NLS-1$
                + "/description"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.PUT, address,
                description);

        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }


}
