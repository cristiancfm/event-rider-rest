package es.udc.eventrider.rest.model.service.util;

public interface EmailService {
  public void sendSimpleMessage(String to, String subject, String text);
}
