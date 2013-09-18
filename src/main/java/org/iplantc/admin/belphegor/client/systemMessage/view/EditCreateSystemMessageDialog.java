package org.iplantc.admin.belphegor.client.systemMessage.view;

import java.util.Date;
import java.util.List;

import org.iplantc.core.uicommons.client.models.sysmsgs.Message;
import org.iplantc.core.uicommons.client.models.sysmsgs.MessageFactory;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TimeField;

public class EditCreateSystemMessageDialog extends IPlantDialog {

    private static CreateSystemMessageDialogUiBinder uiBinder = GWT.create(CreateSystemMessageDialogUiBinder.class);

    @UiTemplate("SystemMessageDialogPanel.ui.xml")
    interface CreateSystemMessageDialogUiBinder extends UiBinder<Widget, EditCreateSystemMessageDialog> {
    }

    @UiField
    VerticalLayoutContainer vlc;

    @UiField
    SimpleComboBox<String> typeCombo;

    @UiField
    TextArea messageField;

    @UiField
    DateField activationDateField, deActivationDateField;

    @UiField
    TimeField activationTimeField, deActivationTimeField;

    @UiField
    CheckBox dismissibleField, loginsDisabledField;

    @UiField(provided = true)
    Date minTime = new DateWrapper().clearTime().asDate();

    @UiField(provided = true)
    Date maxTime = new DateWrapper().clearTime().addHours(23).addSeconds(46).asDate();

    private final Message message;

    private final MessageFactory factory = GWT.create(MessageFactory.class);

    private final List<String> announcementTypes = Lists.newArrayList("warning", "announcement", "maintenance");

    public static EditCreateSystemMessageDialog createSystemMessage(List<String> announcementTypes) {
        EditCreateSystemMessageDialog dlg = new EditCreateSystemMessageDialog(null, announcementTypes);
        dlg.setHeadingText("Create System Message");
        return dlg;
    }
    
    public static EditCreateSystemMessageDialog editSystemMessage(Message message, List<String> announcementTypes) {
        EditCreateSystemMessageDialog dlg = new EditCreateSystemMessageDialog(message, announcementTypes);
        dlg.messageField.setValue(message.getBody());
        dlg.typeCombo.select(message.getType());
        dlg.typeCombo.setValue(message.getType());
        dlg.setHeadingText("Edit System Message");
        return dlg;
    }

    private EditCreateSystemMessageDialog(Message message, List<String> announcementTypes) {
        this.message = message;
        // If the announcement types are not empty, clear local defaults and add them.
        if (!announcementTypes.isEmpty()) {
            this.announcementTypes.clear();
            this.announcementTypes.addAll(announcementTypes);
        }
        if (this.message == null) {
            message = factory.makeMessage().as();
        }
        setSize("500", "400");
        add(uiBinder.createAndBindUi(this));
        // JDS Set activation date to current time
        DateWrapper initialDate = (message.getActivationTime() == null) ? new DateWrapper() : new DateWrapper(message.getActivationTime());
        getOkButton().setText("Submit");

        activationTimeField.setValue(initialDate.asDate());
        activationDateField.setValue(initialDate.clearTime().asDate());
    }

    @UiFactory
    SimpleComboBox<String> createTypeCombo() {
        SimpleComboBox<String> cb = new SimpleComboBox<String>(new StringLabelProvider<String>());
        cb.add(announcementTypes);
        return cb;
    }

    @Override
    protected void onButtonPressed(TextButton button) {
        if (button == getButtonBar().getItemByItemId(PredefinedButton.OK.name())) {
            if (FormPanelHelper.isValid(vlc)) {
                super.onButtonPressed(button);
                new AlertMessageBox("Congratulations", "You've submitted your stuff to a server").show();
            }
        } else {
            super.onButtonPressed(button);
        }
    }

    public Message getMessage() {
        Splittable split = StringQuoter.createSplittable();
        StringQuoter.create(messageField.getCurrentValue()).assign(split, "message");
        StringQuoter.create(typeCombo.getCurrentValue()).assign(split, "type");
        DateWrapper actD = new DateWrapper(activationDateField.getCurrentValue());
        actD.clearTime();
        DateWrapper subA = new DateWrapper(activationTimeField.getCurrentValue());
        actD.addHours(subA.getHours());
        actD.addMinutes(subA.getMinutes());

        DateWrapper deActD = new DateWrapper(deActivationDateField.getCurrentValue());
        deActD.clearTime();
        DateWrapper subB = new DateWrapper(deActivationTimeField.getValue());
        deActD.addHours(subB.getHours());
        deActD.addMinutes(subB.getMinutes());
        StringQuoter.create(actD.getTime()).assign(split, "activation_date");
        StringQuoter.create(deActD.getTime()).assign(split, "deactivation_date");
        return AutoBeanCodex.decode(factory, Message.class, split).as();
    }

}
