package es.udc.eventrider.rest.web;

import es.udc.eventrider.rest.model.exception.NotFoundException;
import es.udc.eventrider.rest.model.service.EventService;
import es.udc.eventrider.rest.model.service.dto.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventResource {

  @Autowired
  private EventService eventService;

  @GetMapping
  public List<EventDTO> findAll(@RequestParam(required = false) String query) {
    return eventService.findAll();
  }

  @GetMapping("/{id}")
  public EventDTO findOne(@PathVariable Long id) throws NotFoundException {
    return eventService.findById(id);
  }
}
