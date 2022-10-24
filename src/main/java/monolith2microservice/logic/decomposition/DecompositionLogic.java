package monolith2microservice.logic.decomposition;

import monolith2microservice.shared.models.DecompositionParameters;
import monolith2microservice.shared.models.graph.Decomposition;

public interface DecompositionLogic {

    Decomposition decompose(long id, DecompositionParameters decompositionDTO);

}
