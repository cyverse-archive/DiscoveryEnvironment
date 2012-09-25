/**
 * 
 */
package org.iplantc.de.client.analysis.models;

import org.iplantc.de.client.I18N;

/**
 * @author sriram
 * 
 */
/**
 * Indicates the status of an analysis.
 */
public enum AnalysisExecutionStatus {
    /** analysis status unknown */
    UNKNOWN(I18N.CONSTANT.unknown()),
    /** analysis is ready */
    SUBMITTED(I18N.CONSTANT.submitted()),
    /** analysis is running */
    RUNNING(I18N.CONSTANT.running()),
    /** analysis is complete */
    COMPLETED(I18N.CONSTANT.completed()),
    /** analysis timed out */
    HELD(I18N.CONSTANT.held()),
    /** analysis failed */
    FAILED(I18N.CONSTANT.failed()),
    /** analysis was stopped */
    SUBMISSION_ERR(I18N.CONSTANT.subErr()),
    /** analysis is idle */
    IDLE(I18N.CONSTANT.idle()),
    /** analysis is removed */
    REMOVED(I18N.CONSTANT.removed());

    private String displayText;

    private AnalysisExecutionStatus(String displaytext) {
        this.displayText = displaytext;
    }

    /**
     * Returns a string that identifies the EXECUTION_STATUS.
     * 
     * @return
     */
    public String getTypeString() {
        return toString().toLowerCase();
    }

    /**
     * Null-safe and case insensitive variant of valueOf(String)
     * 
     * @param typeString name of an EXECUTION_STATUS constant
     * @return
     */
    public static AnalysisExecutionStatus fromTypeString(String typeString) {
        if (typeString == null || typeString.isEmpty()) {
            return null;
        }

        return valueOf(typeString.toUpperCase());
    }

    @Override
    public String toString() {
        return displayText;
    }
}
