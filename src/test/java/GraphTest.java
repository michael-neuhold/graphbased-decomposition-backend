import monolith2microservice.util.comparators.WeightedEdgeComparator;
import monolith2microservice.shared.models.couplings.BaseCoupling;
import monolith2microservice.shared.models.couplings.impl.SemanticCoupling;
import monolith2microservice.shared.models.graph.WeightedEdge;
import monolith2microservice.logic.decomposition.graph.ConnectedComponents;
import monolith2microservice.logic.decomposition.graph.MinimumSpanningTree;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by Genc on 13.12.2016.
 */
public class GraphTest {


    private List<BaseCoupling> couplings;

    @Before
    public void setUpCouplings(){
        couplings = new ArrayList<>();
        couplings.add(SemanticCoupling.of("a","d",7d));
        couplings.add(SemanticCoupling.of("d","b",8d));
        couplings.add(SemanticCoupling.of("b","g",2d));
        couplings.add(SemanticCoupling.of("a","b",2d));
        couplings.add(SemanticCoupling.of("d","g",4d));
        couplings.add(SemanticCoupling.of("b","e",3d));
        couplings.add(SemanticCoupling.of("e","c",8d));
        couplings.add(SemanticCoupling.of("c","h",7d));
        couplings.add(SemanticCoupling.of("e","g",5d));
        couplings.add(SemanticCoupling.of("h","g",4d));
        couplings.add(SemanticCoupling.of("g","f",5d));
        couplings.add(SemanticCoupling.of("g","j",2d));
        couplings.add(SemanticCoupling.of("f","i",7d));
        couplings.add(SemanticCoupling.of("j","i",3d));
        couplings.add(SemanticCoupling.of("h","k",2d));
        couplings.add(SemanticCoupling.of("k","j",9d));
        couplings.add(SemanticCoupling.of("j","n",8d));
        couplings.add(SemanticCoupling.of("j","m",1d));
        couplings.add(SemanticCoupling.of("j","l",4d));
        couplings.add(SemanticCoupling.of("l","m",2d));
    }

    @Test
    public void testMinimalSpanningTree(){
        Set<WeightedEdge> edges =  MinimumSpanningTree.of(couplings);
        assertEquals(edges.size(), 13);

        double minimalSpanningTreeWeight = edges.stream().map(edge -> {
            return edge.getScore();
        }).mapToDouble(Double::doubleValue).sum();
        assertEquals(minimalSpanningTreeWeight,2.65, 0.1d );
    }

    @Test
    public void testConnectedComponents(){
        Set<WeightedEdge> edges =  MinimumSpanningTree.of(couplings);
        List<WeightedEdge> edgeList = edges.stream().collect(Collectors.toList());

        assertEquals(1, ConnectedComponents.numberOfComponents(edgeList));

        Collections.sort(edgeList,new WeightedEdgeComparator());
        Collections.reverse(edgeList);
        edgeList.remove(0);

        assertEquals(1, ConnectedComponents.numberOfComponents(edgeList));

        edgeList.remove(0);

        assertEquals(2, ConnectedComponents.numberOfComponents(edgeList));
    }

}
