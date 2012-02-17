package org.iplantc.de.client.views.panels;

import org.iplantc.core.uidiskresource.client.models.FileIdentifier;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.Element;

/**
 * Provides a user interface for provenance related content.
 * 
 * @author amuir
 * 
 */
public abstract class ViewerContentPanel extends ContentPanel {
    protected BorderLayoutData centerData;
    protected BorderLayoutData southData;
    protected FileIdentifier fileIdentifier;
    protected boolean dirty = false;

    /**
     * Instantiate from a file identifier.
     * 
     * @param fileIdentifier identifier of file containing provenance information.
     */
    protected ViewerContentPanel(FileIdentifier fileIdentifier) {
        this.fileIdentifier = fileIdentifier;
        setHeight(380);
        setHeaderVisible(false);

        centerData = buildCenterData();
    }

    /**
     * Build center data for BorderLayout.
     * 
     * @return an object describing the layout for the panel.
     */
    protected BorderLayoutData buildCenterData() {
        BorderLayoutData ret = new BorderLayoutData(LayoutRegion.CENTER, 400);
        ret.setMargins(new Margins(0, 0, 0, 0));

        return ret;
    }

    /**
     * Build south data for BorderLayout.
     * 
     * @return south region of border layout.
     */
    protected BorderLayoutData buildSouthData() {
        BorderLayoutData ret = new BorderLayoutData(LayoutRegion.SOUTH, 80);

        ret.setSplit(true);
        ret.setCollapsible(true);
        ret.setFloatable(false);
        ret.setMargins(new Margins(0, 0, 0, 0));
        ret.setMinSize(36);
        ret.setMaxSize(260);

        return ret;
    }

    /**
     * Initialize text area widget.
     * 
     * @param editable flag to indicate whether text area contents are allowed to be edited.
     * @return newly allocted text area.
     */
    protected TextArea buildTextArea(boolean editable) {
        TextArea ret = new TextArea();

        ret.setHideLabel(true);

        if (!editable) {
            ret.setAutoValidate(false);
            ret.setHideLabel(true);
            ret.setReadOnly(true);
        }

        return ret;
    }

    /**
     * Set file identifier.
     * 
     * @param fileIdentifier new file identifier.
     */
    public void setFileIdentifier(FileIdentifier fileIdentifier) {
        this.fileIdentifier = fileIdentifier;
    }

    /**
     * Retrieve dirty flag.
     * 
     * @return true if the user has unsaved changes.
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Retrieve tab header.
     * 
     * @return text to be displayed as the tab caption.
     */
    public abstract String getTabHeader();

    /**
     * Retrieve tab index.
     * 
     * @return desired tab position.
     */
    public abstract int getTabIndex();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new BorderLayout());
    }
}
