package org.iplantc.de.fileViewers.client.callbacks;

import static org.iplantc.de.resources.client.messages.I18N.ERROR;
import org.iplantc.de.client.events.FileSavedEvent;
import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.diskResource.client.events.DefaultUploadCompleteHandler;
import org.iplantc.de.resources.client.messages.IplantErrorStrings;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

/**
 * @author jstroot
 */
public class FileSaveCallback implements AsyncCallback<String> {

    private final IplantErrorStrings errorStrings;
    private final HasHandlers hasHandlers;
    private final String parentFolder;
    private final String fileName;
    private final IsMaskable maskingContainer;
    private final boolean newFile;
    private DiskResourceAutoBeanFactory drFactory;
    private UserSessionServiceFacade userSessionService;
    private final JsonUtil jsonUtil;

    public FileSaveCallback(final UserSessionServiceFacade userSessionService,
                            final DiskResourceAutoBeanFactory drFactory,
                            final String path,
                            final boolean newFile,
                            final IsMaskable container,
                            final HasHandlers hasHandlers) {
        this.userSessionService = userSessionService;
        this.drFactory = drFactory;
        this.hasHandlers = hasHandlers;
        this.fileName = DiskResourceUtil.getInstance().parseNameFromPath(path);
        this.parentFolder = DiskResourceUtil.getInstance().parseParent(path);
        this.maskingContainer = container;
        this.newFile = newFile;
        errorStrings = ERROR;
        this.jsonUtil = JsonUtil.getInstance();
    }

    @Override
    public void onSuccess(String result) {
        JSONObject obj = JSONParser.parseStrict(result).isObject();
        DefaultUploadCompleteHandler uch = new DefaultUploadCompleteHandler(userSessionService,
                                                                            drFactory,
                                                                            parentFolder);
        String fileJson = jsonUtil.getObject(obj, "file").toString();
        if (newFile) {
            uch.onCompletion(fileName, fileJson);
        }

        DiskResourceAutoBeanFactory factory = GWT.create(DiskResourceAutoBeanFactory.class);
        AutoBean<File> fileAB = AutoBeanCodex.decode(factory, File.class, fileJson);
        hasHandlers.fireEvent(new FileSavedEvent(fileAB.as()));
    }

    @Override
    public void onFailure(Throwable caught) {
        maskingContainer.unmask();
        ErrorHandler.post(errorStrings.fileUploadsFailed(Lists.newArrayList(fileName)),
                          caught);
    }
}
