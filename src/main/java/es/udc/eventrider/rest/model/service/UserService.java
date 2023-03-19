package es.udc.eventrider.rest.model.service;

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
import es.udc.eventrider.rest.model.repository.UserDao;
import es.udc.eventrider.rest.model.service.dto.*;
import es.udc.eventrider.rest.model.service.util.ImageService;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.eventrider.rest.security.SecurityUtils;

import javax.management.InstanceNotFoundException;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class UserService {

  @Autowired
  private UserDao userDAO;

  @Autowired
  private ImageService imageService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public List<UserDTOWithEvents> findAllWithEvents() {
    Stream<UserDTOWithEvents> users = userDAO.findAll().stream().map(user -> new UserDTOWithEvents(user));
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

  public UserDTOWithEvents findByIdWithEvents(Long id) throws NotFoundException {
    User user = userDAO.findById(id);
    if (user == null) {
      throw new NotFoundException(id.toString(), User.class);
    }
    return new UserDTOWithEvents(user);
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

  @Transactional(readOnly = false)
  public void registerUser(String name, String surname, String email, String password) throws UserEmailExistsException {
    registerUser(name, surname, email, password, false);
  }

  @Transactional(readOnly = false)
  public void registerUser(String name, String surname, String email, String password, boolean isAdmin) throws UserEmailExistsException {
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

  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public UserDTOPublic updateActive(Long id, boolean active) throws NotFoundException, OperationNotAllowed {
    User user = userDAO.findById(id);
    if (user == null) {
      throw new NotFoundException(id.toString(), User.class);
    }

    UserDTOPrivate currentUser = getCurrentUserWithAuthority();
    if (currentUser.getId().equals(user.getId())) {
      throw new OperationNotAllowed("The user cannot activate/deactive itself");
    }

    user.setActive(active);
    userDAO.update(user);
    return new UserDTOPublic(user);
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
