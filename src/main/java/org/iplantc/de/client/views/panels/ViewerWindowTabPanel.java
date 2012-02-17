package org.iplantc.de.client.views.panels;

import org.iplantc.de.client.views.tabs.ViewTab;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;

/**
 * Provides a user interface for a tabbed window panel.
 */
public class ViewerWindowTabPanel extends TabPanel {
    /**
     * Constructs a default instance of the widget.
     */
    public ViewerWindowTabPanel() {
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
    public void addTab(ViewerContentPanel panel) {
        if (panel != null) {
            int idx = panel.getTabIndex();
            ViewTab item = new ViewTab(panel);
            if (idx < 0 || idx > this.getItems().size()) {
                add(item);
            } else {
                insert(item, idx);
            }

            // select the first tab because tabs are added after rendering, so even if autoSelect is
            // true, nothing is automatically selected
            setSelection(getItem(0));
            layout();
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
            ViewTab tab = (ViewTab)item;

            if (tab.isDirty()) {
                ret = true;
                break;
            }
        }

        return ret;
    }
}
