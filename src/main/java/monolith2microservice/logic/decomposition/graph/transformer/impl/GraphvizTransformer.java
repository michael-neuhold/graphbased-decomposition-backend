package monolith2microservice.logic.decomposition.graph.transformer.impl;

import monolith2microservice.logic.decomposition.graph.component.GraphRepresentation;
import monolith2microservice.logic.decomposition.graph.transformer.GraphTransformer;

import java.util.Set;


public class GraphvizTransformer implements GraphTransformer<String> {

    public static GraphvizTransformer create() {
        return new GraphvizTransformer();
    }

    public String transform(Set<GraphRepresentation> graphRepresentations) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("digraph \"architecture-suggestion\" {\n");
        stringBuilder.append("\tnode [shape=\"circle\"]\n");

        graphRepresentations.forEach(subGraph -> {
            subGraph.getNodes()
                    .forEach(node ->
                            stringBuilder.append(String.format("\t%d [label=\"%s\"]\n", node.getId(), node.getLabel()))
                    );

            subGraph.getEdges()
                    .forEach(edge ->
                            stringBuilder.append(String.format("\t%d -> %d\n", edge.getFrom(), edge.getTo()))
                    );
        });

        stringBuilder.append("}");

        return stringBuilder.toString();
    }

}
