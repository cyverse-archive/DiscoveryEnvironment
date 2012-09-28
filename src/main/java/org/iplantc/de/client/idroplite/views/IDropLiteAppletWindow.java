/**
 * 
 */
package org.iplantc.de.client.idroplite.views;

import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.ManageDataRefreshEvent;
import org.iplantc.de.client.idroplite.presenter.IDropLitePresenter;
import org.iplantc.de.client.idroplite.util.IDropLiteUtil;
import org.iplantc.de.client.idroplite.views.IDropLiteView.Presenter;
import org.iplantc.de.client.models.IDropLiteWindowConfig;
import org.iplantc.de.client.views.windows.Gxt3IplantWindow;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Command;
import com.sencha.gxt.core.client.Style.HideMode;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

/**
 * @author sriram
 * 
 */
public class IDropLiteAppletWindow extends Gxt3IplantWindow {

    public IDropLiteAppletWindow(String tag, IDropLiteWindowConfig config) {
        super(tag);
        this.config = config;
        setSize("780px", "410px");
        setResizable(false);
        init();
    }

    private void init() {
        // These settings enable the window to be minimized or moved without reloading the applet.
        removeFromParentOnHide = false;
        setHideMode(HideMode.VISIBILITY);
        initViewMode();
        IDropLiteView view = new IDropLiteViewImpl();
        Presenter p = new IDropLitePresenter(view, ((IDropLiteWindowConfig)config));
        view.setPresenter(p);
        p.go(this);
    }

    private int initViewMode() {
        // Set the heading and add the correct simple mode button based on the applet display mode.
        int displayMode = ((IDropLiteWindowConfig)config).getDisplayMode();
        if (displayMode == IDropLiteUtil.DISPLAY_MODE_UPLOAD) {
            setTitle(I18N.DISPLAY.upload());

        } else if (displayMode == IDropLiteUtil.DISPLAY_MODE_DOWNLOAD) {
            setTitle(I18N.DISPLAY.download());
        }

        return displayMode;

    }

    protected void confirmHide() {
        super.doHide();

        // refresh manage data window
        String refreshPath = ((IDropLiteWindowConfig)config).getCurrentPath();
        if (refreshPath != null && !refreshPath.isEmpty()) {
            ManageDataRefreshEvent event = new ManageDataRefreshEvent(Constants.CLIENT.myDataTag(),
                    refreshPath, null);
            EventBus.getInstance().fireEvent(event);
        }
    }

    @Override
    protected void doHide() {
        promptRemoveApplet(new Command() {
            @Override
            public void execute() {
                confirmHide();
            }
        });
    }

    private void promptRemoveApplet(final Command cmdRemoveAppletConfirmed) {
        final ConfirmMessageBox cmb = new ConfirmMessageBox(I18N.DISPLAY.idropLiteCloseConfirmTitle(),
                I18N.DISPLAY.idropLiteCloseConfirmMessage());

        cmb.addHideHandler(new HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                if (cmb.getHideButton().getText().equalsIgnoreCase("yes")) {
                    // The user confirmed closing the applet.
                    cmdRemoveAppletConfirmed.execute();
                }

            }
        });

        cmb.show();
    }

    @Override
    public void show() {
        super.show();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.iplantc.de.client.views.windows.IPlantWindowInterface#getWindowState()
     */
    @Override
    public JSONObject getWindowState() {
        // TODO Auto-generated method stub
        return null;
    }

}
