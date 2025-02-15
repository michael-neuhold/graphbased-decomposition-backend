package monolith2microservice.shared.dto.visualization;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import monolith2microservice.logic.decomposition.graph.component.GraphRepresentation;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static monolith2microservice.util.VisualizationHelper.couldBeApi;
import static monolith2microservice.util.VisualizationHelper.couldBeDatabaseAccess;

@Getter
@Setter
@Builder
public class GraphVisualizationDto {

    private List<GraphVisualizationLinkDto> links;

    private List<GraphVisualizationNodeDto> nodes;

    public static GraphVisualizationDto of(Set<GraphRepresentation> graphRepresentations, boolean guessClassTask) {
        List<GraphVisualizationLinkDto> graphVisualizationLinkDtos = new LinkedList<>();
        List<GraphVisualizationNodeDto> graphVisualizationNodeDtos = new LinkedList<>();
        graphRepresentations.forEach(graphRepresentation -> {
            graphRepresentation.getEdges().forEach(edge -> {
                graphVisualizationLinkDtos.add(GraphVisualizationLinkDto.builder()
                        .source(edge.getFrom())
                        .target(edge.getTo())
                        .value(edge.getWeight())
                        .build());
            });
            graphRepresentation.getNodes().forEach(node -> {
                graphVisualizationNodeDtos.add(GraphVisualizationNodeDto.builder()
                        .id(node.getId())
                        .group(graphRepresentation.getComponentId())
                        .label(node.getLabel())
                        .couldBeApi(guessClassTask && couldBeApi(node.getLabel()))
                        .couldBeDatabaseAccess(guessClassTask && couldBeDatabaseAccess(node.getLabel()))
                        .build());
            });
        });
        return GraphVisualizationDto.builder().links(graphVisualizationLinkDtos).nodes(graphVisualizationNodeDtos).build();
    }

}
