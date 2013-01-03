/**
 * 
 */
package org.iplantc.de.client.notifications.views;

import org.iplantc.de.client.notifications.util.NotificationHelper;
import org.iplantc.de.client.notifications.util.NotificationHelper.Category;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

/**
 * @author sriram
 * 
 */
public class NotificationToolbarViewImpl implements NotificationToolbarView {

    private static NotificationToolbarUiBinder uiBinder = GWT.create(NotificationToolbarUiBinder.class);

    @UiTemplate("NotificationToolbarView.ui.xml")
    interface NotificationToolbarUiBinder extends UiBinder<Widget, NotificationToolbarViewImpl> {
    }

    private final Widget widget;
    private Presenter presenter;

    @UiField
    TextButton btnDelete;

    @UiField
    TextButton btnDeleteAll;

    @UiField
    ToolBar menuToolBar;

    @UiField(provided = true)
    SimpleComboBox<Category> cboFilter = new SimpleComboBox<Category>(
            new StringLabelProvider<Category>());

    public NotificationToolbarViewImpl() {
        widget = uiBinder.createAndBindUi(this);

        initFilters();
    }

    private void initFilters() {
        cboFilter.add(Category.NEW);
        cboFilter.add(Category.ALL);
        cboFilter.add(Category.ANALYSIS);
        cboFilter.add(Category.DATA);
        cboFilter.setValue(Category.ALL);
        cboFilter.addSelectionHandler(new SelectionHandler<NotificationHelper.Category>() {
            @Override
            public void onSelection(SelectionEvent<Category> event) {
                presenter.onFilterSelection(event.getSelectedItem());
            }
        });
        cboFilter.setEditable(false);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setDeleteButtonEnabled(boolean enabled) {
        btnDelete.setEnabled(enabled);

    }

    @UiHandler("btnDelete")
    public void deleteClicked(SelectEvent event) {
        presenter.onDeleteClicked();
    }

    @UiHandler("btnDeleteAll")
    public void deleteAllClicked(SelectEvent event) {
        presenter.onDeleteAllClicked();
    }

    @Override
    public void setPresenter(Presenter p) {
        this.presenter = p;

    }

    @Override
    public void setRefreshButton(TextButton refreshBtn) {
        menuToolBar.add(refreshBtn);
    }
}
