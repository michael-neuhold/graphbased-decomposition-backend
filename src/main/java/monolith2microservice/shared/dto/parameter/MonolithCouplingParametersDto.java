package monolith2microservice.shared.dto.parameter;

import lombok.*;
import monolith2microservice.shared.models.DecompositionCouplingParameters;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonolithCouplingParametersDto {

    private static final int NUMBER_OF_SERVICES = 1;

    private static final int NUMBER_OF_INTERVAL_SECONDS = 3600;

    private boolean logicalCoupling;

    private boolean semanticCoupling;

    private boolean contributorCoupling;

    private boolean dependencyCoupling;

    private double intervalSeconds;

    private boolean guessClassTask;

    public DecompositionCouplingParameters toDecompositionParameters() {
        return DecompositionCouplingParameters.builder()
                .contributorCoupling(contributorCoupling)
                .dependencyCoupling(dependencyCoupling)
                .semanticCoupling(semanticCoupling)
                .logicalCoupling(logicalCoupling)
                .numServices(NUMBER_OF_SERVICES)
                .sizeThreshold(Integer.MAX_VALUE)
                .intervalSeconds(NUMBER_OF_INTERVAL_SECONDS)
                .build();
    }

}
