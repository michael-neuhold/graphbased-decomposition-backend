package monolith2microservice.outbound;

import monolith2microservice.shared.models.graph.Decomposition;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by gmazlami on 1/12/17.
 */
public interface DecompositionRepository extends CrudRepository<Decomposition, Long> {

    Decomposition findById(long id);

    List<Decomposition> findAll();

}
