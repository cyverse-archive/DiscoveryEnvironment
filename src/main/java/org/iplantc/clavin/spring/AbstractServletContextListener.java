package org.iplantc.clavin.spring;

import java.util.Properties;
import java.util.TreeSet;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRegistration;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

/**
 * An abstract ServletContextListener for services that need to authenticate using CAS and obtain configuration
 * settings using a Clavin client.
 *
 * @author Dennis Roberts
 */
public abstract class AbstractServletContextListener extends ContextLoaderListener {

    /**
     * Used to log configuration settings.
     */
    private static final Logger LOG = Logger.getLogger(AbstractServletContextListener.class);

    /**
     * Loads the configuration and initializes the web application context.
     *
     * @param event the ServletContextEvent containing the ServletContext being initialized.
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        ClavinPropertyPlaceholderConfigurer configurer = getPropertyPlaceholderConfigurer();
        Assert.notNull(configurer, "a ClavinPropertyPlaceholderConfigurer bean must be defined");
        loadConfigs(configurer);
        registerServlets(event.getServletContext());
    }

    /**
     * @return the property placeholder configurer.
     */
    private ClavinPropertyPlaceholderConfigurer getPropertyPlaceholderConfigurer() {
        WebApplicationContext wac = getCurrentWebApplicationContext();
        return (ClavinPropertyPlaceholderConfigurer) wac.getBean(ClavinPropertyPlaceholderConfigurer.class);
    }

    /**
     * Loads any configurations that still need to be loaded.  The configurations are assumed to be stored in a
     * {@link ClavinPropertyPlaceholderConfigurer} instance.
     *
     * @param configurer the Clavin property placeholder configurer to get the configurations from.
     */
    protected abstract void loadConfigs(ClavinPropertyPlaceholderConfigurer configurer);

    /**
     * Registers any servlets that need to be registered by the context listener.
     *
     * @param context the servlet context.
     */
    private void registerServlets(ServletContext context) {
        for (ServletRegistrationInfo regInfo : getServletRegistrationInfo()) {
            regInfo.register(context);
        }
    }

    /**
     * Obtains information that can be used to register servlets.
     *
     * @return the an array of ServletRegistrationInfo instances.
     */
    protected abstract ServletRegistrationInfo[] getServletRegistrationInfo();

    /**
     * Loads a single configuration from the configurer bean.
     *
     * @param configurer the configurer bean.
     * @param configName the name of the configuration to load.
     * @return the configuration settings as a {@link Properties} instance.
     */
    protected Properties loadConfig(ClavinPropertyPlaceholderConfigurer configurer, String configName) {
        LOG.warn("CONFIGURATION: retrieving config - " + configName);
        Properties props = configurer.getConfig(configName);
        if (props != null) {
            for (String propName : new TreeSet<String>(props.stringPropertyNames())) {
                LOG.warn("CONFIGURATION: " + propName + " = " + props.getProperty(propName));
            }
        }
        else {
            LOG.warn("CONFIGURATION: no configuration found - " + configName);
        }
        return props;
    }

    /**
     * An interface that provides a simple way to obtain a servlet.
     */
    protected interface ServletProvider {

        /**
         * @return the servlet.
         */
        public Servlet getServlet();
    }

    /**
     * The default servlet provider implementation, which simply returns the servlet that was passed to it
     * in the constructor.
     */
    private class DefaultServletProvider implements ServletProvider {

        /**
         * The servlet.
         */
        private Servlet servlet;

        /**
         * @param servlet the servlet.
         */
        public DefaultServletProvider(Servlet servlet) {
            this.servlet = servlet;
        }

        /**
         * @return the servlet.
         */
        public Servlet getServlet() {
            return servlet;
        }
    }

    /**
     * Information that can be used to register a servlet.
     */
    protected class ServletRegistrationInfo {

        /**
         * Used to obtain the servlet to register.
         */
        ServletProvider servletProvider;

        /**
         * The name of the servlet.
         */
        String name;

        /**
         * The path to the servlet, relative to the context path.
         */
        String path;

        /**
         * @param servlet the servlet to register.
         * @param name the name of the servlet.
         * @param path the path to the servlet, relative to the context path.
         */
        public ServletRegistrationInfo(Servlet servlet, String name, String path) {
            this(new DefaultServletProvider(servlet), name, path);
        }

        /**
         * @param servletProvider used to obtain the servlet to register.
         * @param name the name of the servlet.
         * @param path the path to the servlet, relative to the context path.
         */
        public ServletRegistrationInfo(ServletProvider servletProvider, String name, String path) {
            this.servletProvider = servletProvider;
            this.name = name;
            this.path = path;
        }

        /**
         * Registers the servlet.
         *
         * @param context the servlet context.
         */
        public void register(ServletContext context) {
            LOG.warn("registering servlet " + name + " at " + path);
            ServletRegistration.Dynamic reg = context.addServlet(name, servletProvider.getServlet());
            if (reg == null) {
                throw new ServletRegistrationException(name);
            }
            reg.addMapping(path);
        }
    }
}
