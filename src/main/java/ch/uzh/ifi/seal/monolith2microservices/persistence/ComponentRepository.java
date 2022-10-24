package ch.uzh.ifi.seal.monolith2microservices.persistence;

import monolith2microservice.shared.models.graph.Component;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by gmazlami on 1/12/17.
 */
public interface ComponentRepository extends CrudRepository<Component, Long> {

}
