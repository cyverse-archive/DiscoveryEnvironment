package org.iplantc.de.client.views.tabs;

import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.views.panels.ProvenanceContentPanel;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;

/**
 * Provides a user interface definition for presenting provenanace.
 */
public class ProvenanceTab extends TabItem {
    private ProvenanceContentPanel panel;

    /**
     * Constructs an instance of the tab given a panel and provenance description.
     * 
     * @param panel a panel to display in the body of the tab.
     * @param provenance a provenance text related to the data in the panel.
     */
    public ProvenanceTab(ProvenanceContentPanel panel, String provenance) {
        this.panel = panel;

        setHeight(410);

        setHeader();
        updateProvenance(provenance);
        setLayout(new FitLayout());
    }

    /**
     * Sets the header text to display in the tab.
     */
    private void setHeader() {
        if (panel != null) {
            String header = panel.getTabHeader();

            if (header != null) {
                setText(header);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        if (panel != null) {
            add(panel);
        }
    }

    /**
     * Perform an "update" operation on the provenance text.
     * 
     * @param provenance a string containing provenance text.
     */
    public void updateProvenance(String provenance) {
        if (panel != null) {
            panel.updateProvenance(provenance);
        }
    }

    /**
     * Sets the file identifier associated with the tab.
     * 
     * @param file the identifier for the file.
     */
    public void setFileIdentifier(FileIdentifier file) {
        if (panel != null) {
            panel.setFileIdentifier(file);
        }
    }

    /**
     * Indicates if the tab contains user changes that have not be committed.
     * 
     * @return true, if the tab contains uncommitted changes; otherwise false.
     */
    public boolean isDirty() {
        return (panel == null) ? false : panel.isDirty();
    }
}
