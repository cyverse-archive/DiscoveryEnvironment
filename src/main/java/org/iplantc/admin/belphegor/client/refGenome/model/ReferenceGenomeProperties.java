package org.iplantc.admin.belphegor.client.refGenome.model;

import java.util.Date;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface ReferenceGenomeProperties extends PropertyAccess<ReferenceGenome> {

    ModelKeyProvider<ReferenceGenome> id();

    ValueProvider<ReferenceGenome, String> name();

    ValueProvider<ReferenceGenome, String> path();

    ValueProvider<ReferenceGenome, Date> createdDate();

    ValueProvider<ReferenceGenome, String> createdBy();

}
