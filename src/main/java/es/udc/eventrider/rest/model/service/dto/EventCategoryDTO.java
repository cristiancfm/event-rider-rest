package es.udc.eventrider.rest.model.service.dto;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.domain.EventCategory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventCategoryDTO {
  private Long id;
  private String name;
  private List<UserDTOBase> subscribers = new ArrayList<>();
  private EventCategory.EventCategoryStatus status;
  private Integer upcomingEvents;

  public EventCategoryDTO(){
  }

  public EventCategoryDTO(EventCategory eventCategory) {
    this.id = eventCategory.getId();
    this.name = eventCategory.getName();
    eventCategory.getSubscribers().forEach(s -> {
      this.subscribers.add(new UserDTOBase(s));
    });
    this.status = eventCategory.getStatus();
    this.upcomingEvents = eventCategory.getEvents()
      .stream().filter(event -> event.getStatus() == Event.EventStatus.PUBLISHED &&
        !event.getStartingDate().isBefore(LocalDateTime.now()))
      .collect(Collectors.toList()).size();
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

  public List<UserDTOBase> getSubscribers() {
    return subscribers;
  }

  public void setSubscribers(List<UserDTOBase> subscribers) {
    this.subscribers = subscribers;
  }

  public EventCategory.EventCategoryStatus getStatus() {
    return status;
  }

  public void setStatus(EventCategory.EventCategoryStatus status) {
    this.status = status;
  }

  public Integer getUpcomingEvents() {
    return upcomingEvents;
  }

  public void setUpcomingEvents(Integer upcomingEvents) {
    this.upcomingEvents = upcomingEvents;
  }
}
