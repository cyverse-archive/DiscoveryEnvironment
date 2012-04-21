package org.iplantc.de.client.views.windows;

import java.util.List;

import org.iplantc.core.client.widgets.Hyperlink;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.dispatchers.WindowDispatcher;
import org.iplantc.de.client.factories.EventJSONFactory.ActionType;
import org.iplantc.de.client.factories.WindowConfigFactory;
import org.iplantc.de.client.models.SimpleDownloadWindowConfig;
import org.iplantc.de.client.models.WindowConfig;
import org.iplantc.de.client.services.FolderServiceFacade;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Element;

/**
 * An iPlant window for displaying simple download links.
 * 
 * @author psarando
 * 
 */
public class SimpleDownloadWindow extends IPlantWindow {

    private SimpleDownloadWindowConfig config;
    private LayoutContainer contents;

    public SimpleDownloadWindow(String tag, SimpleDownloadWindowConfig config) {
        super(tag, false, true, true, true);

        this.config = config;

        init();
    }

    private void init() {
        setHeading(I18N.DISPLAY.download());
        setSize(320, 320);
        setLayout(new FitLayout());
        setScrollMode(Scroll.AUTO);

        // Add window contents container for the simple download links
        contents = new LayoutContainer();

        // TODO use a CSS class instead of hard-coding?
        contents.setStyleAttribute("padding", "5px"); //$NON-NLS-1$ //$NON-NLS-2$

        add(new Label(I18N.DISPLAY.simpleDownloadNotice()));
        add(contents);
    }

    @Override
    public void setWindowConfig(WindowConfig config) {
        if (config instanceof SimpleDownloadWindowConfig) {
            this.config = (SimpleDownloadWindowConfig)config;

            contents.removeAll();

            if (isRendered()) {
                compose();
            }
        }
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        compose();
    }

    private void compose() {
        contents.add(buildLinks());
        layout();
    }

    private LayoutContainer buildLinks() {
        VerticalPanel pnlLinks = new VerticalPanel();

        List<String> downloadPaths = config.getDownloadPaths();
        for (final String path : downloadPaths) {
            Hyperlink link = new Hyperlink(DiskResourceUtil.parseNameFromPath(path), "de_hyperlink"); //$NON-NLS-1$

            link.addClickListener(new Listener<ComponentEvent>() {
                @Override
                public void handleEvent(ComponentEvent be) {
                    FolderServiceFacade service = new FolderServiceFacade();
                    service.simpleDownload(path);
                }
            });

            pnlLinks.add(link);
        }

        return pnlLinks;
    }

    @Override
    public JSONObject getWindowState() {
        SimpleDownloadWindowConfig configData = new SimpleDownloadWindowConfig(config);
        storeWindowViewState(configData);

        WindowConfigFactory configFactory = new WindowConfigFactory();
        JSONObject windowConfig = configFactory.buildWindowConfig(Constants.CLIENT.simpleDownloadTag(),
                configData);

        WindowDispatcher dispatcher = new WindowDispatcher(windowConfig);
        return dispatcher.getDispatchJson(Constants.CLIENT.simpleDownloadTag(),
                ActionType.DISPLAY_WINDOW);
    }

}
