/**
 * 
 */
package org.iplantc.de.client.views.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.Permissions;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.events.CollaboratorsLoadedEvent;
import org.iplantc.de.client.events.CollaboratorsLoadedEventHandler;
import org.iplantc.de.client.models.Collaborator;
import org.iplantc.de.client.models.Sharing;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.views.panels.ManageCollaboratorsPanel;
import org.iplantc.de.client.views.panels.ManageCollaboratorsPanel.MODE;
import org.iplantc.de.client.views.panels.SharePanel;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
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
import com.google.gwt.json.client.JSONParser;
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
        setSize(800, 440);
        setHeading(I18N.DISPLAY.share());
        initLayout();
        setButtons(Dialog.OK);
        getOkButton().setText(org.iplantc.de.client.I18N.DISPLAY.done());
        setHideOnButtonClick(true);
        setModal(true);
        setResizable(false);
        initListener();
    }

    private void initListener() {
        EventBus.getInstance().addHandler(CollaboratorsLoadedEvent.TYPE,
                new CollaboratorsLoadedEventHandler() {

                    @Override
                    public void onLoad(CollaboratorsLoadedEvent event) {
                        getUserPermissionsInfo();
                    }
                });
    }

    @Override
    protected void onHide() {
        super.onHide();
        SharePanel view = null;
        for (DiskResource dr : resources) {
            view = (SharePanel)sharingPanel.getItemByItemId(dr.getId());
            doSharing(view);
            doUnsharing(view);
        }
        EventBus.getInstance().removeHandlers(CollaboratorsLoadedEvent.TYPE);
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
        sharingPanel.setHeading(I18N.DISPLAY.share());
        sharingPanel.setLayout(new FlowLayout());
        sharingPanel.setScrollMode(Scroll.AUTOY);
        BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
        data.setSplit(true);
        add(sharingPanel, data);
        buildlistViewForSharing();
    }

    private void buildWest() {
        ContentPanel east = new ContentPanel();
        collaboratorSearchPanel = new ManageCollaboratorsPanel(MODE.SEARCH, 378);
        east.setHeading(I18N.DISPLAY.searchCollab());
        east.add(collaboratorSearchPanel);
        BorderLayoutData data = new BorderLayoutData(LayoutRegion.WEST, 380, 200, 450);
        data.setSplit(true);
        data.setCollapsible(true);
        add(east, data);

    }

    private void buildlistViewForSharing() {
        SharePanel view = null;
        for (DiskResource dr : resources) {
            view = new SharePanel(dr);
            view.setId(dr.getId());
            view.setHeading(dr.getName());
            sharingPanel.add(view);
        }
    }

    private void getUserPermissionsInfo() {
        DiskResourceServiceFacade facade = new DiskResourceServiceFacade();
        facade.getPermissions(buildPermissionsRequestBody(), new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject obj = JSONParser.parseStrict(result).isObject();
                JSONArray permissionsArray = JsonUtil.getArray(obj, "paths");
                if (permissionsArray != null) {
                    for (int i = 0; i < permissionsArray.size(); i++) {
                        JSONObject user_perm_obj = permissionsArray.get(i).isObject();
                        String path = JsonUtil.getString(user_perm_obj, "path");
                        JSONArray user_arr = JsonUtil.getArray(user_perm_obj, "user-permissions");
                        loadPermissions(path, user_arr);
                    }
                }
            }
        });
    }

    private void loadPermissions(String path, JSONArray user_arr) {
        SharePanel view = (SharePanel)sharingPanel.getItemByItemId(path);
        List<Sharing> sharingList = new ArrayList<Sharing>();
        for (int i = 0; i < user_arr.size(); i++) {
            JSONObject obj = user_arr.get(i).isObject();
            JSONObject perm = JsonUtil.getObject(obj, "permissions");
            Sharing s = new Sharing(findCollaboratorByUserName(JsonUtil.getString(obj, "user")),
                    new Permissions(perm));
            sharingList.add(s);
        }
        view.setSharingInfo(sharingList);
    }

    private Collaborator findCollaboratorByUserName(String userName) {
        List<Collaborator> collabs = collaboratorSearchPanel.getCurrentCollaborators();
        for (Collaborator c : collabs) {
            if (c.getUserName().equals(userName)) {
                return c;
            }
        }

        return new Collaborator(null, userName, userName, null, null);

    }

    private JSONObject buildPermissionsRequestBody() {
        JSONObject obj = new JSONObject();
        JSONArray ids = new JSONArray();
        for (int i = 0; i < resources.size(); i++) {
            ids.set(i, new JSONString(resources.get(i).getId()));
        }
        obj.put("paths", ids);
        return obj;
    }

    private void callSharingService(JSONObject obj) {
        DiskResourceServiceFacade facade = new DiskResourceServiceFacade();
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

    private void callUnshareService(JSONObject obj) {
        DiskResourceServiceFacade facade = new DiskResourceServiceFacade();
        facade.unshareDiskResource(obj, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Info.display("Unsharing", "Unsharing completed successfully!!!");
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);

            }
        });
    }

    private void doSharing(SharePanel view) {
        // TODO: permissions needs to be implemented at user level
        // for now take permissions of first user and apply to every one
        List<Sharing> sharingList = view.getSharingList();
        if (sharingList.size() > 0) {
            Sharing s = sharingList.get(0);

            JSONObject obj = new JSONObject();

            JSONArray users = new JSONArray();
            for (int i = 0; i < sharingList.size(); i++) {
                Sharing sh = sharingList.get(i);
                users.set(i, new JSONString(sh.getUserName()));
            }

            JSONArray paths = new JSONArray();
            paths.set(0, new JSONString(view.getId()));

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

    private void doUnsharing(SharePanel view) {
        List<Sharing> unshareList = view.getUnshareList();
        if (unshareList.size() > 0) {
            JSONArray paths = new JSONArray();
            paths.set(0, new JSONString(view.getId()));

            JSONArray users = new JSONArray();
            for (int i = 0; i < unshareList.size(); i++) {
                users.set(i, new JSONString(unshareList.get(i).getUserName()));
            }

            JSONObject obj = new JSONObject();
            obj.put("paths", paths);
            obj.put("users", users);

            callUnshareService(obj);
        }
    }
}
