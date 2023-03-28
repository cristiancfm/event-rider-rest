package es.udc.eventrider.rest.model.domain;

import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_generator")
  @SequenceGenerator(name = "event_generator", sequenceName = "event_seq")
  private Long id;

  private String title;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private User host;

  @ManyToMany(fetch = FetchType.LAZY)
  private List<User> subscribers = new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY)
  private List<User> saves = new ArrayList<>();

  private LocalDateTime startingDate;

  private LocalDateTime endingDate;

  private Point point;

  private String locationDetails;

  private String description;

  @ElementCollection
  @CollectionTable(name = "event_image_paths", joinColumns = @JoinColumn(name = "owner_id"))
  private List<String> imagePaths = new ArrayList<>();

  private String adminComments;

  private String cancellationReason;

  public enum EventStatus {
    UNREVIEWED,
    PUBLISHED,
    REJECTED,
    CANCELLED
  }
  @Enumerated(EnumType.STRING)
  private EventStatus status;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private EventCategory category;

  public Event(){
  }

  public Event(String title, User host, LocalDateTime startingDate, LocalDateTime endingDate,
               Point point, String locationDetails, String description,
               List<String> imagePaths, EventStatus status, EventCategory category) {
    this.title = title;
    this.host = host;
    this.startingDate = startingDate;
    this.endingDate = endingDate;
    this.point = point;
    this.locationDetails = locationDetails;
    this.description = description;
    this.imagePaths = imagePaths;
    this.status = status;
    this.category = category;
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

  public List<User> getSubscribers() {
    return subscribers;
  }

  public void setSubscribers(List<User> subscribers) {
    this.subscribers = subscribers;
  }

  public List<User> getSaves() {
    return saves;
  }

  public void setSaves(List<User> saves) {
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

  public Point getPoint() {
    return point;
  }

  public void setPoint(Point point) {
    this.point = point;
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

  public EventStatus getStatus() {
    return status;
  }

  public void setStatus(EventStatus status) {
    this.status = status;
  }

  public String getImagePath(Long imgId) {
    return this.imagePaths.get(Math.toIntExact(imgId));
  }

  public void setImagePath(String imagePath) {
    this.imagePaths.add(imagePath);
  }

  public EventCategory getCategory() {
    return category;
  }

  public void setCategory(EventCategory category) {
    this.category = category;
  }
}
