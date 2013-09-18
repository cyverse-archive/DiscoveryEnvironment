package org.iplantc.admin.belphegor.client.refGenome.service.impl;

import java.util.List;

import org.iplantc.admin.belphegor.client.refGenome.model.ReferenceGenome;
import org.iplantc.admin.belphegor.client.refGenome.model.ReferenceGenomeAutoBeanFactory;
import org.iplantc.admin.belphegor.client.refGenome.model.ReferenceGenomeList;
import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class ReferenceGenomeListCallbackConverter extends AsyncCallbackConverter<String, List<ReferenceGenome>> {

    private final ReferenceGenomeAutoBeanFactory factory;

    public ReferenceGenomeListCallbackConverter(AsyncCallback<List<ReferenceGenome>> callback, ReferenceGenomeAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected List<ReferenceGenome> convertFrom(String object) {
        final AutoBean<ReferenceGenomeList> decode = AutoBeanCodex.decode(factory, ReferenceGenomeList.class, object);
        return decode.as().getReferenceGenomes();
    }

}
