package es.udc.eventrider.rest.model.service.dto;

import es.udc.eventrider.rest.model.domain.User;

public class UserDTOPublic {
  private Long id;
  private String email;
  private boolean active = true;

  public UserDTOPublic() {
  }

  public UserDTOPublic(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.setActive(user.isActive());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
