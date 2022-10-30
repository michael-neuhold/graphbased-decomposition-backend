package monolith2microservice.logic.decomposition.engine.impl.sc.classvisitor;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SemanticCouplingClassVisitorResult {

    private String filePath;

    private List<String> tokenizedContent;

}
