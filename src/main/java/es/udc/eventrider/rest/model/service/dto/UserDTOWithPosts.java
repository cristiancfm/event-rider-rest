package es.udc.eventrider.rest.model.service.dto;

import java.util.ArrayList;
import java.util.List;

import es.udc.eventrider.rest.model.domain.User;

public class UserDTOWithPosts {
  private Long id;
  private String login;
  private List<PostDTO> posts = new ArrayList<>();

  public UserDTOWithPosts() {
  }

  public UserDTOWithPosts(User user) {
    this.id = user.getId();
    this.login = user.getEmail();
    user.getPosts().forEach(p -> {
      this.posts.add(new PostDTO(p));
    });
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public List<PostDTO> getPosts() {
    return posts;
  }

  public void setPosts(List<PostDTO> posts) {
    this.posts = posts;
  }
}
