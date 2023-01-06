package monolith2microservice.logic.decomposition;

import monolith2microservice.logic.decomposition.graph.component.GraphRepresentation;
import monolith2microservice.shared.dto.parameter.MonolithCouplingParametersDto;
import monolith2microservice.shared.models.DecompositionCouplingParameters;
import monolith2microservice.shared.models.graph.Component;
import monolith2microservice.shared.models.graph.Decomposition;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface DecompositionLogic {

    Decomposition decompose(long repositoryId, DecompositionCouplingParameters decompositionDTO);

    Decomposition monolithication(long repositoryId, MonolithCouplingParametersDto monolithCouplingParametersDto);

    Decomposition findById(long decompositionId);

    List<Component> searchComponent(long decompositionId, String searchTerm, boolean javaSuffix);

    List<Decomposition> findAll();

    static Set<GraphRepresentation> getGraphRepresentation(Decomposition decomposition) {
        return  decomposition.getServices().stream()
                    .map(GraphRepresentation::from)
                    .collect(Collectors.toSet());
    }

}
