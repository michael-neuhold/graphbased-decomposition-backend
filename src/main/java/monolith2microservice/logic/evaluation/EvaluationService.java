package monolith2microservice.logic.evaluation;

import monolith2microservice.shared.models.evaluation.EvaluationMetrics;
import monolith2microservice.shared.models.evaluation.MicroserviceMetrics;
import monolith2microservice.shared.models.graph.Component;
import monolith2microservice.shared.models.graph.Decomposition;
import monolith2microservice.outbound.DecompositionMetricsRepository;
import monolith2microservice.outbound.MicroserviceMetricsRepository;
import ch.uzh.ifi.seal.monolith2microservices.services.evaluation.DecompositionEvaluationService;
import ch.uzh.ifi.seal.monolith2microservices.services.evaluation.MicroserviceEvaluationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Genc on 15.01.2017.
 */
@Service
public class EvaluationService {

    private Logger logger = LoggerFactory.getLogger(EvaluationService.class);

    @Autowired
    DecompositionEvaluationService decompositionEvaluationService;

    @Autowired
    MicroserviceEvaluationService microserviceEvaluationService;

    @Autowired
    MicroserviceMetricsRepository microserviceMetricsRepository;

    @Autowired
    DecompositionMetricsRepository decompositionMetricsRepository;


    @Async
    public void performEvaluation(Decomposition decomposition){
        try{
            List<MicroserviceMetrics> microserviceMetrics = computeMicroserviceMetrics(decomposition);
            microserviceMetricsRepository.save(microserviceMetrics);

            EvaluationMetrics metrics = decompositionEvaluationService.computeMetrics(decomposition, microserviceMetrics);
            decompositionMetricsRepository.save(metrics);
        }catch (IOException ioe){
            logger.error(ioe.getMessage());
        }
    }


    private List<MicroserviceMetrics> computeMicroserviceMetrics(Decomposition decomposition) throws IOException{
        List<MicroserviceMetrics> microserviceMetrics = new ArrayList<>();
        for(Component microservice: decomposition.getServices()){
            microserviceMetrics.add(microserviceEvaluationService.from(microservice, decomposition.getRepository(), decomposition.getHistory()));
        }
        return microserviceMetrics;
    }

}
