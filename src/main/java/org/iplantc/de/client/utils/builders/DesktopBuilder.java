package org.iplantc.de.client.utils.builders;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.de.client.models.ShortcutDesc;

import com.google.gwt.resources.client.ImageResource;

/**
 * Abstract class for managing desktop shortcuts.
 * 
 * @author amuir
 * 
 */
public abstract class DesktopBuilder {
    private List<ShortcutDesc> shortcuts = new ArrayList<ShortcutDesc>();

    /**
     * Default constructor.
     */
    public DesktopBuilder() {
        buildShortcuts();
    }

    /**
     * Creates desktop shortcut widgets.
     */
    protected abstract void buildShortcuts();

    /**
     * Helper method to add a new shortcut.
     * 
     * @param id shortcut id.
     * @param caption shortcut caption.
     * @param action action to be fired on shortcut click.
     * @param tag associated tag.
     */
    protected void addShortcut(String id, String caption, String action, String tag, ImageResource icon) {
        shortcuts.add(new ShortcutDesc(id, caption, action, tag, icon));
    }

    /**
     * Retrieves all added shortcuts.
     * 
     * @return all shortcuts.
     */
    public List<ShortcutDesc> getShortcuts() {
        return shortcuts;
    }
}
