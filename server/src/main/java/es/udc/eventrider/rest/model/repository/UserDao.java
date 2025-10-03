package es.udc.eventrider.rest.model.repository;

import java.util.List;

import es.udc.eventrider.rest.model.domain.User;

public interface UserDao {
  List<User> findAll();

  User findById(Long id);

  User findByEmail(String email);

  void create(User user);

  void update(User user);

  void deleteById(Long id);
}
