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
    userService.registerAccount("Cristian", "Ferreiro", "cristian.ferreiro@udc.es", "cristian");
    userService.registerAccount("Concello de A Coruña", null, "concello@acoruna.gal", "acoruna");
    userService.registerAccount("María", "Machado", "maria@mail.com", "maria");
    userService.registerAccount("Laura", "Lorenzo", "laura@mail.com", "laura");
    userService.registerAccount("Pedro", "Pascal", "pedro@mail.com", "pedro");

    User cristian = userDAO.findByEmail("cristian.ferreiro@udc.es");
    cristian.setImagePath("0.jpg");
    cristian.setBiography("Me llamo Cristian y organizo eventos");
    userDAO.update(cristian);

    User concelloCoruna = userDAO.findByEmail("concello@acoruna.gal");
    concelloCoruna.setImagePath("0.jpg");
    concelloCoruna.setBiography("Cuenta oficial del ayuntamiento de A Coruña");
    concelloCoruna.setAuthority(UserAuthority.USER_VERIFIED);
    userDAO.update(concelloCoruna);

    // Create event categories
    EventCategory competitionCategory = new EventCategory("Competition", EventCategory.EventCategoryStatus.PUBLISHED);
    eventCategoryDao.create(competitionCategory);

    EventCategory concertCategory = new EventCategory("Concert", EventCategory.EventCategoryStatus.PUBLISHED);
    eventCategoryDao.create(concertCategory);

    EventCategory eventCategory = new EventCategory("Conference", EventCategory.EventCategoryStatus.PUBLISHED);
    eventCategoryDao.create(eventCategory);

    EventCategory exhibitionIndoorsCategory = new EventCategory("Exhibition (indoors)", EventCategory.EventCategoryStatus.PUBLISHED);
    eventCategoryDao.create(exhibitionIndoorsCategory);

    EventCategory exhibitionOutdoorsCategory = new EventCategory("Exhibition (outdoors)", EventCategory.EventCategoryStatus.PUBLISHED);
    eventCategoryDao.create(exhibitionOutdoorsCategory);

    EventCategory socialActivityCategory = new EventCategory("Social Activity", EventCategory.EventCategoryStatus.PUBLISHED);
    eventCategoryDao.create(socialActivityCategory);

    EventCategory othersCategory = new EventCategory("Others", EventCategory.EventCategoryStatus.PUBLISHED);
    eventCategoryDao.create(othersCategory);

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
    Point point5 = geometryFactory.createPoint(new Coordinate(43.639811, -8.11439908));

    Event event = new Event("Meisel 93", userDAO.findByEmail("concello@acoruna.gal"),
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
      exhibitionOutdoorsCategory);
    eventDao.create(event);


    eventImages = new ArrayList<>();
    eventImages.add("0.jpg");
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
    event.getSubscribers().add(userDAO.findByEmail("maria@mail.com"));
    eventDao.update(event);


    eventImages = new ArrayList<>();
    eventImages.add("0.jpg");
    event = new Event("Feria del disco", userDAO.findByEmail("concello@acoruna.gal"),
      LocalDateTime.of(
        LocalDate.of(2022, 12, 8),
        LocalTime.of(16, 0)),
      LocalDateTime.of(
        LocalDate.of(2022, 12, 8),
        LocalTime.of(20, 0)),
      point3, "Plaza de María Pita",
      String.format("Feria de discos anual en el centro de A Coruña."),
      eventImages,
      Event.EventStatus.PUBLISHED,
      exhibitionIndoorsCategory);
    eventDao.create(event);

    eventImages = new ArrayList<>();
    eventImages.add("0.jpg");
    eventImages.add("1.jpg");
    event = new Event("Pantín Classic WSL", userDAO.findByEmail("concello@acoruna.gal"),
      LocalDateTime.of(
        LocalDate.of(2023, 8, 26),
        LocalTime.of(16, 0)),
      LocalDateTime.of(
        LocalDate.of(2023, 9, 3),
        LocalTime.of(20, 0)),
      point5, "Playa de Pantín (Ferrol)",
      String.format("Competición anual de la World Surf League. Este año vamos a subir el precio de las sudaderas!"),
      eventImages,
      Event.EventStatus.PUBLISHED,
      competitionCategory);
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
      exhibitionIndoorsCategory);
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
      exhibitionIndoorsCategory);
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
      exhibitionIndoorsCategory);
    event.setCancellationReason("Este es el motivo de cancelación.");
    eventDao.create(event);
  }
}
