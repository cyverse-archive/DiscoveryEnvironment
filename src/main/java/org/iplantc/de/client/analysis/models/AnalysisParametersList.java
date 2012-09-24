/**
 * 
 */
package org.iplantc.de.client.analysis.models;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * @author sriram
 * 
 */
public interface AnalysisParametersList {

    @PropertyName("parameters")
    List<AnalysisParameter> getParameterList();
}
