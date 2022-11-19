package monolith2microservice.logic.decomposition.impl;

import monolith2microservice.logic.evaluation.EvaluationLogic;
import monolith2microservice.outbound.DecompositionRepository;
import monolith2microservice.shared.dto.parameter.MonolithCouplingParametersDto;
import monolith2microservice.shared.models.DecompositionCouplingParameters;
import monolith2microservice.shared.models.git.GitRepository;
import monolith2microservice.shared.models.graph.Decomposition;
import monolith2microservice.logic.decomposition.engine.DecompositionService;
import monolith2microservice.logic.decomposition.DecompositionLogic;
import monolith2microservice.logic.repository.RepositoryLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DecompositionLogicImpl implements DecompositionLogic {

    @Autowired
    private DecompositionService decompositionService;

    @Autowired
    private RepositoryLogic repositoryLogic;

    @Autowired
    private DecompositionRepository decompositionRepository;

    @Autowired
    private EvaluationLogic evaluationLogic;

    private final Logger LOGGER = LoggerFactory.getLogger(DecompositionLogicImpl.class);

    @Override
    public Decomposition decompose(long repositoryId, DecompositionCouplingParameters decompositionDTO) {
        LOGGER.info("Decompose of repository #{}", repositoryId);

        //find repository to be decomposed
        GitRepository gitRepository = repositoryLogic.findById(repositoryId);

        // decompose
        Decomposition decomposition = decompositionService.decompose(
                gitRepository,
                decompositionDTO);

        // Compute evaluation metrics
        evaluationLogic.evaluate(decomposition);

        //perform decomposition
        return decomposition;
    }

    @Override
    public Decomposition monolithication(long repositoryId, MonolithCouplingParametersDto monolithCouplingParametersDto) {
        LOGGER.info("Monolithication of repository #{}", repositoryId);

        // find repository for monolithication
        GitRepository gitRepository = repositoryLogic.findById(repositoryId);

        // monolithify^^
        Decomposition decomposition = decompositionService.decompose(
                gitRepository,
                monolithCouplingParametersDto.toDecompositionParameters());

        // Compute evaluation metrics
        evaluationLogic.evaluate(decomposition);

        //perform decomposition
        return decomposition;

    }

    @Override
    public Decomposition findById(long decompositionId) {
        return decompositionRepository.findById(decompositionId);
    }

    @Override
    public List<Decomposition> findAll() {
        return decompositionRepository.findAll();
    }

}
