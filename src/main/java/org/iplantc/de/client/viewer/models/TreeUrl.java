/**
 * 
 */
package org.iplantc.de.client.viewer.models;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * @author sriram
 * 
 */
public interface TreeUrl {

    @PropertyName("label")
    void setLabel(String label);

    @PropertyName("label")
    String getLabel();

    @PropertyName("url")
    String getUrl();

    @PropertyName("url")
    void setUrl(String url);
}
