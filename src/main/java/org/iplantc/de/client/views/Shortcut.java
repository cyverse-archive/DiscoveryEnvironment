package org.iplantc.de.client.views;

import org.iplantc.de.client.Constants;
import org.iplantc.de.client.models.ShortcutDesc;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.IconSupport;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * A selectable icon and text added to the desktop. Each shortcut requires an id that has matching css
 * styles for the icon.
 * 
 * <pre>
 * &lt;code&gt;
 * Shortcut s2 = new Shortcut();
 * s2.setText(&quot;Accordion Window&quot;);
 * s2.setId(&quot;acc-win-shortcut&quot;);
 * 
 * #acc-win-shortcut img {
 *  width: 48px;
 *  height: 48px;
 *  background-image: url(../images/im48x48.png);
 * }
 * 
 * &lt;/code&gt;
 * </pre>
 */
public class Shortcut extends Component implements IconSupport {
    private String text;
    private String action;
    private String tag;
    private AbstractImagePrototype icon;
    private El iconEl;

    /**
     * Instantiate from description and listener.
     * 
     * @param desc configuration object.
     * @param listener handler for when shortcut is clicked.
     */
    public Shortcut(ShortcutDesc desc, SelectionListener<ComponentEvent> listener) {
        setId(desc.getId());
        setText(desc.getCaption());

        this.action = desc.getAction();
        this.tag = desc.getTag();

        addListener(Events.Select, listener);
    }

    /**
     * Called when the shortcut is clicked.
     * 
     * @param ce click event.
     */
    protected void onClick(ComponentEvent ce) {
        ce.stopEvent();
        fireEvent(Events.Select, ce);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);

        setElement(DOM.createElement("dt"), target, index); //$NON-NLS-1$
        El a = el().createChild("<a href='#'></a>"); //$NON-NLS-1$
        String iconDiv = "<div style='width:" + Constants.CLIENT.shortcutWidth() + "; height:" //$NON-NLS-1$ //$NON-NLS-2$
                + Constants.CLIENT.shortcutHeight() + ";'><img src='" + GXT.BLANK_IMAGE_URL //$NON-NLS-1$
                + "' /></div>"; //$NON-NLS-1$
        iconEl = a.createChild(iconDiv);
        El txt = a.createChild("<div></div>"); //$NON-NLS-1$

        if (txt != null) {
            txt.setInnerHtml(text);
        }

        el().updateZIndex(0);
        sinkEvents(Event.ONCLICK);

        setIconStyle(getId());
    }

    /**
     * Retrieve icon for display.
     * 
     * @return icon for display.
     */
    public AbstractImagePrototype getIcon() {
        return icon;
    }

    /**
     * Returns the shortcut's text.
     * 
     * @return the text to display under shortcut.
     */
    public String getText() {
        return text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onComponentEvent(ComponentEvent ce) {
        super.onComponentEvent(ce);

        if (ce.getEventTypeInt() == Event.ONCLICK) {
            onClick(ce);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIcon(AbstractImagePrototype icon) {
        if (rendered) {
            iconEl.setInnerHtml(""); //$NON-NLS-1$
            iconEl.appendChild((Element)icon.createElement().cast());
        }

        this.icon = icon;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIconStyle(String styleName) {
        setIcon(IconHelper.create(styleName, Constants.CLIENT.shortcutWidth(),
                Constants.CLIENT.shortcutHeight()));
    }

    /**
     * Sets the shortcuts text.
     * 
     * @param text the text
     */
    private void setText(String text) {
        this.text = text;
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