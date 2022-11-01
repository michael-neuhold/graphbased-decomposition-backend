package monolith2microservice.logic.evaluation.impl;

import monolith2microservice.Configs;
import monolith2microservice.shared.models.evaluation.EvaluationMetrics;
import monolith2microservice.shared.models.graph.Decomposition;
import monolith2microservice.outbound.EvaluationMetricsRepository;
import monolith2microservice.outbound.DecompositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gmazlami on 1/26/17.
 */
@Service
public class MetricsExportService {

    private final String newLine = "\n";

    @Autowired
    private DecompositionRepository decompositionRepository;

    @Autowired
    private EvaluationMetricsRepository evaluationMetricsRepository;

    @Autowired
    private Configs configs;



    public String exportSemanticCouplingPerformanceMetrics() throws Exception {
        StringBuilder sb = new StringBuilder();

        List<Decomposition> decompositions = decompositionRepository.findAll().stream().filter(decomposition -> {
            return (!decomposition.getParameters().isLogicalCoupling() &&
                    !decomposition.getParameters().isContributorCoupling() &&
                    decomposition.getParameters().isSemanticCoupling());}).collect(Collectors.toList());

        for(Decomposition decomposition: decompositions){
            EvaluationMetrics metrics = evaluationMetricsRepository.findByDecompositionId(decomposition.getId());
            String row = createSemanticCouplingPerformanceRow(decomposition, metrics, ',');

            sb.append(row);
            sb.append(newLine);
        }
        return sb.toString();
    }

    private String createSemanticCouplingPerformanceRow(Decomposition decomposition, EvaluationMetrics metrics, char separator){
        StringBuilder sb = new StringBuilder();
        sb.append(decomposition.getRepository().getName());
        sb.append(separator);
        sb.append(metrics.getExecutionTimeMillisStrategy());
        return sb.toString();
    }

}
