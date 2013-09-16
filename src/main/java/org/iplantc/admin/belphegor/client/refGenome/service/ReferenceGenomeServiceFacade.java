package org.iplantc.admin.belphegor.client.refGenome.service;

import java.util.List;

import org.iplantc.admin.belphegor.client.refGenome.model.ReferenceGenome;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReferenceGenomeServiceFacade {

    /**
     * https://github.com/iPlantCollaborativeOpenSource/Conrad#listing-all-genome-references
     * 
     * @param callback
     */
    void getReferenceGenomes(AsyncCallback<List<ReferenceGenome>> callback);

    /**
     * https://github.com/iPlantCollaborativeOpenSource/Conrad#modifying-a-genome-reference
     * 
     * @param refGenomes
     * @param callback
     */
    void editReferenceGenomes(List<ReferenceGenome> refGenomes, AsyncCallback<String> callback);

    /**
     * https://github.com/iPlantCollaborativeOpenSource/Conrad#creating-a-new-genome-reference
     * 
     * @param refGenomes
     * @param callback
     */
    void createReferenceGenomes(List<ReferenceGenome> refGenomes, AsyncCallback<String> callback);
}
