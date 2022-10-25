package monolith2microservice.inbound;

import monolith2microservice.logic.decomposition.graph.component.GraphRepresentation;
import monolith2microservice.shared.dto.RepositoryDto;
import monolith2microservice.shared.models.DecompositionParameters;
import monolith2microservice.shared.models.git.GitRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface RepositoryController {
    ResponseEntity<GitRepository> addRepository(RepositoryDto repositoryDto);

    ResponseEntity<List<GitRepository>> listRepositories();

    ResponseEntity<GitRepository> getRepository(Long repositoryId);

    ResponseEntity<Set<GraphRepresentation>> decomposeRepository(Long repoId, DecompositionParameters decompositionDTO);
}
