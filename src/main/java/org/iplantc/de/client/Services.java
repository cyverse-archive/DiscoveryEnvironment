package org.iplantc.de.client;

import org.iplantc.de.client.services.AnalysisServiceFacade;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.client.services.UserSessionServiceFacade;

public class Services {

    public static AnalysisServiceFacade ANALYSIS_SERVICE = new AnalysisServiceFacade();

    public static MessageServiceFacade MESSAGE_SERVICE = new MessageServiceFacade();

    public static UserSessionServiceFacade USER_SESSION_SERVICE = new UserSessionServiceFacade();
}
