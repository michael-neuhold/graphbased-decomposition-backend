package monolith2microservice.inbound.impl;

import monolith2microservice.inbound.EvaluationController;
import monolith2microservice.logic.evaluation.PerformanceMetricLogic;
import monolith2microservice.logic.evaluation.QualityMetricLogic;
import monolith2microservice.shared.dto.evaluation.PerformanceMetricsDto;
import monolith2microservice.shared.dto.evaluation.QualityMetricDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("evaluations")
public class EvaluationControllerImpl implements EvaluationController {

    @Autowired
    PerformanceMetricLogic performanceMetricLogic;

    @Autowired
    QualityMetricLogic qualityMetricLogic;

    @CrossOrigin
    @RequestMapping(value="/performance", method= RequestMethod.GET)
    public ResponseEntity<PerformanceMetricsDto> exportPerformanceMetrics() {
        return ResponseEntity.ok(PerformanceMetricsDto.of(
                performanceMetricLogic.getLogicalCouplingPerformanceMetric(),
                performanceMetricLogic.getContributorCouplingPerformanceMetric(),
                performanceMetricLogic.getSemanticCouplingPerformanceMetric(),
                performanceMetricLogic.getDependencyCouplingPerformanceMetric()
        ));
    }

    @CrossOrigin
    @RequestMapping(value="/quality", method= RequestMethod.GET)
    public ResponseEntity<List<QualityMetricDto>> exportQualityMetrics() {
        return ResponseEntity.ok(qualityMetricLogic.findAll());
    }

}
