package es.udc.eventrider.rest.model.service.dto;

import javax.validation.constraints.NotEmpty;

public class EventCategoryDTOCreate {
  @NotEmpty
  private String name;

  public EventCategoryDTOCreate(){
  }

  public EventCategoryDTOCreate(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
