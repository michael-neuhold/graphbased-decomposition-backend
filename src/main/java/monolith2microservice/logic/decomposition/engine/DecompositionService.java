package monolith2microservice.logic.decomposition.engine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import monolith2microservice.logic.decomposition.engine.impl.dc.DependencyCouplingEngine;
import monolith2microservice.shared.models.DecompositionCouplingParameters;
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
import monolith2microservice.logic.evaluation.impl.evaluators.ServiceEvaluator;
import monolith2microservice.logic.decomposition.util.git.HistoryService;
import monolith2microservice.logic.decomposition.engine.impl.cc.ContributorCouplingEngine;
import monolith2microservice.logic.decomposition.engine.impl.lc.LogicalCouplingEngine;
import monolith2microservice.logic.decomposition.engine.impl.sc.SemanticCouplingEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class DecompositionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecompositionService.class);

    @Autowired
    ContributorCouplingEngine contributorCouplingEngine;

    @Autowired
    SemanticCouplingEngine semanticCouplingEngine;

    @Autowired
    LogicalCouplingEngine logicalCouplingEngine;

    @Autowired
    DependencyCouplingEngine dependencyCouplingEngine;

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
    ServiceEvaluator microserviceEvaluationService;

    @Getter
    @Setter
    @AllArgsConstructor
    private static class ResultWithExecutionTime<T> {
        private T result;
        private long executionTime;
    }

    public Decomposition decompose(GitRepository repository, DecompositionCouplingParameters parameters) {

        try {
            List<ChangeEvent> history = computeHistory(repository);

            logStrategyInformation(parameters);

            // calculate couplings
            ResultWithExecutionTime<List<BaseCoupling>> couplingsWithExecutionTime =
                    calculateCouplings(repository, history, parameters);

            // calculate clusters
            ResultWithExecutionTime<Set<Component>> calculatedComponentsWithExecutionTime =
                    calculateComponents(couplingsWithExecutionTime.result, parameters);

            // build decomposition result
            Decomposition decomposition =
                    Decomposition.builder()
                            .services(calculatedComponentsWithExecutionTime.result)
                            .repository(repository)
                            .parameters(parameters)
                            .history(history)
                            .clusteringTime(calculatedComponentsWithExecutionTime.executionTime)
                            .strategyTime(couplingsWithExecutionTime.executionTime)
                            .build();

            // store decomposition information
            storeDecompositionInformation(decomposition);

            return decomposition;
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    private void logStrategyInformation(DecompositionCouplingParameters parameters) {
        LOGGER.info("DECOMPOSITION-------------------------");
        LOGGER.info(String.format(
                "\nSTRATEGIES: \n\tLogical Coupling: %s\n\tSemantic Coupling: %s\n\tContributor Coupling: %s\n\tDependency Coupling: %s\n",
                parameters.isLogicalCoupling(),
                parameters.isSemanticCoupling(),
                parameters.isContributorCoupling(),
                parameters.isDependencyCoupling())
        );
        LOGGER.info(String.format(
                "\nPARAMETERS: \n\tHistory Interval Size (s): %s\n\tTarget Number of Services: %s\n",
                parameters.getIntervalSeconds(),
                parameters.getNumServices())
        );
    }

    private void storeDecompositionInformation(Decomposition decomposition) {
        LOGGER.info("Store decomposition information to database");
        decomposition.getServices().forEach(component -> {
            classNodeRepository.save(component.getNodes());
            componentRepository.save(component);
            LOGGER.info(component.toString());
        });
        parametersRepository.save(decomposition.getParameters());
        decompositionRepository.save(decomposition);
    }

    private ResultWithExecutionTime<List<BaseCoupling>> calculateCouplings(GitRepository repository, List<ChangeEvent> history,
                                                                           DecompositionCouplingParameters parameters) {

        LOGGER.info("Begin of coupling calculation");
        long strategyStartTimestamp = System.currentTimeMillis();
        List<BaseCoupling> couplings =
                LinearGraphCombination.createWith(repository, history, parameters)
                        .withLogicalCoupling(
                                parameters.isLogicalCoupling(),
                                logicalCouplingEngine)
                        .withContributorCoupling(
                                parameters.isContributorCoupling(),
                                contributorCouplingEngine)
                        .withSemanticCoupling(
                                parameters.isSemanticCoupling(),
                                semanticCouplingEngine)
                        .withDependencyCoupling(
                                parameters.isDependencyCoupling(),
                                dependencyCouplingEngine)
                        .generate();
        long strategyExecutionTimeMillis = System.currentTimeMillis() - strategyStartTimestamp;
        LOGGER.info("End of coupling calculation");

        return new ResultWithExecutionTime<>(couplings, strategyExecutionTimeMillis);
    }

    private ResultWithExecutionTime<Set<Component>> calculateComponents(List<BaseCoupling> couplings,
                                                                        DecompositionCouplingParameters parameters) {
        LOGGER.info("Begin of cluster algorithm");
        long clusteringStartTimestamp = System.currentTimeMillis();
        Set<Component> components =
                MSTGraphClusterer.clusterWithSplit(
                        couplings,
                        parameters.getSizeThreshold(),
                        parameters.getNumServices());
        long clusteringExecutionTimeMillis = System.currentTimeMillis() - clusteringStartTimestamp;
        LOGGER.info("End of cluster algorithm");

        return new ResultWithExecutionTime<>(components, clusteringExecutionTimeMillis);
    }

    private List<ChangeEvent> computeHistory(GitRepository repo) throws Exception{
        List<ChangeEvent> history = historyService.computeChangeEvents(repo);
        return historyService.cleanHistory(history);
    }

}
