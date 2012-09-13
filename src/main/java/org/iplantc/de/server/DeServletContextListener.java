package org.iplantc.de.server;

import java.util.Properties;
import org.apache.log4j.Logger;
import org.iplantc.de.server.spring.AbstractServletContextListener;
import org.iplantc.de.server.spring.ClavinPropertyPlaceholderConfigurer;

/**
 * A Servlet context listener written specifically for the DE.
 *
 * @author Dennis Roberts
 */
public class DeServletContextListener extends AbstractServletContextListener {

    /**
     * Used to log configuration settings.
     */
    private static final Logger LOG = Logger.getLogger(DeServletContextListener.class);

    /**
     * The Discovery Environment configuration settings.
     */
    private Properties deConfig;

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
        deConfig = loadConfig(configurer, "discoveryenvironment");
        confluenceConfig = loadConfig(configurer, "confluence");
    }

    /**
     * Registers the servlets used by the DE.
     */
    @Override
    protected void registerServlets() {}
}
