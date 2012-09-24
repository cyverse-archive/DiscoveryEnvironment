package org.iplantc.de.client.utils.builders.context;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.de.client.notifications.models.NotificationPayload;

/**
 * Build a JSON string to provide context when a user clicks on an item with an analysis context
 * associated it.
 * 
 * @author amuir
 * 
 */
public class AnalysisContextBuilder extends AbstractContextBuilder {

    @Override
    public String build(NotificationPayload payload) {
        String ret = null; // assume failure
        if (payload != null) {
            String action = getAction(payload);
            if (action != null) {
                if (action.equals("job_status_change")) { //$NON-NLS-1$
                    String id = payload.getId();
                    ret = "{\"id\": " + JsonUtil.quoteString(id) + "}"; //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }

        return ret;
    }
}
