package org.iplantc.de.client.models;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * 
 * A native class to model collaborators
 * 
 * 
 * @author sriram
 * 
 */
public class JsCollaborators extends JavaScriptObject {

    protected JsCollaborators() {

    }

    public final native String getFirstName() /*-{
		return this.firstname;
    }-*/;

    public final native String getId() /*-{
		return this.id;
    }-*/;

    public final native String getLastName() /*-{
		return this.lastname;
    }-*/;

    public final native String getEmail() /*-{
		return this.email;
    }-*/;

    public final native String getUserName() /*-{
		return this.username;
    }-*/;

}
