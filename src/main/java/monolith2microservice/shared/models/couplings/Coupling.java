package monolith2microservice.shared.models.couplings;

/**
 * Created by gmazlami on 12/15/16.
 */
public interface Coupling {

    String getFirstFileName();

    String getSecondFileName();

    double getScore();
}
