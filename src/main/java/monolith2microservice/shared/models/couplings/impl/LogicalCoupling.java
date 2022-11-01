package monolith2microservice.shared.models.couplings.impl;

import lombok.Getter;
import lombok.Setter;
import monolith2microservice.shared.models.couplings.BaseCoupling;

@Getter
@Setter
public final class LogicalCoupling extends BaseCoupling {
	
	private int startTimestamp;
	
	private int endTimestamp;
	
	private String hash;

	private LogicalCoupling(String firstFileName, String secondFileName, double score) {
		super(firstFileName, secondFileName, score);
	}

	public static LogicalCoupling of(String firstFileName, String secondFilename, double score) {
		return new LogicalCoupling(firstFileName, secondFilename, score);
	}

	public void incrementScore(){
		this.score++;
	}

}
