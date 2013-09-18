package org.iplantc.admin.belphegor.client.services;

import org.iplantc.admin.belphegor.client.I18N;
import org.iplantc.admin.belphegor.client.models.ToolIntegrationAdminProperties;
import org.iplantc.admin.belphegor.client.services.callbacks.AdminServiceCallback;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Component;

public class ReferenceGenomesServiceFacade {

    private final Component maskingCaller;

    public ReferenceGenomesServiceFacade() {
        this(null);
    }

    public ReferenceGenomesServiceFacade(Component maskingCaller) {
        this.maskingCaller = maskingCaller;
    }

    public void getReferenceGenomes(AsyncCallback<String> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getListRefGenomeServiceUrl();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);
        callService(wrapper, callback);
    }

    public void editReferenceGenomes(JSONObject body, AsyncCallback<String> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getEditRefGenomeServiceUrl();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.POST, address,
                body.toString());
        callService(wrapper, callback);
    }

    public void createReferenceGenomes(JSONObject body, AsyncCallback<String> callback) {
        String address = ToolIntegrationAdminProperties.getInstance().getAddRefGenomeServiceUrl();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.PUT, address,
                body.toString());
        callService(wrapper, callback);
    }

    /**
     * Performs the actual service call, masking any calling component.
     *
     * @param callback executed when RPC call completes.
     * @param wrapper the wrapper used to get to the actual service via the service proxy.
     */
    private void callService(ServiceCallWrapper wrapper, AsyncCallback<String> callback) {
        if (callback instanceof AdminServiceCallback) {
            ((AdminServiceCallback)callback).setMaskedCaller(maskingCaller);
        }

        if (maskingCaller != null) {
            maskingCaller.mask(I18N.DISPLAY.loadingMask());
        }

        ToolIntegrationAdminServiceFacade.getInstance().getServiceData(wrapper, callback);
    }
}
