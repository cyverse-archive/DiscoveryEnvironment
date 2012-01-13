package org.iplantc.de.client.factories;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.Constants;
import org.iplantc.de.client.models.BasicWindowConfig;
import org.iplantc.de.client.models.IDropLiteWindowConfig;
import org.iplantc.de.client.models.NotificationWindowConfig;
import org.iplantc.de.client.models.WindowConfig;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

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
                    } else if (type.equals(Constants.CLIENT.iDropLiteTag())) {
                        ret = new IDropLiteWindowConfig(objData);
                    }
                }
            }
        }

        return ret;
    }

    /**
     * Build a JSON message payload window configuration object.
     * 
     * @return JSON message payload window config
     */
    public JSONObject buildConfigPayload(final String tag, final String configType,
            final JSONObject windowConfigData) {
        if (windowConfigData == null) {
            return null;
        }

        JSONObject windowConfig = new JSONObject();

        windowConfig.put("data", windowConfigData); //$NON-NLS-1$
        windowConfig.put("type", new JSONString(configType == null ? "" : configType)); //$NON-NLS-1$ //$NON-NLS-2$

        JSONObject windowPayload = new JSONObject();
        windowPayload.put("tag", new JSONString(tag == null ? "" : tag)); //$NON-NLS-1$ //$NON-NLS-2$
        windowPayload.put("config", windowConfig); //$NON-NLS-1$

        return windowPayload;
    }
}