/**
 * 
 */
package org.iplantc.admin.belphegor.client.views.panels;

import org.iplantc.admin.belphegor.client.util.FormFieldBuilderUtil;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;

/**
 * @author sriram
 * 
 */
public class RefGenomeFormPanel extends LayoutContainer {

    private HiddenField<String> uuid;
    private TextField<String> userName;
    private TextField<String> path;
    private CheckBox chkDeleted;
    private TextField<String> refGenName;
    private DateField creationDate;
    private TextField<String> lastModUserName;
    private DateField lastModDate;

    private FormPanel panel;

    private FormData formData;

    public RefGenomeFormPanel() {
        panel = new FormPanel();
        formData = new FormData("93%");
        buildFields();
        addFields();
    }

    private void buildFields() {
        userName = FormFieldBuilderUtil.buildTextField("User Name", false, null, "userName", null, 255);
        path = FormFieldBuilderUtil.buildTextField("Path", false, null, "path", null, 1024);
        refGenName = FormFieldBuilderUtil.buildTextField("Reference Genome Name", false, null,
                "refGenName", null, 512);
        // creationDate = FormFieldBuilderUtil.buildDateField("Creation Date");
        lastModUserName = FormFieldBuilderUtil.buildTextField("User Name", false, null, "userName",
                null, 255);
        // lastModDate = FormFieldBuilderUtil.buildDateField("Last Modified Date");
        chkDeleted = new CheckBox();
        chkDeleted.setFieldLabel("Deleted");
    }

    private void addFields() {
        panel.add(userName, formData);
        panel.add(path, formData);
        panel.add(refGenName, formData);
        // panel.add(creationDate, formData);
        // panel.add(lastModDate, formData);
        panel.add(lastModUserName, formData);
        panel.add(chkDeleted, formData);
    }

}
