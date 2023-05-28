package es.udc.eventrider.rest.web;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.exception.ModelException;
import es.udc.eventrider.rest.model.exception.NotFoundException;
import es.udc.eventrider.rest.model.exception.OperationNotAllowed;
import es.udc.eventrider.rest.model.service.EventService;
import es.udc.eventrider.rest.model.service.dto.*;
import es.udc.eventrider.rest.web.exceptions.IdAndBodyNotMatchingOnUpdateException;
import es.udc.eventrider.rest.web.exceptions.RequestBodyNotValidException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.management.InstanceNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventResource {

  @Autowired
  private EventService eventService;

  @GetMapping
  public List<EventDTO> findAll(@RequestParam(required = false) Map<String, String> query) {
    return eventService.findAll(query);
  }

  @GetMapping("/upcoming")
  public List<EventDTO> findPublishedUpcoming(@RequestParam(required = false) Map<String, String> query) {
    List<EventDTO> events = eventService.findAll(query).stream().filter(
        eventDTO -> eventDTO.getStatus() == Event.EventStatus.PUBLISHED &&
          !eventDTO.getEndingDate().isBefore(LocalDateTime.now()))
      .collect(Collectors.toList());
    return events;
  }

  @GetMapping("/{id}")
  public EventDTO findOne(@PathVariable Long id) throws NotFoundException {
    return eventService.findById(id);
  }

  @GetMapping("/{id}/image/{imgId}")
  @ResponseStatus(HttpStatus.OK)
  public void getEventImageById(@PathVariable Long id, HttpServletResponse response, @PathVariable Long imgId )
    throws InstanceNotFoundException, ModelException, IOException {
    ImageDTO image = eventService.getEventImageById(id, imgId);

    if (image == null) {
      response.sendError(404);
      return;
    }

    try {
      response.setContentType(image.getMediaType());
      response.setHeader("Content-disposition", "filename=" + image.getFilename());
      IOUtils.copy(image.getInputStream(), response.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @PostMapping("/{id}/image")
  @ResponseStatus(HttpStatus.OK)
  public void saveEventImageById(@PathVariable Long id, @RequestParam MultipartFile file, HttpServletResponse response)
    throws InstanceNotFoundException, ModelException {
    eventService.saveEventImageById(id, file);
  }

  @PostMapping
  public EventDTO create(@RequestBody @Valid EventDTOCreate event, Errors errors)
    throws RequestBodyNotValidException, OperationNotAllowed {
    if (errors.hasErrors()){
      throw new RequestBodyNotValidException(errors);
    }

    return eventService.create(event);
  }

  @PutMapping("/{id}")
  public EventDTO update(@PathVariable Long id, @RequestBody @Valid EventDTOEdit event, Errors errors)
      throws IdAndBodyNotMatchingOnUpdateException, RequestBodyNotValidException, NotFoundException {
    if (errors.hasErrors()){
      throw new RequestBodyNotValidException(errors);
    }

    if (!Objects.equals(id, event.getId())){
      throw new IdAndBodyNotMatchingOnUpdateException(Event.class);
    }
    return eventService.update(event);
  }
}
