package org.iplantc.de.client.models;

import com.google.gwt.json.client.JSONObject;

/**
 * A config class for app wizard windows
 * 
 * @author sriram
 * 
 */
public class WizardWindowConfig extends BasicWindowConfig {

    /**
     * 
     */
    private static final long serialVersionUID = 1252892800527220654L;
    public static final String WIZARD_CONFIG = "wizardConfig";

    public WizardWindowConfig(JSONObject json) {
        super(json);
    }

}
