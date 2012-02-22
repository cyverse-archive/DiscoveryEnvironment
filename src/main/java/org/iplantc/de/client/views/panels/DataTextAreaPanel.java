package org.iplantc.de.client.views.panels;

import java.util.List;

import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.File;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.user.client.Element;

public abstract class DataTextAreaPanel extends ContentPanel {
    private boolean readyForResizeHandling;
    protected DataTextArea area;
    protected DiskResource file;

    protected DataTextAreaPanel() {
        area = new DataTextArea();

        init();
    }

    protected void init() {
        setVisible(false);
        setBodyStyle("background-color: #EDEDED"); //$NON-NLS-1$
        setHeight(getInitialHeight());
        setHeading(getHeadingText());

        hide();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        add(area);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterRender() {
        super.afterRender();

        readyForResizeHandling = true;
        handleResize();
    }

    public void handleResize() {
        if (readyForResizeHandling) {
            area.setSize(getWidth(), getHeight());
        }
    }

    protected abstract String getHeadingText();

    protected abstract int getInitialHeight();

    protected abstract void updateDisplay();

    private DiskResource getSelectedFile(final List<DiskResource> resources) {
        DiskResource ret = null; // assume failure

        // do we have only one item?
        if (resources != null && resources.size() == 1) {
            DiskResource test = resources.get(0);

            if (test instanceof File) {
                ret = test;
            }
        }

        return ret;
    }

    protected void displayValue(String value) {
        if (value == null) {
            value = ""; //$NON-NLS-1$
        }

        area.setValue(value);
    }

    public void update(final List<DiskResource> resources) {
        // do we have one (and only one) file?
        file = getSelectedFile(resources);

        if (file == null) {
            // we do not... simply hide
            hide();
        } else {
            // getting the manifest sets a chain of rpc calls that will eventually
            // lead to preview data being displayed
            updateDisplay();
        }
    }

    protected class DataTextArea extends TextArea {
        public DataTextArea() {
            init();
        }

        private void init() {
            setInputStyleAttribute("font-size", "8px"); //$NON-NLS-1$ //$NON-NLS-2$
            setSelectOnFocus(true);
            setReadOnly(true);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void afterRender() {
            super.afterRender();
            el().setElementAttribute("spellcheck", "false"); //$NON-NLS-1$ //$NON-NLS-2$
            setHeight(116);   // reduce height so the scroll buttons are fully visible
        }
    }
}
