package monolith2microservice.logic.decomposition.impl;

import ch.uzh.ifi.seal.monolith2microservices.models.DecompositionParameters;
import ch.uzh.ifi.seal.monolith2microservices.models.git.GitRepository;
import ch.uzh.ifi.seal.monolith2microservices.models.graph.Decomposition;
import monolith2microservice.inbound.impl.RepositoryControllerImpl;
import monolith2microservice.logic.decomposition.engine.DecompositionService;
import monolith2microservice.logic.decomposition.DecompositionLogic;
import monolith2microservice.logic.decomposition.graph.component.GraphRepresentation;
import monolith2microservice.logic.evaluation.EvaluationService;
import monolith2microservice.logic.repository.RepositoryLogic;
import monolith2microservice.shared.dto.DecompositionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DecompositionLogicImpl implements DecompositionLogic {

    @Autowired
    private DecompositionService decompositionService;

    @Autowired
    private RepositoryLogic repositoryLogic;

    @Autowired
    private EvaluationService evaluationService;

    private final Logger logger = LoggerFactory.getLogger(DecompositionLogicImpl.class);

    @Override
    public Decomposition decompose(long repositoryId, DecompositionParameters decompositionDTO) {

        //find repository to be decomposed
        GitRepository repo = repositoryLogic.findById(repositoryId);

        Decomposition decomposition = decompositionService.decompose(repo,decompositionDTO);

        // Compute evaluation metrics
        evaluationService.performEvaluation(decomposition);

        //perform decomposition
        return decomposition;
    }

}
