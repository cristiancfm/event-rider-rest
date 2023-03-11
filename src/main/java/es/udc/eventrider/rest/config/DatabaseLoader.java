package es.udc.eventrider.rest.config;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.repository.EventDao;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
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

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

  @Autowired
  private Properties properties;

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
    User pedro = userDAO.findByEmail("pepe@mail.com");
    pedro.setActive(true);
    pedro.setImagePath("profile.jpg");
    pedro.setBiography("Me llamo Pepe y organizo eventos");
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

    List<String> eventImages = new ArrayList<>();
    eventImages.add("0.jpg");
    eventImages.add("1.jpg");
    eventImages.add("2.jpg");

    GeometryFactory geometryFactory = new GeometryFactory();
    Point point = geometryFactory.createPoint(new Coordinate(43.3613731, -8.3901377));

    Event event = new Event("Meisel 93", userDAO.findByEmail("pepe@mail.com"),
      LocalDateTime.of(2022, 10, 20, 9, 0),
      LocalDateTime.of(2023, 05, 10, 22, 0),
      point, "Puerto de A Coruña",
      String.format("Muestra de fotografías realizadas por Steven Meisel, centradas en el año 1993. " +
        "El evento podrá visitarse de forma gratuita en el puerto de A Coruña"),
      eventImages,
      Event.EventStatus.PUBLISHED);

    eventDao.create(event);


    eventImages = new ArrayList<>();
    eventImages.add("foo0.jpg");
    eventImages.add("foo1.jpg");
    event = new Event("Foo Fighters Tour", userDAO.findByEmail("pepe@mail.com"),
      LocalDateTime.of(2023, 10, 20, 22, 0),
      LocalDateTime.of(2023, 10, 20, 0, 0),
      point, "Coliseum de A Coruña",
      String.format("Foo Fighters Tour"),
      eventImages,
      Event.EventStatus.PUBLISHED);

    eventDao.create(event);
  }
}
