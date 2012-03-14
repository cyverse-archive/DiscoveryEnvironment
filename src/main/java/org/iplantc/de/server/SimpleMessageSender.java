package org.iplantc.de.server;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * Sends email messages based on configuration settings.
 * 
 * mail.smtp.host -> SMTP_HOST_KEY mail.from.address -> FROM_ADDRESS_KEY mail.to.address ->
 * TO_ADDRESS_KEY new.tool.message.template -> NEW_TOOL_MSG_KEY new.tool.message.subject ->
 * NEW_TOOL_SUBJECT_KEY
 * 
 * @author lenards
 * 
 */
public class SimpleMessageSender {
    private static final String SMTP_HOST_KEY = "mail.smtp.host"; //$NON-NLS-1$
    private static final String FROM_ADDRESS_KEY = "mail.from.address"; //$NON-NLS-1$
    private static final String TO_ADDRESS_KEY = "mail.to.address"; //$NON-NLS-1$
    private static final String NEW_TOOL_MSG_KEY = "new.tool.email.template"; //$NON-NLS-1$
    private static final String NEW_TOOL_SUBJECT_KEY = "new.tool.message.subject"; //$NON-NLS-1$

    private static final Logger LOG = Logger.getLogger(SimpleMessageSender.class);

    private PropertiesConfiguration appProperties;
    private String messageTmpl;
    private String subject;

    private String fromAddress;
    private String toAddress;
    private String smtpHost;

    public SimpleMessageSender() {
        loadProperties("tito.properties"); //$NON-NLS-1$
        loadMessageTemplate(appProperties.getString(NEW_TOOL_MSG_KEY, "new_tool_email.tmpl")); //$NON-NLS-1$
        subject = appProperties.getString(NEW_TOOL_SUBJECT_KEY);
        fromAddress = appProperties.getString(FROM_ADDRESS_KEY);
        toAddress = appProperties.getString(TO_ADDRESS_KEY);
        smtpHost = appProperties.getString(SMTP_HOST_KEY);
    }

    public void send(String user, String email, String fileUrl) throws Exception {
        Date now = new Date();
        // this is basically the same as a code sample on
        // Sun/Oracle's site called jGuru
        Properties props = System.getProperties();

        props.put("mail.smtp.host", smtpHost); //$NON-NLS-1$

        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromAddress));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
        message.addRecipient(Message.RecipientType.CC, new InternetAddress(email));
        message.setSubject(subject);
        message.setText(MessageFormat.format(messageTmpl, user, fileUrl, now.toString()));
        Transport.send(message);

    }

    private void loadMessageTemplate(String templateName) {
        try {
            InputStream in = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(templateName);
            messageTmpl = IOUtils.toString(in);
        } catch (IOException ioe) {
            LOG.error(ioe.toString(), ioe);
            ioe.printStackTrace();
        }
    }

    private void loadProperties(String propertyFile) {
        appProperties = new PropertiesConfiguration();
        try {
            InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(propertyFile);
            appProperties.load(is);
        } catch (ConfigurationException e) {
            LOG.error(e.toString(), e);
            e.printStackTrace();
        }
    }
}
