package es.udc.eventrider.rest.model.service;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.domain.EventCategory;
import es.udc.eventrider.rest.model.exception.NotFoundException;
import es.udc.eventrider.rest.model.repository.EventCategoryDao;
import es.udc.eventrider.rest.model.service.dto.EventCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class EventCategoryService {

  @Autowired
  private EventCategoryDao eventCategoryDao;

  public List<EventCategoryDTO> findAll() {
    Stream<EventCategory> eventCategories = eventCategoryDao.findAll().stream();
    return eventCategories.map(eventCategory -> new EventCategoryDTO(eventCategory)).collect(Collectors.toList());
  }

  public EventCategoryDTO findById(Long id) throws NotFoundException {
    EventCategory eventCategory = eventCategoryDao.findById(id);
    if(eventCategory == null){
      throw new NotFoundException(id.toString(), Event.class);
    }
    return new EventCategoryDTO(eventCategory);
  }
}
