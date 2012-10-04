package org.iplantc.de.client;

import org.iplantc.core.uidiskresource.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.analysis.services.AnalysisServiceFacade;
import org.iplantc.de.client.notifications.services.MessageServiceFacade;
import org.iplantc.de.client.services.FileEditorServiceFacade;
import org.iplantc.de.client.services.UserSessionServiceFacade;

import com.google.gwt.core.shared.GWT;

public class Services {

    public static AnalysisServiceFacade ANALYSIS_SERVICE = new AnalysisServiceFacade();

    public static MessageServiceFacade MESSAGE_SERVICE = new MessageServiceFacade();

    public static UserSessionServiceFacade USER_SESSION_SERVICE = new UserSessionServiceFacade();

    public static DiskResourceServiceFacade DISK_RESOURCE_SERVICE = GWT
            .create(DiskResourceServiceFacade.class);

    public static FileEditorServiceFacade FILE_EDITOR_SERVICE = new FileEditorServiceFacade();
}
