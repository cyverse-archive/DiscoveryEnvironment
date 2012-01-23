package org.iplantc.de.server;

import java.io.FileWriter;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageAwareTransformer;


public class AppendWorkspaceIdToPayloadTransformer extends AbstractMessageAwareTransformer{


	@Override
	public Object transform(MuleMessage message, String outputEncoding)
	throws TransformerException {

		try {

			String ss = message.getPayloadAsString();
			
			
			JSONObject json = (JSONObject) JSONSerializer.toJSON(message.getPayloadAsString());

			String workspaceid = message.getProperty("workspaceId").toString();

			json.put("workspace_id", workspaceid);



			return json;
		}catch(Exception ex) {
			throw new TransformerException(this, ex);
		}


	}



}
