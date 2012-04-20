package org.iplantc.de.client.models;

import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.json.client.JSONObject;

/**
 * 
 * A config class for Analysis window
 * 
 * @author sriram
 * 
 */
public class AnalysesWindowConfig extends WindowConfig {
    public static final String ANALYSIS_ID = "analysisId";

    public AnalysesWindowConfig(JSONObject json) {
        super(json);
    }

    public void setAnalysisId(String id) {
        setString(ANALYSIS_ID, id);
    }

    public String getAnalysisId() {
        return JsonUtil.getRawValueAsString(get(ANALYSIS_ID));
    }

}
