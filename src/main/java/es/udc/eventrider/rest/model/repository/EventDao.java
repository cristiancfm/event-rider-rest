package es.udc.eventrider.rest.model.repository;

import es.udc.eventrider.rest.model.domain.Event;

import java.util.List;

public interface EventDao {
  List<Event> findAll();

  Event findById(Long id);

  void create(Event event);

  void update(Event event);

  void deleteById(Long id);
}
