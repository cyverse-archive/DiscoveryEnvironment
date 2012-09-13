package org.iplantc.de.server;

import java.util.Properties;
import java.util.TreeSet;
import javax.servlet.ServletContextEvent;
import org.apache.log4j.Logger;
import org.iplantc.de.server.spring.ClavinPropertyPlaceholderConfigurer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

/**
 * A Servlet context listener written specifically for the DE.
 *
 * @author Dennis Roberts
 */
public class DeServletContextListener extends ContextLoaderListener {

    /**
     * Used to log configuration settings.
     */
    private static final Logger LOG = Logger.getLogger(DeServletContextListener.class);

    /**
     * The discovery environment configuration settings.
     */
    private Properties deConfig;

    /**
     * The confluence client configuration settings.
     */
    private Properties confluenceConfig;

    /**
     * Loads the configuration and initializes the web application context.
     *
     * @param event the ServletContextEvent containing the ServletContext being initialized.
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        loadConfigs();
    }

    /**
     * Loads the configuration settings.
     */
    private void loadConfigs() {
        WebApplicationContext wac = getCurrentWebApplicationContext();
        ClavinPropertyPlaceholderConfigurer configurer
                = (ClavinPropertyPlaceholderConfigurer) wac.getBean("propertyPlaceholderConfigurer");
        if (configurer != null) {
            deConfig = loadConfig(configurer, "discoveryenvironment");
            confluenceConfig = loadConfig(configurer, "confluence");
        }
    }

    /**
     * Loads a single configuration from the configurer bean.
     *
     * @param configurer the configurer bean.
     * @param configName the name of the configuration to load.
     * @return the configuration settings as a {@link Properties} instance.
     */
    private Properties loadConfig(ClavinPropertyPlaceholderConfigurer configurer, String configName) {
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
}
