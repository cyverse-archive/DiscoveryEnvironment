/**
 * 
 */
package org.iplantc.de.client.viewer.views;

import java.util.List;

import org.iplantc.de.client.viewer.models.TreeUrl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;

/**
 * @author sriram
 * 
 */
public class TreeViwerImpl implements FileViewer {

    private static TreeViwerUiBinder uiBinder = GWT.create(TreeViwerUiBinder.class);

    @UiTemplate("TreeViewer.ui.xml")
    interface TreeViwerUiBinder extends UiBinder<Widget, TreeViwerImpl> {
    }

    private final Widget widget;

    Grid<TreeUrl> grid;

    @UiField(provided = true)
    ListStore<TreeUrl> listStore;

    @UiField(provided = true)
    ColumnModel<TreeUrl> cm;

    @UiField
    GridView<TreeUrl> gridView;

    public TreeViwerImpl(ColumnModel<TreeUrl> columnModel, ListStore<TreeUrl> store) {
        this.cm = columnModel;
        this.listStore = store;
        this.widget = uiBinder.createAndBindUi(this);
        gridView.setAutoExpandColumn(cm.getColumn(1));
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setPresenter(Presenter p) {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("unchecked")
    @Override
    public void setData(Object data) {
        List<TreeUrl> urls = (List<TreeUrl>)data;
        listStore.addAll(urls);
    }

}
