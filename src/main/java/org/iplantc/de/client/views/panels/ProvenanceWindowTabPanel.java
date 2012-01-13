package org.iplantc.de.client.views.panels;

import org.iplantc.de.client.views.tabs.ProvenanceTab;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;

/**
 * Provides a user interface for a tabbed provenance window panel.
 */
public class ProvenanceWindowTabPanel extends TabPanel {
    /**
     * Constructs a default instance of the widget.
     */
    public ProvenanceWindowTabPanel() {
        setMinTabWidth(55);
        setResizeTabs(true);
        setAnimScroll(true);
        setTabScroll(true);
    }

    /**
     * Add a panel as a tab to this widget.
     * 
     * @param panel to display.
     * @param provenance provenance information to display.
     */
    public void addTab(ProvenanceContentPanel panel, String provenance) {
        if (panel != null) {
            int idx = panel.getTabIndex();

            if (idx < 0 || idx > this.getItems().size()) {
                add(new ProvenanceTab(panel, provenance));
            } else {
                insert(new ProvenanceTab(panel, provenance), idx);
            }

            // select the first tab because tabs are added after rendering, so even if autoSelect is
            // true, nothing is automatically selected
            setSelection(getItem(0));

            layout();
        }
    }

    /**
     * Update the provenance associated with the data for the tab.
     * 
     * @param provenance a string representing information pertaining to the tab in the data.
     */
    public void updateProvenance(String provenance) {
        for (TabItem item : getItems()) {
            ProvenanceTab tab = (ProvenanceTab)item;
            tab.updateProvenance(provenance);
        }
    }



    /**
     * Indicates of the data on the tab has changes that are not committed to the server.
     * 
     * @return true when there are unsaved changes; otherwise false.
     */
    public boolean isDirty() {
        boolean ret = false; // assume clean

        for (TabItem item : getItems()) {
            ProvenanceTab tab = (ProvenanceTab)item;

            if (tab.isDirty()) {
                ret = true;
                break;
            }
        }

        return ret;
    }
}
