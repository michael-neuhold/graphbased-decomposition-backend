package monolith2microservice.outbound;

import monolith2microservice.shared.models.evaluation.MicroserviceMetrics;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Genc on 15.01.2017.
 */
public interface MicroserviceMetricsRepository extends CrudRepository<MicroserviceMetrics,Long> {

}
