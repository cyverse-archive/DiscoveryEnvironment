package org.iplantc.de.server;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.iplantc.workflow.WorkflowException;
import org.iplantc.workflow.core.TransformationActivity;
import org.iplantc.workflow.experiment.AnalysisRetriever;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;
import org.mule.transformer.AbstractTransformer;

/**
 * Used as an intermediary between the notification agent and the DE.
 * 
 * @author Dennis Roberts
 */
public class NotificationTransformer extends AbstractTransformer {

    private static final Logger LOG = Logger.getLogger(NotificationTransformer.class);

    /**
     * Used to obtain information about the analysis that is not stored in the notification.
     */
    private AnalysisRetriever analysisRetriever;

    /**
     * Sets the analysis retriever.
     * 
     * @param analysisRetriever the new analysis retriever.
     */
    public void setAnalysisRetriever(AnalysisRetriever analysisRetriever) {
        this.analysisRetriever = analysisRetriever;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object doTransform(Object src, String encoding) throws TransformerException {
        LOG.debug("transforming some notifications"); //$NON-NLS-1$
        JSONArray sourceMessages = extractMessages(sourceToString(src, encoding));
        JSONArray destMessages = new JSONArray();
        for (int i = 0; i < sourceMessages.length(); i++) {
            JSONObject message = extractMessageAt(sourceMessages, i);
            updateMessagePayload(message);
            destMessages.put(message);
        }
        String result = buildResultJson(destMessages).toString();
        return result;
    }

    /**
     * Updates the message payload with any additional information we want to include.
     * 
     * @param message the message whose payload we're supposed to update.
     * @throws TransformerException if the message can't be updated.
     */
    private void updateMessagePayload(JSONObject message) throws TransformerException {
        LOG.debug("updating the message payload"); //$NON-NLS-1$
        JSONObject payload = message.optJSONObject("payload"); //$NON-NLS-1$
        if (payload != null) {
            addAdditionalAnalysisInfo(payload);
        }
    }

    /**
     * Adds any additional analysis information from the analysis to the given message payload.\
     * 
     * @param payload the message payload to update.
     * @throws TransformerException if we try to create an invalid JSON field.
     */
    private void addAdditionalAnalysisInfo(JSONObject payload) throws TransformerException {
        LOG.debug("adding additional analysis information to the message"); //$NON-NLS-1$
        try {
            String analysisId = payload.optString("analysis_id"); //$NON-NLS-1$
            if (analysisId != null) {
                TransformationActivity analysis = analysisRetriever.getTransformationActivity(analysisId);
                payload.put("analysis_details", (analysis != null) ? analysis.getDescription() : ""); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        catch (JSONException e) {
            String msg = "unable to add the analysis information to the job status message"; //$NON-NLS-1$
            throw new TransformerException(createMessage(msg), e);
        }
        catch (WorkflowException e) {
            LOG.error("unable to add the analysis information to the job status message", e); //$NON-NLS-1$
        }
    }

    /**
     * Builds the JSON object that is the result of this transformer.
     * 
     * @param destMessages the array of messages to include in the JSON object.
     * @return the JSON object.
     * @throws TransformerException if the JSON object can't be created.
     */
    private JSONObject buildResultJson(JSONArray destMessages) throws TransformerException {
        try {
            JSONObject json = new JSONObject();
            json.put("messages", destMessages); //$NON-NLS-1$
            return json;
        }
        catch (JSONException e) {
            String msg = "unable to build result object"; //$NON-NLS-1$
            throw new TransformerException(createMessage(msg), e);
        }
    }

    /**
     * Extracts a message from the given JSON array of source messages.
     * 
     * @param sourceMessages the array of source messages.
     * @param i the index of the message to extract.
     * @return the extracted message.
     * @throws TransformerException if the message can't be extracted.
     */
    private JSONObject extractMessageAt(JSONArray sourceMessages, int i) throws TransformerException {
        try {
            return sourceMessages.getJSONObject(i);
        }
        catch (JSONException e) {
            String msg = "invalid notificaiton messages at position " + i; //$NON-NLS-1$
            throw new TransformerException(createMessage(msg), e);
        }
    }

    /**
     * Extracts the array of messages from the source JSON string.
     * 
     * @param src the source JSON string.
     * @return the array of messages.
     * @throws TransformerException if the source string is invalid.
     */
    private JSONArray extractMessages(String src) throws TransformerException {
        try {
            return new JSONObject((String) src).getJSONArray("messages"); //$NON-NLS-1$
        }
        catch (JSONException e) {
            String msg = "invalid JSON string"; //$NON-NLS-1$
            throw new TransformerException(createMessage(msg), e);
        }
    }

    /**
     * Converts the source to a string.
     * 
     * @param src the source.
     * @param encoding the source encoding.
     * @return the message payload as a string.
     * @throws TransformerException if the payload can't be retrieved
     */
    private String sourceToString(Object src, String encoding) throws TransformerException {
        try {
            if (src instanceof InputStream) {
                return IOUtils.toString((InputStream) src, encoding);
            }
            else {
                return src.toString();
            }
        }
        catch (IOException e) {
            String msg = "unable to get the message body"; //$NON-NLS-1$
            throw new TransformerException(createMessage(msg), e);
        }
    }

    /**
     * Creates a message to be used as the detail message in a TransformerException.
     * 
     * @param msg the message as a string.
     * @return the message as an instance of org.mule.config.i18n.Message.
     */
    private Message createMessage(String msg) {
        return MessageFactory.createStaticMessage(msg);
    }
}
