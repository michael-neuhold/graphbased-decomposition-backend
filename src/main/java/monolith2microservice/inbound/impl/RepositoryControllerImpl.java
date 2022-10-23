package monolith2microservice.inbound.impl;

import monolith2microservice.logic.decomposition.graph.component.GraphRepresentation;
import ch.uzh.ifi.seal.monolith2microservices.models.DecompositionParameters;
import ch.uzh.ifi.seal.monolith2microservices.models.graph.Decomposition;
import monolith2microservice.logic.decomposition.engine.DecompositionService;
import ch.uzh.ifi.seal.monolith2microservices.services.evaluation.EvaluationService;
import monolith2microservice.logic.repository.RepositoryLogic;
import monolith2microservice.shared.dto.RepositoryDto;
import ch.uzh.ifi.seal.monolith2microservices.models.git.GitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("repositories")
public class RepositoryControllerImpl {

	private final Logger logger = LoggerFactory.getLogger(RepositoryControllerImpl.class);

	@Autowired
	private DecompositionService decompositionService;

	@Autowired
	private EvaluationService evaluationService;

	@Autowired
	private RepositoryLogic repositoryLogic;

	@CrossOrigin
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<GitRepository> addRepository(@RequestBody RepositoryDto repositoryDto) {
		GitRepository storedRepository = null;
		try {
			storedRepository = repositoryLogic.add(repositoryDto);
		} catch (Exception exception) {
			// TODO
		}
		return ResponseEntity.ok(storedRepository);
    }

	@CrossOrigin
	@RequestMapping(method=RequestMethod.GET)
	public List<GitRepository> listRepositories() {
		return repositoryLogic.findAll();
	}

	@CrossOrigin
	@RequestMapping(value="{repositoryId}", method=RequestMethod.GET)
	public GitRepository getRepository(@PathVariable Long repositoryId) {
		return repositoryLogic.findById(repositoryId);
	}

	@CrossOrigin
	@RequestMapping(value="{repoId}/decomposition", method=RequestMethod.POST)
	public ResponseEntity<Set<GraphRepresentation>> decomposition(@PathVariable Long repoId, @RequestBody DecompositionParameters decompositionDTO) {
		logger.info(decompositionDTO.toString());

		//find repository to be decomposed
		GitRepository repo = repositoryLogic.findById(repoId);

		//perform decomposition
		Decomposition decomposition = decompositionService.decompose(repo,decompositionDTO);

		// convert to graph representation for frontend
		Set<GraphRepresentation> graph = decomposition.getServices().stream().map(GraphRepresentation::from).collect(Collectors.toSet());

		// Compute evaluation metrics
		evaluationService.performEvaluation(decomposition);

		return new ResponseEntity<>(graph, HttpStatus.OK);
	}
    
}
