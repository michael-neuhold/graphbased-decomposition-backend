package monolith2microservice.shared.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import monolith2microservice.logic.decomposition.DecompositionLogic;
import monolith2microservice.shared.models.DecompositionParameters;
import monolith2microservice.shared.models.git.GitRepository;
import monolith2microservice.shared.models.graph.Decomposition;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gmazlami on 1/17/17.
 */
@Getter
@Setter
@Builder
public class DecompositionDto {

    private GitRepository repo;

    private long decompositionId;

    private DecompositionParameters parameters;

    public static DecompositionDto of(Decomposition decomposition) {
        return DecompositionDto.builder()
                .decompositionId(decomposition.getId())
                .repo(decomposition.getRepository())
                .parameters(decomposition.getParameters())
                .build();
    }

    public static List<DecompositionDto> of(List<Decomposition> decompositions) {
        return decompositions.stream()
                .map(DecompositionDto::of)
                .collect(Collectors.toList());
    }

}
