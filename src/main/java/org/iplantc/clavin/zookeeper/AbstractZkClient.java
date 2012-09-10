package org.iplantc.clavin.zookeeper;

/**
 * An abstract Zookeeper client that implements some common functionality.
 *
 * @author Dennis Roberts
 */
public abstract class AbstractZkClient implements ZkClient {

    /**
     * Validates a Zookeeper path.
     *
     * @param path the path to validate.
     * @return the path that was validated.
     */
    protected String validatePath(String path) {
        if (!path.equals("/") && path.endsWith("/")) {
            throw new IllegalArgumentException("Zookeeper paths may not end with \"/\"");
        }
        return path;
    }
}
