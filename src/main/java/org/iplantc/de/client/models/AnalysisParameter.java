package org.iplantc.de.client.models;

import org.iplantc.core.jsonutil.JsonUtil;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

public class AnalysisParameter extends BaseModelData {

    /**
     * 
     */
    private static final long serialVersionUID = -6138724257937111277L;
    public static String PARAMETER_ID = "param_id";
    public static String PARAMETER_NAME = "param_name";
    public static String PARAMETER_TYPE = "param_type";
    public static String PARAMETER_VALUE = "param_value";
    public static String INFO_TYPE = "info_type";
    public static String DATA_FORMAT = "data_format";

    public AnalysisParameter(JSONObject obj) {
        if (obj != null) {
            set(PARAMETER_ID, JsonUtil.getString(obj, PARAMETER_ID));
            set(PARAMETER_NAME, JsonUtil.getString(obj, PARAMETER_NAME));
            set(PARAMETER_TYPE, JsonUtil.getString(obj, PARAMETER_TYPE));
            set(INFO_TYPE, JsonUtil.getString(obj, INFO_TYPE));
            set(DATA_FORMAT, JsonUtil.getString(obj, DATA_FORMAT));
            setParameter(obj.get(PARAMETER_VALUE).toString());
        }
    }

    public void setParameter(String value) {
        if (value != null && !value.isEmpty()) {
            JSONObject obj = JSONParser.parseStrict(value).isObject();
            if (obj != null) {
                set(PARAMETER_VALUE, JsonUtil.trim(obj.get("display").toString()));
            } else {
                set(PARAMETER_VALUE, JsonUtil.trim(value));
            }
        } else {
            set(PARAMETER_VALUE, "");
        }
    }

    public String getId() {
        return get(PARAMETER_ID);
    }

    public String getParamName() {
        return get(PARAMETER_NAME);
    }

    public String getParamType() {
        return get(PARAMETER_TYPE);
    }

    public String getParamValue() {
        return get(PARAMETER_VALUE);
    }

    public String getInfoType() {
        return get(INFO_TYPE);
    }

    public String getDataFormat() {
        return get(DATA_FORMAT);
    }

}
