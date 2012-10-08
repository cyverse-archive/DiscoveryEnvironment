/**
 * 
 */
package org.iplantc.de.client.viewer.models;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * @author sriram
 * 
 */
public interface TreeUrlAutoBeanFactory extends AutoBeanFactory {

    AutoBean<TreeUrl> getTreeUrl();

    AutoBean<TreeUrlList> getTreeUrlList();
}
