package monolith2microservice.logic.evaluation;

import monolith2microservice.shared.dto.CouplingPerformanceMetricsDto;

import java.util.List;

public interface PerformanceMetricLogic {

    List<CouplingPerformanceMetricsDto> getLogicalCouplingPerformanceMetric();
    List<CouplingPerformanceMetricsDto> getContributorCouplingPerformanceMetric();
    List<CouplingPerformanceMetricsDto> getSemanticCouplingPerformanceMetric();

}
