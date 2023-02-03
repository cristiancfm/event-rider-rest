package es.udc.asi.postexamplerest.model.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.management.InstanceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import es.udc.asi.postexamplerest.model.domain.Post;
import es.udc.asi.postexamplerest.model.exception.ModelException;
import es.udc.asi.postexamplerest.model.exception.NotFoundException;
import es.udc.asi.postexamplerest.model.exception.OperationNotAllowed;
import es.udc.asi.postexamplerest.model.repository.PostDao;
import es.udc.asi.postexamplerest.model.repository.TagDao;
import es.udc.asi.postexamplerest.model.repository.UserDao;
import es.udc.asi.postexamplerest.model.service.dto.ImageDTO;
import es.udc.asi.postexamplerest.model.service.dto.PostDTO;
import es.udc.asi.postexamplerest.model.service.dto.PostSortType;
import es.udc.asi.postexamplerest.model.service.dto.UserDTOPrivate;
import es.udc.asi.postexamplerest.model.service.util.ImageService;
import es.udc.asi.postexamplerest.security.SecurityUtils;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class PostService {
  @Autowired
  private PostDao postDAO;
  
  @Autowired
  private ImageService imageService;

  @Autowired
  private UserDao userDAO;

  @Autowired
  private TagDao tagDAO;

  @Autowired
  private UserService userService;

  public List<PostDTO> findAll(String filter, String tag, PostSortType sort) {
    Stream<Post> posts = postDAO.findAll(filter, tag, sort).stream();
    if (!SecurityUtils.getCurrentUserIsAdmin()) {
      posts = posts.filter(p -> p.getAuthor().isActive());
    }
    return posts.map(post -> new PostDTO(post)).collect(Collectors.toList());
  }

  public PostDTO findById(Long id) throws NotFoundException {
    Post post = postDAO.findById(id);
    if (post == null || !SecurityUtils.getCurrentUserIsAdmin() && !post.getAuthor().isActive()) {
      throw new NotFoundException(id.toString(), Post.class);
    }
    return new PostDTO(post);
  }

  // Con estas anotaciones evitamos que usuarios no autorizados accedan a ciertas
  // funcionalidades
  @PreAuthorize("isAuthenticated()")
  @Transactional(readOnly = false)
  public PostDTO create(PostDTO post) throws OperationNotAllowed {
    Post bdPost = new Post(post.getBody());
    UserDTOPrivate currentUser = userService.getCurrentUserWithAuthority();
    if (currentUser.getAuthority().equals("ADMIN")) {
      bdPost.setAuthor(userDAO.findById(post.getAuthor().getId()));
    } else {
      if (post.getAuthor() != null) {
        throw new OperationNotAllowed("Non admin users cannot set the author of a post (property author should be null)");
      }
      bdPost.setAuthor(userDAO.findById(currentUser.getId()));
    }
    if (post.getTags() != null) {
      post.getTags().forEach(tag -> {
        bdPost.getTags().add(tagDAO.findById(tag.getId()));
      });
    }
    postDAO.create(bdPost);
    return new PostDTO(bdPost);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public PostDTO update(PostDTO post) throws NotFoundException {
    Post bdPost = postDAO.findById(post.getId());
    if (bdPost == null) {
      throw new NotFoundException(post.getId().toString(), Post.class);
    }
    bdPost.setBody(post.getBody());
    bdPost.setAuthor(userDAO.findById(post.getAuthor().getId()));
    bdPost.getTags().clear();
    post.getTags().forEach(tag -> {
      bdPost.getTags().add(tagDAO.findById(tag.getId()));
    });
    postDAO.update(bdPost);
    return new PostDTO(bdPost);
  }

  @PreAuthorize("isAuthenticated()")
  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public void deleteById(Long id) throws NotFoundException, OperationNotAllowed {
    Post post = postDAO.findById(id);
    if (post == null) {
      throw new NotFoundException(id.toString(), Post.class);
    }

    UserDTOPrivate currentUser = userService.getCurrentUserWithAuthority();
    if (!currentUser.getId().equals(post.getAuthor().getId())) {
      throw new OperationNotAllowed("Current user is not the post creator");
    }

    LocalDateTime halfAnHourAgo = LocalDateTime.now().minusMinutes(30);
    if (post.getTimestamp().isBefore(halfAnHourAgo)) {
      throw new OperationNotAllowed("More than half an hour has passed since the post creation");
    }

    postDAO.deleteById(id);
  }

  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public void savePostImageById(Long id, MultipartFile file) throws InstanceNotFoundException, ModelException {

    Post post = postDAO.findById(id);
    if (post == null)
      throw new NotFoundException(id.toString(), Post.class);

    String filePath = imageService.saveImage(file, post.getId());
    post.setImagePath(filePath);
    postDAO.update(post);
  }

  public ImageDTO getPostImageById(Long id) throws InstanceNotFoundException, ModelException {
    Post post = postDAO.findById(id);
    if (post == null || !SecurityUtils.getCurrentUserIsAdmin() && !post.getAuthor().isActive()) {
      throw new NotFoundException(id.toString(), Post.class);
    }
    if (post.getImagePath() == null) {
      return null;
    }
    return imageService.getImage(post.getImagePath(), post.getId());
  }
}
