package monolith2microservice.shared.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import monolith2microservice.shared.models.DecompositionCouplingParameters;
import monolith2microservice.shared.models.git.GitRepository;
import monolith2microservice.shared.models.graph.Decomposition;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class DecompositionDto {

    private GitRepository gitRepository;

    private long decompositionId;

    private DecompositionCouplingParameters parameters;

    public static DecompositionDto of(Decomposition decomposition) {
        return DecompositionDto.builder()
                .decompositionId(decomposition.getId())
                .gitRepository(decomposition.getRepository())
                .parameters(decomposition.getParameters())
                .build();
    }

    public static List<DecompositionDto> of(List<Decomposition> decompositions) {
        return decompositions.stream()
                .map(DecompositionDto::of)
                .collect(Collectors.toList());
    }

}
