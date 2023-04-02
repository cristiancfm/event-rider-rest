package es.udc.eventrider.rest.model.service;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.domain.EventCategory;
import es.udc.eventrider.rest.model.exception.NotFoundException;
import es.udc.eventrider.rest.model.repository.EventCategoryDao;
import es.udc.eventrider.rest.model.repository.EventDao;
import es.udc.eventrider.rest.model.repository.UserDao;
import es.udc.eventrider.rest.model.service.dto.EventCategoryDTO;
import es.udc.eventrider.rest.model.service.dto.EventDTO;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class EventCategoryService {

  @Autowired
  private EventCategoryDao eventCategoryDAO;

  @Autowired
  private UserDao userDAO;

  @Autowired
  private EventDao eventDAO;

  public List<EventCategoryDTO> findAll() {
    Stream<EventCategory> eventCategories = eventCategoryDAO.findAll().stream();
    return eventCategories.map(eventCategory -> new EventCategoryDTO(eventCategory)).collect(Collectors.toList());
  }

  public EventCategoryDTO findById(Long id) throws NotFoundException {
    EventCategory eventCategory = eventCategoryDAO.findById(id);
    if(eventCategory == null){
      throw new NotFoundException(id.toString(), Event.class);
    }
    return new EventCategoryDTO(eventCategory);
  }

  @PreAuthorize("isAuthenticated()")
  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public EventCategoryDTO update(EventCategoryDTO eventCategory) throws NotFoundException {
    EventCategory dbEventCategory = eventCategoryDAO.findById(eventCategory.getId());
    if (dbEventCategory == null) {
      throw new NotFoundException(eventCategory.getId().toString(), Event.class);
    }
    dbEventCategory.setName(eventCategory.getName());

    dbEventCategory.getSubscribers().clear();
    eventCategory.getSubscribers().forEach(s -> {
      dbEventCategory.getSubscribers().add(userDAO.findById(s.getId()));
    });

    dbEventCategory.setStatus(eventCategory.getStatus());

    //TODO send email updates
    //emailService.sendSimpleMessage("cristian.ferreiro@udc.es", "Prueba de Event Rider", "Esta es una prueba");

    eventCategoryDAO.update(dbEventCategory);
    return new EventCategoryDTO(dbEventCategory);
  }
}
