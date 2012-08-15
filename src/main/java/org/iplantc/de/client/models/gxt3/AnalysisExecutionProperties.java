package org.iplantc.de.client.models.gxt3;

import java.util.Date;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface AnalysisExecutionProperties extends PropertyAccess<AnalysisExecution> {
    ModelKeyProvider<AnalysisExecution> id();

    ValueProvider<AnalysisExecution, String> getName();

    ValueProvider<AnalysisExecution, String> getDescription();

    ValueProvider<AnalysisExecution, String> getAnalysisId();

    ValueProvider<AnalysisExecution, String> getAnalysisName();

    ValueProvider<AnalysisExecution, String> getAnalysisDetails();

    ValueProvider<AnalysisExecution, Date> getStartDate();

    ValueProvider<AnalysisExecution, Date> getEndDate();

    ValueProvider<AnalysisExecution, String> getStatus();

    ValueProvider<AnalysisExecution, String> getResultFolderId();

    ValueProvider<AnalysisExecution, String> getWikiUrl();

}
