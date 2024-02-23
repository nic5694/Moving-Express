package com.example.backend.util;

import lombok.Generated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Generated
@Component
public class EmailUtil {
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.mail.password}")
    private String password;

    public static void SendEmail(Session session, String toEmail, String subject, String body) {
        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress(System.getProperty("SPRING_MAIL_USERNAME", "NoReply_JD"), "Moving Express"));
            msg.setReplyTo(InternetAddress.parse(System.getProperty("no_reply@example.com", "NoReply_JD"), false));
            msg.setSubject(subject, "UTF-8");
            msg.setSentDate(new Date());
            msg.setContent(body, "text/html; charset=utf-8");
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SslEmail(String toEmail, String subject, String body) {
        System.out.println("Sending email to " + toEmail);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        Properties props = new Properties();
        System.out.println("fromEmail: " + fromEmail);
        System.out.println("password: " + password);
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
        props.put("mail.smtp.port", "465"); //SMTP Port
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);
        SendEmail(session, toEmail, subject, body);
    }

}
