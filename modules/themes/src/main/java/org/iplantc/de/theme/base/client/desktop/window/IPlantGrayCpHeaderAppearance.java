package org.iplantc.de.theme.base.client.desktop.window;

import com.google.gwt.core.client.GWT;

import com.sencha.gxt.theme.gray.client.panel.GrayHeaderAppearance;

public class IPlantGrayCpHeaderAppearance extends GrayHeaderAppearance {

    public interface IPlantGrayHeaderResources extends GrayHeaderResources {

        @Override
        @Source({"com/sencha/gxt/theme/base/client/widget/Header.css", "com/sencha/gxt/theme/gray/client/panel/GrayHeader.css", "org/iplantc/de/theme/base/client/desktop/window/IPlantGrayWindowHeader.css"})
        GrayHeaderStyle style();

    }

    public IPlantGrayCpHeaderAppearance() {
        super(GWT.<IPlantGrayHeaderResources> create(IPlantGrayHeaderResources.class), GWT.<Template> create(Template.class));
    }
}
