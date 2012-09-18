package org.iplantc.de.server;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.iplantc.de.server.service.IplantEmailClient;

/**
 * Sends email messages based on configuration settings.
 *
 * mail.smtp.host -> SMTP_HOST_KEY
 * mail.from.address -> FROM_ADDRESS_KEY
 * mail.to.address -> TO_ADDRESS_KEY
 * new.tool.message.template -> NEW_TOOL_MSG_KEY
 * new.tool.message.subject -> NEW_TOOL_SUBJECT_KEY
 *
 * @author lenards
 *
 */
@SuppressWarnings("nls")
public class SimpleMessageSender {
    private static final String FROM_ADDRESS_KEY = "mail.from.address";
    private static final String TO_ADDRESS_KEY = "mail.to.address";
    private static final String NEW_TOOL_MSG_KEY = "new.tool.message.template";
    private static final String NEW_TOOL_SUBJECT_KEY = "new.tool.message.subject";

    private static final Logger LOG = Logger.getLogger(SimpleMessageSender.class);

    private String messageTmpl;
    private final String subject;
    private final String fromAddress;
    private final String toAddress;
    private final IplantEmailClient client;

    public SimpleMessageSender(Properties props, IplantEmailClient client) {
        loadMessageTemplate(props.getProperty(NEW_TOOL_MSG_KEY, "new_tool_email.tmpl"));
        subject = props.getProperty(NEW_TOOL_SUBJECT_KEY);
        fromAddress = props.getProperty(FROM_ADDRESS_KEY);
        toAddress = props.getProperty(TO_ADDRESS_KEY);
        this.client = client;
    }

    public void send(String user, String email, String fileUrl) throws Exception {
        Date now = new Date();
        IplantEmailClient.MessageRequest request = new IplantEmailClient.MessageRequest()
                .setFromAddress(fromAddress)
                .setToAddress(toAddress)
                .setCcAddress(email)
                .setSubject(subject)
                .setContent(MessageFormat.format(messageTmpl, user, fileUrl, now.toString()));
        client.sendMessage(request);
    }

    private void loadMessageTemplate(String templateName) {
        try {
            InputStream in = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(templateName);
            messageTmpl = IOUtils.toString(in);
        } catch (IOException ioe) {
            LOG.error(ioe.toString(), ioe);
        }
    }
}
