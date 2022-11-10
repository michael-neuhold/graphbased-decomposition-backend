package monolith2microservice.logic.evaluation;

import monolith2microservice.shared.dto.evaluation.QualityMetricDto;

import java.util.List;
import java.util.Set;

public interface DecompositionQualityLogic {

    List<Set<QualityMetricDto>> findMetrics();

}
