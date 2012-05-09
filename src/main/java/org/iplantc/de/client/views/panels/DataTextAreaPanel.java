package org.iplantc.de.client.views.panels;

import org.iplantc.core.uidiskresource.client.models.DiskResource;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.user.client.Element;

public abstract class DataTextAreaPanel extends LayoutContainer {
    private boolean readyForResizeHandling;
    protected DataTextArea area;
    protected DiskResource file;

    protected DataTextAreaPanel() {
        area = new DataTextArea();
        area.setPreventScrollbars(true);

        init();
    }

    protected void init() {
        setHeight(getInitialHeight());

        area.hide();
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

    protected void displayValue(String value) {
        if (value == null) {
            value = ""; //$NON-NLS-1$
        }

        area.setValue(value);
    }

    public void update(DiskResource resource) {
        file = resource;

        // getting the manifest sets a chain of rpc calls that will eventually
        // lead to preview data being displayed
        updateDisplay();
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
        }
    }
}
