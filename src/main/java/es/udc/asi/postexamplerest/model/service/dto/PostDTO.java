package es.udc.asi.postexamplerest.model.service.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.sun.istack.NotNull;

import es.udc.asi.postexamplerest.model.domain.Post;

public class PostDTO {
  private Long id;
  @NotEmpty
  private String body;
  @NotNull
  private UserDTOPublic author;
  private List<TagDTO> tags = new ArrayList<>();
  private Boolean hasImage = false;
  private LocalDateTime timestamp;

  public PostDTO() {
  }

  public PostDTO(Post post) {
    this.id = post.getId();
    this.body = post.getBody();
    this.author = new UserDTOPublic(post.getAuthor());
    post.getTags().forEach(t -> {
      this.tags.add(new TagDTO(t));
    });
    this.tags.sort(Comparator.comparing(TagDTO::getName));
    if (post.getImagePath() != null) {
      this.hasImage = true;
    }
    this.timestamp = post.getTimestamp();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public UserDTOPublic getAuthor() {
    return author;
  }

  public void setAuthor(UserDTOPublic author) {
    this.author = author;
  }

  public List<TagDTO> getTags() {
    return tags;
  }

  public void setTags(List<TagDTO> tags) {
    this.tags = tags;
  }

  public Boolean getHasImage() {
    return hasImage;
  }
	
  public LocalDateTime getTimestamp() {
    return timestamp;
  }
}
