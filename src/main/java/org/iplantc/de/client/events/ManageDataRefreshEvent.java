package org.iplantc.de.client.events;

import java.util.List;

import org.iplantc.core.uidiskresource.client.models.DiskResource;

import com.google.gwt.event.shared.GwtEvent;

public class ManageDataRefreshEvent extends GwtEvent<ManageDataRefreshEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.ManageDataRefreshEventHandler
     */
    public static final GwtEvent.Type<ManageDataRefreshEventHandler> TYPE = new GwtEvent.Type<ManageDataRefreshEventHandler>();

    private String currentFolderId;
    private String tag;
    private List<DiskResource> resources;

    public ManageDataRefreshEvent(String tag, String currentFolderId, List<DiskResource> resources) {
        this.currentFolderId = currentFolderId;
        this.setTag(tag);
        this.setResources(resources);
    }

    @Override
    protected void dispatch(ManageDataRefreshEventHandler arg0) {
        arg0.onRefresh(this);

    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<ManageDataRefreshEventHandler> getAssociatedType() {
        return TYPE;
    }

    public String getCurrentFolderId() {
        return currentFolderId;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param resources the resources to set
     */
    public void setResources(List<DiskResource> resources) {
        this.resources = resources;
    }

    /**
     * @return the resources
     */
    public List<DiskResource> getResources() {
        return resources;
    }

}
