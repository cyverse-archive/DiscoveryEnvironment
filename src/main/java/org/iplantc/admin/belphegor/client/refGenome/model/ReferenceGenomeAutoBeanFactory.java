package org.iplantc.admin.belphegor.client.refGenome.model;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface ReferenceGenomeAutoBeanFactory extends AutoBeanFactory {

    AutoBean<ReferenceGenome> referenceGenome();

    AutoBean<ReferenceGenomeList> referenceGenomeList();
}
