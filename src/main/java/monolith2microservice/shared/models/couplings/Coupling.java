package monolith2microservice.shared.models.couplings;

public interface Coupling {

    String getFirstFileName();

    String getSecondFileName();

    double getScore();
}
