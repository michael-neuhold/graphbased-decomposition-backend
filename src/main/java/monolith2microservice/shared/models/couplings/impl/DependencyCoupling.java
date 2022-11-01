package monolith2microservice.shared.models.couplings.impl;

import lombok.*;
import monolith2microservice.shared.models.couplings.BaseCoupling;

@Setter
@Getter
public final class DependencyCoupling extends BaseCoupling {

    private DependencyCoupling(String firstFileName, String secondFileName, double score) {
        super(firstFileName, secondFileName, score);
    }

    public static DependencyCoupling of(String firstFileName, String secondFileName, double score) {
        return new DependencyCoupling(firstFileName, secondFileName, score);
    }

}
