package org.iplantc.de.client.models;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * Models common properties of identifiable base model data objects.
 * 
 * @author sriram
 * 
 */
public class DEBaseModelData extends BaseModelData {
    /**
     * Unique identifier for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an instance of the object.
     */
    protected DEBaseModelData() {

    }

    /**
     * Constructs an instance of the object.
     * 
     * @param id a unique identifier for the model object.
     * @param name a name associated with the model object.
     */
    protected DEBaseModelData(String id, String name) {
        set("id", id); //$NON-NLS-1$
        set("name", name); //$NON-NLS-1$
    }

    /**
     * Get id of the object.
     * 
     * @return id of the object.
     */
    public String getId() {
        return get("id").toString(); //$NON-NLS-1$
    }

    /**
     * Get the name of the object .
     * 
     * @return name of the object.
     */
    public String getName() {
        return get("name").toString(); //$NON-NLS-1$
    }
}
