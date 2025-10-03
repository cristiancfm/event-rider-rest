package es.udc.eventrider.rest.model.service.util;

import org.springframework.web.multipart.MultipartFile;

import es.udc.eventrider.rest.model.exception.ModelException;
import es.udc.eventrider.rest.model.service.dto.ImageDTO;

public interface ImageService {

  enum Entity{
    EVENT,
    POST,
    USER,
  }

  String saveImage(Entity entity, MultipartFile file, Long id) throws ModelException;

  ImageDTO getImage(Entity entity, String imagePath, Long id) throws ModelException;

  void deleteImage(Entity entity, String imagePath, Long id) throws ModelException;
}
