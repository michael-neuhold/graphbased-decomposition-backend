package monolith2microservice.logic.decomposition.engine.impl.dc.classvisitor;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class DependencyCouplingClassVisitorResult  {

    private String filePath;

    private List<String> tokenizedDependecies;

}
