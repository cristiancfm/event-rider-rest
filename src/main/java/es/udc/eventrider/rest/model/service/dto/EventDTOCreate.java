package es.udc.eventrider.rest.model.service.dto;

import es.udc.eventrider.rest.model.domain.Event;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDTOCreate {
  @NotEmpty
  private String title;
  @NotNull
  private ZonedDateTime startingDate;
  @NotNull
  private ZonedDateTime endingDate;
  @NotNull
  private double coordinateX;
  @NotNull
  private double coordinateY;
  private String locationDetails;
  @NotEmpty
  private String description;
  private Boolean existingCategoryChecked;
  private String existingCategoryId;
  private String newCategory;

  public EventDTOCreate() {
  }

  public EventDTOCreate(String title, ZonedDateTime startingDate, ZonedDateTime endingDate, double coordinateX, double coordinateY, String locationDetails, String description, Boolean existingCategoryChecked, String existingCategoryId, String newCategory) {
    this.title = title;
    this.startingDate = startingDate;
    this.endingDate = endingDate;
    this.coordinateX = coordinateX;
    this.coordinateY = coordinateY;
    this.locationDetails = locationDetails;
    this.description = description;
    this.existingCategoryChecked = existingCategoryChecked;
    this.existingCategoryId = existingCategoryId;
    this.newCategory = newCategory;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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
}
