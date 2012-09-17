package org.iplantc.de.client.services.callbacks;

import org.iplantc.de.client.I18N;

import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.json.client.JSONObject;

/**
 * A call back class for sharing
 * 
 * @author sriram
 * 
 */
public class DiskResourceShareCallback extends DiskResourceServiceCallback {

    @Override
    public void onSuccess(String result) {
        System.out.println("result-->" + result);
        Info.display(I18N.DISPLAY.share(), "Sharing of file(s) successful.");
    }

    @Override
    protected String getErrorMessageDefault() {
        return I18N.ERROR.shareFailed();
    }

    @Override
    protected String getErrorMessageByCode(ErrorCode code, JSONObject jsonError) {
        return getErrorMessage(code, parsePathsToNameList(jsonError));
    }

}
