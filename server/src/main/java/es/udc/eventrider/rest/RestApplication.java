package es.udc.eventrider.rest;

import javax.annotation.PostConstruct;

import es.udc.eventrider.rest.model.exception.UserEmailExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

import es.udc.eventrider.rest.config.DatabaseLoader;

@SpringBootApplication
public class RestApplication {
  private final Logger logger = LoggerFactory.getLogger(RestApplication.class);

  @Autowired
  @Lazy
  private DatabaseLoader databaseLoader;

  public static void main(String[] args) {
    SpringApplication.run(RestApplication.class, args);
  }

  @PostConstruct
  public void init() throws InterruptedException {
    try {
      databaseLoader.loadData();
    } catch (UserEmailExistsException e) {
      logger.error(e.getMessage(), e);
    }
  }
}
