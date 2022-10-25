package monolith2microservice.logic.decomposition.graph.transformer;

import monolith2microservice.logic.decomposition.graph.component.GraphRepresentation;

import java.util.Set;

public interface GraphTransformer<T> {

    T transform(Set<GraphRepresentation> graphRepresentation);

}
