package org.iplantc.de.client.models;

import com.google.gwt.resources.client.ImageResource;

/**
 * Models the data associated to a desktop shortcut.
 * 
 * @author amuir
 * 
 */
public class ShortcutDesc extends ActionTagPair {
    private String id;
    private String caption;

    // private ImageResource icon;

    /**
     * Instantiate from id, caption, action tag.
     * 
     * @param id unique shortcut id.
     * @param caption caption to display under shortcut.
     * @param action user action.
     * @param tag associated tag.
     */
    public ShortcutDesc(String id, String caption, String action, String tag) {
        super(action, tag);

        this.id = id;
        this.caption = caption;

    }

    /**
     * Retrieves the unique identifier.
     * 
     * @return unique identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the caption.
     * 
     * @return shortcut caption.
     */
    public String getCaption() {
        return caption;
    }
}
