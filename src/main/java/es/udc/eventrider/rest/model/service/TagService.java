package es.udc.eventrider.rest.model.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import es.udc.eventrider.rest.model.domain.Post;
import es.udc.eventrider.rest.model.domain.Tag;
import es.udc.eventrider.rest.model.exception.NotFoundException;
import es.udc.eventrider.rest.model.repository.PostDao;
import es.udc.eventrider.rest.model.repository.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.eventrider.rest.model.service.dto.TagDTO;

@Service
@Transactional(readOnly = true)
public class TagService {

  @Autowired
  private TagDao tagDAO;

  @Autowired
  private PostDao postDAO;

  public List<TagDTO> findAll() {
    return tagDAO.findAll().stream().sorted(Comparator.comparing(Tag::getName)).map(TagDTO::new)
        .collect(Collectors.toList());
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public void deleteById(Long id) throws NotFoundException {
    List<Post> posts = postDAO.findAllByTag(id);
    Tag theTag = tagDAO.findById(id);
    if (theTag == null) {
      throw new NotFoundException(id.toString(), Tag.class);
    }
    posts.forEach(post -> {
      post.getTags().remove(theTag);
      postDAO.update(post);
    });
    tagDAO.delete(theTag);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public TagDTO create(TagDTO tag) {
    Tag bdTag = new Tag(tag.getName());
    tagDAO.create(bdTag);
    return new TagDTO(bdTag);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public TagDTO update(TagDTO tag) throws NotFoundException {
    Tag bdTag = tagDAO.findById(tag.getId());
    if (bdTag == null) {
      throw new NotFoundException(tag.getId().toString(), Tag.class);
    }
    bdTag.setName(tag.getName());
    tagDAO.update(bdTag);
    return new TagDTO(bdTag);
  }
}