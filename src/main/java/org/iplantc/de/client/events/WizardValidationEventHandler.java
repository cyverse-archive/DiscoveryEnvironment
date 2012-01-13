package org.iplantc.de.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Event handler for wizard validation.
 * 
 * @author amuir
 * 
 */
public interface WizardValidationEventHandler extends EventHandler {
    /**
     * Handle when wizard form validation succeeds.
     * 
     * @param event event to be handled.
     */
    void onValid(WizardValidationEvent event);

    /**
     * Handle when wizard form validation fails.
     * 
     * @param event event to be handled.
     */
    void onInvalid(WizardValidationEvent event);
}
