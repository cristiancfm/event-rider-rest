package es.udc.eventrider.rest.model.service.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import es.udc.eventrider.rest.model.domain.User;
import es.udc.eventrider.rest.model.domain.UserAuthority;

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

  private String biography;
  private UserAuthority authority;

  private String image;

  public UserDTOPrivate() {
  }

  public UserDTOPrivate(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.surname = user.getSurname();
    this.email = user.getEmail();
    // la contraseña no se rellena, nunca se envía al cliente
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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
