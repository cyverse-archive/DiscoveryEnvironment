package org.iplantc.admin.belphegor.client.refGenome.presenter;

import java.util.List;

import org.iplantc.admin.belphegor.client.refGenome.RefGenomeView;
import org.iplantc.admin.belphegor.client.refGenome.model.ReferenceGenome;
import org.iplantc.admin.belphegor.client.refGenome.service.ReferenceGenomeServiceFacade;
import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.resources.client.messages.IplantDisplayStrings;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.info.IplantAnnouncer;
import org.iplantc.core.uicommons.client.info.SuccessAnnouncementConfig;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

public class RefGenomePresenterImpl implements RefGenomeView.Presenter {

    private final RefGenomeView view;
    private final ReferenceGenomeServiceFacade refGenService;
    private final IplantDisplayStrings strings;

    @Inject
    public RefGenomePresenterImpl(RefGenomeView view, ReferenceGenomeServiceFacade refGenService, IplantDisplayStrings strings) {
        this.view = view;
        this.refGenService = refGenService;
        this.strings = strings;
        this.view.setPresenter(this);
    }

    @Override
    public void go(HasOneWidget container) {
        view.mask(strings.loadingMask());
        container.setWidget(view);
        refGenService.getReferenceGenomes(new AsyncCallback<List<ReferenceGenome>>() {

            @Override
            public void onSuccess(List<ReferenceGenome> result) {
                view.unmask();
                view.setReferenceGenomes(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                view.unmask();
                ErrorHandler.post(caught);
            }
        });
    }

    @Override
    public void addReferenceGenome(final ReferenceGenome referenceGenome) {
        refGenService.createReferenceGenomes(referenceGenome, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void arg0) {
                IplantAnnouncer.getInstance().schedule(new SuccessAnnouncementConfig(I18N.DISPLAY.addRefGenome()));
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

        });

    }

    @Override
    public void editReferenceGenome(ReferenceGenome referenceGenome) {
        refGenService.editReferenceGenomes(null, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void arg0) {
                IplantAnnouncer.getInstance().schedule(new SuccessAnnouncementConfig(I18N.DISPLAY.updateRefGenome()));
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });

    }

}
