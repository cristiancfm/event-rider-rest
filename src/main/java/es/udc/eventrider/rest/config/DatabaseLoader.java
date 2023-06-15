package es.udc.eventrider.rest.config;

import es.udc.eventrider.rest.model.domain.*;
import es.udc.eventrider.rest.model.repository.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.eventrider.rest.model.exception.UserEmailExistsException;
import es.udc.eventrider.rest.model.service.UserService;

import java.time.*;
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
  private EventCategoryDao eventCategoryDao;

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
    userService.registerAccount("admin", null, "admin@eventrider.com", "admin", true);
    userService.registerAccount("cristian", "ferreiro", "cristian.ferreiro@udc.es", "cristian");
    userService.registerAccount("maría", "machado", "maria@mail.com", "maria");
    userService.registerAccount("laura", "lorenzo", "laura@mail.com", "laura");
    userService.registerAccount("pedro", "pascal", "pedro@mail.com", "pedro");
    userService.registerAccount("ramón", "rey", "ramon@mail.com", "ramon");
    User cristian = userDAO.findByEmail("cristian.ferreiro@udc.es");
    cristian.setImagePath("0.jpg");
    cristian.setBiography("Me llamo Cristian y organizo eventos");
    userDAO.update(cristian);

    Tag news = new Tag("news");
    Tag podcast = new Tag("podcast");
    Tag tech = new Tag("tech");

    tagDAO.create(news);
    tagDAO.create(podcast);
    tagDAO.create(tech);

    Post post = new Post("Texto del primer post", userDAO.findByEmail("cristian.ferreiro@udc.es"));
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

    // Create event categories
    EventCategory exhibitionCategory = new EventCategory("Exhibition", EventCategory.EventCategoryStatus.PUBLISHED);
    eventCategoryDao.create(exhibitionCategory);

    EventCategory concertCategory = new EventCategory("Concert", EventCategory.EventCategoryStatus.PUBLISHED);
    eventCategoryDao.create(concertCategory);

    // Create events
    List<String> eventImages = new ArrayList<>();
    eventImages.add("0.jpg");
    eventImages.add("1.jpg");
    eventImages.add("2.jpg");

    GeometryFactory geometryFactory = new GeometryFactory();
    Point point1 = geometryFactory.createPoint(new Coordinate(43.367308, -8.4021747));
    Point point2 = geometryFactory.createPoint(new Coordinate(43.339062, -8.4095960));
    Point point3 = geometryFactory.createPoint(new Coordinate(43.370922, -8.3959048));
    Point point4 = geometryFactory.createPoint(new Coordinate(43.371816, -8.40451451));

    Event event = new Event("Meisel 93", userDAO.findByEmail("cristian.ferreiro@udc.es"),
      LocalDateTime.of(
        LocalDate.of(2022, 10, 20),
        LocalTime.of(9, 0)),
      LocalDateTime.of(
        LocalDate.of(2023, 10, 10),
        LocalTime.of(22, 0)),
      point1, "Puerto de A Coruña",
      String.format("Muestra de fotografías realizadas por Steven Meisel, centradas en el año 1993. " +
        "El evento podrá visitarse de forma gratuita en el puerto de A Coruña"),
      eventImages,
      Event.EventStatus.PUBLISHED,
      exhibitionCategory);
    eventDao.create(event);


    eventImages = new ArrayList<>();
    eventImages.add("0.jpg");
    eventImages.add("1.jpg");
    event = new Event("Foo Fighters Tour", userDAO.findByEmail("cristian.ferreiro@udc.es"),
      LocalDateTime.of(
        LocalDate.of(2023, 10, 9),
        LocalTime.of(22, 0)),
      LocalDateTime.of(
        LocalDate.of(2023, 10, 9),
        LocalTime.of(0, 0)),
      point2, "Coliseum de A Coruña",
      String.format("Foo Fighters Tour"),
      eventImages,
      Event.EventStatus.PUBLISHED,
      concertCategory);
    eventDao.create(event);
    event.getSubscribers().add(userDAO.findByEmail("pepe@mail.com"));
    eventDao.update(event);


    eventImages = new ArrayList<>();
    eventImages.add("0.jpg");
    event = new Event("Feria del disco", userDAO.findByEmail("cristian.ferreiro@udc.es"),
      LocalDateTime.of(
        LocalDate.of(2022, 12, 8),
        LocalTime.of(16, 0)),
      LocalDateTime.of(
        LocalDate.of(2022, 12, 8),
        LocalTime.of(20, 0)),
      point3, "Plaza de María Pita",
      String.format("Feria de discos anual en el centro de A Coruña"),
      eventImages,
      Event.EventStatus.PUBLISHED,
      exhibitionCategory);
    eventDao.create(event);


    eventImages = new ArrayList<>();
    event = new Event("evento sin revisar", userDAO.findByEmail("cristian.ferreiro@udc.es"),
      LocalDateTime.of(
        LocalDate.of(2022, 12, 15),
        LocalTime.of(16, 0)),
      LocalDateTime.of(
        LocalDate.of(2022, 12, 15),
        LocalTime.of(20, 0)),
      point4, "blablabla",
      String.format("Descripción del evento sin revisar"),
      eventImages,
      Event.EventStatus.UNREVIEWED,
      exhibitionCategory);
    eventDao.create(event);


    eventImages = new ArrayList<>();
    event = new Event("evento rechazado", userDAO.findByEmail("cristian.ferreiro@udc.es"),
      LocalDateTime.of(
        LocalDate.of(2022, 12, 20),
        LocalTime.of(16, 0)),
      LocalDateTime.of(
        LocalDate.of(2022, 12, 20),
        LocalTime.of(20, 0)),
      point4, "blablabla",
      String.format("Descripción del evento rechazado"),
      eventImages,
      Event.EventStatus.REJECTED,
      exhibitionCategory);
    event.setAdminComments("Este es el comentario del administrador.");
    eventDao.create(event);


    eventImages = new ArrayList<>();
    event = new Event("evento cancelado", userDAO.findByEmail("cristian.ferreiro@udc.es"),
      LocalDateTime.of(
        LocalDate.of(2022, 12, 20),
        LocalTime.of(16, 0)),
      LocalDateTime.of(
        LocalDate.of(2022, 12, 20),
        LocalTime.of(20, 0)),
      point4, "blablabla",
      String.format("Descripción del evento cancelado"),
      eventImages,
      Event.EventStatus.CANCELLED,
      exhibitionCategory);
    event.setCancellationReason("Este es el motivo de cancelación.");
    eventDao.create(event);
  }
}
