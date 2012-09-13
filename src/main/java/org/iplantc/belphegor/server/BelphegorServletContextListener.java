package org.iplantc.belphegor.server;

import java.util.Properties;
import org.iplantc.clavin.spring.AbstractServletContextListener;
import org.iplantc.clavin.spring.ClavinPropertyPlaceholderConfigurer;
import org.iplantc.de.server.CasSessionInitializationServlet;

/**
 * A ServletContextListener written specifically for Belphegor.
 *
 * @author Dennis Roberts
 */
public class BelphegorServletContextListener extends AbstractServletContextListener {

    /**
     * The Belphegor configuration settings.
     */
    private Properties belphegorConfig;

    /**
     * The Confluence client configuration settings.
     */
    private Properties confluenceConfig;

    /**
     * Servlet registration information.
     */
    private ServletRegistrationInfo[] regInfo = {
        new ServletRegistrationInfo(new CasSessionInitializationServlet("#workspace"), "sessionInitializationServlet",
            "/belphegor/login")
    };

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
     * Obtains servlet registration information.
     *
     * @return the servlet registration information.
     */
    @Override
    public ServletRegistrationInfo[] getServletRegistrationInfo() {
        return regInfo;
    }
}
