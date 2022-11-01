package monolith2microservice.shared.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by gmazlami on 12/15/16.
 */

@Entity
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DecompositionCouplingParameters {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private boolean logicalCoupling;

    private boolean semanticCoupling;

    private boolean contributorCoupling;

    private boolean dependencyCoupling;

    private int numServices;

    private int intervalSeconds;

    private int sizeThreshold;

}
