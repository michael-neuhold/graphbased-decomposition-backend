package monolith2microservice.inbound.impl;

import monolith2microservice.logic.decomposition.DecompositionLogic;
import monolith2microservice.logic.decomposition.graph.component.GraphRepresentation;
import monolith2microservice.shared.models.DecompositionParameters;
import monolith2microservice.shared.models.graph.Decomposition;
import monolith2microservice.logic.repository.RepositoryLogic;
import monolith2microservice.shared.dto.RepositoryDto;
import monolith2microservice.shared.models.git.GitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("repositories")
public class RepositoryControllerImpl {

	@Autowired
	private RepositoryLogic repositoryLogic;

	@Autowired
	private DecompositionLogic decompositionLogic;

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

		Decomposition decomposition = decompositionLogic.decompose(repoId, decompositionDTO);

		// convert to graph representation for frontend
		Set<GraphRepresentation> graph =
				decomposition.getServices().stream()
						.map(GraphRepresentation::from)
						.collect(Collectors.toSet());

		return ResponseEntity.ok(graph);
	}
    
}
