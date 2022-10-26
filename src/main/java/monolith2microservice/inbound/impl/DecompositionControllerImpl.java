package monolith2microservice.inbound.impl;

import monolith2microservice.logic.decomposition.DecompositionLogic;
import monolith2microservice.logic.decomposition.graph.component.GraphRepresentation;
import monolith2microservice.shared.dto.DecompositionDto;
import monolith2microservice.shared.models.graph.Decomposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("decomposition/suggestions")
public class DecompositionControllerImpl {

    @Autowired
    DecompositionLogic decompositionLogic;
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<DecompositionDto>> listDecompositions() {
        List<Decomposition> decompositions = decompositionLogic.findAll();
        return ResponseEntity.ok(DecompositionDto.of(decompositions));
    }

    @CrossOrigin
    @RequestMapping(value="{decompositionId}", method= RequestMethod.GET)
    public ResponseEntity<Decomposition> getDecomposition(@PathVariable long decompositionId) {
        return ResponseEntity.ok(decompositionLogic.findById(decompositionId));
    }

    @CrossOrigin
    @RequestMapping(value="{decompositionId}/graph", method= RequestMethod.GET)
    public ResponseEntity<Set<GraphRepresentation>> getDecompositionGraph(@PathVariable long decompositionId) {
        Decomposition decomposition = decompositionLogic.findById(decompositionId);
        return ResponseEntity.ok(DecompositionLogic.getGraphRepresentation(decomposition));
    }

}
