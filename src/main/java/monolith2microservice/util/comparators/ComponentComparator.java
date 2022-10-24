package monolith2microservice.util.comparators;

import monolith2microservice.shared.models.graph.Component;

import java.util.Comparator;

/**
 * Created by gmazlami on 12/15/16.
 */
public class ComponentComparator implements Comparator<Component> {

    @Override
    public int compare(Component o1, Component o2) {
        return new Integer(o1.getSize()).compareTo(o2.getSize());
    }
}
