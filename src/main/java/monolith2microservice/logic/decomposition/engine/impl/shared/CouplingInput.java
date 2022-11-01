package monolith2microservice.logic.decomposition.engine.impl.shared;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import monolith2microservice.shared.models.DecompositionCouplingParameters;
import monolith2microservice.shared.models.git.ChangeEvent;
import monolith2microservice.shared.models.git.GitRepository;

import java.util.List;

@Getter
@Setter
@Builder
public class CouplingInput {

    private GitRepository gitRepository;
    private List<ChangeEvent> history;
    DecompositionCouplingParameters decompositionParameters;

}
