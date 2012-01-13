package org.iplantc.de.server;

import org.iplantc.authn.service.UserSessionService;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageAwareTransformer;

/**
 * A transformation for storage of a security context.
 */
public class StoreSecurityContext extends AbstractMessageAwareTransformer {

    private UserSessionService userSessionService;
    private MuleUserSessionService muleUserSessionService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Object transform(MuleMessage message, String outputEncoding) throws TransformerException {
        muleUserSessionService.storeUser(userSessionService.getUser());
        return message.getPayload();
    }

    /**
     * Gets the user session service.
     * 
     * @return a reference to the user session service.
     */
    public UserSessionService getUserSessionService() {
        return userSessionService;
    }

    /**
     * Sets the user session service.
     * 
     * @param userSessionService a reference to the user session service.
     */
    public void setUserSessionService(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    /**
     * Gets the Mule user session service.
     * 
     * @return a reference to the Mule user session service.
     */
    public MuleUserSessionService getMuleUserSessionService() {
        return muleUserSessionService;
    }

    /**
     * Sets the Mule user session server
     * 
     * @param muleUserSessionService a reference to the Mule user session service.
     */
    public void setMuleUserSessionService(MuleUserSessionService muleUserSessionService) {
        this.muleUserSessionService = muleUserSessionService;
    }

}
