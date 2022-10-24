package monolith2microservice.logic.decomposition;

import ch.uzh.ifi.seal.monolith2microservices.models.DecompositionParameters;
import ch.uzh.ifi.seal.monolith2microservices.models.graph.Decomposition;
import monolith2microservice.shared.dto.DecompositionDto;

public interface DecompositionLogic {

    Decomposition decompose(long id, DecompositionParameters decompositionDTO);

}
