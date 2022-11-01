package monolith2microservice.outbound;

import monolith2microservice.shared.models.DecompositionCouplingParameters;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by gmazlami on 1/17/17.
 */
public interface ParametersRepository extends CrudRepository<DecompositionCouplingParameters, Long> {

}
