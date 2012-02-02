package org.iplantc.de.server;

import org.iplantc.de.shared.services.ConfluenceService;
import org.swift.common.cli.CliClient.ExitCode;
import org.swift.confluence.cli.ConfluenceClient;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * A service for interfacing with Confluence.
 * 
 * @author hariolf
 * 
 */
public class ConfluenceServlet extends RemoteServiceServlet implements ConfluenceService {
    private static final long serialVersionUID = -7866351808664668611L;

    /**
     * {@inheritDoc}
     */
    @Override
    public String addPage(String toolName, String description) {
        String url = DiscoveryEnvironmentProperties.getConfluenceBaseUrl();
        String parent = DiscoveryEnvironmentProperties.getConfluenceParentPage();
        String user = DiscoveryEnvironmentProperties.getConfluenceUser();
        String password = DiscoveryEnvironmentProperties.getConfluencePassword();
        String space = DiscoveryEnvironmentProperties.getConfluenceSpaceName();

        String[] args = new String[] {"--server", url, "--user", user, "--password", password, "-a", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                "addPage", "--space", space, "--title", toolName, "--content", description, "--parent", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
                parent};
        ExitCode code = new ConfluenceClient().doWork(args);

        if (code != ExitCode.SUCCESS) {
            throw new RuntimeException("Can't create Confluence page! Error code = " + code); //$NON-NLS-1$
        }
        return DiscoveryEnvironmentProperties.getConfluenceSpaceUrl() + toolName;
    }

}
