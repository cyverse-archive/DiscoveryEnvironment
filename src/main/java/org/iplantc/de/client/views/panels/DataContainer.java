package org.iplantc.de.client.views.panels;

import java.util.List;

import org.iplantc.core.uidiskresource.client.models.DiskResource;

import com.extjs.gxt.ui.client.store.ListStore;

public interface DataContainer {
    /**
     * Gets the path of the current folder for this container.
     * 
     * @return The current folder path.
     */
    String getCurrentPath();



    /**
     * Get items selected in this container.
     * 
     * @return A list of selected DiskResource objects.
     */
    List<DiskResource> getSelectedItems();

    /**
     * Gets the list store of this data container.
     * 
     * @return A ListStore of DiskResource objects.
     */
    ListStore<DiskResource> getDataStore();
}
