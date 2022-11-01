package monolith2microservice.shared.dto.visualization;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GraphVisualizationLinkDto {

    private long source;

    private long target;

    private long value;

}
