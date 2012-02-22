/**
 * 
 */
package org.iplantc.de.client.models;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * 
 * A class that uses native methods to return fields for attributes.
 * 
 * This object simulates a Plain Old Java Object from the Java perspective and use the JavaScript Native
 * Interface to inter-operate with the JSON representation.
 * 
 * @author sriram
 * 
 */
public class JsAnalysisParameter extends JavaScriptObject {

    protected JsAnalysisParameter() {

    }

    /**
     * Gets an internal identifier value for the parameter from the object.
     * 
     * @return a string representing a unique identifier for the parameter
     */
    public final native String getId() /*-{
		return this.param_id;
    }-*/;

    /**
     * Gets the name of the parameter from the object.
     * 
     * @return a string representing the name of the parameter
     */
    public final native String getName() /*-{
		return this.param_name;
    }-*/;

    /**
     * Gets the type of the parameter from the object.
     * 
     * @return a string representing the type of the parameter
     */
    public final native String getType() /*-{
		return this.param_type;
    }-*/;

    /**
     * Gets the value of the parameter from the object.
     * 
     * @return a string representing the type of the parameter
     */
    public final native String getValue() /*-{
		return this.param_value;
    }-*/;

}
