package org.iplantc.de.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event fired after a wizard has been validated.
 * 
 * @author amuir
 * 
 */
public class WizardValidationEvent extends GwtEvent<WizardValidationEventHandler> {
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.events.WizardValidationEventHandler
     */
    public static final GwtEvent.Type<WizardValidationEventHandler> TYPE = new GwtEvent.Type<WizardValidationEventHandler>();

    private boolean isValid;

    /**
     * Instantiate from validation flag.
     * 
     * @param isValid flag for the success/failure of wizard form validation.
     */
    public WizardValidationEvent(boolean isValid) {
        this.isValid = isValid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatch(WizardValidationEventHandler handler) {
        if (isValid) {
            handler.onValid(this);
        } else {
            handler.onInvalid(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<WizardValidationEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Retrieve the flag to determine whether a wizard passed or failed validation.
     * 
     * @return valid flag.
     */
    public boolean isValid() {
        return isValid;
    }
}
