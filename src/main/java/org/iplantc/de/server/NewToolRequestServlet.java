package org.iplantc.de.server;

import gwtupload.server.exceptions.UploadActionException;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.iplantc.de.server.service.IplantEmailClient;

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

    /**
     * The configuration settings for the application.
     */
    private final Properties props;

    /**
     * Used to communicate with the iPlant e-mail service.
     */
    private final IplantEmailClient emailClient;

    /**
     * @param serviceResolver used to resolve calls to aliased services.
     * @param props the configuration settings for the application.
     * @param emailClient the client used to communicate with the iPlant e-mail service.
     */
    public NewToolRequestServlet(ServiceCallResolver serviceResolver, Properties props, IplantEmailClient emailClient) {
        super(serviceResolver);
        this.props = props;
        this.emailClient = emailClient;
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
                SimpleMessageSender msgSender = new SimpleMessageSender(props, emailClient);
                LOG.debug("executeAction - Attempting to send email.");
                msgSender.send(user, email, jsonInfo.toString(2));

                jsonErrors.put("success", "Your tool request was successfully submitted.");
            } catch (Exception e) {
                LOG.error(
                        "executeAction - Exception while sending email to support about tool request.",
                        e);
                jsonErrors.put("error", e.getMessage() == null ? e.toString() : e.getMessage());
            }
        }

        LOG.debug("executeAction - JSON returned: " + jsonErrors);

        // remove files from session. this avoids duplicate submissions
        removeSessionFileItems(request, false);

        return jsonErrors.toString();
    }
}
