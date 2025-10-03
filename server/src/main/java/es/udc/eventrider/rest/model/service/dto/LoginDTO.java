package es.udc.eventrider.rest.model.service.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class LoginDTO {
  @NotEmpty
  private String email;

  @NotEmpty
  @Size(min = 4)
  private String password;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
