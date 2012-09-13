package org.iplantc.de.server;

import java.util.Properties;
import org.iplantc.clavin.spring.AbstractServletContextListener;
import org.iplantc.clavin.spring.ClavinPropertyPlaceholderConfigurer;

/**
 * A Servlet context listener written specifically for the DE.
 *
 * @author Dennis Roberts
 */
public class DeServletContextListener extends AbstractServletContextListener {

    /**
     * The Discovery Environment configuration settings.
     */
    private Properties deConfig;

    /**
     * The Confluence client configuration settings.
     */
    private Properties confluenceConfig;

    /**
     * Servlet registration information.
     */
    private ServletRegistrationInfo[] regInfo = {
        new ServletRegistrationInfo(new CasSessionInitializationServlet("#workspace"), "sessionInitializationServlet",
            "/discoveryenvironment/login")
    };

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
     * Obtains servlet registration information.
     *
     * @return the servlet registration information.
     */
    @Override
    public ServletRegistrationInfo[] getServletRegistrationInfo() {
        return regInfo;
    }
}
