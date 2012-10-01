package org.iplantc.clavin.spring;

import java.util.Map;
import java.util.Properties;

/**
 * Resolves aliased configurations names.
 *
 * @author Dennis Roberts
 */
public class ConfigAliasResolver {

    /**
     * The configurer that stores the actual configurations.
     */
    private ClavinPropertyPlaceholderConfigurer configurer;

    /**
     * Maps configuration aliases to their actual names.
     */
    private Map<String, String> aliases;

    /**
     * @param configurer the configurer that stores the actual configurations.
     */
    public void setConfigurer(ClavinPropertyPlaceholderConfigurer configurer) {
        this.configurer = configurer;
    }

    /**
     * @param aliases maps configuration aliases to their actual names.
     */
    public void setAliases(Map<String, String> aliases) {
        this.aliases = aliases;
    }

    /**
     * Retrieves an aliased configuration.
     *
     * @param alias the alias.
     * @return the configuration or null if the configuration isn't found.
     */
    public Properties getAliasedConfig(String alias) {
        String name = aliases.get(alias);
        return name == null ? null : configurer.getConfig(name);
    }
}
