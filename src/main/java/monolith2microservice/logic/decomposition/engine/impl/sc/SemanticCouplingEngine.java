package monolith2microservice.logic.decomposition.engine.impl.sc;

import monolith2microservice.Configs;
import monolith2microservice.logic.decomposition.engine.impl.shared.CouplingInput;
import monolith2microservice.logic.decomposition.engine.impl.sc.classvisitor.SemanticCouplingClassVisitorResult;
import monolith2microservice.logic.decomposition.util.PathBuilder;
import monolith2microservice.shared.models.couplings.impl.SemanticCoupling;
import monolith2microservice.logic.decomposition.engine.impl.sc.classvisitor.SemanticCouplingClassVisitor;
import monolith2microservice.logic.decomposition.engine.impl.shared.tfidf.TfIdfWrapper;
import monolith2microservice.util.ClassContentFilter;
import monolith2microservice.logic.decomposition.engine.CouplingEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class SemanticCouplingEngine implements CouplingEngine<SemanticCoupling> {

    @Autowired
    private Configs config;

    @Override
    public List<SemanticCoupling> compute(CouplingInput couplingInput) {
        List<SemanticCoupling> couplings = new ArrayList<>();

        Path repositoryDirectory = PathBuilder.buildLocalRepoPath(
                config.localRepositoryDirectory,
                couplingInput.getGitRepository().getName(),
                couplingInput.getGitRepository().getId()
        );

        SemanticCouplingClassVisitor visitor =
                SemanticCouplingClassVisitor.createWith(couplingInput.getGitRepository(),config, new ClassContentFilter());

        try {
            Files.walkFileTree(repositoryDirectory, visitor);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        List<SemanticCouplingClassVisitorResult> classes = visitor.getResult();

        for (SemanticCouplingClassVisitorResult current: classes) {
            for (SemanticCouplingClassVisitorResult other: classes) {
                if (!current.getFilePath().equals(other.getFilePath())) {
                    couplings.add(
                            SemanticCoupling.of(
                                    current.getFilePath(),other.getFilePath(),
                                    TfIdfWrapper.computeSimilarity(current.getTokenizedContent(), other.getTokenizedContent())
                            )
                    );
                }
            }
        }

        return couplings;
    }

}
