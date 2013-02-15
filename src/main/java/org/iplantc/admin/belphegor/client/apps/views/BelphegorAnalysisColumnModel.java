package org.iplantc.admin.belphegor.client.apps.views;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.admin.belphegor.client.apps.views.cells.AvgAnalysisUserRatingCell;
import org.iplantc.core.uiapplications.client.CommonAppDisplayStrings;
import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.models.autobeans.App;
import org.iplantc.core.uiapplications.client.models.autobeans.AppFeedback;
import org.iplantc.core.uiapplications.client.models.autobeans.AppProperties;
import org.iplantc.core.uiapplications.client.views.AppsView;
import org.iplantc.core.uiapplications.client.views.cells.AppHyperlinkCell;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

public class BelphegorAnalysisColumnModel extends ColumnModel<App> {

    public BelphegorAnalysisColumnModel(AppsView view) {
        super(createColumnConfigList(EventBus.getInstance(), I18N.DISPLAY, view));
    }

    public static List<ColumnConfig<App, ?>> createColumnConfigList(final EventBus eventBus,
 final CommonAppDisplayStrings displayStrings, AppsView view) {
        AppProperties props = GWT.create(AppProperties.class);
        List<ColumnConfig<App, ?>> list = new ArrayList<ColumnConfig<App, ?>>();

        ColumnConfig<App, App> name = new ColumnConfig<App, App>(new IdentityValueProvider<App>(), 180,
                I18N.DISPLAY.name());
        ColumnConfig<App, String> integrator = new ColumnConfig<App, String>(
                props.integratorName(), 130, I18N.DISPLAY.integratedby());
        ColumnConfig<App, AppFeedback> rating = new ColumnConfig<App, AppFeedback>(
                props.rating(), 40, "Average User Rating"); //$NON-NLS-1$

        name.setResizable(true);
        rating.setResizable(false);

        name.setCell(new AppHyperlinkCell(view));
        rating.setCell(new AvgAnalysisUserRatingCell());

        rating.setAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        list.add(name);
        list.add(integrator);
        list.add(rating);
        return list;
    }

}