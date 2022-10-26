package monolith2microservice.shared.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import monolith2microservice.shared.models.evaluation.EvaluationMetrics;
import monolith2microservice.shared.models.graph.Decomposition;

@Setter
@Getter
@Builder
public class QualityMetricDto {

    private String repositoryName;
    private boolean isLogicalCoupling;
    private boolean isContributorCoupling;
    private boolean isSemanticCoupling;
    private double averageClassCount;
    private double averageLoc;
    private double contributorOverlapping;
    private double contributorsPerMicroservice;
    private double similarity;

    public static QualityMetricDto of(Decomposition decomposition, EvaluationMetrics metric) {
        return QualityMetricDto.builder()
                .repositoryName(decomposition.getRepository().getName())
                .isLogicalCoupling(decomposition.getParameters().isLogicalCoupling())
                .isContributorCoupling(decomposition.getParameters().isContributorCoupling())
                .isSemanticCoupling(decomposition.getParameters().isSemanticCoupling())
                .averageClassCount(metric.getAverageClassNumber())
                .averageLoc(metric.getAverageLoc())
                .contributorOverlapping(metric.getContributorOverlapping())
                .contributorsPerMicroservice(metric.getContributorsPerMicroservice())
                .similarity(metric.getSimilarity())
                .build();
    }

}
