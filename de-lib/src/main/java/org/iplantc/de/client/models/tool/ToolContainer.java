package org.iplantc.de.client.models.tool;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * Created by aramsey on 10/30/15.
 */


public interface ToolContainer extends HasName {

    @PropertyName("ToolContainer")
    ToolContainer getToolContainer();

    @PropertyName("ToolContainer")
    void setToolContainer(ToolContainer container);

    @PropertyName("working_directory")
    void setWorkingDirectory(String directory);

    @PropertyName("entrypoint")
    void setEntryPoint(String entryPoint);

    @PropertyName("memory_limit")
    void setMemoryLimit(int memoryLimit);

    @PropertyName("cpu_shares")
    void setCPUShares(int cpuShares);

    @PropertyName("network_mode")
    void setNetworkMode(String networkMode);

    List<ToolDevice> getDevices();

    List<ToolImage> getImages();

    List<ToolVolumesFrom> getVolumesFrom();

}
