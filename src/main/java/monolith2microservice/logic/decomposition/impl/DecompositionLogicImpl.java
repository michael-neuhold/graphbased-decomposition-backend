package monolith2microservice.logic.decomposition.impl;

import ch.uzh.ifi.seal.monolith2microservices.models.graph.Decomposition;
import ch.uzh.ifi.seal.monolith2microservices.services.decomposition.DecompositionService;
import monolith2microservice.logic.decomposition.DecompositionLogic;
import monolith2microservice.shared.dto.DecompositionDto;
import org.springframework.beans.factory.annotation.Autowired;

public class DecompositionLogicImpl implements DecompositionLogic {

    @Autowired
    private DecompositionService decompositionService;
    
    @Override
    public Decomposition decompose(long id, DecompositionDto decompositionDTO) {
        return null;
    }

}
