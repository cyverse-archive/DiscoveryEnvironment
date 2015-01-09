package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.analysis.client.views.AnalysesView;
import org.iplantc.de.analysis.shared.AnalysisModule;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.commons.client.views.window.configs.AnalysisWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author sriram, jstroot
 */
public class MyAnalysesWindow extends IplantWindowBase {

    private final AnalysesView.Presenter presenter;

    public MyAnalysesWindow(AnalysisWindowConfig config,
                            final AnalysesView.Presenter presenter,
                            final IplantDisplayStrings displayStrings) {
        super(null, config);
        this.presenter = presenter;

        ensureDebugId(DeModule.WindowIds.ANALYSES_WINDOW);
        setHeadingText(displayStrings.analyses());
        setSize("670", "375");
        setMinWidth(400);

        presenter.go(this, config.getSelectedAnalyses());
    }

    @Override
    public WindowState getWindowState() {
        AnalysisWindowConfig config = ConfigFactory.analysisWindowConfig();
        List<Analysis> selectedAnalyses = Lists.newArrayList();
        selectedAnalyses.addAll(presenter.getSelectedAnalyses());
        config.setSelectedAnalyses(selectedAnalyses);
        return createWindowState(config);
    }

    @Override
    public <C extends WindowConfig> void update(C config) {
        super.update(config);

        if (config instanceof AnalysisWindowConfig) {
            AnalysisWindowConfig analysisWindowConfig = (AnalysisWindowConfig) config;
            presenter.setSelectedAnalyses(analysisWindowConfig.getSelectedAnalyses());
        }
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        presenter.setViewDebugId(baseID + AnalysisModule.Ids.ANALYSES_VIEW);
    }
}
