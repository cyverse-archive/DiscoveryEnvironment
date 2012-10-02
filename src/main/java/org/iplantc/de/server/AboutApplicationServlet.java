package org.iplantc.de.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import javax.servlet.ServletException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.iplantc.de.shared.services.AboutApplicationService;

/**
 * Communicates application information to include as "about" data regarding the current build and client UserAgent.
 *
 * This servlet will include information about services once the modeling of software components has been completed.
 *
 * @see org.iplantc.de.client.services.AboutApplicationService
 * @author lenards
 */
@SuppressWarnings("nls")
public class AboutApplicationServlet extends RemoteServiceServlet implements AboutApplicationService {

    private static final long serialVersionUID          = 6046105023536377635L;
    private static final String MANIFEST_LOC            = "/META-INF/MANIFEST.MF";
    private static final String BUILD_NUMBER_ATTRIBUTE  = "Hudson-Build-Number";
    private static final String DEFAULT_RELEASE_VERSION = "unversioned";

    /**
     * The logger for error and informational messages.
     */
    private static Logger LOG = Logger.getLogger(AboutApplicationServlet.class);

    /**
     * The DE configuration properties.
     */
    private DiscoveryEnvironmentProperties deProps;

    /**
     * The default constructor.
     */
    public AboutApplicationServlet() {}

    /**
     * @param deProps the DE configuration properties.
     */
    public AboutApplicationServlet(DiscoveryEnvironmentProperties deProps) {
        this.deProps = deProps;
    }

    /**
     * Initializes the servlet.
     *
     * @throws ServletException if the servlet can't be initialized.
     * @throws IllegalStateException if the discovery environment properties can't be loaded.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        if (deProps == null) {
            deProps = DiscoveryEnvironmentProperties.getDiscoveryEnvironmentProperties(getServletContext());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAboutInfo() {
        return produceInfo();
    }

    /**
     * Produces the information used to build the about application screen.
     *
     * @return a JSON string containing the build number and release version.
     */
    private String produceInfo() {
        String buildNumber = getBuildNumber();
        String relVersion = getReleaseVersion();
        String response = buildResponse(buildNumber, relVersion);
        LOG.debug("the about application JSON is: " + response);
        return response;
    }

    /**
     * Builds the response body.
     *
     * @param buildNumber the build number to include in the response.
     * @param relVersion the release version number to include in the response.
     * @return the response body.
     */
    private String buildResponse(String buildNumber, String relVersion) {
        JSONObject json = new JSONObject();
        json.put("buildnumber", buildNumber);
        json.put("release", relVersion);
        return json.toString(4);
    }

    /**
     * Get the release version that is stored in the application properties file.
     *
     * @return a string representation of the release version.
     */
    private String getReleaseVersion() {
        String version = deProps.getReleaseVersion();
        return (StringUtils.isNotEmpty(version)) ? version : DEFAULT_RELEASE_VERSION;
    }

    /**
     * Get the build number to report in the about application screen. If the build number is available in the project
     * manifest then that build number will be used. Otherwise, the default build number will be obtained from the
     * properties file.
     *
     * @return a string representation of the build number.
     */
    private String getBuildNumber() {
        String buildNumber = getBuildNumberFromManifest();
        return StringUtils.isEmpty(buildNumber) ? deProps.getDefaultBuildNumber() : buildNumber;
    }

    /**
     * Attempts to obtain the the build number from the manifest file.
     *
     * @return the build number or an empty value (null or the empty string) if the build number is not available.
     */
    private String getBuildNumberFromManifest() {
        String buildNumber = null;
        Manifest manifest;
        try {
            manifest = new Manifest(getServletContext().getResourceAsStream(MANIFEST_LOC));
            Attributes attrs = manifest.getMainAttributes();
            buildNumber = attrs.getValue(BUILD_NUMBER_ATTRIBUTE);
        }
        catch (Exception e) {
            LOG.error("unable to get the build number", e);
        }
        return buildNumber;
    }
}