package org.iplantc.de.client.models;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A class that uses native methods to return fields for attributes.
 * 
 * This object simulates a Plain Old Java Object from the Java perspective and use the JavaScript Native
 * Interface to inter-operate with the JSON representation.
 * 
 * @author sriram
 * 
 */
public class JsAnalysisExecution extends JavaScriptObject {

    protected JsAnalysisExecution() {

    }

    /**
     * Gets the name value of the execution from the object.
     * 
     * @return a string representing the name of the Analysis
     */
    public final native String getName() /*-{
                                         return this.name;
                                         }-*/;

    /**
     * Gets the status of the execution from the object
     * 
     * @return a string representing the status of the execution
     */
    public final native String getStatus() /*-{
                                           return this.status;
                                           }-*/;

    /**
     * Gets the start date/time of the execution from the object
     * 
     * @return a string representing the start date/time of the execution
     */
    public final native String getStartDate() /*-{
                                              return this.startdate;
                                              }-*/;

    /**
     * Gets the id of the analysis id
     * 
     * @return a string representing the analysis id
     */
    public final native String getAnalysisId() /*-{
                                               return this.analysis_id;
                                               }-*/;

    /**
     * Gets the name of the analysis
     * 
     * @return a string representing the analysis name
     */
    public final native String getAnalysisName() /*-{
                                                 return this.analysis_name;
                                                 }-*/;

    /**
     * Gets the details of the analysis
     * 
     * @return a string representing the analysis details
     */
    public final native String getAnalysisDetails() /*-{
                                                    return this.analysis_details;
                                                    }-*/;

    /**
     * Gets the end date/time of the execution from the object
     * 
     * @return a string representing the end date/time of the execution
     */
    public final native String getEndDate() /*-{
                                            return this.enddate;
                                            }-*/;

    /**
     * Gets an internal identifier value for the file from the object.
     * 
     * @return a string representing a unique identifier for the Analysis
     */
    public final native String getId() /*-{
                                       return this.id;
                                       }-*/;

    /**
     * Gets the description of the execution
     * 
     * @return a string representing a execution's description
     */
    public final native String getDescription() /*-{
                                                return this.description;
                                                }-*/;

    /**
     * Gets the result folder id for the execution
     * 
     * @return a string representing a execution's result folder id
     */
    public final native String getResultFolderId() /*-{
                                                   return this.resultfolderid;
                                                   }-*/;

    
    /**
     * Gets the wiki url for the app
     * 
     * @return a string representing a wiki url for the app
     */
    public final native String getWikiUrl() /*-{
                                                   return this.wiki_url;
                                                   }-*/;
}
