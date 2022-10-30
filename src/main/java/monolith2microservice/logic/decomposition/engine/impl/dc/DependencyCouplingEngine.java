package monolith2microservice.logic.decomposition.engine.impl.dc;

import monolith2microservice.Configs;
import monolith2microservice.logic.decomposition.engine.CouplingEngine;
import monolith2microservice.logic.decomposition.engine.DecompositionService;
import monolith2microservice.logic.decomposition.engine.impl.CouplingInput;
import monolith2microservice.logic.decomposition.engine.impl.dc.classvisitor.DependencyCouplingClassVisitor;
import monolith2microservice.logic.decomposition.engine.impl.dc.classvisitor.DependencyCouplingClassVisitorResult;
import monolith2microservice.logic.decomposition.engine.impl.sc.tfidf.TfIdfWrapper;
import monolith2microservice.logic.decomposition.util.PathBuilder;
import monolith2microservice.shared.models.couplings.DependencyCoupling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class DependencyCouplingEngine implements CouplingEngine<DependencyCoupling> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecompositionService.class);

    @Autowired
    private Configs config;

    @Override
    public List<DependencyCoupling> compute(CouplingInput couplingInput) {
        LOGGER.info(String.format("BEGIN |-> compute dependency coupling for %s",couplingInput.getGitRepository().getName()));
        List<DependencyCoupling> couplings = new ArrayList<>();

        // Read class files (content) from repo
        Path repositoryDirectory = PathBuilder.buildLocalRepoPath(
                config.localRepositoryDirectory,
                couplingInput.getGitRepository().getName(),
                couplingInput.getGitRepository().getId()
        );

        DependencyCouplingClassVisitor visitor = DependencyCouplingClassVisitor.create();

        try {
            Files.walkFileTree(repositoryDirectory, visitor);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        List<DependencyCouplingClassVisitorResult> classes = visitor.getResult();

        for(DependencyCouplingClassVisitorResult current: classes) {
            for(DependencyCouplingClassVisitorResult other: classes) {
                if (!current.getFilePath().equals(other.getFilePath())) {
                    DependencyCoupling coupling = new DependencyCoupling(current.getFilePath(),other.getFilePath(), TfIdfWrapper.computeSimilarity(current.getTokenizedDependecies(), other.getTokenizedDependecies()));
                    couplings.add(coupling);
                }
            }
        }

        LOGGER.info(String.format("END |-> compute dependency coupling for %s", couplingInput.getGitRepository().getName()));
        return null;
    }

}
