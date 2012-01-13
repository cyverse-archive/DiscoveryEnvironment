package org.iplantc.de.client.models;

import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.json.client.JSONObject;

/**
 * Models an execution of an Analysis
 * 
 * @author sriram
 * 
 */
public class AnalysisExecution extends DEBaseModelData {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * create an new Analysis execution
     */
    public AnalysisExecution(JsAnalysisExecution exec) {
        super(exec.getId(), exec.getName());
        set("startdate", exec.getStartDate()); //$NON-NLS-1$
        set("enddate", exec.getEndDate()); //$NON-NLS-1$
        set("analysis_id", exec.getAnalysisId()); //$NON-NLS-1$
        set("analysis_name", exec.getAnalysisName()); //$NON-NLS-1$
        set("analysis_details", exec.getAnalysisDetails()); //$NON-NLS-1$
        set("status", exec.getStatus()); //$NON-NLS-1$
        set("description", JsonUtil.formatString(exec.getDescription())); //$NON-NLS-1$
        set("resultfolderid", exec.getResultFolderId()); //$NON-NLS-1$
        set("wiki_url", exec.getWikiUrl());//$NON-NLS-1$
    }

    /**
     * create an new Analysis execution
     */
    public AnalysisExecution() {

    }

    public AnalysisExecution(JSONObject payload) {
        setStartDate(JsonUtil.trim(payload.get("startdate").toString())); //$NON-NLS-1$
        setEndDate(JsonUtil.trim(payload.get("enddate").toString())); //$NON-NLS-1$
        setId(JsonUtil.getString(payload, "id")); //$NON-NLS-1$
        setName(JsonUtil.getString(payload, "name")); //$NON-NLS-1$
        setAnalysisId(JsonUtil.getString(payload, "analysis_id")); //$NON-NLS-1$
        setAnalysisName(JsonUtil.getString(payload, "analysis_name")); //$NON-NLS-1$
        setAnalysisDetails(JsonUtil.getString(payload, "analysis_details")); //$NON-NLS-1$
        setEndDate(JsonUtil.getString(payload, "enddate")); //$NON-NLS-1$
        setStatus(JsonUtil.getString(payload, "status")); //$NON-NLS-1$
        setResultFolderId(JsonUtil.getString(payload, "resultfolderid")); //$NON-NLS-1$
        setWikiUrl(JsonUtil.getString(payload, "wiki_url"));
    }

    public void setWikiUrl(String url) {
        set("wiki_url", url);//$NON-NLS-1$
    }

    public String getResultFolderId() {
        return get("resultfolderid").toString(); //$NON-NLS-1$
    }

    public String getStartDate() {
        return get("startdate").toString(); //$NON-NLS-1$
    }

    public String getEndData() {
        return get("enddate").toString(); //$NON-NLS-1$
    }

    public String getAnalysisId() {
        return get("analysis_id").toString(); //$NON-NLS-1$
    }

    public String getAnalysisName() {
        return get("analysis_name").toString(); //$NON-NLS-1$
    }

    public String getAnalysisDetails() {
        return get("analysis_details").toString(); //$NON-NLS-1$
    }

    public String getStatus() {
        return get("status").toString(); //$NON-NLS-1$
    }

    public String getDescription() {
        return get("description").toString(); //$NON-NLS-1$
    }

    public void setStartDate(String startdata) {
        set("startdate", startdata); //$NON-NLS-1$
    }

    public void setEndDate(String enddate) {
        set("enddate", enddate); //$NON-NLS-1$
    }

    public void setAnalysisId(String analysis_id) {
        set("analysis_id", analysis_id); //$NON-NLS-1$
    }

    public void setAnalysisName(String analysisname) {
        set("analysis_name", analysisname); //$NON-NLS-1$
    }

    public void setAnalysisDetails(String analysis_details) {
        set("analysis_details", analysis_details); //$NON-NLS-1$
    }

    public void setStatus(String status) {
        set("status", status); //$NON-NLS-1$
    }

    public void setDescription(String description) {
        set("description", description); //$NON-NLS-1$
    }

    public void setId(String id) {
        set("id", id); //$NON-NLS-1$
    }

    public void setName(String name) {
        set("name", name); //$NON-NLS-1$
    }

    public void setResultFolderId(String resultfolderid) {
        set("resultfolderid", resultfolderid); //$NON-NLS-1$
    }

    public String getWikiUrl() {
        return get("wiki_url");
    }

}
