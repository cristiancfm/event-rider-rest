package es.udc.eventrider.rest.model.repository;

import es.udc.eventrider.rest.model.domain.Event;

import java.util.List;
import java.util.Map;

public interface EventDao {
  List<Event> findAll(Map<String, String> query);

  Event findById(Long id);

  void create(Event event);

  void update(Event event);

  void deleteById(Long id);
}
