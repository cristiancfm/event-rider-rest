package es.udc.eventrider.rest.model.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.eventrider.rest.model.domain.Post;
import es.udc.eventrider.rest.model.repository.util.GenericDaoJpa;
import es.udc.eventrider.rest.model.service.dto.PostSortType;

import javax.persistence.TypedQuery;

@Repository
public class PostDaoJpa extends GenericDaoJpa implements PostDao {

  @Override
  public List<Post> findAll(String filter, String tag, PostSortType sort) {
    Boolean hasFilter = filter != null;
    Boolean hasTag = tag != null;
    Boolean isSorted = sort != null;

    String queryStr = "select p from Post p";

    if (isSorted && sort == PostSortType.AUTHOR_NAME) {
      queryStr += " join p.author a";
    }

    if (hasFilter || hasTag) {
      if (hasTag) {
        queryStr += " join p.tags t";
      }
      queryStr += " where ";
      if (hasFilter) {
        queryStr += "(p.body like :filter or p.author.login like :filter)";
      }
      if (hasFilter && hasTag) {
        queryStr += " and ";
      }
      if (hasTag) {
        queryStr += "t.name like :tag";
      }
    }

    String sortStr = "p.id asc";
    if (isSorted) {
      switch (sort) {
        case AUTHOR_NAME:
          sortStr = "a.login asc";
          break;
        case LESS_RECENT:
          sortStr = "p.timestamp asc";
          break;
        case MOST_RECENT:
          sortStr = "p.timestamp desc";
          break;
      }
    }
    queryStr += " order by " + sortStr;

    TypedQuery<Post> query = entityManager.createQuery(queryStr, Post.class);
    if (hasFilter) {
      query.setParameter("filter", "%" + filter + "%");
    }
    if (hasTag) {
      query.setParameter("tag", "%" + tag + "%");
    }

    return query.getResultList();
  }

  @Override
  public Post findById(Long id) {
    return entityManager.find(Post.class, id);
  }

  @Override
  public void create(Post post) {
    entityManager.persist(post);
  }

  @Override
  public void update(Post post) {
    entityManager.merge(post);
  }

  @Override
  public void deleteById(Long id) {
    Post post = findById(id);
    delete(post);
  }

  @Override
  public List<Post> findAllByTag(Long tagId) {
    return entityManager.createQuery("select p from Post p join p.tags pt where pt.id = :tagId", Post.class)
        .setParameter("tagId", tagId).getResultList();
  }

  private void delete(Post post) {
    entityManager.remove(post);
  }
}
