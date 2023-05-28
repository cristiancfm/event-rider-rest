package es.udc.eventrider.rest.model.service;

import es.udc.eventrider.rest.config.Properties;
import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.exception.ModelException;
import es.udc.eventrider.rest.model.exception.NotFoundException;
import es.udc.eventrider.rest.model.exception.OperationNotAllowed;
import es.udc.eventrider.rest.model.repository.EventCategoryDao;
import es.udc.eventrider.rest.model.repository.EventDao;
import es.udc.eventrider.rest.model.repository.UserDao;
import es.udc.eventrider.rest.model.service.dto.EventDTO;
import es.udc.eventrider.rest.model.service.dto.EventDTOCreate;
import es.udc.eventrider.rest.model.service.dto.EventDTOEdit;
import es.udc.eventrider.rest.model.service.dto.ImageDTO;
import es.udc.eventrider.rest.model.service.util.EmailServiceImpl;
import es.udc.eventrider.rest.model.service.util.ImageService;
import es.udc.eventrider.rest.security.SecurityUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.management.InstanceNotFoundException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class EventService {
  @Autowired
  private EventDao eventDAO;

  @Autowired
  private UserDao userDAO;

  @Autowired
  private EventCategoryDao eventCategoryDao;

  @Autowired
  private ImageService imageService;

  @Autowired
  private EmailServiceImpl emailService;

  @Autowired
  private UserService userService;

  @Autowired
  Properties properties;

  private Path rootLoc;

  public List<EventDTO> findAll(Map<String, String> query) {
    Stream<Event> events = eventDAO.findAll(query).stream();
    if(!SecurityUtils.getCurrentUserIsAdmin()) {
      events = events.filter(e -> e.getHost().isActive());
    }
    return events.map(event -> new EventDTO(event)).collect(Collectors.toList());
  }

  public EventDTO findById(Long id) throws NotFoundException {
    Event event = eventDAO.findById(id);
    if(event == null || !SecurityUtils.getCurrentUserIsAdmin() && !event.getHost().isActive()){
      throw new NotFoundException(id.toString(), Event.class);
    }
    return new EventDTO(event);
  }

  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public void saveEventImageById(Long id, MultipartFile file) throws InstanceNotFoundException, ModelException {
    Event event = eventDAO.findById(id);
    if (event == null)
      throw new NotFoundException(id.toString(), Event.class);

    String filePath = imageService.saveImage(ImageService.Entity.EVENT, file, event.getId());

    List<String> imagePaths = event.getImagePaths();
    if(event.getImagePaths() == null){
      imagePaths = new ArrayList<>();
    }
    imagePaths.add(filePath);
    event.setImagePaths(imagePaths);

    eventDAO.update(event);
  }

  public ImageDTO getEventImageById(Long id, Long imgId) throws InstanceNotFoundException, ModelException {
    Event event = eventDAO.findById(id);
    if(event == null || !SecurityUtils.getCurrentUserIsAdmin() && !event.getHost().isActive()) {
      throw new NotFoundException(id.toString(), Event.class);
    }
    if(event.getImagePaths() == null) {
      return null;
    }

    return imageService.getImage(ImageService.Entity.EVENT, event.getImagePath(imgId), event.getId());
  }

  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public void deleteEventImageById(Long id, Long imgId) throws InstanceNotFoundException, ModelException {
    Event event = eventDAO.findById(id);
    if (event == null)
      throw new NotFoundException(id.toString(), Event.class);

    imageService.deleteImage(ImageService.Entity.EVENT, event.getImagePath(imgId), event.getId());

    Path folderPath = getRootLoc().resolve(  "events/" + id + "/" + "images");
    File[] files = folderPath.toFile().listFiles();
    List<String> imagePaths = new ArrayList<>();
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      imagePaths.add(file.getName());
    }
    event.setImagePaths(imagePaths);

    eventDAO.update(event);
  }

  private Path getRootLoc() {
    if (rootLoc == null)
      this.rootLoc = Paths.get(properties.getImagesPath());
    return rootLoc;
  }

  @PreAuthorize("isAuthenticated()")
  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public EventDTO create(EventDTOCreate event) throws OperationNotAllowed {
    Event dbEvent = new Event();

    dbEvent.setTitle(event.getTitle());

    Long userId = userService.getCurrentUserWithAuthority().getId();
    dbEvent.setHost(userDAO.findById(userId));

    //Check if starting & ending dates are not past dates
    if(event.getStartingDate().isBefore(LocalDateTime.now()) ||
    event.getEndingDate().isBefore(LocalDateTime.now())){
      throw new OperationNotAllowed("The starting, ending or both dates are past dates");
    }
    //Check if ending date is after starting date
    if(event.getEndingDate().isBefore(event.getStartingDate())){
      throw new OperationNotAllowed("The ending date is before the starting date");
    }
    dbEvent.setStartingDate(event.getStartingDate());
    dbEvent.setEndingDate(event.getEndingDate());

    GeometryFactory geometryFactory = new GeometryFactory();
    Point point = geometryFactory.createPoint(new Coordinate(event.getCoordinateX(), event.getCoordinateY()));
    dbEvent.setPoint(point);

    dbEvent.setLocationDetails(event.getLocationDetails());
    dbEvent.setDescription(event.getDescription());

    dbEvent.setStatus(Event.EventStatus.UNREVIEWED); //TODO verified user (no hace falta revisar los eventos)

    if(event.getExistingCategoryChecked()){
      dbEvent.setCategory(eventCategoryDao.findById(Long.parseLong(event.getExistingCategoryId())));
    }
    //TODO create category

    eventDAO.create(dbEvent);
    return new EventDTO(dbEvent);
  }

  @PreAuthorize("isAuthenticated()")
  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public EventDTO update(EventDTOEdit event) throws NotFoundException {
    Event dbEvent = eventDAO.findById(event.getId());
    if (dbEvent == null) {
      throw new NotFoundException(event.getId().toString(), Event.class);
    }
    dbEvent.setTitle(event.getTitle());

    if(event.getSubscribers() != null){
      dbEvent.getSubscribers().clear();
      event.getSubscribers().forEach(s -> {
        dbEvent.getSubscribers().add(userDAO.findById(s.getId()));
      });
    }

    if(event.getSaves() != null){
      dbEvent.getSaves().clear();
      event.getSaves().forEach(s -> {
        dbEvent.getSaves().add(userDAO.findById(s.getId()));
      });
    }

    dbEvent.setStartingDate(event.getStartingDate());
    dbEvent.setEndingDate(event.getEndingDate());

    GeometryFactory geometryFactory = new GeometryFactory();
    Point point = geometryFactory.createPoint(new Coordinate(event.getCoordinateX(), event.getCoordinateY()));
    dbEvent.setPoint(point);

    dbEvent.setLocationDetails(event.getLocationDetails());
    dbEvent.setDescription(event.getDescription());

    if(event.getAdminComments() != null){
      dbEvent.setAdminComments(event.getAdminComments());
    }

    if(event.getCancellationReason() != null){
      dbEvent.setCancellationReason(event.getCancellationReason());
    }

    if(event.getStatus() != null){
      dbEvent.setStatus(event.getStatus());
    }

    if(event.getExistingCategoryChecked()){
      dbEvent.setCategory(eventCategoryDao.findById(Long.parseLong(event.getExistingCategoryId())));
    }
    //TODO create category

    //TODO send email updates
    //emailService.sendSimpleMessage("cristian.ferreiro@udc.es", "Prueba de Event Rider", "Esta es una prueba");

    eventDAO.update(dbEvent);
    return new EventDTO(dbEvent);
  }
}
