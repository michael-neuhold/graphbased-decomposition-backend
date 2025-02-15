package monolith2microservice.logic.evaluation.impl;

import monolith2microservice.Configs;
import monolith2microservice.logic.evaluation.DecompositionPerformanceLogic;
import monolith2microservice.outbound.DecompositionRepository;
import monolith2microservice.outbound.EvaluationMetricsRepository;
import monolith2microservice.shared.dto.evaluation.CouplingPerformanceMetricsDto;
import monolith2microservice.shared.models.evaluation.EvaluationMetrics;
import monolith2microservice.shared.models.git.GitRepository;
import monolith2microservice.shared.models.graph.Decomposition;
import monolith2microservice.util.git.GitClient;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DecompositionPerformanceLogicImpl implements DecompositionPerformanceLogic {

    @Autowired
    private Configs configs;

    @Autowired
    DecompositionRepository decompositionRepository;

    @Autowired
    EvaluationMetricsRepository evaluationMetricsRepository;

    @Override
    public List<CouplingPerformanceMetricsDto> findLogicalCouplingMetrics() {

        List<Decomposition> decompositions = decompositionRepository.findAll();

        List<Decomposition> decompositionsWithLogicalCoupling =
                decompositions.stream()
                        .filter(decomposition ->
                                decomposition.getParameters().isLogicalCoupling() &&
                                !decomposition.getParameters().isSemanticCoupling() &&
                                !decomposition.getParameters().isContributorCoupling() &&
                                !decomposition.getParameters().isDependencyCoupling())
                        .collect(Collectors.toList());

        return decompositionsWithLogicalCoupling.stream()
                .map(this::mapDecompositionToPerformanceMetrics)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouplingPerformanceMetricsDto> findContributorCouplingMetrics() {

        List<Decomposition> decompositions = decompositionRepository.findAll();

        List<Decomposition> decompositionsWithContributorCoupling =
                decompositions.stream()
                        .filter(decomposition ->
                                !decomposition.getParameters().isLogicalCoupling() &&
                                !decomposition.getParameters().isSemanticCoupling() &&
                                decomposition.getParameters().isContributorCoupling() &&
                                !decomposition.getParameters().isDependencyCoupling())
                        .collect(Collectors.toList());

        return decompositionsWithContributorCoupling.stream()
                .map(this::mapDecompositionToPerformanceMetrics)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouplingPerformanceMetricsDto> findSemanticCouplingMetrics() {

        List<Decomposition> decompositions = decompositionRepository.findAll();

        List<Decomposition> decompositionsWithSemanticCoupling =
                decompositions.stream()
                        .filter(decomposition ->
                                !decomposition.getParameters().isLogicalCoupling() &&
                                decomposition.getParameters().isSemanticCoupling() &&
                                !decomposition.getParameters().isContributorCoupling() &&
                                !decomposition.getParameters().isDependencyCoupling())
                        .collect(Collectors.toList());

        return decompositionsWithSemanticCoupling.stream()
                .map(this::mapDecompositionToPerformanceMetrics)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouplingPerformanceMetricsDto> findDependencyCouplingMetrics() {

        List<Decomposition> decompositions = decompositionRepository.findAll();

        List<Decomposition> decompositionsWithSemanticCoupling =
                decompositions.stream()
                        .filter(decomposition ->
                                !decomposition.getParameters().isLogicalCoupling() &&
                                !decomposition.getParameters().isSemanticCoupling() &&
                                !decomposition.getParameters().isContributorCoupling() &&
                                decomposition.getParameters().isDependencyCoupling())
                        .collect(Collectors.toList());

        return decompositionsWithSemanticCoupling.stream()
                .map(this::mapDecompositionToPerformanceMetrics)
                .collect(Collectors.toList());
    }

    private CouplingPerformanceMetricsDto mapDecompositionToPerformanceMetrics(Decomposition decomposition) {
        EvaluationMetrics metrics = evaluationMetricsRepository.findByDecompositionId(decomposition.getId());
        int historyLength = 0;
        int commitCount = 0;
        try {
            historyLength = computeHistoryLengthInDays(decomposition.getRepository());
            commitCount = computeCommitCount(decomposition.getRepository());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return CouplingPerformanceMetricsDto.of(decomposition.getRepository().getName(),
                commitCount, historyLength, metrics.getExecutionTimeMillisStrategy());
    }

    private int computeHistoryLengthInDays(GitRepository repo) throws Exception {
        GitClient gitClient = new GitClient(repo, configs);
        List<RevCommit> log = gitClient.getCommitLog();
        int startTime = log.get(log.size() - 1).getCommitTime();
        int endTime = log.get(0).getCommitTime();
        int historyLengthSeconds = endTime - startTime;
        return historyLengthSeconds / (60 * 60 * 24);
    }

    private int computeCommitCount(GitRepository repo) throws Exception {
        GitClient gitClient = new GitClient(repo, configs);
        return gitClient.getCommitLog().size();
    }

}
