package org.iplantc.de.client.views.dialogs;

import java.util.List;
import java.util.Map;

import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.DiskResourceMetadata;
import org.iplantc.de.client.services.DiskResourceMetadataUpdateCallback;
import org.iplantc.de.client.services.FolderServiceFacade;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

/**
 * 
 * Ok Button Listener for DiskResourceMetadataEditorDialog
 * 
 * @author sriram
 * 
 */

public class DiskResourceMetadataEditorDialogOkBtnListener extends SelectionListener<ButtonEvent> {
    private List<DiskResource> resources;
    private Map<String, Object> toUpdate;

    public DiskResourceMetadataEditorDialogOkBtnListener(List<DiskResource> resources) {
        this.resources = resources;
    }

    @Override
    public void componentSelected(ButtonEvent ce) {
        if (toUpdate != null) {
            JSONArray arr = new JSONArray();
            FolderServiceFacade facade = new FolderServiceFacade();
            int i = 0;
            @SuppressWarnings("unchecked")
            List<DiskResourceMetadata> metadatas = (List<DiskResourceMetadata>)toUpdate.get("add");
            for (DiskResourceMetadata r : metadatas) {
                arr.set(i++, r.toJson());
            }
            JSONObject obj = new JSONObject();
            obj.put("avus", arr);
            facade.setMetaData(resources.get(0), obj.toString(),
                    new DiskResourceMetadataUpdateCallback());
        }
    }

    /**
     * Set records to updated after ok btn is clicked in the dialog
     * 
     * @param toUpdate map containing metadate to add and delete
     */
    public void setUpdateRecords(final Map<String, Object> toUpdate) {
        this.toUpdate = toUpdate;
    }

}
