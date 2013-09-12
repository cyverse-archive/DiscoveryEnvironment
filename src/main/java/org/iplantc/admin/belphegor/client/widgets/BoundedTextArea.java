package org.iplantc.admin.belphegor.client.widgets;

import com.google.gwt.user.client.Element;


/**
 * A bounded text area field that does not allow users to type beneath the defined maximum length
 * 
 * @author sriram
 *
 */
public class BoundedTextArea extends UsefulTextArea {
    @Override
    public void setMaxLength(int m) {
        super.setMaxLength(m);

        if (rendered) {
            getInputEl().setElementAttribute("maxLength", m); //$NON-NLS-1$
        }
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        getInputEl().setElementAttribute("maxLength", getMaxLength()); //$NON-NLS-1$
    }
}
