package org.iplantc.de.client.analysis.views;

import java.util.List;

import org.iplantc.de.client.analysis.models.Analysis;

import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.button.TextButton;

/**
 * 
 * 
 * 
 * @author sriram
 * 
 */
public interface AnalysesView extends IsWidget {
    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {

        void onAnalysesSelection(List<Analysis> selectedItems);

    }

    public void setPresenter(final Presenter presenter);

    void setNorthWidget(IsWidget widget);

    public void loadAnalyses(List<Analysis> items);

    public List<Analysis> getSelectedAnalyses();

    public void removeFromStore(List<Analysis> items);

    public ListStore<Analysis> getListStore();

    public void setLoader(PagingLoader<PagingLoadConfig, PagingLoadResult<Analysis>> initRemoteLoader);

    public TextButton getRefreshButton();

}
