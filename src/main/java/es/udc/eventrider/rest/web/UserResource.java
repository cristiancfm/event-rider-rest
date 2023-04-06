package es.udc.eventrider.rest.web;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.exception.ModelException;
import es.udc.eventrider.rest.model.service.EventService;
import es.udc.eventrider.rest.model.service.dto.*;
import es.udc.eventrider.rest.web.exceptions.IdAndBodyNotMatchingOnUpdateException;
import es.udc.eventrider.rest.web.exceptions.RequestBodyNotValidException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import es.udc.eventrider.rest.model.exception.NotFoundException;
import es.udc.eventrider.rest.model.exception.OperationNotAllowed;
import es.udc.eventrider.rest.model.service.UserService;

import javax.management.InstanceNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserResource {

  @Autowired
  private UserService userService;

  @Autowired
  private EventService eventService;

  @GetMapping
  public List<UserDTOPublic> findAll() {
    return userService.findAll();
  }

  @GetMapping("/{id}")
  public UserDTOPublic findOne(@PathVariable Long id) throws NotFoundException {
    return userService.findById(id);
  }

  @GetMapping("/{id}/base")
  public UserDTOBase findOneBase(@PathVariable Long id) throws NotFoundException {
    return userService.findByIdBase(id);
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

  @PostMapping
  public UserDTOBase create(@RequestBody @Valid UserDTOBase user, Errors errors){
    return null; //TODO
  }

  @PutMapping("/{id}")
  public UserDTOPublic update(@PathVariable Long id, @RequestBody @Valid UserDTOPublic user, Errors errors)
    throws IdAndBodyNotMatchingOnUpdateException, RequestBodyNotValidException, NotFoundException {
    if (errors.hasErrors()){
      throw new RequestBodyNotValidException(errors);
    }

    if (!Objects.equals(id, user.getId())){
      throw new IdAndBodyNotMatchingOnUpdateException(Event.class);
    }
    return userService.update(user);
  }

  @PutMapping("/{id}/active")
  public UserDTOBase activate(@PathVariable Long id) throws NotFoundException, OperationNotAllowed {
    return userService.updateActive(id, true);
  }

  @DeleteMapping("/{id}/active")
  public UserDTOBase deactivate(@PathVariable Long id) throws NotFoundException, OperationNotAllowed {
    return userService.updateActive(id, false);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) throws NotFoundException, OperationNotAllowed {
    userService.deleteById(id);
  }
}
