package org.iplantc.de.client.models;

/**
 * Action/tag pair used for dispatching user events.
 * 
 * @author amuir
 * 
 */
public class ActionTagPair {
    protected String action;
    protected String tag;

    /**
     * Instantiate from an action and tag.
     * 
     * @param action string representation of a user action.
     * @param tag string representation of the associated tag.
     */
    public ActionTagPair(final String action, final String tag) {
        if (action != null) {
            this.action = action;
        }

        if (tag != null) {
            this.tag = tag;
        }
    }

    /**
     * Default constructor.
     */
    public ActionTagPair() {
        this("", ""); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Retrieve action.
     * 
     * @return action field.
     */
    public String getAction() {
        return action;
    }

    /**
     * Retrieve tag.
     * 
     * @return tag field.
     */
    public String getTag() {
        return tag;
    }
}
