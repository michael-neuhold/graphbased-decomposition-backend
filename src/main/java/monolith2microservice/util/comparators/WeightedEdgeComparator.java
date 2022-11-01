package monolith2microservice.util.comparators;


import monolith2microservice.shared.models.graph.WeightedEdge;

import java.util.Comparator;


/**
 * Created by Genc on 09.12.2016.
 */
public class WeightedEdgeComparator implements Comparator<WeightedEdge> {

    @Override
    public int compare(WeightedEdge o1, WeightedEdge o2) {
        return Double.compare(o1.getScore(), o2.getScore());
    }
}
