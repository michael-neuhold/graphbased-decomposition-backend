package monolith2microservice.inbound.impl;

import monolith2microservice.inbound.RepositoryController;
import monolith2microservice.logic.decomposition.DecompositionLogic;
import monolith2microservice.logic.decomposition.graph.component.GraphRepresentation;
import monolith2microservice.logic.decomposition.graph.transformer.GraphvizTransformer;
import monolith2microservice.shared.models.DecompositionParameters;
import monolith2microservice.logic.repository.RepositoryLogic;
import monolith2microservice.shared.dto.RepositoryDto;
import monolith2microservice.shared.models.git.GitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("repositories")
public class RepositoryControllerImpl implements RepositoryController {

	@Autowired
	private RepositoryLogic repositoryLogic;

	@Autowired
	private DecompositionLogic decompositionLogic;

	@Override
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

	@Override
	@CrossOrigin
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<GitRepository>> listRepositories() {
		return ResponseEntity.ok(repositoryLogic.findAll());
	}

	@Override
	@CrossOrigin
	@RequestMapping(value="{repositoryId}", method=RequestMethod.GET)
	public ResponseEntity<GitRepository> getRepository(@PathVariable Long repositoryId) {
		return ResponseEntity.ok(repositoryLogic.findById(repositoryId));
	}

	@Override
	@CrossOrigin
	@RequestMapping(value="{repoId}/decomposition", method=RequestMethod.POST)
	public ResponseEntity<Set<GraphRepresentation>> decomposeRepository(@PathVariable Long repoId, @RequestBody DecompositionParameters decompositionDTO) {

		Set<GraphRepresentation> graph =
				DecompositionLogic.getGraphRepresentation(decompositionLogic.decompose(repoId, decompositionDTO));

		return ResponseEntity.ok(graph);
	}

	@CrossOrigin
	@ResponseBody
	@RequestMapping(value="{repoId}/decomposition/graphviz", method=RequestMethod.POST)
	public ResponseEntity<String> decomposeRepositoryImage(@PathVariable Long repoId, @RequestBody DecompositionParameters decompositionDTO) {

		Set<GraphRepresentation> graph =
				DecompositionLogic.getGraphRepresentation(decompositionLogic.decompose(repoId, decompositionDTO));

		return ResponseEntity.ok().body(GraphvizTransformer.create().transform(graph));
	}
    
}
