package es.udc.eventrider.rest.model.exception;

public class UserEmailExistsException extends ModelException {
  public UserEmailExistsException(String login) {
    super("User email " + login + " already exists");
  }
}
