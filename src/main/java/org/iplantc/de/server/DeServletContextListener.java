package org.iplantc.de.server;

import com.google.gwt.junit.server.JUnitHostImpl;
import java.util.Properties;
import javax.servlet.Servlet;
import org.iplantc.clavin.spring.AbstractServletContextListener;
import org.iplantc.clavin.spring.ClavinPropertyPlaceholderConfigurer;

/**
 * A Servlet context listener written specifically for the DE.
 *
 * @author Dennis Roberts
 */
public class DeServletContextListener extends AbstractServletContextListener {

    /**
     * The prefix for property names in the application configuration settings.
     */
    private static final String PROPERTY_NAME_PREFIX = "org.iplantc.discoveryenvironment";

    /**
     * The Discovery Environment configuration settings.
     */
    private Properties deConfig;

    /**
     * The Confluence client configuration settings.
     */
    private Properties confluenceConfig;

    /**
     * A service call resolver used in several dispatcher services.
     */
    private ServiceCallResolver serviceCallResolver;

    /**
     * Servlet registration information.
     */
    private ServletRegistrationInfo[] regInfo = {

        // The session initialization servlet.
        new ServletRegistrationInfo(new CasSessionInitializationServlet("#workspace"), "sessionInitializationServlet",
            "/discoveryenvironment/login"),

        // The logout success servlet.
        new ServletRegistrationInfo(new ServletProvider() {
            @Override public Servlet getServlet() {
                return new CasLogoutServlet(deConfig, PROPERTY_NAME_PREFIX);
            }
        }, "logoutSuccessServlet", "/discoveryenvironment/logout"),

        // The servlet used to manage file uploads.
        new ServletRegistrationInfo(new ServletProvider() {
            @Override public Servlet getServlet() {
                return new FileUploadServlet(serviceCallResolver);
            }
        }, "fileUploadServlet", "*.gupld"),

        // The servlet used to manage file downloads.
        new ServletRegistrationInfo(new ServletProvider() {
            @Override public Servlet getServlet() {
                return new FileDownloadServlet(serviceCallResolver);
            }
        }, "fileDownloadServlet", "*.gdwnld"),

        // A service dispatcher for secured service calls.
        new ServletRegistrationInfo(new ServletProvider() {
            @Override public Servlet getServlet() {
                return new CasServiceDispatcher(serviceCallResolver);
            }
        }, "deServiceDispatcher", "/discoveryenvironment/deservice"),

        // A service dispatcher for unsecured service calls.
        new ServletRegistrationInfo(new ServletProvider() {
            @Override public Servlet getServlet() {
                return new UnsecuredDEServiceDispatcher(serviceCallResolver);
            }
        }, "unsecuredDeServiceDispatcher", "/discoveryenvironment/unsecureddeservice"),

        // A service dispatcher for data API service calls.
        new ServletRegistrationInfo(new ServletProvider() {
            @Override public Servlet getServlet() {
                return new DataApiServiceDispatcher(serviceCallResolver);
            }
        }, "dataApiServiceDispatcher", "/discoveryenvironment/data-api-service"),

        // A service dispatcher that validates authentication.
        new ServletRegistrationInfo(new ServletProvider() {
            @Override public Servlet getServlet() {
                return new AuthenticationValidatingServiceDispatcher(serviceCallResolver);
            }
        }, "authenticationValidatingServiceDispatcher", "/discoveryenvironment/auth-de-service"),

        // A servlet to manage user sessions.
        new ServletRegistrationInfo(new SessionManagementServlet(), "sessionManagementServlet",
            "/discoveryenvironment/sessionmanagement"),

        // A servlet to return configuration properties to the UI.
        new ServletRegistrationInfo(new ServletProvider() {
            @Override public Servlet getServlet() {
                return new PropertyServlet(deConfig);
            }
        }, "propertyServlet", "/discoveryenvironment/properties"),

        // A servlet to provide a mock host for unit testing.
        new ServletRegistrationInfo(new JUnitHostImpl(), "jUnitHostImpl", "/discoveryenvironment/junithost"),

        // A servlet to provide information about the application.
        new ServletRegistrationInfo(new AboutApplicationServlet(), "aboutAppServlet", "/discoveryenvironment/about"),

        // A servlet to provide an interface to Confluence.
        new ServletRegistrationInfo(new ServletProvider() {
            @Override public Servlet getServlet() {
                return new ConfluenceServlet(confluenceConfig);
            }
        }, "confluenceServlet", "/discoveryenvironment/confluence")
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
    protected ServletRegistrationInfo[] getServletRegistrationInfo() {
        return regInfo;
    }

    /**
     * Initializes some objects that are required by some of the servlets.
     */
    @Override
    protected void initialize() {
        serviceCallResolver = new DefaultServiceCallResolver(deConfig);
    }
}
