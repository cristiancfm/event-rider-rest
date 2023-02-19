package es.udc.eventrider.rest.model.domain;

import io.github.sebasbaumh.postgis.Point;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "title", nullable = false)
  private String title;

  @ManyToOne(optional = false)
  @JoinColumn(name = "host_id", nullable = false)
  private User host;

  @Column(name = "starting_date", nullable = false)
  private LocalDateTime startingDate;

  @Column(name = "ending_date", nullable = false)
  private LocalDateTime endingDate;

  @Column(name = "coordinates", nullable = false)
  private Point coordinates;

  @Column(name = "location_details")
  private String locationDetails;

  @Column(name = "description")
  private String description;

  @ElementCollection
  @Column(name = "image_path")
  @CollectionTable(name = "event_image_paths", joinColumns = @JoinColumn(name = "owner_id"))
  private List<String> imagePaths = new ArrayList<>();

  @Column(name = "admin_comments")
  private String adminComments;

  @Column(name = "cancellation_reason")
  private String cancellationReason;

  public enum EventStatus {
    UNREVIEWED,
    PUBLISHED,
    REJECTED,
    CANCELLED
  }
  @Enumerated(EnumType.STRING)
  @Column(name = "event_status")
  private EventStatus eventStatus;


  public Event(){
  }

  public Event(String title, User host, LocalDateTime startingDate, LocalDateTime endingDate,
               Point coordinates, String locationDetails, String description,
               List<String> imagePaths, EventStatus eventStatus) {
    this.title = title;
    this.host = host;
    this.startingDate = startingDate;
    this.endingDate = endingDate;
    this.coordinates = coordinates;
    this.locationDetails = locationDetails;
    this.description = description;
    this.imagePaths = imagePaths;
    this.eventStatus = eventStatus;
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

  public User getHost() {
    return host;
  }

  public void setHost(User host) {
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

  public Point getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Point coordinates) {
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

  public List<String> getImagePaths() {
    return imagePaths;
  }

  public void setImagePaths(List<String> imagePaths) {
    this.imagePaths = imagePaths;
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

  public EventStatus getEventStatus() {
    return eventStatus;
  }

  public void setEventStatus(EventStatus eventStatus) {
    this.eventStatus = eventStatus;
  }
}
