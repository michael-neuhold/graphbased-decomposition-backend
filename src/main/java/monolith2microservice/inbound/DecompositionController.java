package monolith2microservice.inbound;

import monolith2microservice.logic.decomposition.graph.component.GraphRepresentation;
import monolith2microservice.shared.dto.parameter.DecompositionCouplingParametersDto;
import monolith2microservice.shared.dto.DecompositionDto;
import monolith2microservice.shared.dto.parameter.MonolithCouplingParametersDto;
import monolith2microservice.shared.dto.visualization.GraphVisualizationDto;
import monolith2microservice.shared.models.graph.Decomposition;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface DecompositionController {
    ResponseEntity<List<DecompositionDto>> getAllDecompositions();

    ResponseEntity<Decomposition> getDecompositionById(Long decompositionId);

    ResponseEntity<Set<GraphRepresentation>> getDecompositionByIdAsGraph(Long decompositionId);

    ResponseEntity<GraphVisualizationDto> getDecompositionByIdAsVisualization(Long decompositionId, Boolean guessClassTask);

    ResponseEntity<Set<GraphRepresentation>> decomposeRepositoryById(Long repoId, DecompositionCouplingParametersDto decompositionDTO);

    ResponseEntity<String> decomposeRepositoryByIdAsGraphviz(Long repoId, DecompositionCouplingParametersDto decompositionDTO);

    ResponseEntity<GraphVisualizationDto> decomposeRepositoryByIdAsGraphVisualization(Long repositoryId, DecompositionCouplingParametersDto decompositionParameters);

    ResponseEntity<GraphVisualizationDto> monolithicCouplingVisualization(Long repositoryId, MonolithCouplingParametersDto monolithCouplingParametersDto);

}
