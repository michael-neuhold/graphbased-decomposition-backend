package monolith2microservice.util.comparators.impl;

import java.util.Comparator;

import monolith2microservice.shared.models.couplings.impl.LogicalCoupling;

public class LogicalCouplingComparator implements Comparator<LogicalCoupling> {

	@Override
	public int compare(LogicalCoupling o1, LogicalCoupling o2) {
		return Double.compare(o1.getScore(), o2.getScore());
	}

}
