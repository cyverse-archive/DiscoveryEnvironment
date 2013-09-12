package org.iplantc.admin.belphegor.client.widgets;

import java.util.HashSet;
import java.util.Set;

import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * 
 * @deprecated Class needs to be deleted or ported to GXT3
 */
@Deprecated
public class BoundedTextField<D> extends TextField<D> {
    private Set<Character> allowed;

    @Override
    public void setMaxLength(int m) {
        super.setMaxLength(m);

        if (rendered) {
            getInputEl().setElementAttribute("maxLength", m);
        }
    }

    /**
     * Limits characters that can be entered.
     * 
     * @param allowed a string containing allowed characters, or null for all characters
     */
    public void setAllowedchars(String allowed) {
        if (allowed == null) {
            this.allowed = null;
        } else {
            this.allowed = new HashSet<Character>();
            for (char c : allowed.toCharArray()) {
                this.allowed.add(c);
            }
        }
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        getInputEl().setElementAttribute("maxLength", getMaxLength());
    }

    @Override
    protected void onKeyPress(FieldEvent fe) {
        if (allowed == null) {
            return;
        }

        Event gwtEvent = fe.getEvent();
        char key = (char)gwtEvent.getCharCode();

        // suppress key if not allowed and it maps to a char (unlike BkSpc, arrow keys, etc.)
        if (!allowed.contains(key) && key != 0) {
            fe.stopEvent();
        }
    }
}
