package edu.mssm.pharm.maayanlab.Harmonizome.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Emailer {
	
	private static final Properties props = new Properties();
	static {
		props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream resource = loader.getResourceAsStream("email.properties");
			System.out.println(resource);
			props.load(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void send(String user, String topic, String body) throws UnsupportedEncodingException {
		final String botEmail = props.getProperty("email.username");
		final String botPassword = props.getProperty("email.password");
		String avi = props.getProperty("email.avi");
		String andrew = props.getProperty("email.andrew");
		String greg = props.getProperty("email.greg");
		
        Session session = Session.getInstance(props,
        	new javax.mail.Authenticator() {
            	protected PasswordAuthentication getPasswordAuthentication() {
            		return new PasswordAuthentication(botEmail, botPassword);
            	}
            });
        
        StringBuilder finalMessage = new StringBuilder();
        finalMessage.append("From: " + user);
        finalMessage.append(System.lineSeparator());
        finalMessage.append("Topic: " + topic);
        finalMessage.append(System.lineSeparator());
        finalMessage.append(System.lineSeparator());
        finalMessage.append(body);
        
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(botEmail, "Harmonizome Bot"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(avi));
            msg.addRecipient(Message.RecipientType.CC, new InternetAddress(andrew));
            msg.addRecipient(Message.RecipientType.CC, new InternetAddress(greg));
            msg.setSubject("Feedback from user " + user);
            msg.setText(finalMessage.toString());
            Transport.send(msg);
        } catch (AddressException e) {
        	e.printStackTrace();
        } catch (MessagingException e) {
        	e.printStackTrace();
        }
	}
}
