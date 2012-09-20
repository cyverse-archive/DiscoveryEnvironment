/**
 * 
 */
package org.iplantc.de.client.gxt3.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;

/**
 * @author sriram
 * 
 */
public class AnalysesToolbarViewImpl implements AnalysesToolbarView {

    private static AnalysesToolbarUiBinder uiBinder = GWT.create(AnalysesToolbarUiBinder.class);

    @UiTemplate("AnalysesToobarView.ui.xml")
    interface AnalysesToolbarUiBinder extends UiBinder<Widget, AnalysesToolbarViewImpl> {
    }

    private final Widget widget;
    private Presenter presenter;

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
        // TODO Auto-generated method stub

    }

    @Override
    public void setViewParamButtonEnabled(boolean enabled) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCancelButtonEnabled(boolean enabled) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPresenter(Presenter p) {
        this.presenter = p;

    }

}
