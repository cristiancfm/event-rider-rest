package es.udc.asi.postexamplerest.model.repository;

import java.util.List;

import es.udc.asi.postexamplerest.model.domain.Tag;

public interface TagDao {

  List<Tag> findAll();

  Tag findById(Long id);

  void create(Tag tag);

  void update(Tag tag);

  void delete(Tag tag);
}
