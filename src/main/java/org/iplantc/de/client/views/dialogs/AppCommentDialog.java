package org.iplantc.de.client.views.dialogs;

import org.iplantc.de.client.I18N;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Command;

/**
 * A simple dialog that lets the user enter a comment on an app.
 * 
 * @author hariolf
 * 
 */
public class AppCommentDialog extends Dialog {
    private TextArea textArea;
    private Command onConfirm;

    /**
     * Creates a new AppCommentDialog with no on-confirm command set.
     * 
     * @param appName name of the app
     */
    public AppCommentDialog(String appName) {
        init(appName);
    }

    private void init(String appName) {
        setHeading(I18N.DISPLAY.appCommentDialogTitle());
        setLayout(new FitLayout());
        setButtons(Dialog.OKCANCEL);
        setSize(400, 250);
        setResizable(false);

        textArea = new TextArea();
        textArea.setSize(385, 164);
        setFocusWidget(textArea);
        compose(appName);

        setHideOnButtonClick(true);

        // call onConfirm on OK
        getButtonById(OK).addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                onConfirm.execute();
            }
        });

        setModal(true);
    }

    private void compose(String appName) {
        VerticalPanel pnl = new VerticalPanel();
        pnl.add(new Label(I18N.DISPLAY.appCommentExplanation(appName)));
        LayoutContainer lc = new LayoutContainer(new FitLayout());
        lc.add(textArea);
        pnl.add(lc);
        add(pnl);
    }

    /**
     * Sets a command to run when the OK button is clicked.
     * 
     * @param onConfirm
     */
    public void setCommand(Command onConfirm) {
        this.onConfirm = onConfirm;
    }

    /**
     * Returns the comment entered by the user if the dialog was closed with the OK button. If the Cancel
     * button was clicked, the return value is undefined.
     * 
     * @return
     */
    public String getComment() {
        return textArea.getValue();
    }
}
