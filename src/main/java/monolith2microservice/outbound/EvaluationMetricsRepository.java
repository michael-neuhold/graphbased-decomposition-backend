package monolith2microservice.outbound;

import monolith2microservice.shared.models.evaluation.EvaluationMetrics;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Genc on 15.01.2017.
 */
public interface EvaluationMetricsRepository extends CrudRepository<EvaluationMetrics,Long> {

    EvaluationMetrics findByDecompositionId(long decompositionId);

    List<EvaluationMetrics> findAll();

}
