package org.iplantc.admin.belphegor.client.services;

import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class ReferenceGenomesServiceFacade {

    public void getReferenceGenomes(AsyncCallback<String> callback) {
        String address = "http://montosa.iplantcollaborative.org/~dennis/genomes.json";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, callback);
    }
}
