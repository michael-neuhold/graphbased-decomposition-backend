package monolith2microservice.inbound.impl;

import monolith2microservice.logic.decomposition.DecompositionLogic;
import monolith2microservice.logic.decomposition.graph.component.GraphRepresentation;
import monolith2microservice.logic.decomposition.graph.transformer.GraphvizTransformer;
import monolith2microservice.shared.dto.DecompositionDto;
import monolith2microservice.shared.models.DecompositionParameters;
import monolith2microservice.shared.models.graph.Decomposition;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.ok(decompositionLogic.findById(decompositionId));
    }

    @Override
    @CrossOrigin
    @RequestMapping(value="{decompositionId}/graph", method= RequestMethod.GET)
    public ResponseEntity<Set<GraphRepresentation>> getDecompositionByIdAsGraph(@PathVariable Long decompositionId) {
        Decomposition decomposition = decompositionLogic.findById(decompositionId);
        return ResponseEntity.ok(DecompositionLogic.getGraphRepresentation(decomposition));
    }

    @Override
    @CrossOrigin
    @RequestMapping(value="decompose/{repoId}", method=RequestMethod.POST)
    public ResponseEntity<Set<GraphRepresentation>> decomposeRepositoryById(@PathVariable Long repoId, @RequestBody DecompositionParameters decompositionDTO) {

        Set<GraphRepresentation> graph =
                DecompositionLogic.getGraphRepresentation(decompositionLogic.decompose(repoId, decompositionDTO));

        return ResponseEntity.ok(graph);
    }

    @Override
    @CrossOrigin
    @RequestMapping(value="decompose/{repoId}/graphviz", method=RequestMethod.POST)
    public ResponseEntity<String> decomposeRepositoryByIdAsGraphviz(@PathVariable Long repoId, @RequestBody DecompositionParameters decompositionDTO) {

        Set<GraphRepresentation> graph =
                DecompositionLogic.getGraphRepresentation(decompositionLogic.decompose(repoId, decompositionDTO));

        return ResponseEntity.ok().body(GraphvizTransformer.create().transform(graph));
    }

}
