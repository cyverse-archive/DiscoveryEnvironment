package org.iplantc.de.client.views.panels;

import java.util.Date;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uidiskresource.client.models.DiskResource;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Folder;
import org.iplantc.de.client.I18N;
import org.iplantc.de.client.Services;
import org.iplantc.de.client.utils.DataUtils;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Panel for listing details of a selected resource in the Manage Data window.
 * 
 * @author psarando
 * 
 */
public class DataDetailListPanel extends ContentPanel {
    public DataDetailListPanel() {
        init();
    }

    /**
     * Initializes this content panel.
     */
    private void init() {
        setHeaderVisible(false);
        setVisible(false);
        setBodyStyle("background-color: #EDEDED"); //$NON-NLS-1$

        TableLayout layout = new TableLayout(2);
        layout.setCellPadding(2);
        setLayout(layout);
    }

    /**
     * Update the this panel with the details of the given resource list. Hides the panel if the list
     * does not contain exactly one resource.
     * 
     * @param resources
     */
    public void update(final List<DiskResource> resources) {
        removeAll();

        if (resources == null || resources.size() != 1) {
            hide();
        } else {
            addDetails(resources.get(0));
            show();
        }
    }

    /**
     * Adds the details of the given resource to this panel.
     * 
     * @param resource
     */
    private void addDetails(final DiskResource resource) {
        addDateLabel(I18N.DISPLAY.lastModified(), resource.getLastModified());
        addDateLabel(I18N.DISPLAY.createdDate(), resource.getDateCreated()); 
        addPermissionsLabel(I18N.DISPLAY.permissions(), resource.getPermissions().isReadable(), resource
                .getPermissions().isWritable(), resource.getPermissions().isOwner());

        if (resource instanceof File) {
            File file = (File)resource;
            addSizeLabel(I18N.DISPLAY.size(), file.getSize());
        } else if (resource instanceof Folder) {
            Folder folder = (Folder)resource;
            getFolderDetails(folder.getId());
        }

        layout();
    }

    /**
     * Adds the given "label: value" field to this panel.
     * 
     * @param label
     * @param value
     */
    private void addLabel(String label, String value) {
        Text fieldLabel = new Text(label + ": "); //$NON-NLS-1$
        fieldLabel.addStyleName("data_details_label"); //$NON-NLS-1$

        Text fieldValue = new Text(value);
        fieldValue.addStyleName("data_details_value"); //$NON-NLS-1$

        add(fieldLabel, new TableData(HorizontalAlignment.LEFT, VerticalAlignment.TOP));
        add(fieldValue, new TableData());
    }

    /**
     * Parses a timestamp string into a formatted date string and adds it to this panel.
     * 
     * @param label
     * @param value
     */
    private void addDateLabel(String label, Date date) {
        String value = "";

        if (date != null) {
            DateTimeFormat formatter = DateTimeFormat
                    .getFormat(DateTimeFormat.PredefinedFormat.RFC_2822);

            value = formatter.format(date);
        }

        addLabel(label, value);
    }

    /**
     * Parses a Long number into a formatted size string and adds it to this panel.
     * 
     * @param label
     * @param size
     */
    private void addSizeLabel(String label, Long size) {
        addLabel(label, DataUtils.getSizeForDisplay(size));
    }

    /**
     * Add permissions detail
     * 
     * @param label label to display
     * @param readable boolean true if the resource is readable else false
     * @param writable boolean true if the resource is writable else false
     */
    private void addPermissionsLabel(String label, boolean readable, boolean writable, boolean owner) {
        if (owner) {
            addLabel(label, I18N.DISPLAY.owner());
            return;
        }
        if (!writable) {
            addLabel(label, I18N.DISPLAY.readOnly());
        } else {
            addLabel(label, I18N.DISPLAY.readWrite());
        }
    }

    /**
     * Gets the folder contents of the given path, then adds details of the results to this panel.
     * 
     * @param path
     */
    private void getFolderDetails(final String path) {
        Services.DISK_RESOURCE_SERVICE.getFolderContents(path, new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonFolder = JsonUtil.getObject(result);

                JSONArray folders = JsonUtil.getArray(jsonFolder, "folders"); //$NON-NLS-1$
                if (folders != null) {
                    addLabel(I18N.DISPLAY.folders(), String.valueOf(folders.size()));
                }

                JSONArray files = JsonUtil.getArray(jsonFolder, "files"); //$NON-NLS-1$
                if (files != null) {
                    addLabel(I18N.DISPLAY.files(), String.valueOf(files.size()));
                }

                layout();
            }

            @Override
            public void onFailure(Throwable caught) {
                // ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);
                GWT.log(caught.getMessage(), caught);
            }
        });
    }

}
