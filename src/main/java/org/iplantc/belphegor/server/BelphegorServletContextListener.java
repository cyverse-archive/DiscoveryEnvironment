package org.iplantc.belphegor.server;

import java.util.Properties;
import javax.servlet.Servlet;
import org.iplantc.clavin.spring.AbstractServletContextListener;
import org.iplantc.clavin.spring.ClavinPropertyPlaceholderConfigurer;
import org.iplantc.de.server.CasLogoutServlet;
import org.iplantc.de.server.CasSessionInitializationServlet;

/**
 * A ServletContextListener written specifically for Belphegor.
 *
 * @author Dennis Roberts
 */
public class BelphegorServletContextListener extends AbstractServletContextListener {

    /**
     * The prefix for property names in the application configuration settings.
     */
    private static final String PROPERTY_NAME_PREFIX = "org.iplantc.belphegor";

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

        // The session initialization servlet.
        new ServletRegistrationInfo(new CasSessionInitializationServlet("#workspace"), "sessionInitializationServlet",
            "/belphegor/login"),

        // The logout success servlet.
        new ServletRegistrationInfo(new ServletProvider() {
            @Override public Servlet getServlet() {
                return new CasLogoutServlet(belphegorConfig, PROPERTY_NAME_PREFIX);
            }
        }, "logoutSuccessServlet", "/belphegor/logout")
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
