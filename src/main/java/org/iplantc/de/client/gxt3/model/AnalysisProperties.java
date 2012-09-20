/**
 * 
 */
package org.iplantc.de.client.gxt3.model;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author sriram
 * 
 */
public interface AnalysisProperties extends PropertyAccess<Analysis> {

    ValueProvider<Analysis, String> name();

    ValueProvider<Analysis, String> analysisName();

    ValueProvider<Analysis, Long> startDate();

    ValueProvider<Analysis, Long> endDate();

    ValueProvider<Analysis, String> status();
}
