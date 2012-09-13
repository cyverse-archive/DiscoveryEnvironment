package org.iplantc.belphegor.server;

import java.util.Properties;
import org.apache.log4j.Logger;
import org.iplantc.de.server.spring.AbstractServletContextListener;
import org.iplantc.de.server.spring.ClavinPropertyPlaceholderConfigurer;

/**
 * A ServletContextListener written specifically for Belphegor.
 *
 * @author Dennis Roberts
 */
public class BelphegorServletContextListener extends AbstractServletContextListener {

    /**
     * Used to log configuration settings.
     */
    private static final Logger LOG = Logger.getLogger(BelphegorServletContextListener.class);

    /**
     * The Belphegor configuration settings.
     */
    private Properties belphegorConfig;

    /**
     * The Confluence client configuration settings.
     */
    private Properties confluenceConfig;

    /**
     * Loads the configuration settings.
     *
     * @param configurer the property placeholder configurer that was used to load the configuration settings.
     */
    @Override
    protected void loadConfigs(ClavinPropertyPlaceholderConfigurer configurer) {
        belphegorConfig = loadConfig(configurer, "belphegor");
        confluenceConfig = loadConfig(configurer, "belphegor-confluence");
    }

    /**
     * Registers the servlets that are used by Belphegor.
     */
    @Override
    protected void registerServlets() {}
}
