package org.iplantc.de.client.images;

import org.iplantc.de.client.images.Icons;

import com.google.gwt.core.client.GWT;

/**
 * Image resources singleton object.
 */
public class Resources {
    /** The singleton instance. */
    public static final Icons ICONS = GWT.create(Icons.class);

}
