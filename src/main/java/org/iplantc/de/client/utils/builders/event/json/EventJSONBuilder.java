package org.iplantc.de.client.utils.builders.event.json;

/**
 * Interface for event JSON builders.
 * 
 * @author amuir
 * 
 */
public interface EventJSONBuilder {
    /**
     * Interface to build a payload event from RPC success JSON.
     * 
     * @param json JSON returned from an RPC.
     * @return JSON for a new payload event.
     */
    String build(String json);
}
