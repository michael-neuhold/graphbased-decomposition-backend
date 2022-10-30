package monolith2microservice.shared.models.couplings;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class CouplingQuartett {

    public CouplingQuartett(String firstFile, String secondFile) {
        this.firstFile = firstFile;
        this.secondFile = secondFile;
    }

    public CouplingQuartett(LogicalCoupling logicalCoupling) {
        this(logicalCoupling.firstFileName, logicalCoupling.secondFileName);
        this.logicalCoupling = logicalCoupling;
    }

    public CouplingQuartett(SemanticCoupling semanticCoupling) {
        this(semanticCoupling.firstFileName, semanticCoupling.secondFileName);
        this.semanticCoupling = semanticCoupling;
    }

    public CouplingQuartett(ContributorCoupling contributorCoupling) {
        this(contributorCoupling.firstFileName, contributorCoupling.secondFileName);
        this.contributorCoupling = contributorCoupling;
    }

    public CouplingQuartett(DependencyCoupling dependencyCoupling) {
        this(dependencyCoupling.firstFileName, dependencyCoupling.secondFileName);
        this.dependencyCoupling = dependencyCoupling;
    }

    private String firstFile;

    private String secondFile;

    private LogicalCoupling logicalCoupling;

    private SemanticCoupling semanticCoupling;

    private ContributorCoupling contributorCoupling;

    private DependencyCoupling dependencyCoupling;

}
