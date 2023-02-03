package es.udc.asi.postexamplerest.model.repository;

import java.util.List;

import es.udc.asi.postexamplerest.model.domain.Post;
import es.udc.asi.postexamplerest.model.service.dto.PostSortType;

public interface PostDao {
  List<Post> findAll(String filter, String tag, PostSortType sort);

  Post findById(Long id);

  void create(Post post);

  void update(Post post);

  void deleteById(Long id);

  List<Post> findAllByTag(Long tagId);
}
