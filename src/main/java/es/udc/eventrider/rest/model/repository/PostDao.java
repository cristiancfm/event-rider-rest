package es.udc.eventrider.rest.model.repository;

import java.util.List;

import es.udc.eventrider.rest.model.domain.Post;
import es.udc.eventrider.rest.model.service.dto.PostSortType;

public interface PostDao {
  List<Post> findAll(String filter, String tag, PostSortType sort);

  Post findById(Long id);

  void create(Post post);

  void update(Post post);

  void deleteById(Long id);

  List<Post> findAllByTag(Long tagId);
}
