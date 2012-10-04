package org.iplantc.de.client;

import org.iplantc.de.client.analysis.services.AnalysisServiceFacade;
import org.iplantc.de.client.notifications.services.MessageServiceFacade;
import org.iplantc.de.client.services.DiskResourceServiceFacadeImpl;
import org.iplantc.de.client.services.FileEditorServiceFacade;
import org.iplantc.de.client.services.UserSessionServiceFacade;

public class Services {

    public static AnalysisServiceFacade ANALYSIS_SERVICE = new AnalysisServiceFacade();

    public static MessageServiceFacade MESSAGE_SERVICE = new MessageServiceFacade();

    public static UserSessionServiceFacade USER_SESSION_SERVICE = new UserSessionServiceFacade();

    public static DiskResourceServiceFacadeImpl DISK_RESOURCE_SERVICE = new DiskResourceServiceFacadeImpl();

    public static FileEditorServiceFacade FILE_EDITOR_SERVICE = new FileEditorServiceFacade();
}
