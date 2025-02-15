import monolith2microservice.logic.decomposition.graph.MSTGraphClusterer;
import monolith2microservice.shared.models.couplings.BaseCoupling;
import monolith2microservice.shared.models.couplings.impl.ContributorCoupling;
import monolith2microservice.shared.models.couplings.impl.LogicalCoupling;
import monolith2microservice.shared.models.couplings.impl.SemanticCoupling;
import monolith2microservice.shared.models.graph.Component;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by gmazlami on 12/15/16.
 */
public class CouplingTest {

    /**
     * TODO
     */
    @Test
    @Ignore
    public void testCombinedCouplings(){
        //Test Data
        List<SemanticCoupling> semanticCouplings = generateGenericSemanticCouplings();
        List<LogicalCoupling> logicalCouplings = generateGenericLogicalCouplings();
        List<ContributorCoupling> contributorCouplings = generateGenericContributorCouplings();

        //Expected Data
        List<BaseCoupling> expectedCouplings = generateExpectedCombinedCouplings();

        //Compute result
        //List<BaseCoupling> combinedCouplings = LinearGraphCombination.create().withContributorCouplings(contributorCouplings).withLogicalCouplings(logicalCouplings).withSemanticCouplings(semanticCouplings).generate();
        //assertEquals(expectedCouplings,combinedCouplings);
    }

    /**
     * TODO
     */
    @Test
    @Ignore
    public void testCombinedCouplingsWithoutLogical(){
        //Test Data
        List<SemanticCoupling> semanticCouplings = generateGenericSemanticCouplings();
        List<ContributorCoupling> contributorCouplings = generateGenericContributorCouplings();

        //Expected Data
        List<BaseCoupling> expectedCouplings = generateExpectedCombinedCouplingsWithoutLogicalCouplings();

        //Compute result
        //List<BaseCoupling> combinedCouplings = LinearGraphCombination.create().withContributorCouplings(contributorCouplings).withSemanticCouplings(semanticCouplings).generate();
        //assertEquals(expectedCouplings,combinedCouplings);
    }

    /**
     * TODO
     */
    @Test
    @Ignore
    public void testLogicalCouplingCombination(){
        //Test Data
        List<LogicalCoupling> couplings = generateExtendedLogicalCouplings();

        // Compute result from couplings after combination
        //List<BaseCoupling> combinedCouplings = LinearGraphCombination.create().withLogicalCouplings(couplings).generate();


        //Set<Component> componentsFromCombined = MSTGraphClusterer.clusterWithSplit(combinedCouplings,6,4);

        // Compute result from couplings without prior combination
        Set<Component> componentsFromLogicalCoupling = MSTGraphClusterer.clusterWithSplit(couplings,6,4);

        // Resulting components should be the same
        //assertEquals(componentsFromCombined, componentsFromLogicalCoupling);

    }

    private List<LogicalCoupling> generateExtendedLogicalCouplings(){
        List<LogicalCoupling> couplings = new ArrayList<>();
        couplings.add(LogicalCoupling.of("A","C",3.0));
        couplings.add(LogicalCoupling.of("A","B",5.0));
        couplings.add(LogicalCoupling.of("C","D",2.0));
        couplings.add(LogicalCoupling.of("B","D",7.0));
        couplings.add(LogicalCoupling.of("C","E",1.0));
        couplings.add(LogicalCoupling.of("E","H",7.0));
        couplings.add(LogicalCoupling.of("H","I",8.0));
        couplings.add(LogicalCoupling.of("D","G",2.0));
        couplings.add(LogicalCoupling.of("G","L",8.0));
        couplings.add(LogicalCoupling.of("L","M",7.0));
        couplings.add(LogicalCoupling.of("M","G",6.0));
        couplings.add(LogicalCoupling.of("B","F",2.0));
        couplings.add(LogicalCoupling.of("F","J",9.0));
        couplings.add(LogicalCoupling.of("J","K",7.0));
        couplings.add(LogicalCoupling.of("K","F",5.0));
        return couplings;
    }

    private List<ContributorCoupling> generateGenericContributorCouplings(){
        List<ContributorCoupling> contributorCouplings = new ArrayList<>();
        contributorCouplings.add(ContributorCoupling.of("A","B", 2.0));
        contributorCouplings.add(ContributorCoupling.of("A","E", 3.0));
        contributorCouplings.add(ContributorCoupling.of("A","C", 4.0));
        return contributorCouplings;
    }

    private List<SemanticCoupling> generateGenericSemanticCouplings(){
        List<SemanticCoupling> semanticCouplings = new ArrayList<>();
        semanticCouplings.add(SemanticCoupling.of("A","B",6.0));
        semanticCouplings.add(SemanticCoupling.of("B","C",1.0));
        semanticCouplings.add(SemanticCoupling.of("C","D",3.0));
        semanticCouplings.add(SemanticCoupling.of("D","E",7.0));
        return semanticCouplings;

    }

    private List<LogicalCoupling> generateGenericLogicalCouplings(){
        List<LogicalCoupling> logicalCouplings = new ArrayList<>();
        logicalCouplings.add(LogicalCoupling.of("A","B",5.0));
        logicalCouplings.add(LogicalCoupling.of("A","C",3.0));
        logicalCouplings.add(LogicalCoupling.of("C","D",2.0));
        logicalCouplings.add(LogicalCoupling.of("B","D",7.0));
        return logicalCouplings;
    }

    private List<BaseCoupling> generateExpectedCombinedCouplings(){
        List<BaseCoupling> expectedCouplings = new ArrayList<>();
        expectedCouplings.add(new BaseCoupling("A","B", 13d));
        expectedCouplings.add(new BaseCoupling("B","C", 1d));
        expectedCouplings.add(new BaseCoupling("A","C", 7d));
        expectedCouplings.add(new BaseCoupling("C","D", 5d));
        expectedCouplings.add(new BaseCoupling("B","D", 7d));
        expectedCouplings.add(new BaseCoupling("A","E", 3d));
        expectedCouplings.add(new BaseCoupling("D","E", 7d));
        return expectedCouplings;
    }

    private List<BaseCoupling> generateExpectedCombinedCouplingsWithoutLogicalCouplings(){
        List<BaseCoupling> expectedCouplings = new ArrayList<>();
        expectedCouplings.add(new BaseCoupling("A","B",8d));
        expectedCouplings.add(new BaseCoupling("B","C",1d));
        expectedCouplings.add(new BaseCoupling("A","C",4d));
        expectedCouplings.add(new BaseCoupling("C","D",3d));
        expectedCouplings.add(new BaseCoupling("A","E",3d));
        expectedCouplings.add(new BaseCoupling("D","E",7d));
        return expectedCouplings;
    }
}
