package org.iplantc.de.server;

import org.apache.commons.lang.StringUtils;
import org.iplantc.de.shared.services.AboutApplicationService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import net.sf.json.JSONObject;

/**
 * Communicates application information to include as "about" data regarding the current build and client
 * UserAgent.
 *
 * This servlet will include information about services once the modeling of software components has been
 * completed.
 *
 * @see org.iplantc.de.client.services.AboutApplicationService
 * @author lenards
 */
@SuppressWarnings("nls")
public class AboutApplicationServlet extends RemoteServiceServlet implements AboutApplicationService {

    /**
     * Generated Unique Identifier for serialization.
     */
    private static final long serialVersionUID = 6046105023536377635L;
    private static final String DEFAULT_RELEASE_VERSION = "unversioned";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAboutInfo() {
        return produceInfo();
    }

    private String produceInfo() {
        JSONObject json = new JSONObject();
        json.put("buildnumber", DiscoveryEnvironmentProperties.getDefaultBuildNumber());
        json.put("release", getReleaseVersion());
        return json.toString();
    }

    /**
     * Get the release version that is stored in the application properties file.
     *
     * @return a string representation of the release version.
     */
    private String getReleaseVersion() {
        String version = DiscoveryEnvironmentProperties.getReleaseVersion();
        return StringUtils.isEmpty(version) ? DEFAULT_RELEASE_VERSION : version;
    }
}
