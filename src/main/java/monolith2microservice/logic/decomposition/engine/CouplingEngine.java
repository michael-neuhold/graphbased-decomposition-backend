package monolith2microservice.logic.decomposition.engine;

import ch.uzh.ifi.seal.monolith2microservices.models.DecompositionParameters;
import ch.uzh.ifi.seal.monolith2microservices.models.couplings.BaseCoupling;
import ch.uzh.ifi.seal.monolith2microservices.models.git.ChangeEvent;
import ch.uzh.ifi.seal.monolith2microservices.models.git.GitRepository;

import java.util.List;

public interface CouplingEngine<T extends BaseCoupling> {

    List<T> compute(GitRepository gitRepository, List<ChangeEvent> history, DecompositionParameters decompositionParameters);

}
