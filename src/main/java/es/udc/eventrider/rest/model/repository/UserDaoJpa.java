package es.udc.eventrider.rest.model.repository;

import java.util.List;

import javax.persistence.TypedQuery;

import es.udc.eventrider.rest.model.domain.User;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import es.udc.eventrider.rest.model.repository.util.GenericDaoJpa;

@Repository
public class UserDaoJpa extends GenericDaoJpa implements UserDao {

  @Override
  public List<User> findAll() {
    return entityManager.createQuery("from User", User.class).getResultList();
  }

  @Override
  public User findById(Long id) {
    return entityManager.find(User.class, id);
  }

  @Override
  public User findByEmail(String email) {
    TypedQuery<User> query = entityManager.createQuery("from User u where u.email = :email", User.class)
        .setParameter("email", email);
    return DataAccessUtils.singleResult(query.getResultList());
  }

  @Override
  public void create(User user) {
    entityManager.persist(user);
  }

  @Override
  public void update(User user) {
    entityManager.merge(user);
  }

  @Override
  public void deleteById(Long id) {
    User user = findById(id);
    delete(user);
  }

  private void delete(User post) {
    entityManager.remove(post);
  }
}
