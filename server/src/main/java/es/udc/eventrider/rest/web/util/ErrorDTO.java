package es.udc.eventrider.rest.web.util;

public class ErrorDTO {
  private String message;

  public ErrorDTO(String message) {
    super();
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
