package org.iplantc.belphegor.server;

import java.util.Properties;
import javax.servlet.Servlet;
import org.iplantc.clavin.spring.AbstractServletContextListener;
import org.iplantc.clavin.spring.ClavinPropertyPlaceholderConfigurer;
import org.iplantc.de.server.CasLogoutServlet;
import org.iplantc.de.server.CasServiceDispatcher;
import org.iplantc.de.server.CasSessionInitializationServlet;
import org.iplantc.de.server.ConfluenceServlet;
import org.iplantc.de.server.DefaultServiceCallResolver;
import org.iplantc.de.server.EmptyResponseServlet;
import org.iplantc.de.server.PropertyServlet;
import org.iplantc.de.server.ServiceCallResolver;
import org.iplantc.de.server.SessionManagementServlet;
import org.iplantc.de.server.UnsecuredDEServiceDispatcher;

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
     * Used by several servlets to resolve aliased service calls.
     */
    private ServiceCallResolver serviceCallResolver;

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
        }, "logoutSuccessServlet", "/belphegor/logout"),

        // A service dispatcher for secured service calls.
        new ServletRegistrationInfo(new ServletProvider() {
            @Override public Servlet getServlet() {
                return new CasServiceDispatcher(serviceCallResolver);
            }
        }, "deServiceDispatcher", "/belphegor/deservice"),

        // A service dispatcher for unsecured service calls.
        new ServletRegistrationInfo(new ServletProvider() {
            @Override public Servlet getServlet() {
                return new UnsecuredDEServiceDispatcher(serviceCallResolver);
            }
        }, "unsecuredDeServiceDispatcher", "/belphegor/unsecureddeservice"),

        // A servlet to manage user sessions.
        new ServletRegistrationInfo(new SessionManagementServlet(), "sessionManagementServlet",
            "/belphegor/sessionmanagement"),

        // A servlet to return configuration properties to the UI.
        new ServletRegistrationInfo(new ServletProvider() {
            @Override public Servlet getServlet() {
                return new PropertyServlet(belphegorConfig);
            }
        }, "propertyServlet", "/belphegor/properties"),

        // A servlet to provide an interface to Confluence.
        new ServletRegistrationInfo(new ServletProvider() {
            @Override public Servlet getServlet() {
                return new ConfluenceServlet(confluenceConfig);
            }
        }, "confluenceServlet", "/discoveryenvironment/confluence", "/belphegor/confluence"),

        // A servlet that always returns an empty response, which is useful for keepalive pings.
        new ServletRegistrationInfo(new EmptyResponseServlet(), "emptyResponseServlet", "/belphegor/empty")
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

    /**
     * Initializes some objects that are required by some of the servlets.
     */
    @Override
    protected void initialize() {
        serviceCallResolver = new DefaultServiceCallResolver(belphegorConfig);
    }
}
