package es.udc.eventrider.rest.model.exception;

public class UserLoginExistsException extends ModelException {
  public UserLoginExistsException(String login) {
    super("User login " + login + " already exists");
  }
}
