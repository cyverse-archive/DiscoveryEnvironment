package org.iplantc.de.server;

import gwtupload.server.exceptions.UploadActionException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

/**
 * A class to accept files from the client.
 *
 * This class extends the UploadAction class provided by the GWT Upload library. The executeAction method
 * must be overridden for custom behavior.
 *
 * @author sriram
 *
 */
@SuppressWarnings("nls")
public class NewToolRequestServlet extends UploadServlet {
    private static final long serialVersionUID = 1L;

    /**
     * The logger for error and informational messages.
     */
    private static Logger LOG = Logger.getLogger(NewToolRequestServlet.class);

    public NewToolRequestServlet(ServiceCallResolver serviceResolver) {
        super(serviceResolver);
    }

    /**
     * Performs the necessary operations for an upload action.
     *
     * @param request the HTTP request associated with the action.
     * @param fileItems the file associated with the action.
     * @return a string representing data in JSON format.
     * @throws UploadActionException if there is an issue invoking the dispatch to the servlet
     */
    @Override
    public String executeAction(HttpServletRequest request, List<FileItem> fileItems)   {
        super.executeAction(request, fileItems);

        if (!jsonErrors.containsKey("error")) {
            try {
                SimpleMessageSender msgSender = new SimpleMessageSender();
                LOG.debug("executeAction - Attempting to send email.");
                msgSender.send(user, email, jsonInfo.toString(2));

                jsonErrors.put("success", "Your tool request was successfully submitted.");
            } catch (Exception e) {
                LOG.error(
                        "executeAction - Exception while sending email to support about tool request.",
                        e);
                e.printStackTrace();
                jsonErrors.put("error", e.getMessage() == null ? e.toString() : e.getMessage());
            }
        }

        LOG.debug("executeAction - JSON returned: " + jsonErrors);

        // remove files from session. this avoids duplicate submissions
        removeSessionFileItems(request, false);

        return jsonErrors.toString();
    }

}
