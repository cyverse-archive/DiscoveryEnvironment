package org.iplantc.clavin.zookeeper;

import com.netflix.curator.RetryPolicy;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.RetryNTimes;
import java.io.IOException;

/**
 * The primary implementation of the {@link ZkClient} interface.
 *
 * @author Dennis Roberts
 */
public class ZkClientImpl extends AbstractZkClient {

    /**
     * The default number of times to retry failed operations.
     */
    public static final int DEFAULT_RETRY_COUNT = 4;

    /**
     * The default number of milliseconds to sleep between operation attempts.
     */
    public static final int DEFAULT_SLEEP_BETWEEN_TRIES = 100;

    /**
     * The default character encoding.
     */
    public static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * The connection specification string.
     */
    private String connectionSpec;

    /**
     * The number of times to retry failed operations.
     */
    private int retryCount;

    /**
     * The number of milliseconds to sleep between operation attempts.
     */
    private int sleepBetweenTries;

    /**
     * The character encoding to use.
     */
    private String encoding;

    /**
     * @param connectionSpec the connection specification string.
     */
    public ZkClientImpl(String connectionSpec) {
        this(connectionSpec, DEFAULT_RETRY_COUNT, DEFAULT_SLEEP_BETWEEN_TRIES);
    }

    /**
     * @param connectionSpec the connection specification string.
     * @param retryCount the number of times to retry failed operations.
     * @param sleepBetweenTries the number of milliseconds to sleep between operation attempts.
     */
    public ZkClientImpl(String connectionSpec, int retryCount, int sleepBetweenTries) {
        this(connectionSpec, retryCount, sleepBetweenTries, DEFAULT_ENCODING);
    }

    /**
     * @param connectionSpec the connection specification string.
     * @param retryCount the number of times to retry failed operations.
     * @param sleepBetweenTries the number of milliseconds to sleep between operation attempts.
     * @param encoding the character encoding to use.
     */
    public ZkClientImpl(String connectionSpec, int retryCount, int sleepBetweenTries, String encoding) {
        this.connectionSpec = connectionSpec;
        this.retryCount = retryCount;
        this.sleepBetweenTries = sleepBetweenTries;
        this.encoding = encoding;
    }

    /**
     * Connects to Zookeeper.
     *
     * @return the Zookeeper client connection.
     * @throws ZookeeperConnectionException if we can't connect to Zookeeper.
     */
    private CuratorFramework connect() throws ZookeeperConnectionException {
        try {
            RetryPolicy retryPolicy = new RetryNTimes(retryCount, sleepBetweenTries);
            CuratorFramework client = CuratorFrameworkFactory.newClient(connectionSpec, retryPolicy);
            client.start();
            return client;
        }
        catch (IOException e) {
            throw new ZookeeperConnectionException(connectionSpec, e);
        }
    }

    /**
     * Gets the list of children for the node corresponding to a path.
     *
     * @param path the path to the node.
     * @return the list of children.
     */
    public Iterable<String> getChildren(String path) {
        validatePath(path);
        CuratorFramework client = connect();
        try {
            return client.getChildren().forPath(path);
        }
        catch (Exception e) {
            throw new ChildListException(path, e);
        }
        finally {
            client.close();
        }
    }

    /**
     * Gets the data stored at a node corresponding to a path.
     *
     * @param path the path to the node.
     * @return the value of the node as a string.
     */
    public String readNode(String path) {
        validatePath(path);
        CuratorFramework client = connect();
        try {
            return new String(client.getData().forPath(path), encoding);
        }
        catch (Exception e) {
            throw new ReadNodeException(path, e);
        }
        finally {
            client.close();
        }
    }
}
