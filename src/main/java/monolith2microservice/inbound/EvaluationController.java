package monolith2microservice.inbound;

import monolith2microservice.shared.dto.evaluation.PerformanceMetricsDto;
import monolith2microservice.shared.dto.evaluation.QualityMetricDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EvaluationController {

    ResponseEntity<PerformanceMetricsDto> exportPerformanceMetrics();

    ResponseEntity<List<QualityMetricDto>> exportQualityMetrics();

}
