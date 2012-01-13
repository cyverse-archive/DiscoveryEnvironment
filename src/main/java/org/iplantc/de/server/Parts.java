package org.iplantc.de.server;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * A collection of Part objects.
 * 
 * @see org.iplantc.de.server.Part
 */
public class Parts {

    private Set<Part> parts = new HashSet<Part>();

    /**
     * Constructs an instance of the object.
     */
    public Parts() {

    }

    /**
     * Adds a part object to the collection.
     * 
     * @param part the part object to add.
     */
    public void add(Part part) {
        parts.add(part);
    }

    /**
     * Gets a param part by name associated with it.
     * 
     * @param name name of the param part object.
     * @return an instance of a param part object; otherwise null to indicate not found.
     */
    public ParamPart getParamPart(String name) {
        for (Part part : parts) {
            if (part instanceof ParamPart && StringUtils.equals(name, part.getName())) {
                return (ParamPart)part;
            }
        }
        return null;
    }
}