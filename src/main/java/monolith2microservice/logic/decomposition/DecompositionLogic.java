package monolith2microservice.logic.decomposition;

import monolith2microservice.logic.decomposition.graph.component.GraphRepresentation;
import monolith2microservice.logic.decomposition.graph.transformer.GraphTransformer;
import monolith2microservice.shared.models.DecompositionParameters;
import monolith2microservice.shared.models.graph.Decomposition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface DecompositionLogic {

    Decomposition decompose(long id, DecompositionParameters decompositionDTO);

    Decomposition findById(long decompositionId);

    List<Decomposition> findAll();

    static Set<GraphRepresentation> getGraphRepresentation(Decomposition decomposition) {
        return  decomposition.getServices().stream()
                    .map(GraphRepresentation::from)
                    .collect(Collectors.toSet());
    }

}
