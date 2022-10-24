package monolith2microservice.shared.models.graph;

import monolith2microservice.shared.models.DecompositionParameters;
import monolith2microservice.shared.models.git.ChangeEvent;
import monolith2microservice.shared.models.git.GitRepository;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by gmazlami on 1/12/17.
 */

@Entity
public class Decomposition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade={CascadeType.REMOVE})
    private Set<Component> services;

    @OneToOne(cascade={CascadeType.REMOVE})
    private GitRepository repository;

    @OneToOne(cascade={CascadeType.REMOVE})
    private DecompositionParameters parameters;

    @Transient
    private long clusteringTime;

    @Transient
    private long strategyTime;

    @Transient
    private List<ChangeEvent> history;

    public Long getId(){
        return this.id;
    }

    public void setParameters(DecompositionParameters params){
        this.parameters = params;
    }

    public DecompositionParameters getParameters(){
        return this.parameters;
    }

    public void setComponents(Set<Component> services){
        this.services = services;
    }

    public Set<Component> getServices() {
        return this.services;
    }

    public GitRepository getRepository(){
        return this.repository;
    }

    public void setRepository(GitRepository repo){
        this.repository = repo;
    }

    public long getClusteringTime() {
        return clusteringTime;
    }

    public void setClusteringTime(long clusteringTime) {
        this.clusteringTime = clusteringTime;
    }

    public long getStrategyTime() {
        return strategyTime;
    }

    public void setStrategyTime(long strategyTime) {
        this.strategyTime = strategyTime;
    }

    public void setHistory(List<ChangeEvent> history){
        this.history = history;
    }

    public List<ChangeEvent> getHistory() {
        return history;
    }
}
