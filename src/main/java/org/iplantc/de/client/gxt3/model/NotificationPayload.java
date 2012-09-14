/**
 * 
 */
package org.iplantc.de.client.gxt3.model;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * @author sriram
 * 
 */
public interface NotificationPayload {

    @PropertyName("action")
    String getAction();

    @PropertyName("action")
    void setAction(String action);

    @PropertyName("analysis-details")
    String getAnalysisDetails();

    @PropertyName("analysis-details")
    void setAnalysisDetails(String analysisdetails);

    @PropertyName("analysis_id")
    void setAnalysisId(String analysisId);

    @PropertyName("analysis_id")
    String getAnalysisId();

    @PropertyName("analysis_name")
    void setAnalysisName(String name);

    @PropertyName("analysis_name")
    String getAnalysisName();

    @PropertyName("description")
    void setDescription(String desc);

    @PropertyName("description")
    String getDescription();

    @PropertyName("enddate")
    long getEndDate();

    @PropertyName("enddate")
    void setEndDate(long date);

    @PropertyName("id")
    void setId(String id);

    @PropertyName("id")
    String getId();

    @PropertyName("name")
    void setName(String name);

    @PropertyName("name")
    String getName();

    @PropertyName("resultfolderid")
    void setResultFolderId(String resultId);

    @PropertyName("resultfolderid")
    String getResultFolderId();

    @PropertyName("startdate")
    long getStartDate();

    @PropertyName("startdate")
    void setStartDate(long startdate);

    @PropertyName("status")
    String getStatus();

    @PropertyName("status")
    void setStatus(String status);

}
