package monolith2microservice.logic.evaluation;

import monolith2microservice.shared.models.graph.Decomposition;

public interface EvaluationLogic {

    void evaluate(Decomposition decomposition);

}
