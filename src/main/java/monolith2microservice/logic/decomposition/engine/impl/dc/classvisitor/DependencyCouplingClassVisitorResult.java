package monolith2microservice.logic.decomposition.engine.impl.dc.classvisitor;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DependencyCouplingClassVisitorResult  {

    private String filePath;

    private List<String> tokenizedDependecies;

}
