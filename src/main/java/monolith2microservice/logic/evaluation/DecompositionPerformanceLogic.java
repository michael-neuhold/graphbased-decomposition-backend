package monolith2microservice.logic.evaluation;

import monolith2microservice.shared.dto.evaluation.CouplingPerformanceMetricsDto;

import java.util.List;

public interface DecompositionPerformanceLogic {

    List<CouplingPerformanceMetricsDto> findLogicalCouplingMetrics();
    List<CouplingPerformanceMetricsDto> findContributorCouplingMetrics();
    List<CouplingPerformanceMetricsDto> findSemanticCouplingMetrics();
    List<CouplingPerformanceMetricsDto> findDependencyCouplingMetrics();

}
