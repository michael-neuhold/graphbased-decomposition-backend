package monolith2microservice.logic.evaluation.impl;

import monolith2microservice.logic.evaluation.QualityMetricLogic;
import monolith2microservice.outbound.DecompositionRepository;
import monolith2microservice.outbound.EvaluationMetricsRepository;
import monolith2microservice.shared.dto.evaluation.QualityMetricDto;
import monolith2microservice.shared.models.graph.Decomposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QualityMetricLogicImpl implements QualityMetricLogic {

    @Autowired
    EvaluationMetricsRepository evaluationMetricsRepository;

    @Autowired
    DecompositionRepository decompositionRepository;

    @Override
    public List<QualityMetricDto> findAll() {
        return evaluationMetricsRepository.findAll().stream().map(metric -> {
            Decomposition decomposition = decompositionRepository.findById(metric.getDecomposition().getId());
            return QualityMetricDto.of(decomposition, metric);
        }).collect(Collectors.toList());
    }

}
