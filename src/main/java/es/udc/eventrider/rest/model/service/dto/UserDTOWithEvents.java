package es.udc.eventrider.rest.model.service.dto;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDTOWithEvents {
  private Long id;
  private String name;
  private String surname;
  private String email;
  private String image;
  private boolean active = true;
  private List<EventDTO> hostedEvents = new ArrayList<>();
  private List<EventDTO> upcomingHostedEvents = new ArrayList<>();
  private List<EventDTO> pastHostedEvents = new ArrayList<>();

  public UserDTOWithEvents(){
  }

  public UserDTOWithEvents(User user){
    this.id = user.getId();
    this.name = user.getName();
    this.surname = user.getSurname();
    this.email = user.getEmail();
    this.image = user.getImagePath();
    this.setActive(user.isActive());
    user.getEvents().forEach(event -> {
      this.hostedEvents.add(new EventDTO(event));
    });
    this.upcomingHostedEvents = this.hostedEvents.stream().filter(
        eventDTO -> eventDTO.getEventStatus() == Event.EventStatus.PUBLISHED &&
        !eventDTO.getEndingDate().isBefore(LocalDateTime.now()))
      .collect(Collectors.toList());
    this.pastHostedEvents = this.hostedEvents.stream().filter(
        eventDTO -> eventDTO.getEventStatus() == Event.EventStatus.PUBLISHED &&
        eventDTO.getEndingDate().isBefore(LocalDateTime.now()))
      .collect(Collectors.toList());
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
}
