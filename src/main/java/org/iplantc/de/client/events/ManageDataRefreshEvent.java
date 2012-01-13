package org.iplantc.de.client.events;

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
    
    public ManageDataRefreshEvent(String tag, String currentFolderId) {
        this.currentFolderId = currentFolderId;
        this.setTag(tag);
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

}
