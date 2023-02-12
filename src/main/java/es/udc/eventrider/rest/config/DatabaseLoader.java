package es.udc.eventrider.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.eventrider.rest.model.domain.Post;
import es.udc.eventrider.rest.model.domain.Tag;
import es.udc.eventrider.rest.model.domain.User;
import es.udc.eventrider.rest.model.exception.UserLoginExistsException;
import es.udc.eventrider.rest.model.repository.PostDao;
import es.udc.eventrider.rest.model.repository.TagDao;
import es.udc.eventrider.rest.model.repository.UserDao;
import es.udc.eventrider.rest.model.service.UserService;

@Configuration
public class DatabaseLoader {
  @Autowired
  private UserDao userDAO;

  @Autowired
  private UserService userService;

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
  public void loadData() throws UserLoginExistsException, InterruptedException {
    userService.registerUser("pepe@mail.com", "pepe", true);
    userService.registerUser("maria@mail.com", "maria", true);
    userService.registerUser("laura@mail.com", "laura");
    userService.registerUser("pedro@mail.com", "pedro");
    User pedro = userDAO.findByEmail("pedro@mail.com");
    pedro.setActive(false);
    userDAO.update(pedro);
    userService.registerUser("ramon@mail.com", "ramon");

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
  }
}
