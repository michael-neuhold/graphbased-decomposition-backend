package monolith2microservice.logic.decomposition.engine.impl.sc;

import monolith2microservice.Configs;
import monolith2microservice.logic.decomposition.engine.impl.CouplingInput;
import monolith2microservice.shared.models.ClassContent;
import monolith2microservice.shared.models.couplings.SemanticCoupling;
import monolith2microservice.logic.decomposition.engine.impl.sc.classprocessing.ClassContentVisitor;
import monolith2microservice.logic.decomposition.engine.impl.sc.tfidf.TfIdfWrapper;
import monolith2microservice.util.ClassContentFilter;
import monolith2microservice.logic.decomposition.engine.CouplingEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class SemanticCouplingEngine implements CouplingEngine<SemanticCoupling> {

    @Autowired
    private Configs config;

    @Override
    public List<SemanticCoupling> compute(CouplingInput couplingInput) {
        List<SemanticCoupling> couplings = new ArrayList<>();

        //Read class files (content) from repo
        String localRepoPath = config.localRepositoryDirectory + "/" + couplingInput.getGitRepository().getName() + "_" + couplingInput.getGitRepository().getId();

        Path repoDirectory = Paths.get(localRepoPath);

        ClassContentVisitor visitor = new ClassContentVisitor(couplingInput.getGitRepository(),config, new ClassContentFilter());

        try {
            Files.walkFileTree(repoDirectory, visitor);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        List<ClassContent> classes = visitor.getClasses();

        for(ClassContent current: classes){
            for(ClassContent other: classes){
                if (!current.getFilePath().equals(other.getFilePath())) {
                    SemanticCoupling coupling = new SemanticCoupling(current.getFilePath(),other.getFilePath(), TfIdfWrapper.computeSimilarity(current.getTokenizedContent(), other.getTokenizedContent()));
                    couplings.add(coupling);
                }
            }
        }

        return couplings;
    }

}
