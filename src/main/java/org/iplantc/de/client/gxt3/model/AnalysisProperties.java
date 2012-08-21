package org.iplantc.de.client.gxt3.model;


import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

@Deprecated
public interface AnalysisProperties extends PropertyAccess<Analysis>{

	ValueProvider<Analysis, String> getName();

	ValueProvider<Analysis, String> getIntegratedBy();

	ValueProvider<Analysis, String> getRating();

}
