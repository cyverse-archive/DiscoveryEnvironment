package org.iplantc.admin.belphegor.client.services.model;

import java.util.List;

import org.iplantc.core.uicommons.client.models.HasId;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface AppCategorizeRequest {

    public interface CategoryPath {
        String getUsername();

        void setUsername(String username);

        List<String> getPath();

        void setPath(List<String> path);
    }

    public interface CategoryRequest {
        HasId getAnalysis();

        void setAnalysis(HasId analysis);

        @PropertyName("category_path")
        CategoryPath getCategoryPath();

        @PropertyName("category_path")
        void setCategoryPath(CategoryPath category_path);
    }

    List<CategoryRequest> getCategories();

    void setCategories(List<CategoryRequest> categories);
}
