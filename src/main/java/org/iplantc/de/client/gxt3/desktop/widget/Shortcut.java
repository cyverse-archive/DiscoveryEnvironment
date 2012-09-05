/**
 * Sencha GXT 3.0.1 - Sencha for GWT Copyright(c) 2007-2012, Sencha, Inc. licensing@sencha.com
 * 
 * http://www.sencha.com/products/gxt/license/
 */
package org.iplantc.de.client.gxt3.desktop.widget;

import org.iplantc.de.client.models.ShortcutDesc;

import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * A desktop shortcut.
 */
public class Shortcut extends TextButton {

    private String action;
    private String tag;

    /**
     * Creates a new shortcut.
     */
    public Shortcut(ShortcutDesc desc, SelectHandler handler) {
        setId(desc.getId());
        setText(desc.getCaption());

        this.action = desc.getAction();
        this.tag = desc.getTag();

        this.addSelectHandler(handler);
        setIcon(desc.getIcon());
    }

    /**
     * Retrieve the action for dispatch.
     * 
     * @return the action associated with this shortcut.
     */
    public String getAction() {
        return action;
    }

    /**
     * Retrieve the tag for dispatch.
     * 
     * @return the tag associated with this shortcut.
     */
    public String getTag() {
        return tag;
    }
}
