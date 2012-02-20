package org.iplantc.de.client.views.tabs;

import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.views.panels.ViewerContentPanel;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;

/**
 * Provides a user interface definition for presenting viewing file.
 */
public class ViewTab extends TabItem {
    private ViewerContentPanel panel;

    /**
     * Constructs an instance of the tab given a panel
     * 
     * @param panel a panel to display in the body of the tab.
     */
    public ViewTab(ViewerContentPanel panel) {
        this.panel = panel;

        setHeight(410);

        setHeader();
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
