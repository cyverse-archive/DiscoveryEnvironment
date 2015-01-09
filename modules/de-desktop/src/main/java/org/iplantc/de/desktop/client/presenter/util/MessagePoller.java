package org.iplantc.de.desktop.client.presenter.util;

import java.util.ArrayList;


/**
 * Polls for messages from the backend.
 */
public final class MessagePoller {
	
	private static MessagePoller instance;

    /**
     * Retrieve singleton instance.
     * 
     * @return the singleton instance.
     */
    public static MessagePoller getInstance() {
        if (instance == null) {
            instance = new MessagePoller();
        }

        return instance;
    }

    private final ArrayList<Runnable> tasks = new ArrayList<Runnable>();
    
    /**
     * Ensures only 1 MessagePoller at a time is added to the TaskRunner.
     */
    private boolean polling = false;

    private MessagePoller() {
    }

    /**
     * Adds a task to be run periodically
     * 
     * @param task The task to run
     */
    public void addTask(final Runnable task) {
    	tasks.add(task);
    	if (polling) {
    		TaskRunner.getInstance().addTask(task);
    	}
    }
    
    /**
     * Stops a task from being run periodically.
     * 
     * @param task the task to no longer run
     */
    public void removeTask(final Runnable task) {
        if (polling) {
            TaskRunner.getInstance().removeTask(task);
        }
        tasks.remove(task);
    }

    /**
     * Starts polling.
     */
    public void start() {
        if (!polling) {
        	for (Runnable task: tasks) {
        		TaskRunner.getInstance().addTask(task);
        	}
            polling = true;
        }
    }

    /**
     * Stops polling.
     */
    public void stop() {
        if (polling) {
        	for (Runnable task: tasks) {
        		TaskRunner.getInstance().removeTask(task);
        	}
            polling = false;
        }
    }

}
