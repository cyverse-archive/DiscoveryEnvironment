package org.iplantc.admin.belphegor.client.refGenome.model;

import java.util.Date;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uidiskresource.client.services.errors.HasPath;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface ReferenceGenome extends HasId, HasName, HasPath {

    @PropertyName("last_modified")
    Date getLastModifiedDate();

    @PropertyName("last_modified_by")
    String getLastModifiedBy();

    @PropertyName("created_on")
    Date getCreatedDate();

    @PropertyName("created_by")
    String getCreatedBy();

    boolean isDeleted();

    String getUuid();

}
