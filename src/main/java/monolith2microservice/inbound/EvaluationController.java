package monolith2microservice.inbound;

import monolith2microservice.shared.dto.evaluation.PerformanceMetricsDto;
import monolith2microservice.shared.dto.evaluation.QualityMetricDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EvaluationController {

    ResponseEntity<PerformanceMetricsDto> exportPerformanceMetrics();

    ResponseEntity<List<Set<QualityMetricDto>>> exportQualityMetrics();

}
