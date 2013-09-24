package org.iplantc.admin.belphegor.client.systemMessage.presenter;

import java.util.Collections;
import java.util.List;

import org.iplantc.admin.belphegor.client.systemMessage.SystemMessageView;
import org.iplantc.admin.belphegor.client.systemMessage.model.SystemMessage;
import org.iplantc.admin.belphegor.client.systemMessage.service.SystemMessageServiceFacade;
import org.iplantc.core.resources.client.messages.IplantDisplayStrings;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.info.IplantAnnouncer;
import org.iplantc.core.uicommons.client.info.SuccessAnnouncementConfig;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

public class SystemMessagePresenterImpl implements SystemMessageView.Presenter {

    private final SystemMessageServiceFacade sysMsgService;
    private final SystemMessageView view;
    private final IplantDisplayStrings strings;
    private List<String> systemMessageTypes = Lists.newArrayList();

    @Inject
    public SystemMessagePresenterImpl(SystemMessageView view, SystemMessageServiceFacade sysMsgService, IplantDisplayStrings strings) {
        this.view = view;
        this.sysMsgService = sysMsgService;
        this.strings = strings;
        view.setPresenter(this);

        // Fetch all system messages
        sysMsgService.getSystemMessageTypes(new AsyncCallback<List<String>>() {

            @Override
            public void onSuccess(List<String> result) {
                if ((result != null) && !result.isEmpty()) {
                    systemMessageTypes.addAll(result);
                } else {
                    systemMessageTypes = Collections.emptyList();
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });

    }


    @Override
    public void go(HasOneWidget container) {
        view.mask(strings.loadingMask());
        container.setWidget(view);
        sysMsgService.getSystemMessages(new AsyncCallback<List<SystemMessage>>() {
    
            @Override
            public void onSuccess(List<SystemMessage> result) {
                view.unmask();
                view.setSystemMessages(result);
            }
    
            @Override
            public void onFailure(Throwable caught) {
                view.unmask();
                ErrorHandler.post(caught);
            }
        });
    }


    @Override
    public void addSystemMessage(SystemMessage msg) {
        sysMsgService.addSystemMessage(msg, new AsyncCallback<SystemMessage>() {

            @Override
            public void onSuccess(SystemMessage result) {
                IplantAnnouncer.getInstance().schedule(new SuccessAnnouncementConfig("System message successfully added."));
                view.addSystemMessage(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });

    }

    @Override
    public void editSystemMessage(SystemMessage msg) {
        sysMsgService.updateSystemMessage(msg, new AsyncCallback<SystemMessage>() {

            @Override
            public void onSuccess(SystemMessage result) {
                IplantAnnouncer.getInstance().schedule(new SuccessAnnouncementConfig("System message successfully updated."));
                view.updateSystemMessage(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });

    }

    @Override
    public void deleteSystemMessage(final SystemMessage msg) {
        sysMsgService.deleteSystemMessage(msg, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                IplantAnnouncer.getInstance().schedule(new SuccessAnnouncementConfig("Message successfully deleted"));
                view.deleteSystemMessage(msg);
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });

    }

    @Override
    public List<String> getAnnouncementTypes() {
        return systemMessageTypes;
    }

}
