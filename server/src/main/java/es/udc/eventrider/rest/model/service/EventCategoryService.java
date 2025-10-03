package es.udc.eventrider.rest.model.service;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.domain.EventCategory;
import es.udc.eventrider.rest.model.domain.User;
import es.udc.eventrider.rest.model.exception.NotFoundException;
import es.udc.eventrider.rest.model.exception.OperationNotAllowed;
import es.udc.eventrider.rest.model.repository.EventCategoryDao;
import es.udc.eventrider.rest.model.repository.EventDao;
import es.udc.eventrider.rest.model.repository.UserDao;
import es.udc.eventrider.rest.model.service.dto.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
  public EventCategoryDTO create(EventCategoryDTOCreate category) throws OperationNotAllowed {
    List<EventCategory> existingCategories = eventCategoryDAO.findAll();
    for (EventCategory existingCategory : existingCategories) {
      if(Objects.equals(existingCategory.getName(), category.getName())) {
        throw new OperationNotAllowed("The event category already exists");
      }
    }

    EventCategory dbCategory = new EventCategory();
    dbCategory.setName(category.getName());

    //This method is used only by administrators, hence the categories
    //are marked as published directly
    dbCategory.setStatus(EventCategory.EventCategoryStatus.PUBLISHED);

    eventCategoryDAO.create(dbCategory);
    return new EventCategoryDTO(dbCategory);
  }

  @PreAuthorize("isAuthenticated()")
  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public EventCategoryDTO update(EventCategoryDTOEdit eventCategory) throws NotFoundException {
    EventCategory dbEventCategory = eventCategoryDAO.findById(eventCategory.getId());
    if (dbEventCategory == null) {
      throw new NotFoundException(eventCategory.getId().toString(), Event.class);
    }

    if(!Objects.equals(eventCategory.getName(), dbEventCategory.getName())){
      dbEventCategory.setName(eventCategory.getName());
    }

    if(eventCategory.getSubscribers() != null){
      dbEventCategory.getSubscribers().clear();
      eventCategory.getSubscribers().forEach(s -> {
        dbEventCategory.getSubscribers().add(userDAO.findById(s.getId()));
      });
    }

    if(!Objects.equals(eventCategory.getStatus(), dbEventCategory.getStatus())){
      dbEventCategory.setStatus(eventCategory.getStatus());
    }

    eventCategoryDAO.update(dbEventCategory);
    return new EventCategoryDTO(dbEventCategory);
  }

  @PreAuthorize("isAuthenticated()")
  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public void delete(Long id) throws OperationNotAllowed {
    // Check if the category contains events
    if(!eventCategoryDAO.findById(id).getEvents().isEmpty()){
      throw new OperationNotAllowed("This category contains events");
    }

    eventCategoryDAO.deleteById(id);
  }
}
