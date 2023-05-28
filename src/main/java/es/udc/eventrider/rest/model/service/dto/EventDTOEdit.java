package es.udc.eventrider.rest.model.service.dto;

import es.udc.eventrider.rest.model.domain.Event;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDTOEdit {
  @NotNull
  private Long id;
  @NotEmpty
  private String title;
  private List<UserDTOBase> subscribers;
  private List<UserDTOBase> saves;
  private LocalDateTime startingDate;
  private LocalDateTime endingDate;
  private double coordinateX;
  private double coordinateY;
  private String locationDetails;
  private String description;
  private Boolean existingCategoryChecked;
  private String existingCategoryId;
  private String newCategory;
  private String adminComments;
  private String cancellationReason;
  private Event.EventStatus status;

  public EventDTOEdit() {
  }

  public EventDTOEdit(Long id, String title, List<UserDTOBase> subscribers, List<UserDTOBase> saves, LocalDateTime startingDate, LocalDateTime endingDate, double coordinateX, double coordinateY, String locationDetails, String description, Boolean existingCategoryChecked, String existingCategoryId, String newCategory, String adminComments, String cancellationReason, Event.EventStatus status) {
    this.id = id;
    this.title = title;
    this.subscribers = subscribers;
    this.saves = saves;
    this.startingDate = startingDate;
    this.endingDate = endingDate;
    this.coordinateX = coordinateX;
    this.coordinateY = coordinateY;
    this.locationDetails = locationDetails;
    this.description = description;
    this.existingCategoryChecked = existingCategoryChecked;
    this.existingCategoryId = existingCategoryId;
    this.newCategory = newCategory;
    this.adminComments = adminComments;
    this.cancellationReason = cancellationReason;
    this.status = status;
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

  public Boolean getExistingCategoryChecked() {
    return existingCategoryChecked;
  }

  public void setExistingCategoryChecked(Boolean existingCategoryChecked) {
    this.existingCategoryChecked = existingCategoryChecked;
  }

  public String getExistingCategoryId() {
    return existingCategoryId;
  }

  public void setExistingCategoryId(String existingCategoryId) {
    this.existingCategoryId = existingCategoryId;
  }

  public String getNewCategory() {
    return newCategory;
  }

  public void setNewCategory(String newCategory) {
    this.newCategory = newCategory;
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
}
