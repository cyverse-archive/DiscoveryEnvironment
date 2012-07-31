package org.iplantc.de.client.utils;

import java.util.Map;

import org.iplantc.core.client.widgets.utils.ComponentValue;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * Utility class to format export JSON from a ComponentValueTable.
 * 
 * @author amuir
 * 
 */
public class WizardExportHelper {
    /**
     * Build export JSON from a component value table.
     * 
     * @param tblComponentVals table of values to export.
     * @param sendEmailNotification flag indicating users notification intent.
     * @return properly formatted export JSON.
     */
    public static String buildJSON(final ComponentValueTable tblComponentVals,
            boolean sendEmailNotification) {
        JSONObject analysis = new JSONObject();

        if (tblComponentVals != null) {
            analysis.put("name", new JSONString(tblComponentVals.getName())); //$NON-NLS-1$
            analysis.put("type", new JSONString(tblComponentVals.getType())); //$NON-NLS-1$
            analysis.put("description", new JSONString(tblComponentVals.getDescription())); //$NON-NLS-1$
            analysis.put("analysis_id", new JSONString(tblComponentVals.getTemplateId())); //$NON-NLS-1$
            analysis.put("notify", JSONBoolean.getInstance(sendEmailNotification)); //$NON-NLS-1$
            analysis.put("debug", JSONBoolean.getInstance(tblComponentVals.isDebugEnabled())); //$NON-NLS-1$
            analysis.put("notify", JSONBoolean.getInstance(tblComponentVals.isNotifyEnabled())); //$NON-NLS-1$
            analysis.put("output_dir", new JSONString(tblComponentVals.getOutputFolderId()));//$NON-NLS-1$
            analysis.put("create_output_subdir",
                    JSONBoolean.getInstance(tblComponentVals.isCreateSubFolder()));//$NON-NLS-1$
            JSONObject config = new JSONObject();

            Map<String, ComponentValue> map = tblComponentVals.getValues();

            for (String id : map.keySet()) {
                ComponentValue val = map.get(id);

                if (val.isExportable()) {
                    JSONValue exportValue = val.getExportValue();

                    if (exportValue == null) {
                        exportValue = new JSONString(""); //$NON-NLS-1$
                    }

                    config.put(id, exportValue);
                }
            }

            // add config
            analysis.put("config", config); //$NON-NLS-1$
        }

        return analysis.toString();
    }
}
