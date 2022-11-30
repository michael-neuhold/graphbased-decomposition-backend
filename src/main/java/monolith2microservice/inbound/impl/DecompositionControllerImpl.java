package monolith2microservice.inbound.impl;

import monolith2microservice.inbound.DecompositionController;
import monolith2microservice.logic.decomposition.DecompositionLogic;
import monolith2microservice.logic.decomposition.graph.component.GraphRepresentation;
import monolith2microservice.logic.decomposition.graph.transformer.impl.GraphvizTransformer;
import monolith2microservice.shared.dto.parameter.DecompositionCouplingParametersDto;
import monolith2microservice.shared.dto.DecompositionDto;
import monolith2microservice.shared.dto.parameter.MonolithCouplingParametersDto;
import monolith2microservice.shared.dto.visualization.GraphVisualizationDto;
import monolith2microservice.shared.models.graph.Decomposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("decompositions")
public class DecompositionControllerImpl implements DecompositionController {

    private final Logger LOGGER = LoggerFactory.getLogger(DecompositionControllerImpl.class);

    @Autowired
    DecompositionLogic  decompositionLogic;

    @Override
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<DecompositionDto>> getAllDecompositions() {
        LOGGER.info("|-> getAllDecompositions");
        List<Decomposition> decompositions = decompositionLogic.findAll();

        return ResponseEntity.ok(DecompositionDto.of(decompositions));
    }

    @Override
    @CrossOrigin
    @RequestMapping(value="{decompositionId}", method= RequestMethod.GET)
    public ResponseEntity<Decomposition> getDecompositionById(@PathVariable Long decompositionId) {
        LOGGER.info("|-> getDecompositionById");
        Decomposition decomposition = decompositionLogic.findById(decompositionId);
        if (decomposition == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(decomposition);
    }

    @Override
    @CrossOrigin
    @RequestMapping(value="{decompositionId}/graph", method= RequestMethod.GET)
    public ResponseEntity<Set<GraphRepresentation>> getDecompositionByIdAsGraph(@PathVariable Long decompositionId) {
        LOGGER.info("getDecompositionByIdAsGraph");
        Decomposition decomposition = decompositionLogic.findById(decompositionId);
        if (decomposition == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(DecompositionLogic.getGraphRepresentation(decomposition));
    }

    @Override
    @CrossOrigin
    @RequestMapping(value="{decompositionId}/visualization/{guessClassTask}", method = RequestMethod.GET)
    public ResponseEntity<GraphVisualizationDto> getDecompositionByIdAsVisualization(@PathVariable Long decompositionId, @PathVariable Boolean guessClassTask) {
        LOGGER.info("getDecompositionByIdAsVisualization");
        Decomposition decomposition = decompositionLogic.findById(decompositionId);
        if (decomposition == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Set<GraphRepresentation> graph =
                DecompositionLogic.getGraphRepresentation(decomposition);

        return ResponseEntity.ok().body(GraphVisualizationDto.of(graph, guessClassTask));
    }

    @Override
    @CrossOrigin
    @RequestMapping(value="decompose/{repoId}", method=RequestMethod.POST)
    public ResponseEntity<Set<GraphRepresentation>> decomposeRepositoryById(
            @PathVariable Long repoId,
            @RequestBody DecompositionCouplingParametersDto decompositionDTO) {
        LOGGER.info("decomposeRepositoryById");
        Decomposition decomposition = decompositionLogic.decompose(repoId, decompositionDTO.toDecompositionParameters());
        if (decomposition == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(DecompositionLogic.getGraphRepresentation(decomposition));
    }

    @Override
    @CrossOrigin
    @RequestMapping(value="decompose/{repoId}/graphviz", method=RequestMethod.POST)
    public ResponseEntity<String> decomposeRepositoryByIdAsGraphviz(
            @PathVariable Long repoId,
            @RequestBody DecompositionCouplingParametersDto decompositionDTO) {
        LOGGER.info("decomposeRepositoryByIdAsGraphviz");
        Decomposition decomposition = decompositionLogic.decompose(repoId, decompositionDTO.toDecompositionParameters());
        if (decomposition == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Set<GraphRepresentation> graph =
                DecompositionLogic.getGraphRepresentation(decomposition);

        return ResponseEntity.ok().body(GraphvizTransformer.create().transform(graph));
    }

    @Override
    @CrossOrigin
    @RequestMapping(value="decompose/{repositoryId}/visualization", method = RequestMethod.POST)
    public ResponseEntity<GraphVisualizationDto> decomposeRepositoryByIdAsGraphVisualization(
            @PathVariable Long repositoryId,
            @RequestBody DecompositionCouplingParametersDto decompositionParameters) {
        LOGGER.info("decomposeRepositoryByIdAsGraphVisualization");
        Decomposition decomposition = decompositionLogic.decompose(repositoryId, decompositionParameters.toDecompositionParameters());
        if (decomposition == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Set<GraphRepresentation> graph =
                DecompositionLogic.getGraphRepresentation(decomposition);

        return ResponseEntity.ok().body(GraphVisualizationDto.of(graph, decompositionParameters.isGuessClassTask()));
    }

    @Override
    @CrossOrigin
    @RequestMapping(value="monolith/{repositoryId}/coupling/visualization", method = RequestMethod.POST)
    public ResponseEntity<GraphVisualizationDto> monolithicCouplingVisualization(
            @PathVariable Long repositoryId,
            @RequestBody MonolithCouplingParametersDto monolithCouplingParametersDto) {
        LOGGER.info("monolithicCouplingVisualization");
        Decomposition decomposition = decompositionLogic.monolithication(repositoryId, monolithCouplingParametersDto);
        if (decomposition == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Set<GraphRepresentation> graph =
                DecompositionLogic.getGraphRepresentation(decomposition);

        return ResponseEntity.ok().body(GraphVisualizationDto.of(graph, monolithCouplingParametersDto.isGuessClassTask()));
    }

}
