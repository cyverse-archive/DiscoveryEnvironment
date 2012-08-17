package org.iplantc.de.client.gxt3.views;

import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uiapplications.client.models.AnalysisGroup;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Folder;

import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.data.shared.loader.TreeLoader;

public interface AppsView extends IsWidget {

    public interface Presenter {
        void onAnalysisSelected(final Analysis analysis);

        void onAnalysisGroupSelected(final AnalysisGroup ag);

    }

    void setPresenter(final Presenter presenter);

    ListStore<File> getListStore();

    TreeStore<Folder> getTreeStore();

    void setListLoader(final PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>> listLoader);

    void setTreeLoader(final TreeLoader<Folder> treeLoader);

    void setMainPanelHeading(final String name);

    void maskMainPanel(final String loadingMask);

}
