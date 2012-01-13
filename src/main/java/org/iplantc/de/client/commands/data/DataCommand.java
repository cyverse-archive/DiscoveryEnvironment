package org.iplantc.de.client.commands.data;

import org.iplantc.de.client.controllers.DataMonitor;

import com.google.gwt.json.client.JSONObject;

public interface DataCommand {
    /**
     * Execute command from an payload's data.
     * 
     * @param monitor class for actually performing operation.
     * @param objData data region which contains params.
     */
    void execute(final DataMonitor monitor, JSONObject objData);
}
