package es.udc.eventrider.rest.web;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.exception.ModelException;
import es.udc.eventrider.rest.model.service.EventService;
import es.udc.eventrider.rest.model.service.dto.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import es.udc.eventrider.rest.model.exception.NotFoundException;
import es.udc.eventrider.rest.model.exception.OperationNotAllowed;
import es.udc.eventrider.rest.model.service.UserService;

import javax.management.InstanceNotFoundException;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/users")
public class UserResource {

  @Autowired
  private UserService userService;

  @Autowired
  private EventService eventService;

  @GetMapping("/with-events")
  public List<UserDTOWithEvents> findAllWithEvents() {
    return userService.findAllWithEvents();
  }

  @GetMapping("/{id}")
  public UserDTOPublic findOne(@PathVariable Long id) throws NotFoundException {
    return userService.findById(id);
  }

  @GetMapping("/{id}/with-events")
  public UserDTOWithEvents findOneWithEvents(@PathVariable Long id) throws NotFoundException {
    return userService.findByIdWithEvents(id);
  }

  @GetMapping("/{id}/image")
  @ResponseStatus(HttpStatus.OK)
  public void getUserImage(@PathVariable Long id, HttpServletResponse response)
    throws InstanceNotFoundException, ModelException, IOException {
    ImageDTO image = userService.getUserImageById(id);

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

//  @GetMapping("/{id}/events")
//  public List<EventDTO> findEvents(@PathVariable Long id) throws NotFoundException {
//    UserDTOPublic userDTO = userService.findById(id);
//    return eventService.findAll().stream()
//      .filter(eventDTO -> Objects.equals(eventDTO.getHost().getEmail(), userDTO.getEmail()) &&
//          eventDTO.getEventStatus() == Event.EventStatus.PUBLISHED &&
//          !eventDTO.getStartingDate().isBefore(LocalDateTime.now()))
//      .collect(Collectors.toList());
//  }

  @PutMapping("/{id}/active")
  public UserDTOPublic activate(@PathVariable Long id) throws NotFoundException, OperationNotAllowed {
    return userService.updateActive(id, true);
  }

  @DeleteMapping("/{id}/active")
  public UserDTOPublic deactivate(@PathVariable Long id) throws NotFoundException, OperationNotAllowed {
    return userService.updateActive(id, false);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) throws NotFoundException, OperationNotAllowed {
    userService.deleteById(id);
  }
}
