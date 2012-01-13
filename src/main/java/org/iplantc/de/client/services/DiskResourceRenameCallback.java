package org.iplantc.de.client.services;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Defines an asynchronous callback for DiskResource (a file or folder) rename event.
 * 
 * @author amuir
 * 
 */
public abstract class DiskResourceRenameCallback extends DiskResourceServiceCallback {
    protected final String id;
    protected final String name;

    /**
     * Instantiate from id and name.
     * 
     * @param id id of resource to rename.
     * @param name new disk resource name.
     */
    public DiskResourceRenameCallback(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JSONObject buildPayload(final JSONObject jsonResult) {
        JSONObject ret = new JSONObject();

        ret.put("id", new JSONString(id)); //$NON-NLS-1$
        ret.put("name", new JSONString(name)); //$NON-NLS-1$

        return ret;
    }
}
