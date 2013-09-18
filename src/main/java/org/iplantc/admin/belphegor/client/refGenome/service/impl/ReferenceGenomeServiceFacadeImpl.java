package org.iplantc.admin.belphegor.client.refGenome.service.impl;

import java.util.List;

import org.iplantc.admin.belphegor.client.models.ToolIntegrationAdminProperties;
import org.iplantc.admin.belphegor.client.refGenome.model.ReferenceGenome;
import org.iplantc.admin.belphegor.client.refGenome.model.ReferenceGenomeAutoBeanFactory;
import org.iplantc.admin.belphegor.client.refGenome.service.ReferenceGenomeServiceFacade;
import org.iplantc.admin.belphegor.client.services.ToolIntegrationAdminServiceFacade;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class ReferenceGenomeServiceFacadeImpl implements ReferenceGenomeServiceFacade {

    private final ReferenceGenomeAutoBeanFactory factory;

    @Inject
    public ReferenceGenomeServiceFacadeImpl(ReferenceGenomeAutoBeanFactory factory) {
        this.factory = factory;
    }

    @Override
    public void getReferenceGenomes(AsyncCallback<List<ReferenceGenome>> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getListRefGenomeServiceUrl();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, new ReferenceGenomeListCallbackConverter(callback, factory));
    }

    @Override
    public void editReferenceGenomes(List<ReferenceGenome> refGenomes, AsyncCallback<String> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void createReferenceGenomes(List<ReferenceGenome> refGenomes, AsyncCallback<String> callback) {
        // TODO Auto-generated method stub

    }


}
