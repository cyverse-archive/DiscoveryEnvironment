package org.iplantc.de.server;

import gwtupload.server.exceptions.UploadActionException;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.iplantc.clavin.spring.ConfigAliasResolver;
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
    private Properties props;

    /**
     * Used to communicate with the iPlant e-mail service.
     */
    private IplantEmailClient emailClient;

    /**
     * The default constructor.
     */
    public NewToolRequestServlet() {}

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
     * Initializes the servlet.
     *
     * @throws ServletException if the servlet can't be initialized.
     * @throws IllegalStateException if the configuration settings or the iPlant e-mail client can't be found.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        if (props == null && emailClient == null) {
            props = ConfigAliasResolver.getRequiredAliasedConfigFrom(getServletContext(), "webapp");
            emailClient = IplantEmailClient.getClient(getServletContext());
        }
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
