package es.udc.eventrider.rest.web;

import es.udc.eventrider.rest.model.exception.ModelException;
import es.udc.eventrider.rest.model.exception.NotFoundException;
import es.udc.eventrider.rest.model.service.EventService;
import es.udc.eventrider.rest.model.service.dto.EventDTO;
import es.udc.eventrider.rest.model.service.dto.ImageDTO;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.InstanceNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventResource {

  @Autowired
  private EventService eventService;

  @GetMapping
  public List<EventDTO> findAll(@RequestParam(required = false) Map<String, String> query) {
    return eventService.findAll(query);
  }

  @GetMapping("/{id}")
  public EventDTO findOne(@PathVariable Long id) throws NotFoundException {
    return eventService.findById(id);
  }

  @GetMapping("/{id}/image/{imgId}")
  @ResponseStatus(HttpStatus.OK)
  public void getEventImageById(@PathVariable Long id, HttpServletResponse response, @PathVariable Long imgId )
    throws InstanceNotFoundException, ModelException, IOException {
    ImageDTO image = eventService.getEventImageById(id, imgId);

    if (image == null) {
      response.sendError(404);
      return;
    }

    try {
      response.setContentType(image.getMediaType());
      response.setHeader("Content-disposition", "filename=" + image.getFilename());
      IOUtils.copy(image.getInputStream(), response.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @PostMapping("/{id}/image")
  @ResponseStatus(HttpStatus.OK)
  public void saveEventImageById(@PathVariable Long id, @RequestParam MultipartFile file, HttpServletResponse response)
    throws InstanceNotFoundException, ModelException {
    eventService.saveEventImageById(id, file);
  }
}
