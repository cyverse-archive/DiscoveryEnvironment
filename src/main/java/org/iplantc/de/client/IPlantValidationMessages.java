package org.iplantc.de.client;

import com.google.gwt.i18n.client.Messages;

/**
 * Localized strings for validation messages.
 * 
 * @author lenards
 * 
 */
public interface IPlantValidationMessages extends Messages {

    /**
     * Message provided when a user enters a field that is not valid 3' adapter clipper. data.
     * 
     * @see org.iplantc.de.client.validator.rules.ClipperDataRule
     * @param field the name of the field that the value is associated with.
     * @return a parameterized string representing the message shown when the field is not valid.
     */
    public String nonValidClipperDataMsg(String field);

    /**
     * Message provided when a user enters an invalid App Documentation Wiki URL.
     * 
     * @param field the name of the field that the value is associated with.
     * @return a parameterized string representing the message shown when the field is not valid.
     */
    public String notValidAppWikiUrl(String field);
}
