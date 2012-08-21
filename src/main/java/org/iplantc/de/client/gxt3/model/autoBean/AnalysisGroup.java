package org.iplantc.de.client.gxt3.model.autoBean;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface AnalysisGroup {

    String getName();

    @PropertyName("template_count")
    int getTemplateCount();

    List<AnalysisGroup> getGroups();

    @PropertyName("is_public")
    boolean isPublic();

    @PropertyName("workspace_id")
    String getWorkspaceId();

    String getId();

    String getDescription();

    void setName(String name);

    @PropertyName("template_count")
    void setTemplateCount(int templateCount);

    void setGroups(List<AnalysisGroup> groups);

    @PropertyName("is_public")
    void setIsPublic(boolean isPublic);

    @PropertyName("workspace_id")
    void setWorkspaceId(String workspaceId);

    void setId();

    void setDescription(String description);

}
