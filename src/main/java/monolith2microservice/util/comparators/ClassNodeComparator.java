package monolith2microservice.util.comparators;

import monolith2microservice.shared.models.graph.ClassNode;

import java.util.Comparator;

/**
 * Created by gmazlami on 12/15/16.
 */
public class ClassNodeComparator implements Comparator<ClassNode> {

    @Override
    public int compare(ClassNode o1, ClassNode o2) {
        if(o1.getDegree() == o2.getDegree()){
            return Double.compare(o1.getCombinedWeight(), o2.getCombinedWeight());
        }else{
            return Integer.compare(o1.getDegree(), o2.getDegree());
        }
    }
}
