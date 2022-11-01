package monolith2microservice.util.comparators;

import monolith2microservice.shared.models.couplings.BaseCoupling;

import java.util.Comparator;

/**
 * Created by Genc on 09.12.2016.
 */
public class CouplingComparator implements Comparator<BaseCoupling>{

    @Override
    public int compare(BaseCoupling o1, BaseCoupling o2) {
        return Double.compare(o1.getScore(), o2.getScore());
    }
}
