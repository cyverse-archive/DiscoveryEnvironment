package org.iplantc.de.client.views.panels;

import org.iplantc.de.client.I18N;
import org.iplantc.de.client.services.RawDataServices;
import org.iplantc.de.client.utils.ProvenanceFormatter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class DataProvenancePanel extends DataTextAreaPanel {
    public DataProvenancePanel() {
    }

    @Override
    protected String getHeadingText() {
        return I18N.CONSTANT.provenance();
    }

    @Override
    protected int getInitialHeight() {
        return 80;
    }

    @Override
    protected void updateDisplay(String idFile) {
        RawDataServices.getFileProvenance(idFile, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable arg0) {
                hide();
            }

            @Override
            public void onSuccess(String result) {
                displayValue(ProvenanceFormatter.format(result));
                show();
            }
        });
    }
}
