package es.udc.eventrider.rest.model.service.dto;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDTOPublic {
  private Long id;
  private String name;
  private String surname;
  private String email;
  private String biography;
  private String image;
  private boolean active = true;
  private List<EventDTO> hostedEvents = new ArrayList<>();
  private List<EventDTO> upcomingHostedEvents = new ArrayList<>();
  private List<EventDTO> pastHostedEvents = new ArrayList<>();
  private List<EventDTO> subscribedEvents = new ArrayList<>();
  private List<EventDTO> savedEvents = new ArrayList<>();
  private List<UserDTOBase> followers = new ArrayList<>();

  public UserDTOPublic(){
  }

  public UserDTOPublic(User user){
    this.id = user.getId();
    this.name = user.getName();
    this.surname = user.getSurname();
    this.email = user.getEmail();
    this.biography = user.getBiography();
    this.image = user.getImagePath();
    this.setActive(user.isActive());
    user.getHostedEvents().forEach(event -> {
      this.hostedEvents.add(new EventDTO(event));
    });
    this.upcomingHostedEvents = this.hostedEvents.stream().filter(
        eventDTO -> eventDTO.getStatus() == Event.EventStatus.PUBLISHED &&
        !eventDTO.getEndingDate().isBefore(LocalDateTime.now()))
      .collect(Collectors.toList());
    this.pastHostedEvents = this.hostedEvents.stream().filter(
        eventDTO -> eventDTO.getStatus() == Event.EventStatus.PUBLISHED &&
        eventDTO.getEndingDate().isBefore(LocalDateTime.now()))
      .collect(Collectors.toList());
    user.getSubscribedEvents().forEach(event -> {
      this.subscribedEvents.add(new EventDTO(event));
    });
    user.getSavedEvents().forEach(event -> {
      this.savedEvents.add(new EventDTO(event));
    });
    user.getFollowers().forEach(follower -> {
      this.followers.add(new UserDTOBase(follower));
    });
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

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public List<EventDTO> getHostedEvents() {
    return hostedEvents;
  }

  public void setHostedEvents(List<EventDTO> hostedEvents) {
    this.hostedEvents = hostedEvents;
  }

  public List<EventDTO> getUpcomingHostedEvents() {
    return upcomingHostedEvents;
  }

  public void setUpcomingHostedEvents(List<EventDTO> upcomingHostedEvents) {
    this.upcomingHostedEvents = upcomingHostedEvents;
  }

  public List<EventDTO> getPastHostedEvents() {
    return pastHostedEvents;
  }

  public void setPastHostedEvents(List<EventDTO> pastHostedEvents) {
    this.pastHostedEvents = pastHostedEvents;
  }

  public List<EventDTO> getSubscribedEvents() {
    return subscribedEvents;
  }

  public void setSubscribedEvents(List<EventDTO> subscribedEvents) {
    this.subscribedEvents = subscribedEvents;
  }

  public List<EventDTO> getSavedEvents() {
    return savedEvents;
  }

  public void setSavedEvents(List<EventDTO> savedEvents) {
    this.savedEvents = savedEvents;
  }

  public List<UserDTOBase> getFollowers() {
    return followers;
  }

  public void setFollowers(List<UserDTOBase> followers) {
    this.followers = followers;
  }
}
