package monolith2microservice.inbound;

import monolith2microservice.logic.decomposition.graph.component.GraphRepresentation;
import monolith2microservice.shared.dto.DecompositionDto;
import monolith2microservice.shared.dto.visualization.GraphVisualizationDto;
import monolith2microservice.shared.models.DecompositionParameters;
import monolith2microservice.shared.models.graph.Decomposition;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface DecompositionController {
    ResponseEntity<List<DecompositionDto>> getAllDecompositions();

    ResponseEntity<Decomposition> getDecompositionById(Long decompositionId);

    ResponseEntity<Set<GraphRepresentation>> getDecompositionByIdAsGraph(Long decompositionId);

    ResponseEntity<Set<GraphRepresentation>> decomposeRepositoryById(Long repoId, DecompositionParameters decompositionDTO);

    ResponseEntity<String> decomposeRepositoryByIdAsGraphviz(Long repoId, DecompositionParameters decompositionDTO);

    ResponseEntity<GraphVisualizationDto> decomposeRepositoryByIdAsGraphVisualization(Long repositoryId, DecompositionParameters decompositionParameters);

}
