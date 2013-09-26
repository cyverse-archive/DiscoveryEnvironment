package org.iplantc.admin.belphegor.client.refGenome;

import java.util.List;

import org.iplantc.admin.belphegor.client.refGenome.model.ReferenceGenome;
import org.iplantc.core.uicommons.client.views.IsMaskable;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface RefGenomeView extends IsWidget, IsMaskable {

    public interface Presenter {

        void go(HasOneWidget container);

        void addReferenceGenome(ReferenceGenome referenceGenome);

        void editReferenceGenome(ReferenceGenome referenceGenome);

    }

    void setReferenceGenomes(List<ReferenceGenome> refGenomes);

    void setPresenter(RefGenomeView.Presenter presenter);

    void editReferenceGenome(ReferenceGenome refGenome);

}
