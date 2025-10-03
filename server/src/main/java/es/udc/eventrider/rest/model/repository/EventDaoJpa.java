package es.udc.eventrider.rest.model.repository;

import es.udc.eventrider.rest.model.domain.Event;
import es.udc.eventrider.rest.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class EventDaoJpa extends GenericDaoJpa implements EventDao {

  @Override
  public List<Event> findAll(Map<String, String> query) {
    Boolean hasFilter = !query.isEmpty();


    String queryStr = "select * from Event e";

    if (hasFilter) {
      queryStr += " where ";
      String latitude = null;
      String longitude = null;
      String distance = null;
      for (String key : query.keySet()) {
        String value = query.get(key);

        if(Objects.equals(key, "title")) {
          queryStr += "lower(e." + key + ") like lower('%" + value + "%') and ";
        }

        if(Objects.equals(key, "latitude")) {
          latitude = value;
        }

        if(Objects.equals(key, "longitude")) {
          longitude = value;
        }

        if(Objects.equals(key, "date")) {
          queryStr += "cast(e.starting_date as date) <= '" + value
            + "' and cast(e.ending_date as date) >= '" + value + "' and ";
        }

        if(Objects.equals(key, "distance")) {
          distance = value;
        }

        if(Objects.equals(key, "category")) {
          queryStr += "e." + key + "_id = " + value + " and ";
        }
      }

      // apply location and distance filters
      if(latitude != null && longitude != null && distance != null){
        queryStr += "st_dwithin(e.point, " +
          "st_geogfromtext('POINT(" + latitude + " " + longitude + ")'), " + distance + ") = true and ";
      }

      if(queryStr.equals("select * from Event e where ")){  //no filters were applied
        // delete the " WHERE "
        queryStr = queryStr.substring(0, queryStr.length() - 7);
      } else {
        // delete the last " AND "
        queryStr = queryStr.substring(0, queryStr.length() - 5);
      }

    } else {
      // cancelled events are not shown by default
      queryStr += " where e.status != 'CANCELLED' ";
    }


    // order results by event title
    queryStr += " order by e.title";

    Query myQuery = entityManager.createNativeQuery(queryStr, Event.class);
    return myQuery.getResultList();
  }

  @Override
  public Event findById(Long id) {
    return entityManager.find(Event.class, id);
  }

  @Override
  public void create(Event event) {
    entityManager.persist(event);
  }

  @Override
  public void update(Event event) {
    entityManager.merge(event);
  }

  @Override
  public void deleteById(Long id) {
    Event event = findById(id);
    delete(event);
  }

  public void delete(Event event) {
    entityManager.remove(event);
  }
}
