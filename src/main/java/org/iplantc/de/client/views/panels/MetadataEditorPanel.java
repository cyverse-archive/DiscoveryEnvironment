/**
 * 
 */
package org.iplantc.de.client.views.panels;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * 
 * A panel that will fetch / save metadata
 * 
 * @author sriram
 * 
 */
public abstract class MetadataEditorPanel extends ContentPanel {

    public MetadataEditorPanel() {
        setSize(500, 300);
        setHeaderVisible(false);
        setLayout(new FitLayout());
    }

    public abstract void UpdateMetadata();

    protected abstract void retrieveMetaData();

    public abstract boolean isDirty();

}
