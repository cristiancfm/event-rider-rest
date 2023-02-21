package es.udc.eventrider.rest.model.repository;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class EventDaoJpa extends GenericDaoJpa implements EventDao {

  @Override
  public List<Event> findAll() {
    String queryStr = "select e from Event e";

    TypedQuery<Event> query = entityManager.createQuery(queryStr, Event.class);

    return query.getResultList();
  }

  @Override
  public Event findById(Long id) {
    return entityManager.find(Event.class, id);
  }

  @Override
  public void create(Event event) {
    entityManager.persist(event);
  }

  @Override
  public void update(Event event) {
    entityManager.merge(event);
  }

  @Override
  public void deleteById(Long id) {
    Event event = findById(id);
    delete(event);
  }

  public void delete(Event event) {
    entityManager.remove(event);
  }
}
