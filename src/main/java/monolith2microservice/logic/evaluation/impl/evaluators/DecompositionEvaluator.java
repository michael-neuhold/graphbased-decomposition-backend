package monolith2microservice.logic.evaluation.impl.evaluators;

import monolith2microservice.shared.models.evaluation.EvaluationMetrics;
import monolith2microservice.shared.models.evaluation.MicroserviceMetrics;
import monolith2microservice.shared.models.graph.Component;
import monolith2microservice.shared.models.graph.Decomposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component
public class DecompositionEvaluator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecompositionEvaluator.class);

    @Autowired
    SerivceSimilarityEvaluator similarityService;

    public EvaluationMetrics computeMetrics(Decomposition decomposition, List<MicroserviceMetrics> microserviceMetrics) {
        EvaluationMetrics metrics = new EvaluationMetrics();
        try {
            metrics.setDecomposition(decomposition);
            metrics.setContributorsPerMicroservice(computeAvgContributorsPerService(microserviceMetrics));
            metrics.setContributorOverlapping(computeAvgContributorOverlapping(microserviceMetrics));
            metrics.setAverageLoc(computeAvgLinesOfCode(microserviceMetrics));
            metrics.setAverageClassNumber(computeAvgClassesInService(microserviceMetrics));
            metrics.setSimilarity(computeServiceSimilarity(decomposition));
            metrics.setExecutionTimeMillisClustering(decomposition.getClusteringTime());
            metrics.setExecutionTimeMillisStrategy(decomposition.getStrategyTime());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return metrics;
    }

    private double computeAvgContributorsPerService(List<MicroserviceMetrics> microserviceMetrics) {
        return microserviceMetrics.stream()
                .map(MicroserviceMetrics::getNumOfContributors)
                .mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private double computeAvgContributorOverlapping(List<MicroserviceMetrics> microserviceMetrics) {
        List<Double> overlappingContributors = microserviceMetrics.stream()
                .flatMap(firstServiceMetrics -> microserviceMetrics.stream()
                        .map(secondServiceMetrics ->
                                getNumberOfOverlappingContributors(
                                        firstServiceMetrics.getContributors(),
                                        secondServiceMetrics.getContributors())))
                .collect(Collectors.toList());
        return overlappingContributors.stream().mapToDouble(Double::doubleValue).sum() / overlappingContributors.size();
    }

    private double computeAvgLinesOfCode(List<MicroserviceMetrics> microserviceMetrics) {
        return microserviceMetrics.stream()
                .map(metric -> (double) metric.getSizeInLoc())
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    private double computeAvgClassesInService(List<MicroserviceMetrics> microserviceMetrics) {
        return microserviceMetrics.stream()
                .map(metric -> (double) metric.getSizeInClasses())
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    private double computeServiceSimilarity(Decomposition decomposition) throws IOException {
        if (decomposition.getServices().size() > 1) {
            List<Double> similarities = new ArrayList<>();
            for (Component firstService : decomposition.getServices()) {
                for (Component secondService : decomposition.getServices()) {
                    if (!Objects.equals(firstService.getId(), secondService.getId())) {
                        similarities.add(similarityService.computeServiceSimilarity(decomposition.getRepository(), firstService, secondService));
                    }
                }
            }
            return similarities.stream().mapToDouble(Double::doubleValue).sum() / similarities.size();
        } else {
            return 1d;
        }
    }

    public double getNumberOfOverlappingContributors(Set<String> firstSet, Set<String> secondSet) {
        Set<String> intersection = new HashSet<>(firstSet);
        intersection.retainAll(secondSet);
        return intersection.size();
    }
}
