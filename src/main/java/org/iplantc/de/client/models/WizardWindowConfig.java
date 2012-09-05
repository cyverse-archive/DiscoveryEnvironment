package org.iplantc.de.client.models;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.models.WindowConfig;

import com.google.gwt.json.client.JSONObject;

/**
 * A config class for app wizard windows
 * 
 * @author sriram
 * 
 */
public class WizardWindowConfig extends WindowConfig {

    /**
     * 
     */
    private static final long serialVersionUID = 1252892800527220654L;
    public static final String WIZARD_CONFIG = "wizardConfig"; //$NON-NLS-1$

    public WizardWindowConfig(JSONObject json) {
        super(json);
    }

    public JSONObject getWizardConfig() {
        return JsonUtil.getObject(this, WIZARD_CONFIG);
    }

    public void setWizardConfig(JSONObject wizardConfig) {
        put(WIZARD_CONFIG, wizardConfig);
    }
}
