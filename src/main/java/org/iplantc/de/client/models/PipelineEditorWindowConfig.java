package org.iplantc.de.client.models;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.models.WindowConfig;

import com.google.gwt.json.client.JSONObject;

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
    public static final String PIPELINE_CONFIG = "pipeline_config"; //$NON-NLS-1$

    public PipelineEditorWindowConfig(JSONObject json) {
        super(json);
    }

    public JSONObject getPipelineConfig() {
        return JsonUtil.getObject(this, PIPELINE_CONFIG);
    }

    public void setPipelineConfig(JSONObject pipelineConfig) {
        put(PIPELINE_CONFIG, pipelineConfig);
    }
}
