package es.udc.eventrider.rest.model.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class EventCategory {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_cat_generator")
  @SequenceGenerator(name = "event_cat_generator", sequenceName = "event_cat_seq")
  private Long id;

  private String name;

  @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
  private List<Event> events = new ArrayList<>();

  public enum EventCategoryStatus {
    UNREVIEWED,
    PUBLISHED,
    REJECTED
  }
  @Enumerated(EnumType.STRING)
  private EventCategoryStatus status;

  public EventCategory(){
  }

  public EventCategory(String name, EventCategoryStatus status){
    this.name = name;
    this.status = status;
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

  public EventCategoryStatus getStatus() {
    return status;
  }

  public void setStatus(EventCategoryStatus status) {
    this.status = status;
  }

  public List<Event> getEvents() {
    return events;
  }

  public void setEvents(List<Event> events) {
    this.events = events;
  }
}
