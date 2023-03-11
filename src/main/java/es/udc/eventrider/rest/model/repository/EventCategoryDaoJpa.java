package es.udc.eventrider.rest.model.repository;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.domain.EventCategory;
import es.udc.eventrider.rest.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class EventCategoryDaoJpa extends GenericDaoJpa implements EventCategoryDao {

  @Override
  public List<EventCategory> findAll() {
    String queryStr = "select e from EventCategory e";

    TypedQuery<EventCategory> query = entityManager.createQuery(queryStr, EventCategory.class);

    return query.getResultList();
  }

  @Override
  public EventCategory findById(Long id) {
    return entityManager.find(EventCategory.class, id);
  }

  @Override
  public void create(EventCategory eventCategory) {
    entityManager.persist(eventCategory);
  }

  @Override
  public void update(EventCategory eventCategory) {
    entityManager.merge(eventCategory);
  }

  @Override
  public void deleteById(Long id) {
    EventCategory eventCategory = findById(id);
    delete(eventCategory);
  }

  public void delete(EventCategory eventCategory) {
    entityManager.remove(eventCategory);
  }
}
