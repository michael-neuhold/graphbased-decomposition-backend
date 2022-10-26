package monolith2microservice.inbound.impl;

import monolith2microservice.logic.evaluation.QualityMetricLogic;
import monolith2microservice.logic.evaluation.impl.MetricsExportService;
import monolith2microservice.logic.evaluation.impl.QualityMetricLogicImpl;
import monolith2microservice.logic.evaluation.reporting.TextFileReport;
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
    MetricsExportService metricsExportService;

    @Autowired
    QualityMetricLogic qualityMetricLogic;

    @CrossOrigin
    @RequestMapping(value="/performance", method= RequestMethod.GET)
    public ResponseEntity<String> exportPerformanceMetrics() throws Exception {
        TextFileReport.writeToFile(metricsExportService.exportLogicalCouplingPerformanceMetrics(), "logicalCouplingPerformance.txt");
        TextFileReport.writeToFile(metricsExportService.exportSemanticCouplingPerformanceMetrics(), "semanticCouplingPerformance.txt");
        TextFileReport.writeToFile(metricsExportService.exportContributorCouplingPerformanceMetrics(), "contributorCouplingPerformance.txt");
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value="/quality", method= RequestMethod.GET)
    public ResponseEntity<List<QualityMetricDto>> exportQualityMetrics() throws Exception{
        return ResponseEntity.ok(qualityMetricLogic.findAll());
    }

}
