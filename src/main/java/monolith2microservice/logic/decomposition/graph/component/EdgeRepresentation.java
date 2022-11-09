package monolith2microservice.logic.decomposition.graph.component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EdgeRepresentation {

    private long from;

    private long to;

    private double weight;

    public EdgeRepresentation(long from, long to, double weight){
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdgeRepresentation)) return false;

        EdgeRepresentation that = (EdgeRepresentation) o;

        if (from != that.from) return false;
        return to == that.to;

    }

    @Override
    public int hashCode() {
        int result = (int) (from ^ (from >>> 32));
        result = 31 * result + (int) (to ^ (to >>> 32));
        return result;
    }

}
