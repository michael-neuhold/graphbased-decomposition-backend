package monolith2microservice.logic.evaluation;

import monolith2microservice.shared.models.evaluation.EvaluationMetrics;
import monolith2microservice.shared.models.evaluation.MicroserviceMetrics;
import monolith2microservice.shared.models.graph.Decomposition;
import monolith2microservice.outbound.EvaluationMetricsRepository;
import monolith2microservice.outbound.MicroserviceMetricsRepository;
import monolith2microservice.logic.evaluation.evaluation.DecompositionEvaluationLogic;
import monolith2microservice.logic.evaluation.evaluation.MicroserviceEvaluationLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component
public class EvaluationLogic {

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationLogic.class);

    @Autowired
    DecompositionEvaluationLogic decompositionEvaluationLogic;

    @Autowired
    MicroserviceEvaluationLogic microserviceEvaluationLogic;

    @Autowired
    MicroserviceMetricsRepository microserviceMetricsRepository;

    @Autowired
    EvaluationMetricsRepository evaluationMetricsRepository;

    @Async
    public void evaluate(Decomposition decomposition) {

        // microservice metrics
        List<MicroserviceMetrics> microserviceMetrics = calculateMicroserviceMetrics(decomposition);
        microserviceMetricsRepository.save(microserviceMetrics);

        // evaluation metrics
        EvaluationMetrics metrics = decompositionEvaluationLogic.computeMetrics(decomposition, microserviceMetrics);
        evaluationMetricsRepository.save(metrics);

    }

    private List<MicroserviceMetrics> calculateMicroserviceMetrics(Decomposition decomposition) {
        return decomposition.getServices().stream()
                .map(service -> microserviceEvaluationLogic
                        .from(service, decomposition.getRepository(), decomposition.getHistory()))
                .collect(Collectors.toList());
    }

}
