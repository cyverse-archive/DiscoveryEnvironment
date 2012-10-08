/**
 * 
 */
package org.iplantc.de.client.viewer.commands;

import java.util.LinkedList;
import java.util.List;

import org.iplantc.core.uidiskresource.client.models.FileIdentifier;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.viewer.models.TreeUrl;
import org.iplantc.de.client.viewer.models.TreeUrlProperties;
import org.iplantc.de.client.viewer.views.FileViewer;
import org.iplantc.de.client.viewer.views.TreeViwerImpl;
import org.iplantc.de.client.viewer.views.cells.TreeUrlCell;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

/**
 * @author sriram
 * 
 */
public class TreeViewerCommand implements ViewCommand {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.iplantc.de.client.viewer.commands.ViewCommand#execute(org.iplantc.core.uidiskresource.client
     * .models.FileIdentifier)
     */
    @Override
    public FileViewer execute(FileIdentifier file) {
        FileViewer viewer = new TreeViwerImpl(buildColumnModel(), new ListStore<TreeUrl>(
                new TreeUrlKeyProvider()));
        return viewer;
    }

    private ColumnModel<TreeUrl> buildColumnModel() {
        TreeUrlProperties props = GWT.create(TreeUrlProperties.class);
        List<ColumnConfig<TreeUrl, ?>> configs = new LinkedList<ColumnConfig<TreeUrl, ?>>();
        ColumnConfig<TreeUrl, String> label = new ColumnConfig<TreeUrl, String>(props.label(), 75);
        label.setHeader(I18N.DISPLAY.label());
        configs.add(label);

        ColumnConfig<TreeUrl, TreeUrl> url = new ColumnConfig<TreeUrl, TreeUrl>(
                new IdentityValueProvider<TreeUrl>(), 280);
        url.setHeader(I18N.DISPLAY.treeUrl());
        url.setCell(new TreeUrlCell());
        configs.add(url);

        return new ColumnModel<TreeUrl>(configs);

    }

    private class TreeUrlKeyProvider implements ModelKeyProvider<TreeUrl> {
        @Override
        public String getKey(TreeUrl item) {
            return item.getLabel();
        }

    }

}
