package org.iplantc.admin.belphegor.client.refGenome.service.impl;

import java.util.List;

import org.iplantc.admin.belphegor.client.models.ToolIntegrationAdminProperties;
import org.iplantc.admin.belphegor.client.refGenome.model.ReferenceGenome;
import org.iplantc.admin.belphegor.client.refGenome.model.ReferenceGenomeAutoBeanFactory;
import org.iplantc.admin.belphegor.client.refGenome.service.ReferenceGenomeServiceFacade;
import org.iplantc.admin.belphegor.client.services.ToolIntegrationAdminServiceFacade;
import org.iplantc.core.uicommons.client.services.StringToVoidCallbackConverter;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

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
    public void editReferenceGenomes(ReferenceGenome referenceGenome, AsyncCallback<Void> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getEditRefGenomeServiceUrl();
        Splittable body = StringQuoter.createSplittable();
        StringQuoter.create(referenceGenome.getName()).assign(body, ReferenceGenome.NAME);
        StringQuoter.create(referenceGenome.getPath()).assign(body, ReferenceGenome.PATH);
        StringQuoter.create(referenceGenome.isDeleted()).assign(body, ReferenceGenome.DELETED);
        StringQuoter.create(referenceGenome.getId()).assign(body, ReferenceGenome.UUID);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.PUT, address, body.getPayload());
        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, new StringToVoidCallbackConverter(callback));

    }

    @Override
    public void createReferenceGenomes(ReferenceGenome referenceGenome, AsyncCallback<Void> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getAddRefGenomeServiceUrl();
        Splittable body = StringQuoter.createSplittable();
        StringQuoter.create(referenceGenome.getName()).assign(body, ReferenceGenome.NAME);
        StringQuoter.create(referenceGenome.getPath()).assign(body, ReferenceGenome.PATH);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.PUT, address, body.getPayload());
        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, new StringToVoidCallbackConverter(callback));
    }

}
