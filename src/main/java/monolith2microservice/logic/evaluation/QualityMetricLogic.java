package monolith2microservice.logic.evaluation;

import monolith2microservice.shared.dto.QualityMetricDto;

import java.util.List;

public interface QualityMetricLogic {

    List<QualityMetricDto> findAll();

}
