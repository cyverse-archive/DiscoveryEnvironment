package org.iplantc.de.client.models;

import com.google.gwt.json.client.JSONObject;

/**
 * Configuration object for the analysis window
 * 
 * @author amuir
 * 
 */
public class BasicWindowConfig extends WindowConfig {
    /**
	 * 
	 */
    private static final long serialVersionUID = 2566711649445703315L;

    /**
     * Instantiates from the JSON parameters.
     * 
     * @param json JSON object containing initialization data.
     */
    public BasicWindowConfig(JSONObject json) {
        super(json);
    }

    /** Returns the unique id of the window */
    public String getId() {
        return get("id"); //$NON-NLS-1$
    }
}
