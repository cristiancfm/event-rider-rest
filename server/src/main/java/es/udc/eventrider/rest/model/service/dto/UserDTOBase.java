package es.udc.eventrider.rest.model.service.dto;

import es.udc.eventrider.rest.model.domain.User;
import es.udc.eventrider.rest.model.domain.UserAuthority;

public class UserDTOBase {
  private Long id;
  private String name;
  private String surname;
  private String email;
  private String biography;
  private UserAuthority authority;
  private String image;

  public UserDTOBase() {
  }

  public UserDTOBase(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.surname = user.getSurname();
    this.email = user.getEmail();
    this.biography = user.getBiography();
    this.authority = user.getAuthority();
    this.image = user.getImagePath();
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

  public String getBiography() {
    return biography;
  }

  public void setBiography(String biography) {
    this.biography = biography;
  }

  public UserAuthority getAuthority() {
    return authority;
  }

  public void setAuthority(UserAuthority authority) {
    this.authority = authority;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
