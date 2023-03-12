package es.udc.eventrider.rest.model.service;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.exception.ModelException;
import es.udc.eventrider.rest.model.exception.NotFoundException;
import es.udc.eventrider.rest.model.repository.EventDao;
import es.udc.eventrider.rest.model.repository.UserDao;
import es.udc.eventrider.rest.model.service.dto.EventDTO;
import es.udc.eventrider.rest.model.service.dto.ImageDTO;
import es.udc.eventrider.rest.model.service.util.ImageService;
import es.udc.eventrider.rest.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.management.InstanceNotFoundException;
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
  private ImageService imageService;

  @Autowired
  private UserDao userDAO;

  @Autowired
  private UserService userService;

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
    List<String> imagePaths = new ArrayList<>();
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
}
