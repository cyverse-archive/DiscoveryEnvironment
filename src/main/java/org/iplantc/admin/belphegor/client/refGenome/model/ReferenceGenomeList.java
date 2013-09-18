package org.iplantc.admin.belphegor.client.refGenome.model;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface ReferenceGenomeList {

    @PropertyName("genomes")
    List<ReferenceGenome> getReferenceGenomes();
}
