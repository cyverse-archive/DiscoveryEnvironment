package org.iplantc.de.server;

// import org.apache.log4j.Logger;
import org.iplantc.authn.exception.UserNotFoundException;
import org.iplantc.authn.service.UserSessionService;
import org.iplantc.authn.user.User;
import org.mule.RequestContext;

/**
 * A mule user session service.
 */
public class MuleUserSessionService implements UserSessionService {

    // private static Logger LOG = Logger.getLogger(MuleUserSessionService.class);

    /**
     * The name of the user property.
     */
    private static final String USER_PROPERTY = "de.user"; //$NON-NLS-1$

    /**
     * Gets the user associated with a mule session.
     * 
     * @return the user associated with a mule session.
     */
    @Override
    public User getUser() {
        User user = User.fromString((String)RequestContext.getEvent().getSession().getProperty(USER_PROPERTY));
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    /**
     * Stores the user with the request context.
     * 
     * @param user the username associated with the context.
     */
    public void storeUser(User user) {
        RequestContext.getEvent().getSession().setProperty(USER_PROPERTY, user.toString());
    }
}
