package es.udc.eventrider.rest.model.service.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import es.udc.eventrider.rest.model.domain.User;

public class UserDTOPrivate {
  private Long id;

  @NotEmpty
  private String name;

  private String surname;

  @NotEmpty
  @Size(min = 4)
  private String email;

  @NotEmpty
  @Size(min = 4)
  private String password;

  private String authority;

  public UserDTOPrivate() {
  }

  public UserDTOPrivate(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    // la contraseña no se rellena, nunca se envía al cliente
    this.authority = user.getAuthority().name();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

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

  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }
}