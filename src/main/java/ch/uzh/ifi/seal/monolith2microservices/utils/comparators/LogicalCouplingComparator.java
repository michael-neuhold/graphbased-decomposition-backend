package ch.uzh.ifi.seal.monolith2microservices.utils.comparators;

import java.util.Comparator;

import monolith2microservice.shared.models.couplings.LogicalCoupling;

public class LogicalCouplingComparator implements Comparator<LogicalCoupling> {

	@Override
	public int compare(LogicalCoupling o1, LogicalCoupling o2) {
		return new Double(o1.getScore()).compareTo(new Double(o2.getScore()));
	}

}
