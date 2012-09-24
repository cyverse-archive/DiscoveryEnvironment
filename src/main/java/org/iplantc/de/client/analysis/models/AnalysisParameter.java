/**
 * 
 */
package org.iplantc.de.client.analysis.models;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * @author sriram
 * 
 */
public interface AnalysisParameter {

    @PropertyName("param_id")
    void setId(String ig);

    @PropertyName("param_id")
    String getId();

    @PropertyName("param_name")
    void setName(String name);

    @PropertyName("param_name")
    String getName();

    @PropertyName("param_type")
    void setType(String type);

    @PropertyName("param_type")
    String getType();

    @PropertyName("property_value")
    void setValue(String value);

    @PropertyName("property_value")
    String getValue();

    // TODO: port info type and data formats
}
