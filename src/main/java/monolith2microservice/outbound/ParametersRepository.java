package monolith2microservice.outbound;

import monolith2microservice.shared.models.DecompositionParameters;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by gmazlami on 1/17/17.
 */
public interface ParametersRepository extends CrudRepository<DecompositionParameters, Long> {

}
