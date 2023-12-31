package es.udc.eventrider.rest.web;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.domain.UserAuthority;
import es.udc.eventrider.rest.model.exception.ModelException;
import es.udc.eventrider.rest.model.service.EventCategoryService;
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
import org.springframework.web.multipart.MultipartFile;

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

  @Autowired
  private EventCategoryService eventCategoryService;

  @GetMapping
  public List<UserDTOPublic> findAll() {
    return userService.findAll();
  }

  @GetMapping("/active")
  public List<UserDTOPublic> findActive() {
    List<UserDTOPublic> users = userService.findAll().stream().filter(
      userDTO -> (userDTO.getAuthority() == UserAuthority.USER ||
                  userDTO.getAuthority() == UserAuthority.USER_VERIFIED ||
                  userDTO.getAuthority() == UserAuthority.ADMIN)).toList();
    return users;
  }

  @GetMapping("/unverified")
  public List<UserDTOPublic> findUnverified() {
    List<UserDTOPublic> users = userService.findAll().stream().filter(
      userDTO -> (userDTO.getAuthority() == UserAuthority.USER)).toList();
    return users;
  }

  @GetMapping("/verified")
  public List<UserDTOPublic> findVerified() {
    List<UserDTOPublic> users = userService.findAll().stream().filter(
      userDTO -> (userDTO.getAuthority() == UserAuthority.USER_VERIFIED)).toList();
    return users;
  }

  @GetMapping("/admin")
  public List<UserDTOPublic> findAdmin() {
    List<UserDTOPublic> users = userService.findAll().stream().filter(
      userDTO -> (userDTO.getAuthority() == UserAuthority.ADMIN)).toList();
    return users;
  }

  @GetMapping("/suspended")
  public List<UserDTOPublic> findSuspended() {
    List<UserDTOPublic> users = userService.findAll().stream().filter(
      userDTO -> (userDTO.getAuthority() == UserAuthority.USER_SUSPENDED)).toList();
    return users;
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

  @PostMapping("/{id}/image")
  @ResponseStatus(HttpStatus.OK)
  public void saveUserImage(@PathVariable Long id, @RequestParam MultipartFile file, HttpServletResponse response)
    throws InstanceNotFoundException, ModelException {

    userService.saveUserImageById(id, file);
  }

  @GetMapping("/{id}/events")
  public List<EventDTO> findUserEvents(@PathVariable Long id, @RequestParam(required = false) Map<String, String> query) {
    try {
      UserDTOPublic user = userService.findById(id);
      List<EventDTO> events = eventService.findAll(query).stream().filter(
        eventDTO -> Objects.equals(eventDTO.getHost().getId(), user.getId())
      ).collect(Collectors.toList());

      return events;
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/{id}/events/upcoming")
  public List<EventDTO> findUserUpcomingEvents(@PathVariable Long id, @RequestParam(required = false) Map<String, String> query) {
    try {
      UserDTOPublic user = userService.findById(id);
      List<EventDTO> events = eventService.findAll(query).stream().filter(
        eventDTO -> Objects.equals(eventDTO.getHost().getId(), user.getId())
      ).collect(Collectors.toList());

      events = events.stream().filter(
        eventDTO -> (eventDTO.getStatus() == Event.EventStatus.PUBLISHED ||
            eventDTO.getStatus() == Event.EventStatus.CANCELLED) &&
          !eventDTO.getEndingDate().isBefore(LocalDateTime.now()))
        .collect(Collectors.toList());

      return events;
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/{id}/events/past")
  public List<EventDTO> findUserPastEvents(@PathVariable Long id, @RequestParam(required = false) Map<String, String> query) {
    try {
      UserDTOPublic user = userService.findById(id);
      List<EventDTO> events = eventService.findAll(query).stream().filter(
        eventDTO -> Objects.equals(eventDTO.getHost().getId(), user.getId())
      ).collect(Collectors.toList());

      events = events.stream().filter(
          eventDTO -> (eventDTO.getStatus() == Event.EventStatus.PUBLISHED ||
              eventDTO.getStatus() == Event.EventStatus.CANCELLED) &&
            eventDTO.getEndingDate().isBefore(LocalDateTime.now()))
        .collect(Collectors.toList());

      return events;
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/{id}/events/unreviewed")
  public List<EventDTO> findUserUnreviewedEvents(@PathVariable Long id, @RequestParam(required = false) Map<String, String> query) {
    try {
      UserDTOPublic user = userService.findById(id);
      List<EventDTO> events = eventService.findAll(query).stream().filter(
        eventDTO -> Objects.equals(eventDTO.getHost().getId(), user.getId())
      ).collect(Collectors.toList());

      events = events.stream().filter(
          eventDTO -> eventDTO.getStatus() == Event.EventStatus.UNREVIEWED)
        .collect(Collectors.toList());

      return events;
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/{id}/events/rejected")
  public List<EventDTO> findUserRejectedEvents(@PathVariable Long id, @RequestParam(required = false) Map<String, String> query) {
    try {
      UserDTOPublic user = userService.findById(id);
      List<EventDTO> events = eventService.findAll(query).stream().filter(
        eventDTO -> Objects.equals(eventDTO.getHost().getId(), user.getId())
      ).collect(Collectors.toList());

      events = events.stream().filter(
          eventDTO -> eventDTO.getStatus() == Event.EventStatus.REJECTED)
        .collect(Collectors.toList());

      return events;
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/{id}/events/saved/upcoming")
  public List<EventDTO> findUserSavedUpcomingEvents(@PathVariable Long id, @RequestParam(required = false) Map<String, String> query) {
    try {
      UserDTOPublic user = userService.findById(id);
      List<EventDTO> events = eventService.findAll(query).stream().filter(
        eventDTO -> eventDTO.getSaves().stream()
          .anyMatch(savedUser -> Objects.equals(savedUser.getId(), user.getId()))
      ).collect(Collectors.toList());

      events = events.stream().filter(
          eventDTO -> (eventDTO.getStatus() == Event.EventStatus.PUBLISHED ||
              eventDTO.getStatus() == Event.EventStatus.CANCELLED) &&
            !eventDTO.getEndingDate().isBefore(LocalDateTime.now()))
        .collect(Collectors.toList());

      return events;
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/{id}/events/saved/past")
  public List<EventDTO> findUserSavedPastEvents(@PathVariable Long id, @RequestParam(required = false) Map<String, String> query) {
    try {
      UserDTOPublic user = userService.findById(id);
      List<EventDTO> events = eventService.findAll(query).stream().filter(
        eventDTO -> eventDTO.getSaves().stream()
          .anyMatch(savedUser -> Objects.equals(savedUser.getId(), user.getId()))
      ).collect(Collectors.toList());

      events = events.stream().filter(
          eventDTO -> (eventDTO.getStatus() == Event.EventStatus.PUBLISHED ||
              eventDTO.getStatus() == Event.EventStatus.CANCELLED) &&
            eventDTO.getEndingDate().isBefore(LocalDateTime.now()))
        .collect(Collectors.toList());

      return events;
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/{id}/events/subscribed/upcoming")
  public List<EventDTO> findUserSubscribedUpcomingEvents(@PathVariable Long id, @RequestParam(required = false) Map<String, String> query) {
    try {
      UserDTOPublic user = userService.findById(id);
      List<EventDTO> events = eventService.findAll(query).stream().filter(
        eventDTO -> eventDTO.getSubscribers().stream()
          .anyMatch(subscribedUser -> Objects.equals(subscribedUser.getId(), user.getId()))
      ).collect(Collectors.toList());

      events = events.stream().filter(
          eventDTO -> (eventDTO.getStatus() == Event.EventStatus.PUBLISHED ||
            eventDTO.getStatus() == Event.EventStatus.CANCELLED) &&
            !eventDTO.getEndingDate().isBefore(LocalDateTime.now()))
        .collect(Collectors.toList());

      return events;
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/{id}/events/subscribed/past")
  public List<EventDTO> findUserSubscribedPastEvents(@PathVariable Long id, @RequestParam(required = false) Map<String, String> query) {
    try {
      UserDTOPublic user = userService.findById(id);
      List<EventDTO> events = eventService.findAll(query).stream().filter(
        eventDTO -> eventDTO.getSubscribers().stream()
          .anyMatch(subscribedUser -> Objects.equals(subscribedUser.getId(), user.getId()))
      ).collect(Collectors.toList());

      events = events.stream().filter(
          eventDTO -> (eventDTO.getStatus() == Event.EventStatus.PUBLISHED ||
            eventDTO.getStatus() == Event.EventStatus.CANCELLED) &&
            eventDTO.getEndingDate().isBefore(LocalDateTime.now()))
        .collect(Collectors.toList());

      return events;
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/{id}/categories/subscribed")
  public List<EventCategoryDTO> findUserSubscribedCategories(@PathVariable Long id, @RequestParam(required = false) Map<String, String> query) {
    try {
      UserDTOPublic user = userService.findById(id);
      List<EventCategoryDTO> eventCategories = eventCategoryService.findAll().stream().filter(
        eventCategoryDTO -> eventCategoryDTO.getSubscribers().stream()
          .anyMatch(subscribedUser -> Objects.equals(subscribedUser.getId(), user.getId()))
      ).collect(Collectors.toList());

      return eventCategories;
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/{id}/followers")
  public List<UserDTOPublic> findUserFollowers(@PathVariable Long id, @RequestParam(required = false) Map<String, String> query) {
    try {
      UserDTOPublic user = userService.findById(id);
      List<UserDTOPublic> followers = new ArrayList<>();
      for (UserDTOBase followerBase: user.getFollowers()) {
        followers.add(userService.findById(followerBase.getId()));
      }
      return followers;
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/{id}/following")
  public List<UserDTOPublic> findUserFollowing(@PathVariable Long id, @RequestParam(required = false) Map<String, String> query) {
    try {
      UserDTOPublic user = userService.findById(id);
      List<UserDTOPublic> following = new ArrayList<>();
      for (UserDTOBase followingBase: user.getFollowing()) {
        following.add(userService.findById(followingBase.getId()));
      }
      return following;
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    }
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
    return userService.updateUser(user);
  }

  @PutMapping("/{id}/authority")
  public UserDTOPublic updateAuthority(@RequestBody @Valid UserDTOBase user)
    throws NotFoundException, OperationNotAllowed {
    return userService.updateUserAuthority(user);
  }

}
