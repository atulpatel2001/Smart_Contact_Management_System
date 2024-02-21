package com.smart.contact.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;
@Service
public class EmailService {
    private Logger logger= LoggerFactory.getLogger(EmailService.class);
    private final String password="utoxoonhywgaenue";
    private final String userName="smartcontactmanager2023@gmail.com";

    private final String from="smartcontactmanager2023@gmail.com";

   public Properties getProperties(){
       Properties properties = System.getProperties();
       properties.put("mail.smtp.host","smtp.gmail.com");
       properties.put("mail.smtp.port","465");
       properties.put("mail.smtp.ssl.enable","true");
       properties.put("mail.smtp.auth","true");
       return properties;
   }

    public boolean sendEmail(String message, String subject, String to) {
        boolean f=false;

        Session session=Session.getInstance(getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName,password);
            }
        });

        session.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipients(Message.RecipientType.TO,String.valueOf(new InternetAddress(to)));
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(message,"text/html");
//            mimeMessage.setText(message);
            Transport.send(mimeMessage);
            logger.info("successFull send Email");
            f=true;
        }
        catch (Exception e){
            e.printStackTrace();
            logger.info("Something Went Wrong");
        }
        return  f;
    }

    public boolean sendAttachEmail(String message, String subject, String to, String from,String fileName) {
          boolean f=false;
        //step 1 get the session object
        Session session = Session.getInstance(getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
        session.setDebug(true);
        //step 2 compose the message[text,multi media]

        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            //from
            mimeMessage.setFrom(new InternetAddress(from));
            //adding recepient
            mimeMessage.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(to)));
            //adding subject
            mimeMessage.setSubject(subject);
            //send file

            //file path

            // String path="C:\\Users\\ajpat\\Downloads\\download.jpeg";



            MimeMultipart mimeMultipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            MimeBodyPart filePart = new MimeBodyPart();

            textPart.setText(message);

            
            filePart.attachFile(fileName);

            mimeMultipart.addBodyPart(textPart);
            mimeMultipart.addBodyPart(filePart);

            mimeMessage.setContent(mimeMultipart);
            //send
            //step 3 :send message

            Transport.send(mimeMessage);
            logger.info("successFull send Email");
          f=true;

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Something Went Wrong");
        }
        return f;
    }




}
