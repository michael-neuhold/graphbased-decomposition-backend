package monolith2microservice.shared.models.couplings.impl;

import lombok.Getter;
import lombok.Setter;
import monolith2microservice.shared.models.couplings.BaseCoupling;

import java.util.List;

@Getter
@Setter
public final class ContributorCoupling extends BaseCoupling {

    private List<String> firstFileAuthors;

    private List<String> secondFileAuthors;

    private ContributorCoupling(String firstFileName, String secondFileName, double score) {
        super(firstFileName, secondFileName, score);
    }

    public static ContributorCoupling of(String firstFileName, String secondFileName, double score) {
        return new ContributorCoupling(firstFileName, secondFileName, score);
    }

}
