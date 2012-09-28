/**
 * 
 */
package org.iplantc.de.client.analysis.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * @author sriram
 * 
 */
public class AnalysesToolbarViewImpl implements AnalysesToolbarView {

    private static AnalysesToolbarUiBinder uiBinder = GWT.create(AnalysesToolbarUiBinder.class);

    @UiTemplate("AnalysesToolbarView.ui.xml")
    interface AnalysesToolbarUiBinder extends UiBinder<Widget, AnalysesToolbarViewImpl> {
    }

    private final Widget widget;
    private Presenter presenter;

    @UiField
    TextButton btnViewParam;

    @UiField
    TextButton btnCancel;

    @UiField
    TextButton btnDelete;

    public AnalysesToolbarViewImpl() {
        this.widget = uiBinder.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setDeleteButtonEnabled(boolean enabled) {
        btnDelete.setEnabled(enabled);
    }

    @Override
    public void setViewParamButtonEnabled(boolean enabled) {
        btnViewParam.setEnabled(enabled);
    }

    @Override
    public void setCancelButtonEnabled(boolean enabled) {
        btnCancel.setEnabled(enabled);
    }

    @Override
    public void setPresenter(Presenter p) {
        this.presenter = p;

    }

    @UiHandler("btnDelete")
    public void deleteClicked(SelectEvent event) {
        presenter.onDeleteClicked();
    }

    @UiHandler("btnCancel")
    public void cancelClicked(SelectEvent event) {
        presenter.onCancelClicked();
    }

    @UiHandler("btnViewParam")
    public void viewParamClicked(SelectEvent event) {
        presenter.onViewParamClicked();
    }
}
