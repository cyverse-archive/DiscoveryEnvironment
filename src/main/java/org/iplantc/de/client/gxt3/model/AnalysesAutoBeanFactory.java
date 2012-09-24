/**
 * 
 */
package org.iplantc.de.client.gxt3.model;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * @author sriram
 * 
 */
public interface AnalysesAutoBeanFactory extends AutoBeanFactory {

    AutoBean<AnalysesList> getAnalysesList();

    AutoBean<Analysis> getAnalyses();

    AutoBean<AnalysisParameter> getAnalysisParam();

    AutoBean<AnalysisParametersList> getAnalysisParamList();
}
