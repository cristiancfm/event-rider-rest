package es.udc.eventrider.rest.model.repository;

import java.util.List;

import es.udc.eventrider.rest.model.domain.Tag;

public interface TagDao {

  List<Tag> findAll();

  Tag findById(Long id);

  void create(Tag tag);

  void update(Tag tag);

  void delete(Tag tag);
}
