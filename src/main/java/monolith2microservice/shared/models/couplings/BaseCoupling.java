package monolith2microservice.shared.models.couplings;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseCoupling implements Coupling {

    protected String firstFileName;

    protected String secondFileName;

    protected double score;

    public BaseCoupling(String firstFileName, String secondFileName, double score){
        this.firstFileName = firstFileName;
        this.secondFileName = secondFileName;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseCoupling)) return false;

        BaseCoupling coupling = (BaseCoupling) o;

        if (Double.compare(coupling.score, score) != 0) return false;
        if (!firstFileName.equals(coupling.firstFileName)) return false;
        return secondFileName.equals(coupling.secondFileName);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = firstFileName.hashCode();
        result = 31 * result + secondFileName.hashCode();
        temp = Double.doubleToLongBits(score);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
