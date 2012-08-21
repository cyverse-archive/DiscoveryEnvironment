package org.iplantc.de.client.gxt3.model.autoBean;

import java.util.List;

import org.iplantc.core.uiapplications.client.models.Analysis;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * FIXME JDS Revisit the necessity of this class.
 * 
 * @author jstroot
 * 
 */
public interface AnalysisList {
    @PropertyName("templates")
    List<Analysis> getAnalyses();
}
