package org.iplantc.de.desktop.client.views.windows;

import org.iplantc.de.client.gin.ServicesInjector;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.util.WindowUtil;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.SimpleDownloadWindowConfig;
import org.iplantc.de.commons.client.widgets.IPlantAnchor;
import org.iplantc.de.desktop.shared.DeModule;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 * An iPlant window for displaying simple download links.
 *
 * @author psarando
 */
public class SimpleDownloadWindow extends IplantWindowBase {

    private final DiskResourceServiceFacade diskResourceServiceFacade;
    private final IplantDisplayStrings displayStrings;

    public SimpleDownloadWindow(final SimpleDownloadWindowConfig config) {
        super(null, true, config);
        displayStrings = org.iplantc.de.resources.client.messages.I18N.DISPLAY;
        diskResourceServiceFacade = ServicesInjector.INSTANCE.getDiskResourceServiceFacade();
        setHeadingText(displayStrings.download());
        setSize("320", "320");
        ensureDebugId(DeModule.WindowIds.SIMPLE_DOWNLOAD);

        init(config);
    }

    @Override
    public WindowState getWindowState() {
        SimpleDownloadWindowConfig config = ConfigFactory.simpleDownloadWindowConfig();
        return createWindowState(config);
    }

    private void buildLinks(SimpleDownloadWindowConfig config, VerticalLayoutContainer vlc) {
        for (final DiskResource dr : config.getResourcesToDownload()) {
            IPlantAnchor link2 = new IPlantAnchor(DiskResourceUtil.getInstance().parseNameFromPath(dr.getPath()),
                                                  120,
                                                  new ClickHandler() {

                                                      @Override
                                                      public void onClick(ClickEvent event) {
                                                          final String encodedSimpleDownloadURL = diskResourceServiceFacade.getEncodedSimpleDownloadURL(dr.getPath());
                                                          WindowUtil.open(encodedSimpleDownloadURL, "width=100,height=100");
                                                      }
                                                  });

            vlc.add(link2);
        }
    }

    private void init(SimpleDownloadWindowConfig config) {
        // Add window contents container for the simple download links
        VerticalLayoutContainer contents = new VerticalLayoutContainer();

        contents.add(new Label("" + displayStrings.simpleDownloadNotice()));
        buildLinks(config, contents);
        add(contents);

    }

}
