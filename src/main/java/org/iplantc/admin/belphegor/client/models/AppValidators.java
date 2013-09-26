package org.iplantc.admin.belphegor.client.models;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.client.Constants;
import org.iplantc.core.uicommons.client.util.RegExp;
import org.iplantc.core.uicommons.client.validators.BasicEmailValidator3;

import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;

/**
 * @author jstroot
 */
public class AppValidators {
    private static String appNameRegex = Format.substitute("[^{0}{1}][^{1}]*", //$NON-NLS-1$
            Constants.CLIENT.appNameRestrictedStartingChars(), RegExp.escapeCharacterClassSet(Constants.CLIENT.appNameRestrictedChars()));

    public static RegExValidator APP_NAME_VALIDATOR = new RegExValidator(appNameRegex, I18N.ERROR.invalidAppNameMsg(Constants.CLIENT.appNameRestrictedStartingChars(),
            Constants.CLIENT.appNameRestrictedChars()));

    public static RegExValidator APP_WIKI_URL_VALIDATOR = new BasicEmailValidator3();

}
