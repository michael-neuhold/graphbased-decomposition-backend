package monolith2microservice.shared.models.evaluation;

import lombok.*;
import monolith2microservice.shared.models.graph.Decomposition;

import javax.persistence.*;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(cascade={CascadeType.REMOVE})
    private Decomposition decomposition;

    private double contributorsPerMicroservice;

    private double contributorOverlapping;

    private double averageLoc;

    private double averageClassNumber;

    private double similarity;

    private long executionTimeMillisStrategy;

    private long executionTimeMillisClustering;

}
