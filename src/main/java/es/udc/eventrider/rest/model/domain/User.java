package es.udc.eventrider.rest.model.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "theUser")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
  @SequenceGenerator(name = "user_generator", sequenceName = "user_seq")
  private Long id;

  private String name;

  private String surname;

  @Column(unique = true)
  private String email;

  private String password;
  private String biography;

  @Enumerated(EnumType.STRING)
  private UserAuthority authority;

  private String imagePath;

  @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
  private List<Post> posts = new ArrayList<>();

  @OneToMany(mappedBy = "host", cascade = CascadeType.REMOVE)
  private List<Event> events = new ArrayList<>();

  @ManyToMany(mappedBy = "subscribers", fetch = FetchType.LAZY)
  private List<Event> subscribedEvents = new ArrayList<>();

  @ManyToMany(mappedBy = "subscribers", fetch = FetchType.LAZY)
  private List<EventCategory> subscribedEventCategories = new ArrayList<>();

  //el usuario está activo (no está suspendido)
  private boolean active = true;

  public User() {
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

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public List<Post> getPosts() {
    return posts;
  }

  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }

  public List<Event> getEvents() {
    return events;
  }

  public void setEvents(List<Event> events) {
    this.events = events;
  }

  public List<Event> getSubscribedEvents() {
    return subscribedEvents;
  }

  public void setSubscribedEvents(List<Event> subscribedEvents) {
    this.subscribedEvents = subscribedEvents;
  }

  public List<EventCategory> getSubscribedEventCategories() {
    return subscribedEventCategories;
  }

  public void setSubscribedEventCategories(List<EventCategory> subscribedEventCategories) {
    this.subscribedEventCategories = subscribedEventCategories;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
