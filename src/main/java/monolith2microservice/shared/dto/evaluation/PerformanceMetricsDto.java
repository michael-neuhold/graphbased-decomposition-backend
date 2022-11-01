package monolith2microservice.shared.dto.evaluation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class PerformanceMetricsDto {

    private List<CouplingPerformanceMetricsDto> logicalCouplingPerformanceMetric;
    private List<CouplingPerformanceMetricsDto> contributorCouplingPerformanceMetric;
    private List<CouplingPerformanceMetricsDto> semanticCouplingPerformanceMetric;
    private List<CouplingPerformanceMetricsDto> dependencyCouplingPerformanceMetric;

    public static PerformanceMetricsDto of(List<CouplingPerformanceMetricsDto> logicalCouplingPerformanceMetric,
                                           List<CouplingPerformanceMetricsDto> contributorCouplingPerformanceMetric,
                                           List<CouplingPerformanceMetricsDto> semanticCouplingPerformanceMetric,
                                           List<CouplingPerformanceMetricsDto> dependencyCouplingPerformanceMetric) {

        return PerformanceMetricsDto.builder()
                .contributorCouplingPerformanceMetric(contributorCouplingPerformanceMetric)
                .logicalCouplingPerformanceMetric(logicalCouplingPerformanceMetric)
                .semanticCouplingPerformanceMetric(semanticCouplingPerformanceMetric)
                .dependencyCouplingPerformanceMetric(dependencyCouplingPerformanceMetric)
                .build();
    }

}
