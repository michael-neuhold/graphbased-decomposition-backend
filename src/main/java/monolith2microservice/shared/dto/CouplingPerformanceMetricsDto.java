package monolith2microservice.shared.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CouplingPerformanceMetricsDto {

    private String repositoryName;
    private int commitCount;
    private int historyLength;
    private double executionTime;

    public static CouplingPerformanceMetricsDto of(String repositoryName, int commitCount, int historyLength, double executionTime) {
        return CouplingPerformanceMetricsDto.builder()
                .repositoryName(repositoryName)
                .commitCount(commitCount)
                .historyLength(historyLength)
                .executionTime(executionTime)
                .build();
    }

}
