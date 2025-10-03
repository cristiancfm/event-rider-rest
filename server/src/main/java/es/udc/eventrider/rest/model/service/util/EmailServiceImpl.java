package es.udc.eventrider.rest.model.service.util;

import es.udc.eventrider.rest.config.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
  @Autowired
  private JavaMailSender emailSender;

  public void sendSimpleMessage(String to, String subject, String text) {
    try{
      MimeMessage message = emailSender.createMimeMessage();

      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setFrom("noreply@eventrider.com");
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(text, true);

      emailSender.send(message);

    } catch (MessagingException e) {
      System.err.println(e.getMessage());
    }

  }
}
