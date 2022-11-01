package monolith2microservice.inbound.impl;

import monolith2microservice.logic.decomposition.DecompositionLogic;
import monolith2microservice.logic.decomposition.graph.component.GraphRepresentation;
import monolith2microservice.logic.decomposition.graph.transformer.GraphvizTransformer;
import monolith2microservice.shared.dto.DecompositionDto;
import monolith2microservice.shared.dto.visualization.GraphVisualizationDto;
import monolith2microservice.shared.models.DecompositionParameters;
import monolith2microservice.shared.models.graph.Decomposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("decompositions")
public class DecompositionControllerImpl implements monolith2microservice.inbound.DecompositionController {

    @Autowired
    DecompositionLogic  decompositionLogic;

    @Override
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<DecompositionDto>> getAllDecompositions() {
        List<Decomposition> decompositions = decompositionLogic.findAll();
        return ResponseEntity.ok(DecompositionDto.of(decompositions));
    }

    @Override
    @CrossOrigin
    @RequestMapping(value="{decompositionId}", method= RequestMethod.GET)
    public ResponseEntity<Decomposition> getDecompositionById(@PathVariable Long decompositionId) {
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
        Decomposition decomposition = decompositionLogic.findById(decompositionId);
        if (decomposition == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(DecompositionLogic.getGraphRepresentation(decomposition));
    }

    @Override
    @CrossOrigin
    @RequestMapping(value="decompose/{repoId}", method=RequestMethod.POST)
    public ResponseEntity<Set<GraphRepresentation>> decomposeRepositoryById(@PathVariable Long repoId, @RequestBody DecompositionParameters decompositionDTO) {
        Decomposition decomposition = decompositionLogic.decompose(repoId, decompositionDTO);
        if (decomposition == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Set<GraphRepresentation> graph =
                DecompositionLogic.getGraphRepresentation(decomposition);

        return ResponseEntity.ok(graph);
    }

    @Override
    @CrossOrigin
    @RequestMapping(value="decompose/{repoId}/graphviz", method=RequestMethod.POST)
    public ResponseEntity<String> decomposeRepositoryByIdAsGraphviz(@PathVariable Long repoId, @RequestBody DecompositionParameters decompositionDTO) {
        Decomposition decomposition = decompositionLogic.decompose(repoId, decompositionDTO);
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
    public ResponseEntity<GraphVisualizationDto> decomposeRepositoryByIdAsGraphVisualization(@PathVariable Long repositoryId, @RequestBody DecompositionParameters decompositionParameters) {
        Decomposition decomposition = decompositionLogic.decompose(repositoryId, decompositionParameters);
        if (decomposition == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Set<GraphRepresentation> graph =
                DecompositionLogic.getGraphRepresentation(decomposition);

        return ResponseEntity.ok().body(GraphVisualizationDto.of(graph));
    }

}
