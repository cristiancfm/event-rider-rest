package es.udc.eventrider.rest.model.service.dto;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.domain.EventCategory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventCategoryDTOEdit {
  private Long id;
  private String name;
  private List<UserDTOBase> subscribers;
  private EventCategory.EventCategoryStatus status;
  private Integer upcomingEvents;

  public EventCategoryDTOEdit(){
  }

  public EventCategoryDTOEdit(Long id, String name, List<UserDTOBase> subscribers, EventCategory.EventCategoryStatus status, Integer upcomingEvents) {
    this.id = id;
    this.name = name;
    this.subscribers = subscribers;
    this.status = status;
    this.upcomingEvents = upcomingEvents;
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
