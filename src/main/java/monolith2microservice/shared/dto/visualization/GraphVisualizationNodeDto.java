package monolith2microservice.shared.dto.visualization;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GraphVisualizationNodeDto {

    private long id;

    private long group;

    private String label;

}
