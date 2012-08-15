package org.iplantc.de.client.gxt3.views;

import org.iplantc.core.uidiskresource.client.models.Folder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class MyView1 extends Composite implements IsWidget {
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    interface MyUiBinder extends UiBinder<Widget, MyView1> {
    }

    class KeyProvider implements ModelKeyProvider<Folder> {
        @Override
        public String getKey(Folder item) {
            return (item instanceof Folder ? "f-" : "m-") + item.getId().toString();
        }
    }

    @UiField(provided = true)
    TreeStore<Folder> store = new TreeStore<Folder>(new KeyProvider());

    @UiField
    Tree<Folder, String> tree;

    @UiField
    BorderLayoutContainer con;

    @UiField
    ContentPanel navPanel;
    @UiField
    ContentPanel mainPanel;
    @UiField
    ContentPanel detailPanel;

    public MyView1() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return uiBinder.createAndBindUi(this);
    }

}
