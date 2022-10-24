package monolith2microservice.logic.decomposition.engine;

import monolith2microservice.shared.models.DecompositionParameters;
import monolith2microservice.logic.decomposition.graph.LinearGraphCombination;
import monolith2microservice.logic.decomposition.graph.MSTGraphClusterer;
import monolith2microservice.shared.models.couplings.BaseCoupling;
import monolith2microservice.shared.models.git.ChangeEvent;
import monolith2microservice.shared.models.git.GitRepository;
import monolith2microservice.shared.models.graph.Component;
import monolith2microservice.shared.models.graph.Decomposition;
import monolith2microservice.outbound.ClassNodeRepository;
import monolith2microservice.outbound.ComponentRepository;
import monolith2microservice.outbound.DecompositionRepository;
import monolith2microservice.outbound.ParametersRepository;
import monolith2microservice.logic.evaluation.evaluation.MicroserviceEvaluationService;
import monolith2microservice.logic.decomposition.util.git.HistoryService;
import monolith2microservice.logic.evaluation.reporting.TextFileReport;
import monolith2microservice.logic.decomposition.engine.impl.cc.ContributorCouplingEngine;
import monolith2microservice.logic.decomposition.engine.impl.lc.LogicalCouplingEngine;
import monolith2microservice.logic.decomposition.engine.impl.sc.SemanticCouplingEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gmazlami on 12/15/16.
 */
@Service
public class DecompositionService {

    private static final Logger logger = LoggerFactory.getLogger(DecompositionService.class);

    @Autowired
    ContributorCouplingEngine contributorCouplingEngine;

    @Autowired
    SemanticCouplingEngine semanticCouplingEngine;

    @Autowired
    LogicalCouplingEngine logicalCouplingEngine;

    @Autowired
    ClassNodeRepository classNodeRepository;

    @Autowired
    ComponentRepository componentRepository;

    @Autowired
    DecompositionRepository decompositionRepository;

    @Autowired
    ParametersRepository parametersRepository;

    @Autowired
    HistoryService historyService;


    @Autowired
    MicroserviceEvaluationService microserviceEvaluationService;

    public Decomposition decompose(GitRepository repository, DecompositionParameters parameters){

        try {

            List<ChangeEvent> history = computeHistory(repository);

            logger.info("DECOMPOSITION-------------------------");
            logger.info("STRATEGIES: Logical Coupling: " + parameters.isLogicalCoupling() + " Semantic Coupling: " + parameters.isSemanticCoupling() + "  Contributor Coupling: " + parameters.isContributorCoupling());
            logger.info("PARAMETERS: History Interval Size (s): " + parameters.getIntervalSeconds() + " Target Number of Services: " + parameters.getNumServices());

            long strategyStartTimestamp = System.currentTimeMillis();

            List<BaseCoupling> couplings =
                    LinearGraphCombination.create(repository, history, parameters)
                            .withLogicalCoupling(
                                    parameters.isLogicalCoupling(),
                                    logicalCouplingEngine)
                            .withContributorCoupling(
                                    parameters.isContributorCoupling(),
                                    contributorCouplingEngine)
                            .withSemanticCoupling(
                                    parameters.isSemanticCoupling(),
                                    semanticCouplingEngine)
                            .generate();

            long strategyExecutionTimeMillis = System.currentTimeMillis() - strategyStartTimestamp;

            long clusteringStartTimestamp = System.currentTimeMillis();

            Set<Component> components = MSTGraphClusterer.clusterWithSplit(couplings, parameters.getSizeThreshold(), parameters.getNumServices());

            long clusteringExecutionTimeMillis = System.currentTimeMillis() - clusteringStartTimestamp;

            logger.info("Saving decomposition to database.");

            components.forEach(c -> {
                c.getNodes().forEach(n -> {
                    classNodeRepository.save(n);
                });
                componentRepository.save(c);
                logger.info(c.toString());
            });

            parametersRepository.save(parameters);

            Decomposition decomposition = new Decomposition();
            decomposition.setComponents(components);
            decomposition.setRepository(repository);
            decomposition.setParameters(parameters);
            decomposition.setHistory(history);
            decomposition.setClusteringTime(clusteringExecutionTimeMillis);
            decomposition.setStrategyTime(strategyExecutionTimeMillis);
            decompositionRepository.save(decomposition);

            logger.info("Saved all decomposition info and components to database!");

            TextFileReport.generate(repository, components);

            return decomposition;

        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            Decomposition emptyDecomposition = new Decomposition();
            emptyDecomposition.setComponents(new HashSet<>());
            emptyDecomposition.setRepository(repository);
            return emptyDecomposition;
        }
    }

    private List<ChangeEvent> computeHistory(GitRepository repo) throws Exception{
        List<ChangeEvent> history = historyService.computeChangeEvents(repo);
        return historyService.cleanHistory(history);
    }

}
