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

    @PropertyName("param_value")
    void setValue(String value);

    @PropertyName("param_value")
    String getValue();

    @PropertyName("info_type")
    String getInfoType();

    @PropertyName("info_type")
    void setInfoType(String infoType);

    @PropertyName("data_format")
    String getDataFormat();

    @PropertyName("data_format")
    void setDataFormat(String format);
}
