package org.iplantc.admin.belphegor.client.util;

import org.iplantc.core.uicommons.client.widgets.BoundedTextArea;
import org.iplantc.core.uicommons.client.widgets.BoundedTextField;

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;

public class FormFieldBuilderUtil {

    public static final String ID = "id_"; //$NON-NLS-1$

    public static TextField<String> buildTextField(String label, boolean allowBlank, String defaultVal,
            String name, Validator validator, int maxLength) {
        BoundedTextField<String> field = new BoundedTextField<String>();
        field.setMaxLength(maxLength);
        field.setName(name);
        field.setId(ID + name);
        field.setFieldLabel(allowBlank ? label : buildRequiredFieldLabel(label));
        field.setAllowBlank(allowBlank);
        field.setAutoValidate(true);
        field.setStyleAttribute("padding-bottom", "5px"); //$NON-NLS-1$ //$NON-NLS-2$

        if (defaultVal != null) {
            field.setValue(defaultVal);
        }
        if (validator != null) {
            field.setValidator(validator);
        }

        return field;
    }

    public static String buildRequiredFieldLabel(String label) {
        if (label == null) {
            return null;
        }

        return "<span class='required_marker'>*</span> " + label; //$NON-NLS-1$
    }

    public static DateField buildDateField(String label, boolean allowBlank) {
        DateField field = new DateField();
        if (!allowBlank) {
            field.setFieldLabel(buildRequiredFieldLabel(label));
        } else {
            field.setFieldLabel(label);
        }
        field.setAllowBlank(allowBlank);
        return field;
    }

    public static TextArea buildTextArea(String label, boolean allowBlank, String defaultVal,
            String name, int maxLength) {
        TextArea field = new BoundedTextArea();
        field.setMaxLength(maxLength);
        field.setName(name);
        field.setId(ID + name);
        field.setFieldLabel(label);
        field.setAllowBlank(allowBlank);
        field.setValidateOnBlur(true);
        field.setStyleAttribute("padding-bottom", "5px"); //$NON-NLS-1$ //$NON-NLS-2$
        if (defaultVal != null) {
            field.setValue(defaultVal);
        }
        return field;
    }
}
