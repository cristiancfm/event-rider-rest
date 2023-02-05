package es.udc.eventrider.rest.model.service.dto;

import javax.validation.constraints.NotEmpty;

import es.udc.eventrider.rest.model.domain.Tag;

public class TagDTO {
  private Long id;
  @NotEmpty
  private String name;

  public TagDTO() {
  }

  public TagDTO(Tag tag) {
    this.id = tag.getId();
    this.name = tag.getName();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
