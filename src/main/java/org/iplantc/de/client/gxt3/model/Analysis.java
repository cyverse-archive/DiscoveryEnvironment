package org.iplantc.de.client.gxt3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.models.AnalysisFeedback;
import org.iplantc.core.uiapplications.client.models.AnalysisGroup;
import org.iplantc.core.uicommons.client.util.DateParser;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

@Deprecated
public class Analysis implements Serializable {
	private static final long serialVersionUID = -3698605389717210878L;

    private static final String INTEGRATOR_NAME = "integrator_name"; //$NON-NLS-1$
    private static final String INTEGRATOR_EMAIL = "integrator_email"; //$NON-NLS-1$
    private static final String INTEGRATION_DATE = "integration_date"; //$NON-NLS-1$
    private static final String RATING = "rating"; //$NON-NLS-1$
    private static final String FAVOURITE = "is_favorite"; //$NON-NLS-1$
    private static final String PUBLIC = "is_public"; //$NON-NLS-1$
    private static final String WIKI_URL = "wiki_url"; //$NON-NLS-1$
    private static final String DEPLOYED_COMPONENTS = "deployed_components"; //$NON-NLS-1$
    private static final String SUGGESTED_CATEGORIES = "suggested_categories"; //$NON-NLS-1$
    private static final String DISABLED = "disabled"; //$NON-NLS-1$
    private static final String GROUP_ID = "group_id"; //$NON-NLS-1$
    private static final String GROUP_NAME = "group_name"; //$NON-NLS-1$

    private static final String ID = "id"; //$NON-NLS-1$
    private static final String NAME = "name"; //$NON-NLS-1$
    private static final String DESCRIPTION = "description"; //$NON-NLS-1$

    private String id;
    private String name;
    private String description;
    private String integratorName;
    private String integratorEmail;
    private String groupId;
    private String groupName;
    private String wikiUrl;
    private Date integrationDate;
    private boolean userFavourite;
    private boolean public_;
    private boolean disabled;
    private AnalysisFeedback rating;
    private List<AnalysisGroup> suggestedCategories;

    public Analysis(JSONObject json) {
        this.id = JsonUtil.getString(json, ID);
        this.name = JsonUtil.getString(json, NAME);
        if (JsonUtil.getString(json, DESCRIPTION).isEmpty()) {
            this.description = JsonUtil.getString(json, "desc"); //$NON-NLS-1$
        } else {
            this.description = JsonUtil.getString(json, DESCRIPTION);
        }

        this.integratorName = JsonUtil.getString(json, INTEGRATOR_NAME);
        this.integratorEmail = JsonUtil.getString(json, INTEGRATOR_EMAIL);
        this.groupId = JsonUtil.getString(json, GROUP_ID);
        this.groupName = JsonUtil.getString(json, GROUP_NAME);
        this.wikiUrl = JsonUtil.getString(json, WIKI_URL);

        long timestamp = 0;
        Number num = JsonUtil.getNumber(json, INTEGRATION_DATE);
        if (num != null) {
            timestamp = num.longValue();
        }
        if (timestamp != 0) {
            this.integrationDate = DateParser.parseDate(timestamp);
        }

        this.userFavourite = JsonUtil.getBoolean(json, FAVOURITE, false);
        this.public_ = JsonUtil.getBoolean(json, PUBLIC, false);
        this.disabled = JsonUtil.getBoolean(json, DISABLED, false);

        this.rating = new AnalysisFeedback(JsonUtil.getObject(json, RATING));
        this.suggestedCategories = parseSuggestedCategories(JsonUtil.getArray(json, SUGGESTED_CATEGORIES));
    }

    private List<AnalysisGroup> parseSuggestedCategories(JSONArray suggestedCategories) {
        List<AnalysisGroup> ret = null;

        if (suggestedCategories != null && suggestedCategories.size() > 0) {
            ret = new ArrayList<AnalysisGroup>();

            for (int i = 0,size = suggestedCategories.size(); i < size; i++) {
                ret.add(new AnalysisGroup(JsonUtil.getObjectAt(suggestedCategories, i)));
            }
        }

        return ret;
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

    public String getIntegratorName() {
        return integratorName;
    }

    public void setIntegratorName(String integratorName) {
        this.integratorName = integratorName;
    }

    public String getIntegratorEmail() {
        return integratorEmail;
    }

    public void setIntegratorEmail(String integratorEmail) {
        this.integratorEmail = integratorEmail;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }

    public Date getIntegrationDate() {
        return integrationDate;
    }

    public void setIntegrationDate(Date integrationDate) {
        this.integrationDate = integrationDate;
    }

    public boolean isUserFavourite() {
        return userFavourite;
    }

    public void setUserFavourite(boolean userFavourite) {
        this.userFavourite = userFavourite;
    }

    public boolean isPublic_() {
        return public_;
    }

    public void setPublic_(boolean public_) {
        this.public_ = public_;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public AnalysisFeedback getRating() {
        return rating;
    }

    public void setRating(AnalysisFeedback rating) {
        this.rating = rating;
    }

    public List<AnalysisGroup> getSuggestedCategories() {
        return suggestedCategories;
    }

    public void setSuggestedCategories(List<AnalysisGroup> suggestedCategories) {
        this.suggestedCategories = suggestedCategories;
    }

}
