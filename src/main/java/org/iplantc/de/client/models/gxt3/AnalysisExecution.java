package org.iplantc.de.client.models.gxt3;

import java.io.Serializable;
import java.util.Date;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.util.DateParser;
import org.iplantc.de.client.models.JsAnalysisExecution;

import com.google.gwt.json.client.JSONObject;

public class AnalysisExecution implements Serializable {
    private static final long serialVersionUID = 141223548476503700L;

    private String id;
    private String name;
    private String description;
    private String analysisId;
    private String analysisName;
    private String analysisDetails;
    private Date startDate;
    private Date endDate;
    private String status;
    private String resultFolderId;
    private String wikiUrl;

    public AnalysisExecution(JsAnalysisExecution exec) {
        setId(exec.getId());
        setName(exec.getName());
        setDescription(JsonUtil.formatString(exec.getDescription()));
        setAnalysisId(exec.getAnalysisId());
        setAnalysisName(exec.getAnalysisName());
        setAnalysisDetails(exec.getAnalysisDetails());
        setStartDate(DateParser.parseDate(exec.getStartDate()));
        setEndDate(DateParser.parseDate(exec.getEndDate()));
        setStatus(exec.getStatus());
        setResultFolderId(exec.getResultFolderId());
        setWikiUrl(exec.getWikiUrl());
    }

    public AnalysisExecution(JSONObject payload) {
        setId(JsonUtil.getString(payload, "id")); //$NON-NLS-1$
        setName(JsonUtil.getString(payload, "name")); //$NON-NLS-1$
        setDescription(JsonUtil.getString(payload, "description")); //$NON-NLS-1$
        setAnalysisId(JsonUtil.getString(payload, "analysis_id")); //$NON-NLS-1$
        setAnalysisName(JsonUtil.getString(payload, "analysis_name")); //$NON-NLS-1$
        setAnalysisDetails(JsonUtil.getString(payload, "analysis_details")); //$NON-NLS-1$
        setStartDate(DateParser.parseDate(JsonUtil.getString(payload, "startdate"))); //$NON-NLS-1$
        setEndDate(DateParser.parseDate(JsonUtil.getString(payload, "enddate"))); //$NON-NLS-1$
        setStatus(JsonUtil.getString(payload, "status")); //$NON-NLS-1$
        setResultFolderId(JsonUtil.getString(payload, "resultfolderid")); //$NON-NLS-1$
        setWikiUrl(JsonUtil.getString(payload, "wiki_url")); //$NON-NLS-1$
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
    }

    public String getAnalysisName() {
        return analysisName;
    }

    public void setAnalysisName(String analysisName) {
        this.analysisName = analysisName;
    }

    public String getAnalysisDetails() {
        return analysisDetails;
    }

    public void setAnalysisDetails(String analysisDetails) {
        this.analysisDetails = analysisDetails;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResultFolderId() {
        return resultFolderId;
    }

    public void setResultFolderId(String resultFolderId) {
        this.resultFolderId = resultFolderId;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

}
