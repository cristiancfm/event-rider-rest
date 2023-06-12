package es.udc.eventrider.rest.web;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.domain.EventCategory;
import es.udc.eventrider.rest.model.exception.NotFoundException;
import es.udc.eventrider.rest.model.exception.OperationNotAllowed;
import es.udc.eventrider.rest.model.service.EventCategoryService;
import es.udc.eventrider.rest.model.service.dto.EventCategoryDTO;
import es.udc.eventrider.rest.model.service.dto.EventCategoryDTOCreate;
import es.udc.eventrider.rest.model.service.dto.EventCategoryDTOEdit;
import es.udc.eventrider.rest.model.service.dto.EventDTO;
import es.udc.eventrider.rest.web.exceptions.IdAndBodyNotMatchingOnUpdateException;
import es.udc.eventrider.rest.web.exceptions.RequestBodyNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event-categories")
public class EventCategoryResource {

  @Autowired
  private EventCategoryService eventCategoryService;

  @GetMapping
  public List<EventCategoryDTO> findAll() {
    return eventCategoryService.findAll();
  }

  @GetMapping("/published")
  public List<EventCategoryDTO> findPublished() {
    List<EventCategoryDTO> categories = eventCategoryService.findAll().stream().filter(
      categoryDTO -> (categoryDTO.getStatus() == EventCategory.EventCategoryStatus.PUBLISHED)).toList();
    return categories;
  }

  @GetMapping("/unreviewed")
  public List<EventCategoryDTO> findUnreviewed() {
    List<EventCategoryDTO> categories = eventCategoryService.findAll().stream().filter(
      categoryDTO -> (categoryDTO.getStatus() == EventCategory.EventCategoryStatus.UNREVIEWED)).toList();
    return categories;
  }

  @GetMapping("/rejected")
  public List<EventCategoryDTO> findRejected() {
    List<EventCategoryDTO> categories = eventCategoryService.findAll().stream().filter(
      categoryDTO -> (categoryDTO.getStatus() == EventCategory.EventCategoryStatus.REJECTED)).toList();
    return categories;
  }

  @GetMapping("/{id}")
  public EventCategoryDTO findOne(@PathVariable Long id) throws NotFoundException {
    return eventCategoryService.findById(id);
  }

  @PostMapping
  public EventCategoryDTO create(@RequestBody @Valid EventCategoryDTOCreate eventCategory, Errors errors)
    throws RequestBodyNotValidException, OperationNotAllowed {
    if (errors.hasErrors()){
      throw new RequestBodyNotValidException(errors);
    }

    return eventCategoryService.create(eventCategory);
  }

  @PutMapping("/{id}")
  public EventCategoryDTO update(@PathVariable Long id, @RequestBody @Valid EventCategoryDTOEdit eventCategory, Errors errors)
    throws IdAndBodyNotMatchingOnUpdateException, RequestBodyNotValidException, NotFoundException {
    if (errors.hasErrors()){
      throw new RequestBodyNotValidException(errors);
    }

    if (!Objects.equals(id, eventCategory.getId())){
      throw new IdAndBodyNotMatchingOnUpdateException(Event.class);
    }
    return eventCategoryService.update(eventCategory);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) throws OperationNotAllowed {
    eventCategoryService.delete(id);
  }
}
