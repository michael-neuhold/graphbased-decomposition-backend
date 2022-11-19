package monolith2microservice.logic.decomposition.graph;

import monolith2microservice.shared.models.couplings.BaseCoupling;
import monolith2microservice.shared.models.graph.WeightedEdge;
import org.jgrapht.alg.KruskalMinimumSpanningTree;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public final class MinimumSpanningTree {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinimumSpanningTree.class);

    private MinimumSpanningTree() {
        //empty on purpose
    }

    public static Set<WeightedEdge> of(List<? extends BaseCoupling> couplings) {
        KruskalMinimumSpanningTree<String, WeightedEdge> mst = new KruskalMinimumSpanningTree<>(createWeightedGraph(couplings));
        return mst.getMinimumSpanningTreeEdgeSet();
    }

    private static SimpleWeightedGraph<String, WeightedEdge> createWeightedGraph(List<? extends BaseCoupling> couplings) {
        LOGGER.info("| begin |-> createWeightedGraph");
        SimpleWeightedGraph<String, WeightedEdge> weightedGraph = new SimpleWeightedGraph<>(WeightedEdge.class);

        couplings.forEach(coupling -> {
            weightedGraph.addVertex(coupling.getFirstFileName());
            weightedGraph.addVertex(coupling.getSecondFileName());

            WeightedEdge currentEdge = new WeightedEdge();
            currentEdge.setScore(1 / coupling.getScore());
            weightedGraph.addEdge(coupling.getFirstFileName(), coupling.getSecondFileName(), currentEdge);

            LOGGER.debug("vertex <-> vertex with coupling score: '{}' and weight: '{}'",
                    coupling.getScore(),
                    1 / coupling.getScore()
            );

            // Add the score inversed (1/score) so that high score means close distance between vertices
            weightedGraph.setEdgeWeight(currentEdge, (1 / coupling.getScore()));
        });
        LOGGER.info("| end <-| createWeightedGraph");
        return weightedGraph;
    }

}
