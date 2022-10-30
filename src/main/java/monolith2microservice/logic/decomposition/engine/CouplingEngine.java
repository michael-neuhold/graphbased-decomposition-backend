package monolith2microservice.logic.decomposition.engine;

import monolith2microservice.logic.decomposition.engine.impl.CouplingInput;
import monolith2microservice.shared.models.DecompositionParameters;
import monolith2microservice.shared.models.couplings.BaseCoupling;
import monolith2microservice.shared.models.git.ChangeEvent;
import monolith2microservice.shared.models.git.GitRepository;

import java.util.List;

public interface CouplingEngine<T extends BaseCoupling> {

    List<T> compute(CouplingInput couplingInput);

}
