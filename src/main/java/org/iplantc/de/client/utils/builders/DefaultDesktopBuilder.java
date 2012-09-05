package org.iplantc.de.client.utils.builders;

import org.iplantc.de.client.Constants;
import org.iplantc.de.client.images.Resources;

/**
 * Initializes all desktop shortcuts.
 * 
 * @author amuir
 * 
 */
public class DefaultDesktopBuilder extends DesktopBuilder {
    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildShortcuts() {
        addShortcut("iplantc-mydata-shortcut", "", Constants.CLIENT.windowTag(), //$NON-NLS-1$
                Constants.CLIENT.myDataTag(), Resources.ICONS.shortcutData());

        addShortcut("iplantc-myanalysis-shortcut", "", Constants.CLIENT.windowTag(), //$NON-NLS-1$
                Constants.CLIENT.myAnalysisTag(), Resources.ICONS.shortcutAnalyses());

        addShortcut("iplantc-catalog-shortcut", "", Constants.CLIENT.windowTag(), //$NON-NLS-1$
                Constants.CLIENT.deCatalog(), Resources.ICONS.shortcutApps());
    }
}
