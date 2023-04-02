package es.udc.eventrider.rest.web;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.exception.NotFoundException;
import es.udc.eventrider.rest.model.service.EventCategoryService;
import es.udc.eventrider.rest.model.service.dto.EventCategoryDTO;
import es.udc.eventrider.rest.model.service.dto.EventDTO;
import es.udc.eventrider.rest.web.exceptions.IdAndBodyNotMatchingOnUpdateException;
import es.udc.eventrider.rest.web.exceptions.RequestBodyNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/event-categories")
public class EventCategoryResource {

  @Autowired
  private EventCategoryService eventCategoryService;

  @GetMapping
  public List<EventCategoryDTO> findAll(@RequestParam(required = false) String query) {
    return eventCategoryService.findAll();
  }

  @GetMapping("/{id}")
  public EventCategoryDTO findOne(@PathVariable Long id) throws NotFoundException {
    return eventCategoryService.findById(id);
  }

  @PostMapping
  public EventCategoryDTO create(@RequestBody @Valid EventCategoryDTO eventCategory, Errors errors){
    return null; //TODO
  }

  @PutMapping("/{id}")
  public EventCategoryDTO update(@PathVariable Long id, @RequestBody @Valid EventCategoryDTO eventCategory, Errors errors)
    throws IdAndBodyNotMatchingOnUpdateException, RequestBodyNotValidException, NotFoundException {
    if (errors.hasErrors()){
      throw new RequestBodyNotValidException(errors);
    }

    if (!Objects.equals(id, eventCategory.getId())){
      throw new IdAndBodyNotMatchingOnUpdateException(Event.class);
    }
    return eventCategoryService.update(eventCategory);
  }
}
