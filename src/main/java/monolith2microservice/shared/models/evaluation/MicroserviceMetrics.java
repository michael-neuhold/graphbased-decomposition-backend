package monolith2microservice.shared.models.evaluation;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import monolith2microservice.shared.models.graph.Component;

import javax.persistence.*;
import java.util.Set;

@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MicroserviceMetrics {

    public MicroserviceMetrics(Component microservice){
        this.microservice = microservice;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ElementCollection
    private Set<String> contributors;

    private int LOC;

    @OneToOne(cascade={CascadeType.REMOVE})
    private Component microservice;

    public Set<String> getContributors(){
        return this.contributors;
    }

    public double getNumOfContributors(){
        return (double) this.contributors.size();
    }

    public int getSizeInLoc(){
        return this.LOC;
    }

    public int getSizeInClasses(){
        return this.microservice.getNodes().size();
    }
}
