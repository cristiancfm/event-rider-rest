package es.udc.eventrider.rest.web;

import es.udc.eventrider.rest.model.exception.NotFoundException;
import es.udc.eventrider.rest.model.service.EventCategoryService;
import es.udc.eventrider.rest.model.service.dto.EventCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
