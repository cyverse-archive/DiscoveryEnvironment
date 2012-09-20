package org.iplantc.de.client.gxt3.model;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface Analysis {

    @PropertyName("wiki_url")
    public void setWikiUrl(String url);

    @PropertyName("resultfolderid")
    public String getResultFolderId();

    @PropertyName("startdate")
    public long getStartDate();

    @PropertyName("enddate")
    public long getEndDate();

    @PropertyName("analysis_id")
    public String getAnalysisId();

    @PropertyName("analysis_name")
    public String getAnalysisName();

    @PropertyName("analysis_details")
    public String getAnalysisDetails();

    @PropertyName("status")
    public String getStatus();

    @PropertyName("description")
    public String getDescription();

    @PropertyName("startdate")
    public void setStartDate(long startdate);

    @PropertyName("enddate")
    public void setEndDate(long enddate);

    @PropertyName("analysis_id")
    public void setAnalysisId(String analysis_id);

    @PropertyName("analysis_name")
    public void setAnalysisName(String analysisname);

    @PropertyName("analysis_details")
    public void setAnalysisDetails(String analysis_details);

    @PropertyName("status")
    public void setStatus(String status);

    @PropertyName("description")
    public void setDescription(String description);

    @PropertyName("id")
    public void setId(String id);

    @PropertyName("id")
    public String getId();

    @PropertyName("name")
    public String getName();

    @PropertyName("name")
    public void setName(String name);

    @PropertyName("resultfolderid")
    public void setResultFolderId(String resultfolderid);

    @PropertyName("wiki_url")
    public String getWikiUrl();
}
