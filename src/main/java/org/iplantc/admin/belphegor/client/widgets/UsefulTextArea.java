package org.iplantc.admin.belphegor.client.widgets;

import com.extjs.gxt.ui.client.widget.form.TextArea;

/**
 * Text area without spell check enabled.
 * 
 * @author amuir
 * @deprecated Class needs to be deleted or ported to GXT3
 */
@Deprecated
public class UsefulTextArea extends TextArea {
    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterRender() {
        super.afterRender();
        el().setElementAttribute("spellcheck", "false"); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
