package es.udc.eventrider.rest.model.service.util;

import org.springframework.web.multipart.MultipartFile;

import es.udc.eventrider.rest.model.exception.ModelException;
import es.udc.eventrider.rest.model.service.dto.ImageDTO;

public interface ImageService {

  String saveImage(MultipartFile file, Long id) throws ModelException;

  ImageDTO getImage(String imagePath, Long id) throws ModelException;
}
