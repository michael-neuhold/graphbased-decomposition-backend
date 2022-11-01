package monolith2microservice.shared.models.couplings.impl;

import lombok.Getter;
import lombok.Setter;
import monolith2microservice.shared.models.couplings.BaseCoupling;

@Getter
@Setter
public final class SemanticCoupling extends BaseCoupling {

    private SemanticCoupling(String firstFileName, String secondFileName, double score) {
        super(firstFileName, secondFileName, score);
    }

    public static SemanticCoupling of(String firstFileName, String secondFileName, double score) {
        return new SemanticCoupling(firstFileName, secondFileName, score);
    }

}
