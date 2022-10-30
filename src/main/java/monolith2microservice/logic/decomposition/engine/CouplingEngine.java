package monolith2microservice.logic.decomposition.engine;

import monolith2microservice.logic.decomposition.engine.impl.shared.CouplingInput;
import monolith2microservice.shared.models.couplings.BaseCoupling;

import java.util.List;

public interface CouplingEngine<T extends BaseCoupling> {

    List<T> compute(CouplingInput couplingInput);

}
