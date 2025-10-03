package es.udc.eventrider.rest.web;

import javax.management.InstanceNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import es.udc.eventrider.rest.model.exception.ModelException;
import es.udc.eventrider.rest.model.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import es.udc.eventrider.rest.model.exception.UserEmailExistsException;
import es.udc.eventrider.rest.model.service.UserService;
import es.udc.eventrider.rest.model.service.dto.LoginDTO;
import es.udc.eventrider.rest.model.service.dto.UserDTOPrivate;
import es.udc.eventrider.rest.security.JWTToken;
import es.udc.eventrider.rest.security.TokenProvider;
import es.udc.eventrider.rest.web.exceptions.CredentialsAreNotValidException;
import es.udc.eventrider.rest.web.exceptions.RequestBodyNotValidException;
import org.springframework.web.multipart.MultipartFile;

/**
 * Este controlador va por separado que el UserResource porque se encarga de
 * tareas relacionadas con la autenticación, registro, etc.
 *
 * <p>
 * También permite a cada usuario logueado en la aplicación obtener información
 * de su cuenta
 */
@RestController
@RequestMapping("/api")
public class AccountResource {
  private final Logger logger = LoggerFactory.getLogger(AccountResource.class);

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserService userService;

  @PostMapping("/authenticate")
  public JWTToken authenticate(@Valid @RequestBody LoginDTO loginDTO) throws CredentialsAreNotValidException {

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        loginDTO.getEmail(), loginDTO.getPassword());
    try {
      Authentication authentication = authenticationManager.authenticate(authenticationToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = tokenProvider.createToken(authentication);
      return new JWTToken(jwt);
    } catch (AuthenticationException e) {
      logger.warn(e.getMessage(), e);
      throw new CredentialsAreNotValidException(e.getMessage());
    }
  }

  @GetMapping("/account")
  public UserDTOPrivate getAccount() {
    return userService.getCurrentUserWithAuthority();
  }

  @PostMapping("/register")
  public void registerAccount(@Valid @RequestBody UserDTOPrivate account, Errors errors)
    throws UserEmailExistsException, RequestBodyNotValidException {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }

    userService.registerAccount(account.getName(), account.getSurname(), account.getEmail(), account.getPassword());
  }

  @PostMapping("/account")
  public void updateAccount(@RequestBody UserDTOPrivate account)
    throws NotFoundException {

    userService.updateAccount(account);
  }
}
