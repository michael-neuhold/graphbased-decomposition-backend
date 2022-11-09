package monolith2microservice.logic.decomposition.impl;

import monolith2microservice.outbound.DecompositionRepository;
import monolith2microservice.shared.dto.parameter.MonolithCouplingParametersDto;
import monolith2microservice.shared.models.DecompositionCouplingParameters;
import monolith2microservice.shared.models.git.GitRepository;
import monolith2microservice.shared.models.graph.Decomposition;
import monolith2microservice.logic.decomposition.engine.DecompositionService;
import monolith2microservice.logic.decomposition.DecompositionLogic;
import monolith2microservice.logic.evaluation.EvaluationLogic;
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
    private EvaluationLogic evaluationService;

    private final Logger logger = LoggerFactory.getLogger(DecompositionLogicImpl.class);

    @Override
    public Decomposition decompose(long repositoryId, DecompositionCouplingParameters decompositionDTO) {

        //find repository to be decomposed
        GitRepository repo = repositoryLogic.findById(repositoryId);

        Decomposition decomposition = decompositionService.decompose(repo, decompositionDTO);

        // Compute evaluation metrics
        evaluationService.evaluate(decomposition);

        //perform decomposition
        return decomposition;
    }

    @Override
    public Decomposition monolith(long id, MonolithCouplingParametersDto monolithCouplingParametersDto) {

        GitRepository gitRepository = repositoryLogic.findById(id);
        Decomposition decomposition = decompositionService.decompose(gitRepository, monolithCouplingParametersDto.toDecompositionParameters());

        // Compute evaluation metrics
        evaluationService.evaluate(decomposition);

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
