/**
 * 
 */
package org.iplantc.de.client.views.dialogs;

import java.util.List;

import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.de.client.services.DiskResourceShareCallback;
import org.iplantc.de.client.services.FolderServiceFacade;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * @author sriram
 * 
 */
public class SharingDialog extends Dialog {

    private FormPanel panel;
    private FormData formData;
    private List<DiskResource> resources;
    private TextField<String> email;
    private RadioGroup radioGroup;
    private Radio chkRead;
    private Radio chkWrite;
    private Radio chkOwn;

    public SharingDialog(List<DiskResource> resources) {
        this.resources = resources;
        init();
        addPromptText();
        buildEmailField();
        buildPermissionsRadioGroup();
        addOkButtonListener();
    }

    private void addOkButtonListener() {
        getOkButton().addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                JSONObject body = new JSONObject();
                body.put("paths", getResourceIdsAsArray());
                body.put("users", getEmailsAsArray(email.getValue()));
                body.put("permissions", getPermissionObject());
                FolderServiceFacade service = new FolderServiceFacade();

                service.shareDiskResource(body, new DiskResourceShareCallback());
            }
        });

    }

    private JSONArray getResourceIdsAsArray() {
        JSONArray arr = new JSONArray();
        int i = 0;
        if (resources != null) {
            for (DiskResource dr : resources) {
                arr.set(i++, new JSONString(dr.getId()));
            }
        }

        return arr;
    }

    private JSONArray getEmailsAsArray(String emailIds) {
        JSONArray arr = new JSONArray();
        int i = 0;

        if (emailIds != null && !emailIds.isEmpty()) {
            String[] tokens = emailIds.split(",");
            for (String id : tokens) {
                arr.set(i++, new JSONString(id));
            }
        }
        return arr;
    }

    private void buildPermissionsRadioGroup() {
        radioGroup = new RadioGroup();

        chkRead = new Radio();
        chkRead.setBoxLabel("Read Only");
        chkRead.setValue(true);

        chkWrite = new Radio();
        chkWrite.setBoxLabel("Read-Write");

        chkOwn = new Radio();
        chkOwn.setBoxLabel("Owner");

        radioGroup.add(chkRead);
        radioGroup.add(chkWrite);
        radioGroup.add(chkOwn);

        radioGroup.setFieldLabel("Permissions");

        panel.add(radioGroup, formData);
    }

    private void buildEmailField() {
        email = new TextField<String>();
        email.setFieldLabel("Email");
        email.addListener(Events.Valid, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                getOkButton().enable();

            }
        });

        email.addListener(Events.Invalid, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                getOkButton().disable();

            }
        });
        panel.add(email, formData);
    }

    private void init() {
        setSize(350, 300);
        setHeading("Share");

        setButtons(Dialog.OKCANCEL);
        setHideOnButtonClick(true);
        setModal(true);
        setResizable(false);
        initFormPanel();

    }

    private void addPromptText() {
        panel.add(new Label("Enter email address (comma seperated) of the person(s) with whom this"
                + " file(s) / folder(s) will be shared.<br/> <b>Note:</b> The email address"
                + " should be registered with iplant."), formData);
    }

    private void initFormPanel() {
        panel = new FormPanel();
        panel.setHeaderVisible(false);
        panel.setBodyBorder(false);
        formData = new FormData("-20");
        formData.setMargins(new Margins(5, 0, 5, 0));
        panel.setWidth(325);
        add(panel);
    }

    private Button getOkButton() {
        return getButtonById(Dialog.OK);
    }

    private JSONObject getPermissionObject() {
        JSONObject permission = new JSONObject();
        permission.put("read", JSONBoolean.getInstance(true));
        if (chkOwn.getValue()) {
            permission.put("write", JSONBoolean.getInstance(true));
            permission.put("own", JSONBoolean.getInstance(true));
        } else if (chkWrite.getValue()) {
            permission.put("write", JSONBoolean.getInstance(true));
            permission.put("own", JSONBoolean.getInstance(false));
        } else {
            permission.put("write", JSONBoolean.getInstance(false));
            permission.put("own", JSONBoolean.getInstance(false));
        }
        return permission;
    }
}
