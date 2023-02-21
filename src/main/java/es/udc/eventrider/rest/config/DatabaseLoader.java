package es.udc.eventrider.rest.config;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.repository.EventDao;
import io.github.sebasbaumh.postgis.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.eventrider.rest.model.domain.Post;
import es.udc.eventrider.rest.model.domain.Tag;
import es.udc.eventrider.rest.model.domain.User;
import es.udc.eventrider.rest.model.exception.UserEmailExistsException;
import es.udc.eventrider.rest.model.repository.PostDao;
import es.udc.eventrider.rest.model.repository.TagDao;
import es.udc.eventrider.rest.model.repository.UserDao;
import es.udc.eventrider.rest.model.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Configuration
public class DatabaseLoader {
  @Autowired
  private UserDao userDAO;

  @Autowired
  private UserService userService;

  @Autowired
  private EventDao eventDao;

  @Autowired
  private PostDao postDAO;

  @Autowired
  private TagDao tagDAO;

  /*
   * Para hacer que la carga de datos sea transacional, hay que cargar el propio
   * objeto como un bean y lanzar el método una vez cargado, ya que en el
   * PostConstruct (ni similares) se tienen en cuenta las anotaciones de
   * transaciones.
   */
  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public void loadData() throws UserEmailExistsException {
    userService.registerUser("pepe", "pérez", "pepe@mail.com", "pepe", true);
    userService.registerUser("maría", "machado", "maria@mail.com", "maria", true);
    userService.registerUser("laura", "lorenzo", "laura@mail.com", "laura");
    userService.registerUser("pedro", "pascal", "pedro@mail.com", "pedro");
    User pedro = userDAO.findByEmail("pedro@mail.com");
    pedro.setActive(false);
    userDAO.update(pedro);
    userService.registerUser("ramón", "rey", "ramon@mail.com", "ramon");

    Tag news = new Tag("news");
    Tag podcast = new Tag("podcast");
    Tag tech = new Tag("tech");

    tagDAO.create(news);
    tagDAO.create(podcast);
    tagDAO.create(tech);

    Post post = new Post("Texto del primer post", userDAO.findByEmail("pepe@mail.com"));
    post.getTags().add(news);
    post.getTags().add(podcast);
    postDAO.create(post);
    //Thread.sleep(2000);
    post = new Post("Texto del segundo post", userDAO.findByEmail("maria@mail.com"));
    post.getTags().add(news);
    post.getTags().add(tech);
    postDAO.create(post);
    //Thread.sleep(2000);
    post = new Post("Texto del tercero post", userDAO.findByEmail("maria@mail.com"));
    postDAO.create(post);
    //Thread.sleep(2000);
    post = new Post("Texto del cuarto post", userDAO.findByEmail("maria@mail.com"));
    postDAO.create(post);
    //Thread.sleep(2000);
    post = new Post("Texto del quinto post", userDAO.findByEmail("pepe@mail.com"));
    postDAO.create(post);
    //Thread.sleep(2000);
    post = new Post("Texto del sexto post", userDAO.findByEmail("pepe@mail.com"));
    postDAO.create(post);

    ArrayList<String> eventImages = new ArrayList<>();
    eventImages.add("path/to/img1");
    eventImages.add("path/to/img2");
    Event event = new Event("Meisel 93", userDAO.findByEmail("pepe@mail.com"),
                            LocalDateTime.of(2022, 10, 20, 9, 0),
                            LocalDateTime.of(2023, 05, 10, 22, 0),
                            "43.3613731, -8.3901377", "Puerto de A Coruña",
                  "Evento de Meisel 93",
                            new ArrayList<String>(),
                            Event.EventStatus.UNREVIEWED);

    eventDao.create(event);
  }
}
