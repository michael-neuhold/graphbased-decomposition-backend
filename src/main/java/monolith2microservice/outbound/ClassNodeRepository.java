package monolith2microservice.outbound;

import monolith2microservice.shared.models.graph.ClassNode;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by gmazlami on 1/12/17.
 */
public interface ClassNodeRepository extends CrudRepository<ClassNode,Long> {


}
