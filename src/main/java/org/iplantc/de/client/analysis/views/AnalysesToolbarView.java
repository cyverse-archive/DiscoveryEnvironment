/**
 * 
 */
package org.iplantc.de.client.analysis.views;

import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.button.TextButton;

/**
 * @author sriram
 * 
 */
public interface AnalysesToolbarView extends IsWidget {

    public interface Presenter {
        void onDeleteClicked();

        void onViewParamClicked();

        void onCancelClicked();

        void setRefreshButton(TextButton refreshBtn);
    }

    void setDeleteButtonEnabled(boolean enabled);

    void setViewParamButtonEnabled(boolean enabled);

    void setCancelButtonEnabled(boolean enabled);

    void setPresenter(Presenter p);

    void setRefreshButton(TextButton refreshBtn);

}
