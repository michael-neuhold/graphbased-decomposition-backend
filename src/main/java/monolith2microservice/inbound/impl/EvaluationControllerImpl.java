package monolith2microservice.inbound.impl;

import monolith2microservice.logic.evaluation.PerformanceMetricLogic;
import monolith2microservice.logic.evaluation.QualityMetricLogic;
import monolith2microservice.logic.evaluation.impl.MetricsExportService;
import monolith2microservice.logic.evaluation.impl.QualityMetricLogicImpl;
import monolith2microservice.logic.evaluation.reporting.TextFileReport;
import monolith2microservice.shared.dto.PerformanceMetricsDto;
import monolith2microservice.shared.dto.QualityMetricDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("evaluations")
public class EvaluationControllerImpl {

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
                // TODO: fix semantic coupling performance metrics
                performanceMetricLogic.getSemanticCouplingPerformanceMetric()
        ));
    }

    @CrossOrigin
    @RequestMapping(value="/quality", method= RequestMethod.GET)
    public ResponseEntity<List<QualityMetricDto>> exportQualityMetrics() {
        return ResponseEntity.ok(qualityMetricLogic.findAll());
    }

}
