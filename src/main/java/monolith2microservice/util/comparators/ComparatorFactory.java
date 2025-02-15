package monolith2microservice.util.comparators;

import monolith2microservice.shared.models.couplings.BaseCoupling;
import monolith2microservice.shared.models.graph.ClassNode;
import monolith2microservice.shared.models.graph.Component;
import monolith2microservice.shared.models.graph.WeightedEdge;
import monolith2microservice.util.comparators.impl.ClassNodeComparator;
import monolith2microservice.util.comparators.impl.ComponentComparator;
import monolith2microservice.util.comparators.impl.CouplingComparator;
import monolith2microservice.util.comparators.impl.WeightedEdgeComparator;

import java.util.Comparator;

public class ComparatorFactory {

    public static Comparator<ClassNode> classNodeComparator() {
        return new ClassNodeComparator();
    }

    public static Comparator<Component> componentComarator() {
        return new ComponentComparator();
    }

    public static Comparator<BaseCoupling> couplingComparator() {
        return new CouplingComparator();
    }

    public static Comparator<WeightedEdge> weightedEdgeComparator() {
        return new WeightedEdgeComparator();
    }
}
