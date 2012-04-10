package org.iplantc.de.client.models;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * A config object for PipelineEditorWindow
 * 
 * @author sriram
 * 
 */
public class PipelineEditorWindowConfig extends WindowConfig {
    /**
     * 
     */
    private static final long serialVersionUID = 8475782623579692145L;
    public static final String PIPELINE_CONFIG = "pipeline_config";

    public PipelineEditorWindowConfig(JSONObject json) {
        super(json);
    }

    public JSONObject getPipelineConfig() {
        return JSONParser.parseStrict(get(PIPELINE_CONFIG).toString()).isObject();
    }

}
