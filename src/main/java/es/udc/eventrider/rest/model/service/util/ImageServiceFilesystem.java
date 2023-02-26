package es.udc.eventrider.rest.model.service.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import es.udc.eventrider.rest.config.Properties;
import es.udc.eventrider.rest.model.exception.ModelException;
import es.udc.eventrider.rest.model.service.dto.ImageDTO;

@Service
public class ImageServiceFilesystem implements ImageService {

  @Autowired
  Properties properties;

  private Path rootLoc;

  @Override
  public String saveImage(Entity entity, MultipartFile file, Long id) throws ModelException {
    if (file.isEmpty()) {
      throw new ModelException("No se ha enviado ningún fichero");
    }
    String filename = StringUtils.cleanPath(file.getOriginalFilename());

    String entityFolder = getEntityFolder(entity);
    if(entityFolder == null) {
      throw new ModelException("No se ha especificado el tipo de entidad para guardar la imagen");
    }

    int imgId;

    try (InputStream inputStream = file.getInputStream()) {
      Path basePath = getRootLoc().resolve(entityFolder + "/" + id + "/" + "images");
      Files.createDirectories(basePath);

      imgId = new File(basePath.toString()).list().length;
      Files.copy(inputStream, basePath.resolve(imgId + getExtension(filename)),
        StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
      throw new ModelException("Problema procesando el fichero");
    }

    return imgId + getExtension(filename);
  }

  @Override
  public ImageDTO getImage(Entity entity, String imagePath, Long id) throws ModelException {
    try {

      String entityFolder = getEntityFolder(entity);
      if (entityFolder == null){
        throw new ModelException("No se ha especificado el tipo de entidad para guardar la imagen");
      }

      Path path = getRootLoc().resolve(entityFolder + "/" + id + "/" + "images" + "/" + imagePath);
      InputStream is = new FileInputStream(path.toString());
      byte[] buffer = new byte[1024];
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      int len;
      while ((len = is.read(buffer)) > -1) {
        os.write(buffer, 0, len);
      }
      InputStream imageIs = new ByteArrayInputStream(os.toByteArray());
      os.flush();
      is.close();
      return new ImageDTO(imageIs, getImageMediaType(imagePath), imagePath);
    } catch (IOException e) {
      e.printStackTrace();
      throw new ModelException("Problem while getting the image");
    }
  }

  private String getEntityFolder(Entity entity) {
    if(entity == Entity.EVENT) {
      return "events";
    }
    else if(entity == Entity.POST) {
      return "posts";
    }
    return null;
  }

  private Path getRootLoc() {
    if (rootLoc == null)
      this.rootLoc = Paths.get(properties.getImagesPath());
    return rootLoc;
  }

  private String getExtension(String filename) {
    return filename.substring(filename.lastIndexOf("."));
  }

  private String getImageMediaType(String filename) {
    String extension = getExtension(filename);
    switch (extension) {
    case ".jpg":
    case ".jpeg":
      return MediaType.IMAGE_JPEG_VALUE;
    case ".png":
      return MediaType.IMAGE_PNG_VALUE;
    case ".gif":
      return MediaType.IMAGE_GIF_VALUE;
    default:
      return MediaType.IMAGE_JPEG_VALUE;
    }
  }
}
