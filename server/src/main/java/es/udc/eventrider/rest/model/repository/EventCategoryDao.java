package es.udc.eventrider.rest.model.repository;

import es.udc.eventrider.rest.model.domain.EventCategory;
import java.util.List;

public interface EventCategoryDao {
  List<EventCategory> findAll();

  EventCategory findById(Long id);

  void create(EventCategory eventCategory);

  void update(EventCategory eventCategory);

  void deleteById(Long id);
}
