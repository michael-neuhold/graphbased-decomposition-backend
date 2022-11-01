package monolith2microservice.shared.models.graph;

import lombok.*;
import monolith2microservice.shared.models.DecompositionCouplingParameters;
import monolith2microservice.shared.models.git.ChangeEvent;
import monolith2microservice.shared.models.git.GitRepository;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by gmazlami on 1/12/17.
 */

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Decomposition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade={CascadeType.REMOVE})
    private Set<Component> services;

    @OneToOne(cascade={CascadeType.REMOVE})
    private GitRepository repository;

    @OneToOne(cascade={CascadeType.REMOVE})
    private DecompositionCouplingParameters parameters;

    @Transient
    private long clusteringTime;

    @Transient
    private long strategyTime;

    @Transient
    private List<ChangeEvent> history;

}
