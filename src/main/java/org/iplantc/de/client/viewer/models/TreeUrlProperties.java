/**
 * 
 */
package org.iplantc.de.client.viewer.models;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author sriram
 * 
 */
public interface TreeUrlProperties extends PropertyAccess<TreeUrl> {

    ValueProvider<TreeUrl, String> label();

    ValueProvider<TreeUrl, String> url();

}
