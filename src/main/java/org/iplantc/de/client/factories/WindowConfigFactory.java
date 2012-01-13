package org.iplantc.de.client.factories;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.models.BasicWindowConfig;
import org.iplantc.de.client.models.NotificationWindowConfig;
import org.iplantc.de.client.models.WindowConfig;

import com.google.gwt.json.client.JSONObject;

/**
 * Factory class for generating window configuration classes.
 * 
 * @author amuir
 * 
 */
public class WindowConfigFactory {
    /**
     * Build a window configuration object from a window JSON message payload.
     * 
     * @param objPayload payload from window JSON message.
     * @return newly created WindowConfig. null on failure.
     */
    public WindowConfig build(final JSONObject objPayload) {
        WindowConfig ret = null; // assume failure

        if (objPayload != null) {
            JSONObject objData = JsonUtil.getObject(objPayload, "data"); //$NON-NLS-1$

            if (objData != null) {
                JSONObject objConfig = JsonUtil.getObject(objData, "config"); //$NON-NLS-1$

                if (objConfig != null) {
                    objData = JsonUtil.getObject(objConfig, "data"); //$NON-NLS-1$
                    String type = JsonUtil.getString(objConfig, "type"); //$NON-NLS-1$

                    // notification window config
                    if (type.equals("notification_window")) { //$NON-NLS-1$
                        ret = new NotificationWindowConfig(objData);
                    } else if (type.equals("analysis_window")) { //$NON-NLS-1$
                        ret = new BasicWindowConfig(objData);
                    } else if (type.equals("my_data_window")) { //$NON-NLS-1$
                        ret = new BasicWindowConfig(objData);
                    }
                }
            }
        }

        return ret;
    }
}