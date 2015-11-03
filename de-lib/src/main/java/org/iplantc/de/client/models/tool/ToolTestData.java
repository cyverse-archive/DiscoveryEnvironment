package org.iplantc.de.client.models.tool;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * Created by aramsey on 10/30/15.
 */
public interface ToolTestData {

    @PropertyName("params")
    String[] getParams ();

    @PropertyName("params")
    void setParams (String[] params);

    @PropertyName("input_files")
    String[] getInputFiles();

    @PropertyName("input_files")
    void setInputFiles(String[] inputFiles);

    @PropertyName("output_files")
    String[] getOutputFiles();

    @PropertyName("output_files")
    void setOutputFiles(String[] outputFiles);

}
