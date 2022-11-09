package monolith2microservice.logic.decomposition.graph;

import monolith2microservice.shared.models.couplings.BaseCoupling;
import monolith2microservice.shared.models.graph.ClassNode;
import monolith2microservice.shared.models.graph.Component;
import monolith2microservice.shared.models.graph.WeightedEdge;
import monolith2microservice.util.comparators.ComparatorFactory;

import java.util.*;
import java.util.stream.Collectors;

public final class MSTGraphClusterer {

    private MSTGraphClusterer() {
        //empty on purpose
    }

    public static Set<Component> clusterWithSplit(List<? extends BaseCoupling> couplings, int splitThreshold, int numServices) {
        Set<WeightedEdge> minimumSpanningTree = MinimumSpanningTree.of(couplings);
        List<WeightedEdge> clusters = computeClusters(minimumSpanningTree, numServices);
        List<Component> components = ConnectedComponents.connectedComponents(clusters);

        while (components.size() > 0) {

            //Sort components ascending according to size (number of nodes)
            components.sort(ComparatorFactory.componentComarator());

            //Reverse collection to get largest component
            Collections.reverse(components);

            Component largest = components.get(0);


            // split largest component if it exceeds size/degree parameter
            if (largest.getSize() > splitThreshold) {
                components.remove(0);
                List<Component> split = splitByDegree(largest);
                components.addAll(split);
            } else {
                return new HashSet<>(components);
            }

        }

        return new HashSet<>(components);
    }

    private static List<Component> splitByDegree(Component component) {
        List<ClassNode> nodes = component.getNodes();
        nodes.sort(ComparatorFactory.classNodeComparator());
        Collections.reverse(nodes);

        ClassNode nodeToRemove = nodes.get(0);
        nodes.remove(0);

        nodes.forEach(node -> {
            node.deleteNeighborWithId(nodeToRemove.getId());
        });

        List<Component> connectedComponents = ConnectedComponents.connectedComponentsFromNodes(nodes);
        return connectedComponents.stream().filter(c -> c.getSize() > 1).collect(Collectors.toList());
    }

    private static List<WeightedEdge> computeClusters(Set<WeightedEdge> edges, int numServices) {
        List<WeightedEdge> edgeList = new ArrayList<>(edges);
        List<WeightedEdge> oldList = null;

        //Sort ascending in order of distances between the files
        edgeList.sort(ComparatorFactory.weightedEdgeComparator());

        //Reverse collection so that largest distances are first
        Collections.reverse(edgeList);

        int numConnectedComponents = 1;
        int lastNumConnectedComponents = 1;

        do {
            oldList = new ArrayList<>(edgeList);

            //delete edge with largest distance
            edgeList.remove(0);

            //compute number of connected components by DFS
            numConnectedComponents = ConnectedComponents.numberOfComponents(edgeList);

            //stop if we cannot further improve the decomposition anymore and return last acceptable decomposition
            if (lastNumConnectedComponents > numConnectedComponents) {
                return oldList;
            } else {
                lastNumConnectedComponents = numConnectedComponents;
            }

        } while ((numConnectedComponents < numServices) && (!edgeList.isEmpty()));

        return edgeList;
    }

}
