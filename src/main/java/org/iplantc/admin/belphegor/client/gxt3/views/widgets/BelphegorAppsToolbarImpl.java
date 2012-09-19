package org.iplantc.admin.belphegor.client.gxt3.views.widgets;

import org.iplantc.core.uiapplications.client.views.widgets.AppSearchField3;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

/**
 * TODO JDS Need to update ui.xml to use the I18N resource display strings
 * 
 * @author jstroot
 * 
 */
public class BelphegorAppsToolbarImpl implements BelphegorAppsToolbar {

    private static BelphegorAppsViewToolbarUiBinder uiBinder = GWT.create(BelphegorAppsViewToolbarUiBinder.class);

    @UiTemplate("BelphegorAppsViewToolbar.ui.xml")
    interface BelphegorAppsViewToolbarUiBinder extends UiBinder<Widget, BelphegorAppsToolbarImpl> {
    }

    private final Widget widget;
    private Presenter presenter;

    @UiField
    ToolBar toolBar;

    @UiField
    TextButton addCategory;

    @UiField
    TextButton renameCategory;

    @UiField
    AppSearchField3 appSearch;

    @UiField
    TextButton delete;

    @UiField
    TextButton restoreApp;


    public BelphegorAppsToolbarImpl() {
        widget = uiBinder.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @UiHandler("addCategory")
    public void addCategoryClicked(SelectEvent event) {
        presenter.onAddAppGroupClicked();
    }

    @UiHandler("renameCategory")
    public void renameCategoryClicked(SelectEvent event) {
        presenter.onRenameAppGroupClicked();
    }

    @UiHandler("delete")
    public void deleteClicked(SelectEvent event) {
        presenter.onDeleteClicked();
    }

    @UiHandler("restoreApp")
    public void restoreAppClicked(SelectEvent event) {
        presenter.onRestoreAppClicked();
    }

    @Override
    public void setAddAppGroupButtonEnabled(boolean enabled) {
        addCategory.setEnabled(enabled);
    }

    @Override
    public void setRenameAppGroupButtonEnabled(boolean enabled) {
        renameCategory.setEnabled(enabled);
    }

    @Override
    public void setDeleteButtonEnabled(boolean enabled) {
        delete.setEnabled(enabled);
    }

    @Override
    public void setRestoreButtonEnabled(boolean enabled) {
        restoreApp.setEnabled(enabled);
    }

}
