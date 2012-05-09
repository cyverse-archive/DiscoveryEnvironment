/**
 * 
 */
package org.iplantc.de.client.views.dialogs;

import java.util.List;

import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.de.client.models.Sharing;
import org.iplantc.de.client.services.FolderServiceFacade;
import org.iplantc.de.client.views.panels.ManageCollaboratorsPanel;
import org.iplantc.de.client.views.panels.ManageCollaboratorsPanel.MODE;
import org.iplantc.de.client.views.panels.SharePanel;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author sriram
 * 
 */
public class SharingDialog extends Dialog {

    private List<DiskResource> resources;

    private BorderLayout layout;
    private ContentPanel sharingPanel;
    private ManageCollaboratorsPanel collaboratorSearchPanel;

    public SharingDialog(List<DiskResource> resources) {
        this.resources = resources;
        init();
    }

    private void init() {
        setSize(800, 410);
        setHeading("Share");
        initLayout();
        setButtons(Dialog.OK);
        getOkButton().setText(org.iplantc.de.client.I18N.DISPLAY.done());
        getOkButton().addSelectionListener(new SharingDoneListener());
        setHideOnButtonClick(true);
        setModal(true);
        setResizable(false);
    }

    private void initLayout() {
        layout = new BorderLayout();
        setLayout(layout);
        buildWest();
        buildCenter();
    }

    private Button getOkButton() {
        return getButtonById(Dialog.OK);
    }

    private void buildCenter() {
        sharingPanel = new ContentPanel();
        sharingPanel.setHeading("Share");
        sharingPanel.setLayout(new FlowLayout());
        sharingPanel.setScrollMode(Scroll.AUTOY);
        BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
        data.setSplit(true);
        add(sharingPanel, data);
        buildlistViewForSharing();
    }

    private void buildWest() {
        ContentPanel east = new ContentPanel();
        collaboratorSearchPanel = new ManageCollaboratorsPanel(MODE.SEARCH, 390);
        east.setHeading("Search Collaborators");
        east.add(collaboratorSearchPanel);
        BorderLayoutData data = new BorderLayoutData(LayoutRegion.WEST, 390, 200, 450);
        data.setSplit(true);
        data.setCollapsible(true);
        add(east, data);

    }

    private void buildlistViewForSharing() {
        SharePanel view = null;
        for (DiskResource dr : resources) {
            view = new SharePanel(dr);
            view.setHeading(dr.getName());
            sharingPanel.add(view);
        }

    }

    private void callSharingService(JSONObject obj) {
        FolderServiceFacade facade = new FolderServiceFacade();
        facade.shareDiskResource(obj, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Info.display("Sharing", "Sharing completed successfully!!!");

            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);

            }
        });
    }

    private class SharingDoneListener extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            SharePanel view = null;
            for (Component c : sharingPanel.getItems()) {
                // not safe
                view = (SharePanel)c;
                List<Sharing> sharingList = view.getSharingList();
                Sharing s = sharingList.get(0);

                JSONObject obj = new JSONObject();

                JSONArray users = new JSONArray();
                for (int i = 0; i < sharingList.size(); i++) {
                    Sharing sh = sharingList.get(i);
                    users.set(i, new JSONString(sh.getUserName()));

                }

                JSONArray paths = new JSONArray();
                paths.set(0, new JSONString(view.getResource().getId()));

                JSONObject permission = new JSONObject();
                permission.put("read", JSONBoolean.getInstance(s.isReadable()));
                permission.put("write", JSONBoolean.getInstance(s.isWritable()));
                permission.put("own", JSONBoolean.getInstance(s.isOwner()));

                obj.put("paths", paths);
                obj.put("users", users);
                obj.put("permissions", permission);
                callSharingService(obj);
            }
        }
    }

}
