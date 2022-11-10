package monolith2microservice.logic.evaluation.impl;

import monolith2microservice.logic.evaluation.DecompositionQualityLogic;
import monolith2microservice.outbound.DecompositionRepository;
import monolith2microservice.outbound.EvaluationMetricsRepository;
import monolith2microservice.shared.dto.evaluation.QualityMetricDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

@Component
public class DecompositionQualityLogicImpl implements DecompositionQualityLogic {

    @Autowired
    EvaluationMetricsRepository evaluationMetricsRepository;

    @Autowired
    DecompositionRepository decompositionRepository;

    @Override
    public List<Set<QualityMetricDto>> findMetrics() {
        return new ArrayList<>(evaluationMetricsRepository.findAll().stream()
                .map(metric -> QualityMetricDto.of(decompositionRepository.findById(metric.getDecomposition().getId()), metric))
                .collect(groupingBy(QualityMetricDto::getRepositoryId, toSet())).values());
    }

}
