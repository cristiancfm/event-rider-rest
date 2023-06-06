package es.udc.eventrider.rest.model.service.dto;

import es.udc.eventrider.rest.model.domain.Event;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDTO {
  private Long id;
  @NotEmpty
  private String title;
  @NotNull
  private UserDTOBase host;
  private List<UserDTOBase> subscribers = new ArrayList<>();
  private List<UserDTOBase> saves = new ArrayList<>();
  private ZonedDateTime startingDate;
  private ZonedDateTime endingDate;
  private double coordinateX;
  private double coordinateY;
  private String locationDetails;
  private String description;
  private Integer numImages;
  private String adminComments;
  private String cancellationReason;
  private Event.EventStatus status;
  private EventCategoryDTO category;

  public EventDTO() {
  }

  public EventDTO(Event event) {
    this.id = event.getId();
    this.title = event.getTitle();
    this.host = new UserDTOBase(event.getHost());
    event.getSubscribers().forEach(s -> {
      this.subscribers.add(new UserDTOBase(s));
    });
    event.getSaves().forEach(s -> {
      this.saves.add(new UserDTOBase(s));
    });
    this.startingDate = event.getStartingDate();
    this.endingDate = event.getEndingDate();
    this.coordinateX = event.getPoint().getX();
    this.coordinateY = event.getPoint().getY();
    this.locationDetails = event.getLocationDetails();
    this.description = event.getDescription();
    this.numImages = event.getImagePaths().size();
    this.adminComments = event.getAdminComments();
    this.cancellationReason = event.getCancellationReason();
    this.status = event.getStatus();
    this.category = new EventCategoryDTO(event.getCategory());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public UserDTOBase getHost() {
    return host;
  }

  public void setHost(UserDTOBase host) {
    this.host = host;
  }

  public List<UserDTOBase> getSubscribers() {
    return subscribers;
  }

  public void setSubscribers(List<UserDTOBase> subscribers) {
    this.subscribers = subscribers;
  }

  public List<UserDTOBase> getSaves() {
    return saves;
  }

  public void setSaves(List<UserDTOBase> saves) {
    this.saves = saves;
  }

  public ZonedDateTime getStartingDate() {
    return startingDate;
  }

  public void setStartingDate(ZonedDateTime startingDate) {
    this.startingDate = startingDate;
  }

  public ZonedDateTime getEndingDate() {
    return endingDate;
  }

  public void setEndingDate(ZonedDateTime endingDate) {
    this.endingDate = endingDate;
  }

  public double getCoordinateX() {
    return coordinateX;
  }

  public void setCoordinateX(double coordinateX) {
    this.coordinateX = coordinateX;
  }

  public double getCoordinateY() {
    return coordinateY;
  }

  public void setCoordinateY(double coordinateY) {
    this.coordinateY = coordinateY;
  }

  public String getLocationDetails() {
    return locationDetails;
  }

  public void setLocationDetails(String locationDetails) {
    this.locationDetails = locationDetails;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getNumImages() {
    return numImages;
  }

  public void setNumImages(Integer numImages) {
    this.numImages = numImages;
  }

  public String getAdminComments() {
    return adminComments;
  }

  public void setAdminComments(String adminComments) {
    this.adminComments = adminComments;
  }

  public String getCancellationReason() {
    return cancellationReason;
  }

  public void setCancellationReason(String cancellationReason) {
    this.cancellationReason = cancellationReason;
  }

  public Event.EventStatus getStatus() {
    return status;
  }

  public void setStatus(Event.EventStatus status) {
    this.status = status;
  }

  public EventCategoryDTO getCategory() {
    return category;
  }

  public void setCategory(EventCategoryDTO category) {
    this.category = category;
  }
}
