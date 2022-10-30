package monolith2microservice.shared.models.couplings;

import lombok.*;

@Setter
@Getter
@Builder
public class DependencyCoupling extends BaseCoupling {

    public DependencyCoupling(String firstFileName, String secondFileName, double score) {
        super(firstFileName, secondFileName, score);
    }

}
