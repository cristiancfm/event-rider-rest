package es.udc.eventrider.rest.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.domain.User;
import es.udc.eventrider.rest.model.domain.UserAuthority;
import es.udc.eventrider.rest.model.exception.ModelException;
import es.udc.eventrider.rest.model.exception.NotFoundException;
import es.udc.eventrider.rest.model.exception.OperationNotAllowed;
import es.udc.eventrider.rest.model.exception.UserEmailExistsException;
import es.udc.eventrider.rest.model.repository.EventDao;
import es.udc.eventrider.rest.model.repository.UserDao;
import es.udc.eventrider.rest.model.service.dto.*;
import es.udc.eventrider.rest.model.service.util.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.eventrider.rest.security.SecurityUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.management.InstanceNotFoundException;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class UserService {

  @Autowired
  private UserDao userDAO;

  @Autowired
  private EventDao eventDAO;

  @Autowired
  private ImageService imageService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public List<UserDTOPublic> findAll() {
    Stream<UserDTOPublic> users = userDAO.findAll().stream().map(user -> new UserDTOPublic(user));
    if (SecurityUtils.getCurrentUserIsAdmin()) {
      return users.collect(Collectors.toList());
    }
    return users.filter(user -> user.isActive()).collect(Collectors.toList());
  }

  public UserDTOPublic findById(Long id) throws NotFoundException {
    User user = userDAO.findById(id);
    if (user == null) {
      throw new NotFoundException(id.toString(), User.class);
    }
    return new UserDTOPublic(user);
  }

  public UserDTOBase findByIdBase(Long id) throws NotFoundException {
    User user = userDAO.findById(id);
    if (user == null) {
      throw new NotFoundException(id.toString(), User.class);
    }
    return new UserDTOBase(user);
  }

  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public void saveUserImageById(Long id, MultipartFile file) throws InstanceNotFoundException, ModelException {
    User user = userDAO.findById(id);
    if (user == null)
      throw new NotFoundException(id.toString(), Event.class);

    String filePath = imageService.saveImage(ImageService.Entity.USER, file, user.getId());
    user.setImagePath(filePath);
    userDAO.update(user);
  }

  public ImageDTO getUserImageById(Long id) throws InstanceNotFoundException, ModelException {
    User user = userDAO.findById(id);
    if(user == null) {
      throw new NotFoundException(id.toString(), User.class);
    }
    if(user.getImagePath() == null) {
      return null;
    }

    return imageService.getImage(ImageService.Entity.USER, user.getImagePath(), user.getId());
  }

  @PreAuthorize("isAuthenticated()")
  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public UserDTOPublic updateUser(UserDTOPublic user) throws NotFoundException {
    User dbUser = userDAO.findById(user.getId());
    if (dbUser == null) {
      throw new NotFoundException(user.getId().toString(), Event.class);
    }

    dbUser.setName(user.getName());
    dbUser.setSurname(user.getSurname());
    dbUser.setBiography(user.getBiography());
    dbUser.setEmail(user.getEmail());
    //TODO dbUser.setPassword
    //TODO dbUser.setActive

    dbUser.getHostedEvents().clear();
    user.getHostedEvents().forEach(e -> {
      dbUser.getHostedEvents().add(eventDAO.findById(e.getId()));
    });

    dbUser.getSubscribedEvents().clear();
    user.getSubscribedEvents().forEach(e -> {
      dbUser.getSubscribedEvents().add(eventDAO.findById(e.getId()));
    });

    dbUser.getSavedEvents().clear();
    user.getSavedEvents().forEach(e -> {
      dbUser.getSavedEvents().add(eventDAO.findById(e.getId()));
    });

    dbUser.getFollowers().clear();
    user.getFollowers().forEach(f -> {
      dbUser.getFollowers().add(userDAO.findById(f.getId()));
    });

    //TODO send email updates
    //emailService.sendSimpleMessage("cristian.ferreiro@udc.es", "Prueba de Event Rider", "Esta es una prueba");

    userDAO.update(dbUser);
    return new UserDTOPublic(dbUser);
  }

  @Transactional(readOnly = false)
  public void registerAccount(String name, String surname, String email, String password) throws UserEmailExistsException {
    registerAccount(name, surname, email, password, false);
  }

  @Transactional(readOnly = false)
  public void registerAccount(String name, String surname, String email, String password, boolean isAdmin) throws UserEmailExistsException {
    if (userDAO.findByEmail(email) != null) {
      throw new UserEmailExistsException(email);
    }

    User user = new User();
    String encryptedPassword = passwordEncoder.encode(password);

    user.setName(name);
    user.setSurname(surname);
    user.setEmail(email);
    user.setPassword(encryptedPassword);
    user.setAuthority(UserAuthority.USER);
    if (isAdmin) {
      user.setAuthority(UserAuthority.ADMIN);
    }

    userDAO.create(user);
  }

  @PreAuthorize("isAuthenticated()")
  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public void updateAccount(UserDTOPrivate user) throws NotFoundException {
    User dbUser = userDAO.findById(user.getId());
    if (dbUser == null) {
      throw new NotFoundException(user.getId().toString(), Event.class);
    }
    dbUser.setName(user.getName());
    dbUser.setSurname(user.getSurname());
    dbUser.setBiography(user.getBiography());

    userDAO.update(dbUser);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public UserDTOBase updateActive(Long id, boolean active) throws NotFoundException, OperationNotAllowed {
    User user = userDAO.findById(id);
    if (user == null) {
      throw new NotFoundException(id.toString(), User.class);
    }

    UserDTOPrivate currentUser = getCurrentUserWithAuthority();
    if (currentUser.getId().equals(user.getId())) {
      throw new OperationNotAllowed("The user cannot activate/deactivate itself");
    }

    user.setActive(active);
    userDAO.update(user);
    return new UserDTOBase(user);
  }

  public UserDTOPrivate getCurrentUserWithAuthority() {
    String currentUserEmail = SecurityUtils.getCurrentUserLogin();
    if (currentUserEmail != null) {
      return new UserDTOPrivate(userDAO.findByEmail(currentUserEmail));
    }
    return null;
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public void deleteById(Long id) throws NotFoundException, OperationNotAllowed {
    User user = userDAO.findById(id);
    if (user == null) {
      throw new NotFoundException(id.toString(), User.class);
    }

    UserDTOPrivate currentUser = getCurrentUserWithAuthority();
    if (currentUser.getId().equals(user.getId())) {
      throw new OperationNotAllowed("The user cannot delete itself");
    }

    userDAO.deleteById(id);
  }
}
