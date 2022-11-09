package monolith2microservice.logic.decomposition.graph.component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import monolith2microservice.shared.models.graph.Component;

import java.util.Set;

@Getter
@Setter
@ToString
public class GraphRepresentation {

    private static long componentCounter = 1;

    private Set<NodeRepresentation> nodes;

    private Set<EdgeRepresentation> edges;

    private long componentId;

    public GraphRepresentation(Set<NodeRepresentation> nodes, Set<EdgeRepresentation> edges, long id){
        this.nodes = nodes;
        this.edges = edges;
        this.componentId = id;
    }

    public static GraphRepresentation from(Component component){
        Set<NodeRepresentation> nodes = GraphRepresentationConverter.convertNodes(component);
        Set<EdgeRepresentation> edges = GraphRepresentationConverter.convertEdges(component, nodes);
        return new GraphRepresentation(nodes, edges, componentCounter++);
    }
}
