package org.iplantc.admin.belphegor.client.models;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * 
 * A java script object to model reference genome
 * 
 * @author sriram
 * 
 */
public class JsReferenceGenome extends JavaScriptObject {

    protected JsReferenceGenome() {

    }

    public final native String getId() /*-{
		return this.id;
    }-*/;

    public final native String getUUID() /*-{
		return this.uuid;
    }-*/;

    public final native String getName() /*-{
		return this.name;
    }-*/;

    public final native String getPath() /*-{
		return this.path;
    }-*/;

    public final native Boolean getDeleted() /*-{
		return this.deleted;
    }-*/;

    public final native Long getCreatedOn() /*-{
		return this.created_on;
    }-*/;

    public final native String getUserName() /*-{
		return this.created_by;
    }-*/;

    public final native String getLastModifiedBy() /*-{
		return this.last_modified_by;
    }-*/;

    public final native String getLastModifiedOn() /*-{
		return this.last_modified_On;
    }-*/;

}
