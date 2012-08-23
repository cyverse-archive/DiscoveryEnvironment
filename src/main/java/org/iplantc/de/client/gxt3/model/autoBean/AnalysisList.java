package org.iplantc.de.client.gxt3.model.autoBean;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * This is a convenience class for retrieving JSON arrays of analyses
 * FIXME JDS Revisit the necessity of this class.
 * 
 * @author jstroot
 * 
 */
public interface AnalysisList {
    @PropertyName("templates")
    List<Analysis> getAnalyses();
}
