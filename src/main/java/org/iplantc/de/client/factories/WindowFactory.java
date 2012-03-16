package org.iplantc.de.client.factories;

import org.iplantc.de.client.Constants;
import org.iplantc.de.client.models.BasicWindowConfig;
import org.iplantc.de.client.models.IDropLiteWindowConfig;
import org.iplantc.de.client.models.NotificationWindowConfig;
import org.iplantc.de.client.models.WindowConfig;
import org.iplantc.de.client.util.WindowUtil;
import org.iplantc.de.client.views.windows.AboutApplicationWindow;
import org.iplantc.de.client.views.windows.DECatalogWindow;
import org.iplantc.de.client.views.windows.IDropLiteAppletWindow;
import org.iplantc.de.client.views.windows.IPlantWindow;
import org.iplantc.de.client.views.windows.MyAnalysesWindow;
import org.iplantc.de.client.views.windows.MyDataWindow;
import org.iplantc.de.client.views.windows.NotificationWindow;
import org.iplantc.de.client.views.windows.PipelineEditorWindow;
import org.iplantc.de.client.views.windows.TitoWindow;
import org.iplantc.de.client.views.windows.WizardWindow;

/**
 * Defines a factory for the creation of windows.
 * 
 * @see org.iplantc.de.client.views.windows.IPlantWindow
 */
public class WindowFactory {
    /**
     * Builds a window from a tag.
     * 
     * @param type unique type for window used to determine type of window to build.
     * @param config a WindowConfiguration suitable for the type of window to create
     * @return new window.
     */
    public static IPlantWindow build(String type, WindowConfig config) {
        IPlantWindow ret = null;

        if (type != null) {
            if (type.equals(Constants.CLIENT.myDataTag())) {
                ret = new MyDataWindow(type, (BasicWindowConfig)config);
            } else if (type.equals(Constants.CLIENT.myNotifyTag())) {
                ret = new NotificationWindow(type, (NotificationWindowConfig)config);
            } else if (type.equals(Constants.CLIENT.myHelpTag())) {
                // since the help page is now a wiki page, open it in a new window so that the user may
                // login to the Wiki.
                WindowUtil.open(Constants.CLIENT.deHelpFile());
            } else if (type.equals(Constants.CLIENT.myAboutTag())) {
                ret = new AboutApplicationWindow(type);
            } else if (type.equals(Constants.CLIENT.myAnalysisTag())) {
                ret = new MyAnalysesWindow(type, (BasicWindowConfig)config);
            } else if (type.equals(Constants.CLIENT.deCatalog())) {
                ret = new DECatalogWindow(type, (BasicWindowConfig)config);
            } else if (type.equals(Constants.CLIENT.pipelineEditorTag())) {
                ret = new PipelineEditorWindow(type);
            } else if (type.startsWith(Constants.CLIENT.iDropLiteTag())) {
                ret = new IDropLiteAppletWindow(type, (IDropLiteWindowConfig)config);
            } else if (type.equals(Constants.CLIENT.titoTag())) {
                ret = new TitoWindow(type);
            } else {
                ret = new WizardWindow(type);
            }
        }

        return ret;
    }
}
