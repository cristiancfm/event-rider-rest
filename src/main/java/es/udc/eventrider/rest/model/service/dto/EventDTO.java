package es.udc.eventrider.rest.model.service.dto;

import es.udc.eventrider.rest.model.domain.Event;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class EventDTO {
  private Long id;
  @NotEmpty
  private String title;
  @NotNull
  private UserDTOPublic host;
  private LocalDateTime startingDate;
  private LocalDateTime endingDate;
  private String coordinates;
  private String locationDetails;
  private String description;
  private Boolean hasImages = false;
  private String adminComments;
  private String cancellationReason;
  private Event.EventStatus eventStatus;

  public EventDTO() {
  }

  public EventDTO(Event event) {
    this.id = event.getId();
    this.title = event.getTitle();
    this.host = new UserDTOPublic(event.getHost());
    this.startingDate = event.getStartingDate();
    this.endingDate = event.getEndingDate();
    this.coordinates = event.getCoordinates();
    this.locationDetails = event.getLocationDetails();
    this.description = event.getDescription();
    if(event.getImagePaths() != null){
      this.hasImages = true;
    }
    this.adminComments = event.getAdminComments();
    this.cancellationReason = event.getCancellationReason();
    this.eventStatus = event.getEventStatus();
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

  public UserDTOPublic getHost() {
    return host;
  }

  public void setHost(UserDTOPublic host) {
    this.host = host;
  }

  public LocalDateTime getStartingDate() {
    return startingDate;
  }

  public void setStartingDate(LocalDateTime startingDate) {
    this.startingDate = startingDate;
  }

  public LocalDateTime getEndingDate() {
    return endingDate;
  }

  public void setEndingDate(LocalDateTime endingDate) {
    this.endingDate = endingDate;
  }

  public String getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(String coordinates) {
    this.coordinates = coordinates;
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

  public Boolean getHasImages() {
    return hasImages;
  }

  public void setHasImages(Boolean hasImages) {
    this.hasImages = hasImages;
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

  public Event.EventStatus getEventStatus() {
    return eventStatus;
  }

  public void setEventStatus(Event.EventStatus eventStatus) {
    this.eventStatus = eventStatus;
  }
}
