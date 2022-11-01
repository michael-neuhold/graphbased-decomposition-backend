package monolith2microservice.logic.decomposition.graph;

import monolith2microservice.shared.models.couplings.BaseCoupling;
import monolith2microservice.shared.models.graph.WeightedEdge;
import org.jgrapht.alg.KruskalMinimumSpanningTree;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.List;
import java.util.Set;


public final class MinimumSpanningTree {


    private MinimumSpanningTree(){
        //empty on purpose
    }

    public static Set<WeightedEdge> of(List<? extends BaseCoupling> couplings){
        KruskalMinimumSpanningTree<String, WeightedEdge> mst = new KruskalMinimumSpanningTree<>(createWeightedGraph(couplings));
        return mst.getMinimumSpanningTreeEdgeSet();
    }

    private static SimpleWeightedGraph<String, WeightedEdge> createWeightedGraph(List<? extends BaseCoupling> couplings){
        SimpleWeightedGraph<String, WeightedEdge> weightedGraph = new SimpleWeightedGraph<>(WeightedEdge.class);

        couplings.forEach(coupling -> {
            weightedGraph.addVertex(coupling.getFirstFileName());
            weightedGraph.addVertex(coupling.getSecondFileName());

            WeightedEdge currentEdge = new WeightedEdge();
            currentEdge.setScore(1/coupling.getScore());
            weightedGraph.addEdge(coupling.getFirstFileName(), coupling.getSecondFileName(),currentEdge);

            //Add the score inversed (1/score) so that high score means close distance between vertices
            weightedGraph.setEdgeWeight(currentEdge, (1 / coupling.getScore()));
        });

        return weightedGraph;
    }

}
