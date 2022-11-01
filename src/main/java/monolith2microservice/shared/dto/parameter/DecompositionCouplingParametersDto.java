package monolith2microservice.shared.dto.parameter;

import lombok.*;
import monolith2microservice.shared.models.DecompositionCouplingParameters;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DecompositionCouplingParametersDto {

    private boolean logicalCoupling;

    private boolean semanticCoupling;

    private boolean contributorCoupling;

    private boolean dependencyCoupling;

    private int numberOfServices;

    private int intervalSeconds;

    private int classClusterThreshold;

    public DecompositionCouplingParameters toDecompositionParameters() {
        return DecompositionCouplingParameters.builder()
                .intervalSeconds(intervalSeconds)
                .sizeThreshold(classClusterThreshold)
                .semanticCoupling(semanticCoupling)
                .dependencyCoupling(dependencyCoupling)
                .contributorCoupling(contributorCoupling)
                .dependencyCoupling(dependencyCoupling)
                .numServices(numberOfServices)
                .build();
    }

}
