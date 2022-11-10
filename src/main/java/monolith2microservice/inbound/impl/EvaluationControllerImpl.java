package monolith2microservice.inbound.impl;

import monolith2microservice.inbound.EvaluationController;
import monolith2microservice.logic.evaluation.DecompositionPerformanceLogic;
import monolith2microservice.logic.evaluation.DecompositionQualityLogic;
import monolith2microservice.shared.dto.evaluation.PerformanceMetricsDto;
import monolith2microservice.shared.dto.evaluation.QualityMetricDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("evaluations")
public class EvaluationControllerImpl implements EvaluationController {

    private final Logger LOGGER = LoggerFactory.getLogger(EvaluationControllerImpl.class);

    @Autowired
    DecompositionPerformanceLogic decompositionPerformanceLogic;

    @Autowired
    DecompositionQualityLogic decompositionQualityLogic;

    @CrossOrigin
    @RequestMapping(value="/performance", method= RequestMethod.GET)
    public ResponseEntity<PerformanceMetricsDto> exportPerformanceMetrics() {
        LOGGER.info("|-> exportPerformanceMetrics");
        return ResponseEntity.ok(PerformanceMetricsDto.of(
                decompositionPerformanceLogic.getLogicalCouplingPerformanceMetric(),
                decompositionPerformanceLogic.getContributorCouplingPerformanceMetric(),
                decompositionPerformanceLogic.getSemanticCouplingPerformanceMetric(),
                decompositionPerformanceLogic.getDependencyCouplingPerformanceMetric()
        ));
    }

    @CrossOrigin
    @RequestMapping(value="/quality", method= RequestMethod.GET)
    public ResponseEntity<List<Set<QualityMetricDto>>> exportQualityMetrics() {
        LOGGER.info("|-> exportQualityMetrics");
        return ResponseEntity.ok(decompositionQualityLogic.findMetrics());
    }

}
