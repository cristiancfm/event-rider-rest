package es.udc.eventrider.rest.model.service.dto;

import es.udc.eventrider.rest.model.domain.User;

public class UserDTOPublic {
  private Long id;
  private String name;
  private String surname;
  private String email;
  private boolean active = true;

  public UserDTOPublic() {
  }

  public UserDTOPublic(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.surname = user.getSurname();
    this.email = user.getEmail();
    this.setActive(user.isActive());
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

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

}
