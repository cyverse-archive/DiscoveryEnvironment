package org.iplantc.de.client.models;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class AnalysisParameter extends BaseModelData {

    /**
     * 
     */
    private static final long serialVersionUID = -6138724257937111277L;
    public static String PARAMETER_ID = "param_id";
    public static String PARAMETER_NAME = "param_name";
    public static String PARAMETER_TYPE = "param_type";
    public static String PARAMETER_VALUE = "param_value";

    public AnalysisParameter(String id, String name, String type, String value) {
        set(PARAMETER_ID, id);
        set(PARAMETER_NAME, name);
        set(PARAMETER_TYPE, type);
        set(PARAMETER_VALUE, value);
    }

}
